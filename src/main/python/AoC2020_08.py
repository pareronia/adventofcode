#! /usr/bin/env python3
#
# Advent of Code 2020 Day 8
#
from dataclasses import dataclass
from aoc import my_aocd
from aoc.common import log
from aoc.vm import VirtualMachine, Instruction, Program


ERROR_ON_INFINITE_LOOP = True


@dataclass(frozen=True)
class Instruction_:
    operation: str
    argument: int

    @classmethod
    def instruction(cls, in_: tuple[str, str]):
        return cls(in_[0], int(in_[1]))

    def check_valid_operation(self) -> None:
        if self.operation not in ("nop", "acc", "jmp"):
            raise ValueError("Invalid instruction operation")


def _parse(inputs: tuple[str]) -> list[Instruction_]:
    return [Instruction_.instruction(input_.split()) for input_ in inputs]


def _run_program(lines: list[Instruction_],
                 error_on_inf_loop: bool = False):
    log(lines)
    vm = VirtualMachine()
    instructions = list[Instruction]()
    for line in lines:
        line.check_valid_operation()
        if line.operation == "nop":
            instructions.append(Instruction.NOP())
        elif line.operation == "acc":
            instructions.append(Instruction.ACC(line.argument))
        elif line.operation == "jmp":
            instructions.append(Instruction.JMP(line.argument))
    program = Program(instructions, error_on_inf_loop,
                      error_on_jump_beyond_zero=False)
    vm.run_program(program)
    return program.accumulator


def _try_program_run_with_replaced_operation(instructions: list[Instruction_],
                                             index: int,
                                             new_operation: str) -> int:
    orig_instruction = instructions[index]
    instructions[index] = Instruction_(new_operation,
                                       orig_instruction.argument)
    log("{original_instruction.operation} -> {new_operation}")
    try:
        return _run_program(instructions, error_on_inf_loop=True)
    except RuntimeError:
        instructions[index] = orig_instruction
        return None


def part_1(inputs: tuple[str]) -> int:
    log(inputs)
    instructions = _parse(inputs)
    return _run_program(instructions)


def part_2(inputs: tuple[str]) -> int:
    log(inputs)
    instructions = _parse(inputs)
    for i in range(len(instructions)):
        instruction = instructions[i]
        log(instruction)
        instruction.check_valid_operation()
        if instruction.operation == "nop":
            result = _try_program_run_with_replaced_operation(
                          instructions, i, "jmp")
            if result is not None:
                return result
        elif instruction.operation == "jmp":
            result = _try_program_run_with_replaced_operation(
                          instructions, i, "nop")
            if result is not None:
                return result
        elif instruction.operation == "acc":
            log("acc: skip")


test = ("nop +0",
        "acc +1",
        "jmp +4",
        "acc +3",
        "jmp -3",
        "acc -99",
        "acc +1",
        "jmp -4",
        "acc +6",
        )


def main() -> None:
    my_aocd.print_header(2020, 8)

    assert part_1(test) == 5
    assert part_2(test) == 8

    inputs = my_aocd.get_input_as_tuple(2020, 8, 608)
    result1 = part_1(inputs)
    result2 = part_2(inputs)
    print(f"Part 1: {result1}")
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

#! /usr/bin/env python3
#
# Advent of Code 2020 Day 8
#
from typing import NamedTuple
from aoc import my_aocd
from aoc.common import log
from aoc.vm import VirtualMachine, Instruction, Program


class Instruction_(NamedTuple):
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


def _build_program(lines: list[Instruction_]) -> Program:
    log(lines)
    instructions = list[Instruction]()
    for line in lines:
        line.check_valid_operation()
        if line.operation == "nop":
            instructions.append(Instruction.NOP())
        elif line.operation == "acc":
            instructions.append(Instruction.ADD("ACC", line.argument))
        elif line.operation == "jmp":
            instructions.append(Instruction.JMP(line.argument))
    return Program(instructions, inf_loop_treshold=1)


def _try_program_run_with_replaced_operation(instructions: list[Instruction_],
                                             index: int,
                                             new_operation: str) -> int:
    orig_instruction = instructions[index]
    instructions[index] = Instruction_(new_operation,
                                       orig_instruction.argument)
    log(f"{orig_instruction.operation} -> {new_operation}")
    try:
        program = _build_program(instructions)
        VirtualMachine().run_program(program)
    except RuntimeError:
        instructions[index] = orig_instruction
        return None
    return program.registers["ACC"]


def part_1(inputs: tuple[str]) -> int:
    log(inputs)
    instructions = _parse(inputs)
    program = _build_program(instructions)
    try:
        VirtualMachine().run_program(program)
    except RuntimeError:
        pass
    return program.registers["ACC"]


def part_2(inputs: tuple[str]) -> int:
    log(inputs)
    instructions = _parse(inputs)
    for i, instruction in enumerate(instructions):
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


TEST = """\
nop +0
acc +1
jmp +4
acc +3
jmp -3
acc -99
acc +1
jmp -4
acc +6
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 8)

    assert part_1(TEST) == 5
    assert part_2(TEST) == 8

    inputs = my_aocd.get_input(2020, 8, 608)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

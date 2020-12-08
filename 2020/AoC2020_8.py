#! /usr/bin/env python3
#
# Advent of Code 2020 Day 8
#
from dataclasses import dataclass
import my_aocd


ERROR_ON_INFINITE_LOOP = True


@dataclass
class Instruction:
    operation: str
    argument: int

    def __init__(self, operation: str, argument: int):
        self.operation = operation
        self.argument = argument

    @classmethod
    def instruction(cls, in_: tuple[str, str]):
        return cls(in_[0], int(in_[1]))

    def check_valid_operation(self) -> None:
        if self.operation not in ("nop", "acc", "jmp"):
            raise ValueError("Invalid instruction operation")


def _parse(inputs: tuple[str]) -> list[Instruction]:
    return [Instruction.instruction(input_.split()) for input_ in inputs]


def _log(msg: str) -> None:
    if __debug__:
        print(msg)


def _run_program(instructions: list[Instruction],
                 error_on_inf_loop: bool = False) -> int:
    _log(instructions)
    accumulator = 0
    seen = set[int]()
    ip = 0  # Instruction pointer
    while ip not in seen:
        if ip == len(instructions):
            _log("Normal exit")
            return accumulator
        seen.add(ip)
        instruction = instructions[ip]
        _log(f"{ip}: {instruction}")
        instruction.check_valid_operation()
        if instruction.operation == "nop":
            ip += 1
        elif instruction.operation == "acc":
            accumulator += instruction.argument
            ip += 1
        elif instruction.operation == "jmp":
            ip += instruction.argument
            if ip < 0:
                raise ValueError("Invalid instruction argument")
        _log(f" -> {ip} - {accumulator}")
    if error_on_inf_loop:
        raise RuntimeError("Infinite loop!")
    else:
        return accumulator


def _try_program_run_with_replaced_operation(instructions: list[Instruction],
                                             index: int,
                                             new_operation: str) -> int:
    orig_instruction = instructions[index]
    instructions[index] = Instruction(new_operation, orig_instruction.argument)
    _log("{original_instruction.operation} -> {new_operation}")
    try:
        return _run_program(instructions, ERROR_ON_INFINITE_LOOP)
    except RuntimeError:
        instructions[index] = orig_instruction
        return None


def part_1(inputs: tuple[str]) -> int:
    _log(inputs)
    instructions = _parse(inputs)
    return _run_program(instructions)


def part_2(inputs: tuple[str]) -> int:
    _log(inputs)
    instructions = _parse(inputs)
    for i in range(len(instructions)):
        instruction = instructions[i]
        _log(instruction)
        instruction.check_valid_operation()
        if instruction.operation == "nop":
            if instruction.argument == 0:
                _log("nop 0 : skip")
                continue
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
            _log("acc: skip")


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

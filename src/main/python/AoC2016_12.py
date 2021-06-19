#! /usr/bin/env python3
#
# Advent of Code 2015 Day 12
#

from typing import NamedTuple
from aoc import my_aocd
from aoc.common import log
from aoc.vm import Program, Instruction, VirtualMachine


class Instruction_(NamedTuple):
    operation: str
    operands: tuple[str]

    @classmethod
    def of(cls, in_: tuple[str]):
        return Instruction_(in_[0], (in_[1:]))

    def check_valid_operation(self) -> None:
        if self.operation not in ("cpy", "inc", "dec", "jnz"):
            raise ValueError("Invalid instruction operation")
        for operand in self.operands:
            if not (operand.strip('-').isnumeric() or operand in "abcd"):
                raise ValueError("Invalid instruction operand")


def _parse(inputs: tuple[str]) -> list[Instruction_]:
    return [Instruction_.of(input_.split()) for input_ in inputs]


def _build_program(lines: list[Instruction_]) -> Program:
    instructions = list[Instruction]()
    for line in lines:
        line.check_valid_operation()
        if line.operation == "cpy":
            value, register = line.operands
            if value.strip('-').isnumeric():
                instructions.append(Instruction.SET(register, int(value)))
            else:
                instructions.append(Instruction.CPY(value, register))
        elif line.operation == "inc":
            register = line.operands[0]
            instructions.append(Instruction.ADD(register, 1))
        elif line.operation == "dec":
            register = line.operands[0]
            instructions.append(Instruction.ADD(register, -1))
        elif line.operation == "jnz":
            register, value = line.operands
            if register in ("abcd"):
                instructions.append(Instruction.JN0(register, int(value)))
            elif register.strip('-').isnumeric():
                instructions.append(Instruction.SET("tmp", int(register)))
                instructions.append(Instruction.JN0("tmp", int(value)))
            else:
                raise ValueError("Invalid operands for jnz")
    return Program(instructions)


def part_1(inputs: tuple[str]) -> int:
    inss = _parse(inputs)
    log(inss)
    program = _build_program(inss)
    log(program.instructions)
    VirtualMachine().run_program(program)
    return program.registers["a"]


def part_2(inputs: tuple[str]) -> int:
    return 0


TEST = '''\
cpy 41 a
inc a
inc a
dec a
jnz a 2
dec a
'''.splitlines()


def main() -> None:
    my_aocd.print_header(2016, 12)

    assert part_1(TEST) == 42

    inputs = my_aocd.get_input(2016, 12, 23)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

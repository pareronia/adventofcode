#! /usr/bin/env python3
#
# Advent of Code 2015 Day 23
#

from typing import NamedTuple
from aoc import my_aocd
from aoc.vm import Program, Instruction, VirtualMachine


class Instruction_(NamedTuple):
    operation: str
    operands: tuple[str]


def _parse(inputs: tuple[str]) -> list[Instruction_]:
    return [Instruction_(input_[:3], input_[4:].split(", "))
            for input_ in inputs]


def _build_program(inss: list[Instruction_]) -> Program:
    def translate_instruction(ins: Instruction_) -> Instruction:
        if ins.operation == "hlf":
            return Instruction.DIV(ins.operands[0], "2")
        elif ins.operation == "tpl":
            return Instruction.MUL(ins.operands[0], "3")
        elif ins.operation == "inc":
            return Instruction.ADD(ins.operands[0], 1)
        elif ins.operation == "jmp":
            return Instruction.JMP(int(ins.operands[0]))
        elif ins.operation == "jie":
            return Instruction.JIE(ins.operands[0], int(ins.operands[1]))
        elif ins.operation == "jio":
            return Instruction.JI1(ins.operands[0], int(ins.operands[1]))
        else:
            raise ValueError("Invalid instruction")

    return Program([translate_instruction(ins) for ins in inss])


def part_1(inputs: tuple[str]) -> int:
    program = _build_program(_parse(inputs))
    VirtualMachine().run_program(program)
    return program.registers["b"]


def part_2(inputs: tuple[str]) -> int:
    program = _build_program(_parse(inputs))
    program.instructions.insert(0, Instruction.SET("a", 1))
    VirtualMachine().run_program(program)
    return program.registers["b"]


TEST = """\
inc b
jio a, +2
tpl b
inc b
""".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 23)

    assert part_1(TEST) == 4
    assert part_2(TEST) == 2

    inputs = my_aocd.get_input(2015, 23, 47)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

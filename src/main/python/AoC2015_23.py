#! /usr/bin/env python3
#
# Advent of Code 2015 Day 23
#

from typing import NamedTuple
from aoc import my_aocd
from aoc.vm import Program, Instruction, VirtualMachine
from aoc.common import log


class Instruction_(NamedTuple):
    operation: str
    operands: tuple[str]


def _parse(inputs: tuple[str]) -> list[Instruction_]:
    inss = list[Instruction_]()
    for input_ in inputs:
        operation = input_[:3]
        operands = input_[4:].split(", ")
        inss.append(Instruction_(operation, operands))
    return inss


def _build_program(inss: list[Instruction_]) -> Program:
    p_inss = list[Instruction]()
    for ins in inss:
        if ins.operation == "hlf":
            p_inss.append(Instruction.DIV(ins.operands[0], 2))
        elif ins.operation == "tpl":
            p_inss.append(Instruction.MUL(ins.operands[0], 3))
        elif ins.operation == "inc":
            p_inss.append(Instruction.ADD(ins.operands[0], 1))
        elif ins.operation == "jmp":
            p_inss.append(Instruction.JMP(int(ins.operands[0])))
        elif ins.operation == "jie":
            p_inss.append(
                Instruction.JIE(ins.operands[0], int(ins.operands[1])))
        elif ins.operation == "jio":
            p_inss.append(
                Instruction.JI1(ins.operands[0], int(ins.operands[1])))
        else:
            raise ValueError("Invalid instruction")
    return Program(p_inss)


def part_1(inputs: tuple[str]) -> int:
    inss = _parse(inputs)
    log(inss)
    vm = VirtualMachine()
    program = _build_program(inss)
    log(program._instructions)
    vm.run_program(program)
    log(program.registers)
    return program.registers["b"]


def part_2(inputs: tuple[str]) -> int:
    return 0


TEST = """\
inc b
jio b, +2
tpl b
inc b
""".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 23)

    assert part_1(TEST) == 2
    assert part_2(TEST) == 0

    inputs = my_aocd.get_input(2015, 23, 47)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

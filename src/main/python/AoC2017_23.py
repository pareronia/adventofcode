#! /usr/bin/env python3
#
# Advent of Code 2017 Day 23
#

import re
from aoc import my_aocd
import aocd
from aoc.vm import Instruction, Program, VirtualMachine


def _parse(inputs: tuple[str]) -> list[Instruction]:
    def value(x: str) -> str:
        m = re.fullmatch(r"[+-]?[0-9]*", x)
        return "*" + x if m is None else x

    instructions = list[Instruction]()
    for line in inputs:
        splits = line.split()
        if splits[0] == "set":
            instructions.append(Instruction.SET(splits[1], value(splits[2])))
        elif splits[0] == "sub":
            instructions.append(Instruction.SUB(splits[1], value(splits[2])))
        elif splits[0] == "mul":
            instructions.append(Instruction.MUL(splits[1], value(splits[2])))
        elif splits[0] == "jnz":
            instructions.append(
                Instruction.JN0(value(splits[1]), value(splits[2]))
            )
    return instructions


def _is_prime(n: int) -> bool:
    for i in range(2, int(n**0.5) + 1, 1):
        if n % i == 0:
            return False
    return True


def part_1(inputs: tuple[str]) -> int:
    instructions = _parse(inputs)
    muls = [i for i, ins in enumerate(instructions) if ins.is_MUL]
    program = Program(instructions)
    vm = VirtualMachine()
    ans = 0
    while program.instruction_pointer < len(instructions):
        if program.instruction_pointer in muls:
            ans += 1
        vm.step(program)
    return ans


def part_2(inputs: tuple[str]) -> int:
    instructions = _parse(inputs)
    program = Program(instructions)
    program.registers["a"] = 1
    vm = VirtualMachine()
    while program.instruction_pointer < 9:
        vm.step(program)
    from_ = program.registers["b"]
    to = program.registers["c"]
    step = -1 * int(instructions[-2].operands[1])
    return sum(1 for i in range(from_, to + 1, step) if not _is_prime(i))


def main() -> None:
    puzzle = aocd.models.Puzzle(2017, 23)
    my_aocd.print_header(puzzle.year, puzzle.day)

    inputs = my_aocd.get_input(2017, 23, 32)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2016 Day 23
#

import math
import sys

from aoc.assembunny import Assembunny
from aoc.assembunny import AssembunnyInstruction
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import log
from aoc.vm import Program
from aoc.vm import VirtualMachine

TEST1 = """\
cpy 2 a
tgl a
tgl a
tgl a
cpy 1 a
dec a
dec a
"""
TEST2 = """\
cpy a b
dec b
cpy a d
cpy 0 a
cpy b c
inc a
dec c
jnz c -2
dec d
jnz d -5
dec b
cpy b c
cpy c d
dec d
inc c
jnz d -2
tgl c
cpy -16 c
jnz 1 c
cpy 90 c
jnz 73 d
inc a
inc d
jnz d -2
inc c
jnz c -5
"""


def _solve_vm(instructions: list[AssembunnyInstruction], init_a: int) -> int:
    program = Program(Assembunny.translate(instructions))
    log(program.instructions)
    program.set_register_value("a", init_a)
    VirtualMachine().run_program(program)
    log(program.registers["a"])
    return int(program.registers["a"])


Input = list[AssembunnyInstruction]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Assembunny.parse(tuple(input_data))

    def solve(
        self, instructions: list[AssembunnyInstruction], init_a: int
    ) -> int:
        values = [
            int(i)
            for i in [
                i.operands[0]
                for i in instructions
                if i.operation in {"cpy", "jnz"}
            ]
            if i.isnumeric()
        ]
        return values[2] * values[3] + math.factorial(init_a)

    def part_1(self, instructions: list[AssembunnyInstruction]) -> int:
        return self.solve(instructions, 7)

    def part_2(self, instructions: list[AssembunnyInstruction]) -> int:
        return self.solve(instructions, 12)

    def samples(self) -> None:
        assert self.solve(self.parse_input(TEST2.splitlines()), 7) == 11_610
        assert (
            self.solve(self.parse_input(TEST2.splitlines()), 12) == 479_008_170
        )
        assert _solve_vm(self.parse_input(TEST1.splitlines()), 7) == 3


solution = Solution(2016, 23)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

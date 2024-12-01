#! /usr/bin/env python3
#
# Advent of Code 2016 Day 12
#

import sys

from aoc.assembunny import Assembunny
from aoc.assembunny import AssembunnyInstruction
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import log
from aoc.math import Fibonacci
from aoc.vm import Program
from aoc.vm import VirtualMachine

TEST1 = """\
cpy 41 a
inc a
inc a
dec a
jnz a 2
dec a
"""
TEST2 = """\
cpy 1 a
cpy 1 b
cpy 26 d
jnz c 2
jnz 1 5
cpy 7 c
inc d
dec c
jnz c -2
cpy a c
inc a
dec b
jnz b -2
cpy c b
dec d
jnz d -6
cpy 16 c
cpy 12 d
inc a
dec d
jnz d -2
dec c
jnz c -5
"""


# from u/blockingthesky @reddit
def _solve_reddit(inp: list[str], init_c: int) -> int:
    reg = {"a": 0, "b": 0, "c": init_c, "d": 0}
    ind = 0
    while ind < len(inp):
        ins = inp[ind].split(" ")
        if ins[0] == "cpy":
            if ins[1][0] in "abcd":
                reg[ins[2]] = reg[ins[1]]
            else:
                j = int(ins[1])
                reg[ins[2]] = j
        elif ins[0] == "inc":
            reg[ins[1]] += 1
        elif ins[0] == "dec":
            reg[ins[1]] -= 1
        if ins[0] == "jnz":
            if ins[1] in "abcd":
                if reg[ins[1]] != 0:
                    ind += int(ins[2])
                else:
                    ind += 1
            else:
                if int(ins[1]) != 0:
                    ind += int(ins[2])
                else:
                    ind += 1
        else:
            ind += 1
    return reg["a"]


def _solve_vm(instructions: list[AssembunnyInstruction], init_c: int) -> int:
    program = Program(Assembunny.translate(instructions))
    program.set_register_value("c", init_c)
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
        self, instructions: list[AssembunnyInstruction], init_c: int
    ) -> int:
        values = [
            int(i)
            for i in [
                i.operands[0] for i in instructions if i.operation == "cpy"
            ]
            if i.isnumeric()
        ]
        n = values[0] + values[1] + values[2]
        n += 0 if init_c == 0 else 7
        return Fibonacci.binet(n) + values[4] * values[5]

    def part_1(self, instructions: list[AssembunnyInstruction]) -> int:
        return self.solve(instructions, 0)

    def part_2(self, instructions: list[AssembunnyInstruction]) -> int:
        return self.solve(instructions, 1)

    def samples(self) -> None:
        assert self.solve(self.parse_input(TEST2.splitlines()), 0) == 318_003
        assert self.solve(self.parse_input(TEST2.splitlines()), 1) == 9_227_657
        assert _solve_reddit(TEST1.splitlines(), 0) == 42
        assert _solve_reddit(TEST2.splitlines(), 0) == 318_003
        assert _solve_reddit(TEST2.splitlines(), 1) == 9_227_657


solution = Solution(2016, 12)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2019 Day 2
#

import itertools
import sys
from copy import deepcopy

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.intcode import IntCode

Input = list[int]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return IntCode.parse(list(input_data))

    def run_program(self, prog: list[int], noun: int, verb: int) -> int:
        prog[1] = noun
        prog[2] = verb
        int_code = IntCode(prog)
        int_code.run()
        return int_code.get_program()[0]

    def part_1(self, program: list[int]) -> int:
        return self.run_program(program, 12, 2)

    def part_2(self, program: list[int]) -> int:
        for noun, verb in itertools.product(range(100), repeat=2):
            if self.run_program(deepcopy(program), noun, verb) == 19_690_720:
                return 100 * noun + verb
        raise RuntimeError("Unsolved")

    def samples(self) -> None:
        pass


solution = Solution(2019, 2)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

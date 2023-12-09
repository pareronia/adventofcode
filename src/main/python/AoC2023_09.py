#! /usr/bin/env python3
#
# Advent of Code 2023 Day 9
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[list[int]]
Output1 = int
Output2 = int


TEST = """\
0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [[int(_) for _ in line.split()] for line in input_data]

    def solve(self, line: list[int]) -> int:
        tails = [line[-1]]
        while not all(_ == tails[-1] for _ in line):
            line = [b - a for a, b in zip(line, line[1:])]
            tails.append(line[-1])
        return sum(tails)

    def part_1(self, input: Input) -> Output1:
        return sum(self.solve(line) for line in input)

    def part_2(self, input: Input) -> Output2:
        return sum(self.solve(line[::-1]) for line in input)

    @aoc_samples(
        (
            ("part_1", TEST, 114),
            ("part_2", TEST, 2),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 9)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

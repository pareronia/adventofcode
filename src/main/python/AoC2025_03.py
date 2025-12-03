#! /usr/bin/env python3
#
# Advent of Code 2025 Day 3
#

import sys
from functools import cache

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = InputData
Output1 = int
Output2 = int


TEST = """\
987654321111111
811111111111119
234234234234278
818181911112111
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def solve_line(self, line: str, digits: int) -> int:
        @cache
        def dfs(start: int, left: int) -> int:
            if left == 0:
                return 0
            if len(line) - start == left:
                return int(line[start:])
            a = int(line[start]) * 10 ** (left - 1) + dfs(start + 1, left - 1)
            b = dfs(start + 1, left)
            return max(int(a), b)

        return dfs(0, digits)

    def solve(self, inputs: Input, digits: int) -> int:
        return sum(self.solve_line(line, digits) for line in inputs)

    def part_1(self, inputs: Input) -> Output1:
        return self.solve(inputs, digits=2)

    def part_2(self, inputs: Input) -> Output2:
        return self.solve(inputs, digits=12)

    @aoc_samples(
        (
            ("part_1", TEST, 357),
            ("part_2", TEST, 3121910778619),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2025, 3)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

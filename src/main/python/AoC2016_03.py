#! /usr/bin/env python3
#
# Advent of Code 2016 Day 3
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST1 = "5 10 25"
TEST2 = "3 4 5"
TEST3 = """\
5 3 6
10 4 8
25 5 10
"""

Input = list[list[int]]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [list(map(int, line.split())) for line in input_data]

    def valid(self, t: tuple[int, int, int]) -> bool:
        return t[0] + t[1] > t[2] and t[0] + t[2] > t[1] and t[1] + t[2] > t[0]

    def part_1(self, ts: Input) -> int:
        return sum(self.valid(tuple(_ for _ in t[:3])) for t in ts)

    def part_2(self, ts: Input) -> int:
        return sum(
            sum(
                self.valid((ts[i][j], ts[i + 1][j], ts[i + 2][j]))
                for j in range(3)
            )
            for i in range(0, len(ts), 3)
        )

    @aoc_samples(
        (
            ("part_1", TEST1, 0),
            ("part_1", TEST2, 1),
            ("part_2", TEST3, 2),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2016, 3)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

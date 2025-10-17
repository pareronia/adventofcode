#! /usr/bin/env python3
#
# Advent of Code 2024 Day 2
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[list[int]]
Output1 = int
Output2 = int


TEST = """\
7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [list(map(int, line.split())) for line in input_data]

    def safe(self, levels: list[int]) -> bool:
        diffs = [levels[i + 1] - levels[i] for i in range(len(levels) - 1)]
        return all(1 <= diff <= 3 for diff in diffs) or all(
            -1 >= diff >= -3 for diff in diffs
        )

    def part_1(self, reports: Input) -> Output1:
        return sum(self.safe(report) for report in reports)

    def part_2(self, reports: Input) -> Output2:
        return sum(
            any(
                self.safe(report[:i] + report[i + 1 :])
                for i in range(len(report))
            )
            for report in reports
        )

    @aoc_samples(
        (
            ("part_1", TEST, 2),
            ("part_2", TEST, 4),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 2)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

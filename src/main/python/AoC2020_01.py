#! /usr/bin/env python3
#
# Advent of Code 2020 Day 1
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
1721
979
366
299
675
1456
"""

Input = list[int]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [int(_) for _ in input_data]

    def part_1(self, numbers: Input) -> int:
        seen = set[int]()
        for n1 in numbers:
            seen.add(n1)
            n2 = 2020 - n1
            if n2 in seen:
                return n1 * n2
        raise RuntimeError()

    def part_2(self, numbers: Input) -> int:
        seen = set[int]()
        for i, n1 in enumerate(numbers):
            seen.add(n1)
            for n2 in numbers[i:]:
                n3 = 2020 - n1 - n2
                if n3 in seen:
                    return n1 * n2 * n3
        raise RuntimeError()

    @aoc_samples(
        (
            ("part_1", TEST, 514_579),
            ("part_2", TEST, 241_861_950),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2020, 1)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

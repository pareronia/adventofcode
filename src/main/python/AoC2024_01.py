#! /usr/bin/env python3
#
# Advent of Code 2024 Day 1
#

import sys
from collections import Counter

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = tuple[tuple[int, ...], tuple[int, ...]]
Output1 = int
Output2 = int


TEST = """\
3   4
4   3
2   5
1   3
3   9
3   3
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        left, right = [], []
        for line in input_data:
            a, b = line.split()
            left.append(int(a))
            right.append(int(b))
        return tuple(left), tuple(right)

    def part_1(self, lists: Input) -> Output1:
        left, right = lists
        return sum(
            abs(n1 - n2)
            for n1, n2 in zip(sorted(left), sorted(right), strict=True)
        )

    def part_2(self, lists: Input) -> Output2:
        left, right = lists
        ctr = Counter(right)
        return sum(n * ctr[n] for n in left)

    @aoc_samples(
        (
            ("part_1", TEST, 11),
            ("part_2", TEST, 31),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 1)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

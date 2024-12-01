#! /usr/bin/env python3
#
# Advent of Code 2024 Day 1
#

import sys
from collections import Counter

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = tuple[list[int], list[int]]
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
        nums = [_ for line in input_data for _ in line.split()]
        for i in range(0, len(nums), 2):
            left.append(int(nums[i]))
            right.append(int(nums[i + 1]))
        return left, right

    def part_1(self, lists: Input) -> Output1:
        left, right = map(sorted, lists)
        return sum(abs(left[i] - right[i]) for i in range(len(left)))

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

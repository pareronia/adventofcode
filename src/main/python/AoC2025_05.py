#! /usr/bin/env python3
#
# Advent of Code 2025 Day 5
#

import sys

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.range import RangeInclusive

Input = InputData
Output1 = int
Output2 = int


TEST = """\
3-5
10-14
16-20
12-18

1
5
8
11
17
32
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, inputs: Input) -> Output1:
        blocks = my_aocd.to_blocks(list(inputs))
        ranges = set[range]()
        for line in blocks[0]:
            lo, hi = map(int, line.split("-"))
            ranges.add(range(lo, hi + 1))
        ans = 0
        for line in blocks[1]:
            if any(int(line) in rng for rng in ranges):
                ans += 1
        return ans

    def part_2(self, inputs: Input) -> Output2:
        blocks = my_aocd.to_blocks(list(inputs))
        ranges = set[RangeInclusive]()
        for line in blocks[0]:
            lo, hi = map(int, line.split("-"))
            ranges.add(RangeInclusive.between(lo, hi))
        merged = RangeInclusive.merge(ranges)
        ans = 0
        for rng in merged:
            ans += rng.len
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 3),
            ("part_2", TEST, 14),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2025, 5)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

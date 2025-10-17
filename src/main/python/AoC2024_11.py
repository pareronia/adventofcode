#! /usr/bin/env python3
#
# Advent of Code 2024 Day 11
#

import sys
from functools import cache

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[int]
Output1 = int
Output2 = int


TEST = """\
125 17
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(map(int, next(iter(input_data)).split()))

    def solve(self, stones: list[int], blinks: int) -> int:
        @cache
        def count(s: int, cnt: int) -> int:
            if cnt == 0:
                return 1
            if s == 0:
                return count(1, cnt - 1)
            ss = str(s)
            size = len(ss)
            if size % 2 == 0:
                s1 = int(ss[: size // 2])
                s2 = int(ss[size // 2 :])
                return count(s1, cnt - 1) + count(s2, cnt - 1)
            return count(s * 2024, cnt - 1)

        return sum(count(int(s), blinks) for s in stones)

    def part_1(self, stones: Input) -> Output1:
        return self.solve(stones, blinks=25)

    def part_2(self, stones: Input) -> Output2:
        return self.solve(stones, blinks=75)

    @aoc_samples((("part_1", TEST, 55312),))
    def samples(self) -> None:
        pass


solution = Solution(2024, 11)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2025 Day 1
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

Input = InputData
Output1 = int
Output2 = int


TEST = """\
L68
L30
R48
L5
R60
L55
L1
L99
R14
L82
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, inputs: Input) -> Output1:
        ans = 0
        pos = 50
        for line in inputs:
            d, amt = line[0], int(line[1:])
            pos = (pos + 100 + ((1 if d == "R" else -1) * amt)) % 100
            log(pos)
            if pos == 0:
                ans += 1
        return ans

    def part_2(self, inputs: Input) -> Output2:
        ans = 0
        pos = 50
        for line in inputs:
            d, amt = line[0], int(line[1:])
            for _ in range(amt):
                pos = (pos + 100 + (1 if d == "R" else -1)) % 100
                if pos == 0:
                    ans += 1
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 3),
            ("part_2", TEST, 6),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2025, 1)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

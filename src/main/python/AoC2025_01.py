#! /usr/bin/env python3
#
# Advent of Code 2025 Day 1
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[int]
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

START = 50


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [
            (1 if line[0] == "R" else -1) * int(line[1:])
            for line in input_data
        ]

    def part_1(self, rotations: Input) -> Output1:
        pos = START
        ans = 0
        for r in rotations:
            pos += r
            if pos % 100 == 0:
                ans += 1
        return ans

    def part_2(self, rotations: Input) -> Output2:
        pos = START
        ans = 0
        for r in rotations:
            npos = pos + r
            ans += abs(npos // 100 - pos // 100)
            if npos < pos:
                if npos % 100 == 0:
                    ans += 1
                if pos % 100 == 0:
                    ans -= 1
            pos = npos
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

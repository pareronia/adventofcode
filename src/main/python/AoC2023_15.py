#! /usr/bin/env python3
#
# Advent of Code 2023 Day 15
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[str]
Output1 = int
Output2 = int


TEST = """\
rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)[0].split(",")

    def part_1(self, steps: Input) -> Output1:
        ans = 0
        for step in steps:
            prev = 0
            for ch in step:
                prev += ord(ch)
                prev *= 17
                prev %= 256
            ans += prev
        return ans

    def part_2(self, input: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 1320),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 15)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

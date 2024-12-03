#! /usr/bin/env python3
#
# Advent of Code 2024 Day 3
#

import re
import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = InputData
Output1 = int
Output2 = int


TEST_1 = """\
xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
"""
TEST_2 = """\
xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, input: Input) -> Output1:
        ans = 0
        for line in input:
            for m in re.finditer(r"mul\((\d+),(\d+)\)", line):
                ans += int(m.group(1)) * int(m.group(2))
        return ans

    def part_2(self, input: Input) -> Output2:
        do = True
        ans = 0
        for m in re.finditer(
            r"(do(n't)?)\(\)|mul\((\d+),(\d+)\)",
            "\n".join(line for line in input),
        ):
            if m.group(1) == "do":
                do = True
            if m.group(1) == "don't":
                do = False
            if do and m.group(3) is not None and m.group(4) is not None:
                ans += int(m.group(3)) * int(m.group(4))
        return ans

    @aoc_samples(
        (
            ("part_1", TEST_1, 161),
            ("part_2", TEST_2, 48),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 3)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

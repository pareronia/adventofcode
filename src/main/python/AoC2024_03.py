#! /usr/bin/env python3
#
# Advent of Code 2024 Day 3
#

import re
import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = str
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
        return "\n".join(line for line in input_data)

    def solve(self, memory: str, *, use_conditionals: bool) -> int:
        enabled = True
        ans = 0
        for do, _, a, b in re.findall(
            r"(do(n't)?)\(\)|mul\((\d{1,3}),(\d{1,3})\)", memory
        ):
            if do == "do":
                enabled = True
            elif do == "don't":
                enabled = False
            elif not use_conditionals or enabled:
                ans += int(a) * int(b)
        return ans

    def part_1(self, memory: Input) -> Output1:
        return self.solve(memory, use_conditionals=False)

    def part_2(self, memory: Input) -> Output2:
        return self.solve(memory, use_conditionals=True)

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

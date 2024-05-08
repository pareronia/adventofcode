#! /usr/bin/env python3
#
# Advent of Code 2017 Day 5
#

import sys
from typing import Callable

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[int]
Output1 = int
Output2 = int


TEST = """\
0
3
0
1
-3
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [int(_) for _ in input_data]

    def count_jumps(
        self, input: list[int], jump_calculator: Callable[[int], int]
    ) -> int:
        offsets = [_ for _ in input]
        i = 0
        cnt = 0
        while i < len(offsets):
            jump = offsets[i]
            offsets[i] = jump_calculator(jump)
            i += jump
            cnt += 1
        return cnt

    def part_1(self, input: Input) -> int:
        return self.count_jumps(input, lambda j: j + 1)

    def part_2(self, input: Input) -> int:
        return self.count_jumps(input, lambda j: j - 1 if j >= 3 else j + 1)

    @aoc_samples(
        (
            ("part_1", TEST, 5),
            ("part_2", TEST, 10),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2017, 5)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

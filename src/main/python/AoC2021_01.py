#! /usr/bin/env python3
#
# Advent of Code 2021 Day 1
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
199
200
208
210
200
207
240
269
260
263
"""


Input = list[int]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [int(_) for _ in input_data]

    def count_increases(self, depths: Input, window: int) -> int:
        return sum(
            depths[i] > depths[i - window] for i in range(window, len(depths))
        )

    def part_1(self, inputs: Input) -> Output1:
        return self.count_increases(inputs, window=1)

    def part_2(self, inputs: Input) -> Output2:
        return self.count_increases(inputs, window=3)

    @aoc_samples(
        (
            ("part_1", TEST, 7),
            ("part_2", TEST, 5),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2021, 1)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

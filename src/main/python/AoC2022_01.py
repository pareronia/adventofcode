#! /usr/bin/env python3
#
# Advent of Code 2022 Day 1
#

import sys

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Block = list[str]
Input = list[Block]
Output1 = int
Output2 = int


TEST = """\
1000
2000
3000

4000

5000
6000

7000
8000
9000

10000
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def _solve(self, blocks: list[Block], count: int) -> int:
        sums = [sum(int(line) for line in block) for block in blocks]
        return sum(_ for _ in sorted(sums)[-count:])

    def parse_input(self, input_data: InputData) -> Input:
        return my_aocd.to_blocks(input_data)

    def part_1(self, blocks: Input) -> Output1:
        return self._solve(blocks, 1)

    def part_2(self, blocks: Input) -> Output2:
        return self._solve(blocks, 3)

    @aoc_samples(
        (
            ("part_1", TEST, 24_000),
            ("part_2", TEST, 45_000),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 1)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2024 Day 19
#

import sys
from functools import cache

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = tuple[tuple[str, ...], list[str]]
Output1 = int
Output2 = int


TEST = """\
r, wr, b, g, bwu, rb, gb, br

brwrr
bggr
gbbr
rrbgbr
ubwu
bwurrg
brgr
bbrgwb
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        lines = list(input_data)
        return tuple(lines[0].split(", ")), lines[2:]

    @cache
    def count(self, design: str, towels: tuple[str]) -> int:
        if len(design) == 0:
            return 1
        return sum(
            self.count(design[len(towel) :], towels)  # noqa E203
            for towel in towels
            if design.startswith(towel)
        )

    def part_1(self, input: Input) -> Output1:
        towels, designs = input
        return sum(self.count(design, towels) > 0 for design in designs)

    def part_2(self, input: Input) -> Output2:
        towels, designs = input
        return sum(self.count(design, towels) for design in designs)

    @aoc_samples(
        (
            ("part_1", TEST, 6),
            ("part_2", TEST, 16),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 19)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

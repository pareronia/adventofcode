#! /usr/bin/env python3
#
# Advent of Code 2025 Day 12
#

import sys
from dataclasses import dataclass
from math import prod
from typing import Self

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
0:
###
##.
##.

1:
###
##.
.##

2:
.##
###
##.

3:
##.
###
##.

4:
###
#..
###

5:
###
.#.
###

4x4: 0 0 0 0 2 0
12x5: 1 0 1 0 2 2
12x5: 1 0 1 0 3 2
"""


@dataclass(frozen=True)
class Shape:
    area: int

    @classmethod
    def from_input(cls, lines: list[str]) -> Self:
        return cls(len(lines) * max(len(line) for line in lines))


@dataclass(frozen=True)
class Region:
    area: int
    quantities: dict[int, int]

    @classmethod
    def from_input(cls, string: str) -> Self:
        sa, sq = string.split(": ")
        area = prod(int(x) for x in sa.split("x"))
        quantities = {i: int(q) for i, q in enumerate(sq.split())}
        return cls(area, quantities)


Input = tuple[list[Region], dict[int, Shape]]
Output1 = int
Output2 = str


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        blocks = my_aocd.to_blocks(input_data)
        shapes = {
            int(block[0][:-1]): Shape.from_input(block[1:])
            for block in blocks[:-1]
        }
        regions = [Region.from_input(line) for line in blocks[-1]]
        return regions, shapes

    def part_1(self, inputs: Input) -> Output1:
        regions, shapes = inputs
        return sum(
            sum(
                quantity * shapes[idx].area
                for idx, quantity in region.quantities.items()
            )
            <= region.area
            for region in regions
        )

    def part_2(self, _: Input) -> Output2:
        return "🎄"

    @aoc_samples(())
    def samples(self) -> None:
        pass


solution = Solution(2025, 12)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

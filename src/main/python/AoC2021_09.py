#! /usr/bin/env python3
#
# Advent of Code 2021 Day 9
#

import sys
from math import prod
from typing import Iterator

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.graph import flood_fill
from aoc.grid import Cell
from aoc.grid import IntGrid

TEST = """\
2199943210
3987894921
9856789892
8767896789
9899965678
"""

Input = IntGrid
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return IntGrid.from_strings(list(input_data))

    def find_lows(self, grid: IntGrid) -> Iterator[Cell]:
        return (
            c
            for c in grid.get_cells()
            if (
                all(
                    grid.get_value(c) < grid.get_value(n)
                    for n in grid.get_capital_neighbours(c)
                )
            )
        )

    def part_1(self, grid: Input) -> int:
        return sum(grid.get_value(c) + 1 for c in self.find_lows(grid))

    def part_2(self, grid: Input) -> int:
        def size_of_basin_around_low(c: Cell) -> int:
            basin = flood_fill(
                c,
                lambda c: (
                    n
                    for n in grid.get_capital_neighbours(c)
                    if grid.get_value(n) != 9
                ),
            )
            return len(basin)

        return prod(
            sorted(
                size_of_basin_around_low(low) for low in self.find_lows(grid)
            )[-3:]
        )

    @aoc_samples(
        (
            ("part_1", TEST, 15),
            ("part_2", TEST, 1134),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2021, 9)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2021 Day 11
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.grid import Cell
from aoc.grid import IntGrid

TEST = """\
5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526
"""


class Flashes:
    value: int

    def __init__(self) -> None:
        self.value = 0

    def increment(self) -> None:
        self.value += 1

    def get(self) -> int:
        return self.value


Input = list[str]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)

    def flash(self, grid: IntGrid, c: Cell, flashes: int) -> int:
        grid.set_value(c, 0)
        flashes += 1
        for n in grid.get_all_neighbours(c):
            if grid.get_value(n) == 0:
                continue
            grid.increment(n)
            if grid.get_value(n) > 9:
                flashes = self.flash(grid, n, flashes)
        return flashes

    def _cycle(self, grid: IntGrid) -> int:
        for c in grid.get_cells():
            grid.increment(c)
        flashes = 0
        for c in (c for c in grid.get_cells() if grid.get_value(c) > 9):
            flashes = self.flash(grid, c, flashes)
        return flashes

    def part_1(self, input: Input) -> Output1:
        grid = IntGrid.from_strings(input)
        return sum(self._cycle(grid) for _ in range(100))

    def part_2(self, input: Input) -> Output2:
        grid = IntGrid.from_strings(input)
        cycle = flashes = 0
        while flashes != grid.size():
            cycle += 1
            flashes = self._cycle(grid)
        return cycle

    @aoc_samples(
        (
            ("part_1", TEST, 1656),
            ("part_2", TEST, 195),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2021, 11)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

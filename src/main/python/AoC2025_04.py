#! /usr/bin/env python3
#
# Advent of Code 2025 Day 4
#

import sys
from collections import deque

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.grid import Cell
from aoc.grid import CharGrid

TEST = """\
..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
@@.@@@@.@@
.@@@@@@@.@
.@.@.@.@@@
@.@@@.@@@@
.@@@@@@@@.
@.@.@@@.@.
"""

Input = CharGrid
Output1 = int
Output2 = int
ROLL, EMPTY = "@", "."


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings(list(input_data))

    def is_removable(self, grid: CharGrid, cell: Cell) -> bool:
        return (
            grid.get_value(cell) == ROLL
            and sum(
                grid.get_value(n) == ROLL
                for n in grid.get_all_neighbours(cell)
            )
            < 4
        )

    def part_1(self, grid: Input) -> Output1:
        return sum(self.is_removable(grid, cell) for cell in grid.get_cells())

    def part_2(self, grid: Input) -> Output2:
        q = deque(
            cell for cell in grid.get_cells() if self.is_removable(grid, cell)
        )
        seen = set[Cell]()
        while len(q) != 0:
            cell = q.popleft()
            if cell in seen:
                continue
            seen.add(cell)
            grid.set_value(cell, EMPTY)
            for n in grid.get_all_neighbours(cell):
                if self.is_removable(grid, n):
                    q.append(n)
        return len(seen)

    @aoc_samples(
        (
            ("part_1", TEST, 13),
            ("part_2", TEST, 43),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2025, 4)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

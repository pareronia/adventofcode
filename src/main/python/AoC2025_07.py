#! /usr/bin/env python3
#
# Advent of Code 2025 Day 7
#

import itertools
import sys
from functools import cache

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.grid import Cell
from aoc.grid import CharGrid

Input = CharGrid
Output1 = int
Output2 = int


TEST = """\
.......S.......
...............
.......^.......
...............
......^.^......
...............
.....^.^.^.....
...............
....^.^...^....
...............
...^.^...^.^...
...............
..^...^.....^..
...............
.^.^.^.^.^...^.
...............
"""

SPLITTER = "^"
START = "S"


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings(list(input_data))

    def part_1(self, grid: Input) -> Output1:
        beams = {next(grid.get_all_equal_to(START)).col}
        ans = 0
        for sp in grid.get_all_equal_to(SPLITTER):
            if sp.col in beams:
                ans += 1
                beams.remove(sp.col)
                for d in (Direction.LEFT, Direction.RIGHT):
                    beams.add(sp.at(d).col)
        return ans

    def part_2(self, grid: Input) -> Output2:
        start = next(grid.get_all_equal_to(SPLITTER))
        splitters = {start} | set(grid.get_all_equal_to(SPLITTER))

        @cache
        def dfs(cell: Cell) -> int:
            if cell == start:
                return 1
            return sum(
                dfs(nxt)
                for nxt in (
                    n.at(d)
                    for d in (Direction.LEFT, Direction.RIGHT)
                    for n in itertools.takewhile(
                        lambda n: n not in splitters, grid.get_cells_n(cell)
                    )
                )
                if nxt in splitters
            )

        bottom_left = Cell(grid.get_max_row_index(), 0)
        return dfs(bottom_left) + sum(
            dfs(cell) for cell in grid.get_cells_e(bottom_left)
        )

    @aoc_samples(
        (
            ("part_1", TEST, 21),
            ("part_2", TEST, 40),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2025, 7)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

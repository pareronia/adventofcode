#! /usr/bin/env python3
#
# Advent of Code 2025 Day 7
#

import sys
from functools import cache

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.grid import Cell
from aoc.grid import CharGrid

Input = InputData
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


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, inputs: Input) -> Output1:
        grid = CharGrid.from_strings(list(inputs))
        beams = {next(grid.get_all_equal_to("S")).col}
        ans = 0
        for sp in grid.get_all_equal_to("^"):
            if sp.col in beams:
                ans += 1
                beams.remove(sp.col)
                for d in (Direction.LEFT, Direction.RIGHT):
                    beams.add(sp.at(d).col)
        return ans

    def part_2(self, inputs: Input) -> Output2:
        grid = CharGrid.from_strings(list(inputs))
        start = next(grid.get_all_equal_to("^"))
        splitters = set(grid.get_all_equal_to("^")) | {start}

        @cache
        def dfs(cell: Cell) -> int:
            if cell == start:
                return 1
            ans = 0
            for n in grid.get_cells_n(cell):
                if n in splitters:
                    break
                for d in (Direction.LEFT, Direction.RIGHT):
                    nxt = n.at(d)
                    if nxt in splitters:
                        ans += dfs(nxt)
            return ans

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

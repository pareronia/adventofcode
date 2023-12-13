#! /usr/bin/env python3
#
# Advent of Code 2023 Day 11
#

import sys
from itertools import combinations
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.grid import CharGrid, Cell


TEST = """\
...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
"""


class Observations(NamedTuple):
    galaxies: list[Cell]
    empty_rows: set[int]
    empty_cols: set[int]


Input = Observations
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        grid = CharGrid.from_strings([line for line in input_data])
        galaxies = [_ for _ in grid.get_all_equal_to("#")]
        empty_rows = {
            i
            for i, row in enumerate(grid.get_rows_as_strings())
            if "#" not in row
        }
        empty_cols = {
            i
            for i, col in enumerate(grid.get_cols_as_strings())
            if "#" not in col
        }
        return Observations(galaxies, empty_rows, empty_cols)

    def solve(self, observations: Input, expansion_rate: int) -> int:
        def distance(first: Cell, second: Cell, factor: int) -> int:
            dr = abs(first.row - second.row)
            lo = min(first.row, second.row)
            rr = {r for r in range(lo, lo + dr)}
            dr += len(observations.empty_rows & rr) * factor
            dc = abs(first.col - second.col)
            lo = min(first.col, second.col)
            rc = {r for r in range(lo, lo + dc)}
            dc += len(observations.empty_cols & rc) * factor
            return dr + dc

        return sum(
            distance(first, second, expansion_rate - 1)
            for first, second in combinations(observations.galaxies, 2)
        )

    def part_1(self, input: Input) -> Output1:
        return self.solve(input, expansion_rate=2)

    def part_2(self, input: Input) -> Output2:
        return self.solve(input, expansion_rate=1_000_000)

    @aoc_samples((("part_1", TEST, 374),))
    def samples(self) -> None:
        pass


solution = Solution(2023, 11)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

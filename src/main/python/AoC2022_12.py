#! /usr/bin/env python3
#
# Advent of Code 2022 Day 12
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.graph import bfs
from aoc.grid import Cell
from aoc.grid import CharGrid

TEST = """\
Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi
"""


Input = CharGrid
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings(list(input_data))

    def solve(self, grid: CharGrid, end_points: set[str]) -> int:
        def get_value(cell: Cell) -> int:
            ch = grid.get_value(cell)
            return (
                ord("a") if ch == "S" else ord("z") if ch == "E" else ord(ch)
            )

        distance, _ = bfs(
            next(grid.get_all_equal_to("E")),
            lambda cell: grid.get_value(cell) in end_points,
            lambda cell: (
                n
                for n in grid.get_capital_neighbours(cell)
                if get_value(cell) - get_value(n) <= 1
            ),
        )
        return distance

    def part_1(self, grid: Input) -> Output1:
        return self.solve(grid, {"S"})

    def part_2(self, grid: Input) -> Output2:
        return self.solve(grid, {"S", "a"})

    @aoc_samples(
        (
            ("part_1", TEST, 31),
            ("part_2", TEST, 29),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 12)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

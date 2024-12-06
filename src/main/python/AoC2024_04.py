#! /usr/bin/env python3
#
# Advent of Code 2024 Day 4
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.grid import CharGrid

Input = CharGrid
Output1 = int
Output2 = int


TEST = """\
MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings(list(input_data))

    def part_1(self, grid: Input) -> Output1:
        return sum(
            all(
                (n := next(it, None)) is not None and grid.get_value(n) == v
                for v in ["M", "A", "S"]
            )
            for it in (
                grid.get_cells_dir(cell, d)
                for cell in grid.get_all_equal_to("X")
                for d in Direction.octants()
            )
        )

    def part_2(self, grid: Input) -> Output2:
        dirs = [
            Direction.LEFT_AND_UP,
            Direction.RIGHT_AND_DOWN,
            Direction.RIGHT_AND_UP,
            Direction.LEFT_AND_DOWN,
        ]
        matches = {"MSMS", "SMSM", "MSSM", "SMMS"}
        return sum(
            grid.get_value(cell) == "A"
            and "".join(grid.get_value(cell.at(d)) for d in dirs) in matches
            for cell in grid.get_cells_without_border()
        )

    @aoc_samples(
        (
            ("part_1", TEST, 18),
            ("part_2", TEST, 9),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 4)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2024 Day 4
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
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
        ans = 0
        for cell in grid.get_all_equal_to("X"):
            for d in Direction.octants():
                w = "X" + "".join(
                    grid.get_value(n) for n in grid.get_cells_dir(cell, d)
                )
                if w[:4] == "XMAS":
                    ans += 1
        return ans

    def part_2(self, grid: Input) -> Output2:
        ans = 0
        for cell in grid.get_all_equal_to("A"):
            if (
                cell.row == 0
                or cell.row == grid.get_max_row_index()
                or cell.col == 0
                or cell.col == grid.get_max_col_index()
            ):
                continue
            w = ""
            for d in [
                Direction.LEFT_AND_UP,
                Direction.RIGHT_AND_DOWN,
                Direction.RIGHT_AND_UP,
                Direction.LEFT_AND_DOWN,
            ]:
                w += grid.get_value(cell.at(d))
            log(w)
            if w in {"MSMS", "SMSM", "MSSM", "SMMS"}:
                ans += 1
        return ans

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

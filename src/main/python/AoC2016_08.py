#! /usr/bin/env python3
#
# Advent of Code 2016 Day 8
#

import itertools
import sys
from typing import cast

from advent_of_code_ocr import convert_6
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.grid import Cell
from aoc.grid import CharGrid

Input = InputData
Output1 = int
Output2 = str

ON = "#"
OFF = "."

TEST = """\
rect 3x2
rotate column x=1 by 1
rotate row y=0 by 4
rotate column x=1 by 1
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def solve(self, input: Input, rows: int, columns: int) -> CharGrid:
        grid = CharGrid.from_strings([OFF * columns for _ in range(rows)])
        for line in input:
            if line.startswith("rect "):
                y, x = map(int, line[len("rect ") :].split("x"))  # noqa E203
                for r, c in itertools.product(range(x), range(y)):
                    grid.set_value(Cell(r, c), ON)
            elif line.startswith("rotate row "):
                offset = len("rotate row ")
                row, amount = line[offset:].split(" by ")
                grid.roll_row(int(row[2:]), int(amount))
            else:
                offset = len("rotate column ")
                column, amount = line[offset:].split(" by ")
                grid.roll_column(int(column[2:]), int(amount))
        return grid

    def solve_1(self, input: Input, rows: int, columns: int) -> Output1:
        grid = self.solve(input, rows, columns)
        return len(list(grid.get_all_equal_to(ON)))

    def part_1(self, input: Input) -> Output1:
        return self.solve_1(input, 6, 50)

    def part_2(self, input: Input) -> Output2:
        grid = self.solve(input, 6, 50)
        return cast(
            str,
            convert_6(
                "\n".join(grid.get_rows_as_strings()),
                fill_pixel=ON,
                empty_pixel=OFF,
            ),
        )

    def samples(self) -> None:
        assert self.solve_1(self.parse_input(TEST.splitlines()), 3, 7) == 6


solution = Solution(2016, 8)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

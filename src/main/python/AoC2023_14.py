#! /usr/bin/env python3
#
# Advent of Code 2023 Day 14
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
from aoc.grid import CharGrid, Cell

Input = CharGrid
Output1 = int
Output2 = int


TEST = """\
O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings([line for line in input_data])

    def part_1(self, grid: Input) -> Output1:
        ans = 0
        for c in range(grid.get_height()):
            cell = Cell(0, c)
            it = grid.get_cells_s(cell)
            last_cube = 0
            count = 0
            while True:
                if grid.get_value(cell) == "#":
                    last_cube = cell.row + 1
                    count = 0
                elif grid.get_value(cell) == "O":
                    ans += grid.get_height() - (last_cube + count)
                    count += 1
                    log(f"O: {cell=}, {count=}, {ans=} ")
                try:
                    cell = next(it)
                except StopIteration:
                    break
        return ans

    def part_2(self, input: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 136),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 14)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2025 Day 4
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.grid import Cell

Input = InputData
Output1 = int
Output2 = int


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


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, inputs: Input) -> Output1:
        grid = list(inputs)
        ans = 0
        for r, row in enumerate(grid):
            for c, ch in enumerate(row):
                if ch == "@":
                    cnt = 0
                    cell = Cell(r, c)
                    for d in Direction.octants():
                        n = cell.at(d)
                        if (
                            0 <= n[0] < len(grid)
                            and 0 <= n[1] < len(row)
                            and grid[n[0]][n[1]] == "@"
                        ):
                            cnt += 1
                    if cnt < 4:
                        ans += 1
        return ans

    def part_2(self, inputs: Input) -> Output2:
        grid = [list(row) for row in inputs]
        ans = 0
        while True:
            new_grid = [list(row) for row in grid]
            for r, row in enumerate(grid):
                for c, ch in enumerate(row):
                    if ch == "@":
                        cnt = 0
                        cell = Cell(r, c)
                        for d in Direction.octants():
                            n = cell.at(d)
                            if (
                                0 <= n[0] < len(grid)
                                and 0 <= n[1] < len(row)
                                and grid[n[0]][n[1]] == "@"
                            ):
                                cnt += 1
                        if cnt < 4:
                            new_grid[r][c] = "."
                            ans += 1
            if new_grid == grid:
                break
            grid = new_grid
        return ans

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

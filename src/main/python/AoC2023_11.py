#! /usr/bin/env python3
#
# Advent of Code 2023 Day 11
#

import sys
from itertools import combinations

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
from aoc.grid import Cell
from aoc.grid import CharGrid

Input = InputData
Output1 = int
Output2 = int


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


def log_grid(grid: CharGrid) -> None:
    if not __debug__:
        return
    for line in grid.get_rows_as_strings():
        for c in line:
            print(c, end="")
        print()


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, input: Input) -> Output1:
        grid = CharGrid.from_strings([line for line in input])
        rows = [row for row in grid.get_rows_as_strings()]
        empty_rows = [
            i for i, row in enumerate(rows) if all(c == "." for c in row)
        ]
        log(empty_rows)
        cols = [col for col in grid.get_cols_as_strings()]
        empty_cols = [
            i for i, col in enumerate(cols) if all(r == "." for r in col)
        ]
        log(empty_cols)
        pairs = [_ for _ in combinations(grid.get_all_equal_to("#"), 2)]
        log(len(pairs))
        ans = 0
        for first, second in pairs:
            dist = 0
            lo = min(first.row, second.row)
            hi = max(first.row, second.row)
            for r in range(lo, hi, 1):
                dist += (2 if r in empty_rows else 1)
            lo = min(first.col, second.col)
            hi = max(first.col, second.col)
            for r in range(lo, hi, 1):
                dist += (2 if r in empty_cols else 1)
            ans += dist
        return ans

    def part_2(self, input: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 374),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 11)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

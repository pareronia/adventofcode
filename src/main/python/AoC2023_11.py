#! /usr/bin/env python3
#
# Advent of Code 2023 Day 11
#

import sys
from itertools import combinations

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
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


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def solve(self, input: Input, expansion_rate: int) -> int:
        grid = CharGrid.from_strings([line for line in input])
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
        pairs = [_ for _ in combinations(grid.get_all_equal_to("#"), 2)]
        extra = expansion_rate - 1
        ans = 0
        for first, second in pairs:
            dr = abs(first.row - second.row)
            lo = min(first.row, second.row)
            rr = {r for r in range(lo, lo + dr)}
            ans += dr + len(empty_rows & rr) * extra
            dc = abs(first.col - second.col)
            lo = min(first.col, second.col)
            rc = {r for r in range(lo, lo + dc)}
            ans += dc + len(empty_cols & rc) * extra
        return ans

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

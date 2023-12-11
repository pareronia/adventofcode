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
        ans = 0
        for first, second in pairs:
            dist = 0
            for r in range(
                min(first.row, second.row), max(first.row, second.row)
            ):
                dist += expansion_rate if r in empty_rows else 1
            for c in range(
                min(first.col, second.col), max(first.col, second.col)
            ):
                dist += expansion_rate if c in empty_cols else 1
            ans += dist
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

#! /usr/bin/env python3
#
# Advent of Code 2025 Day 7
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = tuple[str, ...]
Output1 = int
Output2 = int


TEST = """\
.......S.......
...............
.......^.......
...............
......^.^......
...............
.....^.^.^.....
...............
....^.^...^....
...............
...^.^...^.^...
...............
..^...^.....^..
...............
.^.^.^.^.^...^.
...............
"""

SPLITTER = "^"
START = "S"


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return tuple(input_data)

    def solve(self, grid: Input) -> tuple[int, int]:
        start = grid[0].index(START)
        beams = [0] * len(grid[0])
        beams[start] = 1
        splits = 0
        for r in range(1, len(grid)):
            new_beams = [0] * len(grid[0])
            for c in range(len(grid[0])):
                if grid[r][c] == SPLITTER:
                    new_beams[c - 1] += beams[c]
                    new_beams[c + 1] += beams[c]
                    splits += beams[c] > 0
                else:
                    new_beams[c] += beams[c]
            beams = new_beams
        return splits, sum(beams)

    def part_1(self, grid: Input) -> Output1:
        return self.solve(grid)[0]

    def part_2(self, grid: Input) -> Output2:
        return self.solve(grid)[1]

    @aoc_samples(
        (
            ("part_1", TEST, 21),
            ("part_2", TEST, 40),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2025, 7)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2023 Day 21
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
...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
...........
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings([_ for _ in input_data])

    def part_1(self, grid: Input) -> Output1:
        start = next(grid.get_all_equal_to("S"))
        plots = set[Cell]([start])
        for _ in range(64):
            new_plots = set[Cell]()
            for cell in plots:
                for n in grid.get_capital_neighbours(cell):
                    if grid.get_value(n) != "#":
                        new_plots.add(n)
            plots = new_plots
        return len(plots)

    def part_2(self, input: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            # ("part_1", TEST, 16),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 21)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

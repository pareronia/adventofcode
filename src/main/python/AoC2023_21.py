#! /usr/bin/env python3
#
# Advent of Code 2023 Day 21
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
from aoc.grid import CharGrid

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

DIRS = {(0, 1), (0, -1), (1, 0), (-1, 0)}
STEPS = 26_501_365


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings([_ for _ in input_data])

    def solve(self, grid: CharGrid, steps: int) -> int:
        w = grid.get_width()
        start = (w // 2, w // 2)
        plots = set[tuple[int, int]]([start])
        for _ in range(steps):
            new_plots = set[tuple[int, int]]()
            for r, c in plots:
                for dr, dc in DIRS:
                    rr, cc = r + dr, c + dc
                    wr, wc = rr % w, cc % w
                    if (
                        0 <= wr < w
                        and 0 <= wc < w
                        and grid.values[wr][wc] != "#"
                    ):
                        new_plots.add((rr, cc))
            plots = new_plots
        return len(plots)

    def part_1(self, grid: Input) -> Output1:
        return self.solve(grid, 64)

    def part_2(self, grid: Input) -> Output2:
        w = grid.get_width()
        modulo = STEPS % w
        x = STEPS // w
        values = [self.solve(grid, i * w + modulo) for i in range(3)]
        log(values)
        a = (values[2] + values[0] - 2 * values[1]) // 2
        b = values[1] - values[0] - a
        c = values[0]
        return a * x * x + b * x + c

    @aoc_samples(())
    def samples(self) -> None:
        pass


solution = Solution(2023, 21)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

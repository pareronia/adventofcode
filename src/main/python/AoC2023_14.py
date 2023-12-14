#! /usr/bin/env python3
#
# Advent of Code 2023 Day 14
#

import sys
from collections import defaultdict

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
from aoc.grid import CharGrid

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
    grid: CharGrid
    height: int
    width: int

    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings([line for line in input_data])

    def tilt_up(self) -> set[tuple[int, int]]:
        os = set[tuple[int, int]]()
        for c in range(self.width):
            last_cube = 0
            count = 0
            for r in range(self.height):
                val = self.grid.values[r][c]
                if val == "#":
                    last_cube = r + 1
                    count = 0
                elif val == "O":
                    new_row = last_cube + count
                    os.add((new_row, c))
                    count += 1
        return os

    def tilt_down(self) -> set[tuple[int, int]]:
        os = set[tuple[int, int]]()
        for c in range(self.width):
            last_cube = self.height - 1
            count = 0
            for r in range(self.height - 1, -1, -1):
                val = self.grid.values[r][c]
                if val == "#":
                    last_cube = r - 1
                    count = 0
                elif val == "O":
                    new_row = last_cube - count
                    os.add((new_row, c))
                    count += 1
        return os

    def tilt_left(self) -> set[tuple[int, int]]:
        os = set[tuple[int, int]]()
        for r in range(self.height):
            last_cube = 0
            count = 0
            for c in range(self.width):
                val = self.grid.values[r][c]
                if val == "#":
                    last_cube = c + 1
                    count = 0
                elif val == "O":
                    new_col = last_cube + count
                    os.add((r, new_col))
                    count += 1
        return os

    def tilt_right(self) -> set[tuple[int, int]]:
        os = set[tuple[int, int]]()
        for r in range(self.height):
            last_cube = self.width - 1
            count = 0
            for c in range(self.width - 1, -1, -1):
                val = self.grid.values[r][c]
                if val == "#":
                    last_cube = c - 1
                    count = 0
                elif val == "O":
                    new_col = last_cube - count
                    os.add((r, new_col))
                    count += 1
        return os

    def spin_cycle(self) -> set[tuple[int, int]]:
        os = self.tilt_up()
        self.redraw(os)
        os = self.tilt_left()
        self.redraw(os)
        os = self.tilt_down()
        self.redraw(os)
        os = self.tilt_right()
        self.redraw(os)
        return os

    def redraw(self, os: set[tuple[int, int]]) -> None:
        for r in range(self.height):
            for c in range(self.width):
                val = self.grid.values[r][c]
                if (r, c) in os:
                    self.grid.values[r][c] = "O"
                elif val != "#":
                    self.grid.values[r][c] = "."

    def calc_load(self, os: set[tuple[int, int]]) -> int:
        return sum(self.height - row for row, _ in os)

    def part_1(self, grid_in: Input) -> Output1:
        self.grid = grid_in
        self.height = self.grid.get_height()
        self.width = self.grid.get_width()
        os = self.tilt_up()
        self.redraw(os)
        return self.calc_load(os)

    def part_2(self, grid_in: Input) -> Output2:
        self.grid = grid_in
        self.height = self.grid.get_height()
        self.width = self.grid.get_width()
        d = defaultdict[frozenset[tuple[int, int]], list[int]](list)
        i = 0
        while True:
            if i % 100 == 0:
                log(i)
            i += 1
            os = self.spin_cycle()
            key = frozenset(os)
            d[key].append(i)
            if i > 100:
                cycle = d[key]
                if len(cycle) < 2:
                    continue
                period = cycle[1] - cycle[0]
                loops = (1_000_000_000 - i) // period
                left = 1_000_000_000 - (i + loops * period)
                log(f"{i=}, {d[key]=}, {period=}, {loops=}, {left=}")
                assert i + loops * period + left == 1_000_000_000
                for i in range(left):
                    os = self.spin_cycle()
                return self.calc_load(os)

    @aoc_samples(
        (
            ("part_1", TEST, 136),
            ("part_2", TEST, 64),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 14)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

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
from aoc.grid import Cell
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
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings([line for line in input_data])

    def tilt_up(self, grid: CharGrid) -> set[Cell]:
        os = set[Cell]()
        for c in range(grid.get_width()):
            cell = Cell(0, c)
            it = grid.get_cells_s(cell)
            last_cube = 0
            count = 0
            while True:
                if grid.get_value(cell) == "#":
                    last_cube = cell.row + 1
                    count = 0
                elif grid.get_value(cell) == "O":
                    new_row = last_cube + count
                    os.add(Cell(new_row, c))
                    count += 1
                try:
                    cell = next(it)
                except StopIteration:
                    break
        return os

    def tilt_down(self, grid: CharGrid) -> set[Cell]:
        os = set[Cell]()
        for c in range(grid.get_width()):
            cell = Cell(grid.get_height() - 1, c)
            it = grid.get_cells_n(cell)
            last_cube = grid.get_height() - 1
            count = 0
            while True:
                if grid.get_value(cell) == "#":
                    last_cube = cell.row - 1
                    count = 0
                elif grid.get_value(cell) == "O":
                    new_row = last_cube - count
                    os.add(Cell(new_row, c))
                    count += 1
                try:
                    cell = next(it)
                except StopIteration:
                    break
        return os

    def tilt_left(self, grid: CharGrid) -> set[Cell]:
        os = set[Cell]()
        for r in range(grid.get_height()):
            cell = Cell(r, 0)
            it = grid.get_cells_e(cell)
            last_cube = 0
            count = 0
            while True:
                if grid.get_value(cell) == "#":
                    last_cube = cell.col + 1
                    count = 0
                elif grid.get_value(cell) == "O":
                    new_col = last_cube + count
                    os.add(Cell(r, new_col))
                    count += 1
                try:
                    cell = next(it)
                except StopIteration:
                    break
        return os

    def tilt_right(self, grid: CharGrid) -> set[Cell]:
        os = set[Cell]()
        for r in range(grid.get_height()):
            cell = Cell(r, grid.get_width() - 1)
            it = grid.get_cells_w(cell)
            last_cube = grid.get_width() - 1
            count = 0
            while True:
                if grid.get_value(cell) == "#":
                    last_cube = cell.col - 1
                    count = 0
                elif grid.get_value(cell) == "O":
                    new_col = last_cube - count
                    os.add(Cell(r, new_col))
                    count += 1
                try:
                    cell = next(it)
                except StopIteration:
                    break
        return os

    def spin_cycle(self, grid: CharGrid) -> tuple[CharGrid, set[Cell]]:
        os = self.tilt_up(grid)
        grid = self.redraw(grid, os)
        os = self.tilt_left(grid)
        grid = self.redraw(grid, os)
        os = self.tilt_down(grid)
        grid = self.redraw(grid, os)
        os = self.tilt_right(grid)
        grid = self.redraw(grid, os)
        return grid, os

    def redraw(self, grid: CharGrid, os: set[Cell]) -> CharGrid:
        for cell in grid.get_cells():
            val = grid.get_value(cell)
            if cell in os:
                grid.set_value(cell, "O")
            elif val != "#":
                grid.set_value(cell, ".")
        return grid

    def calc_load(self, grid: CharGrid, os: set[Cell]) -> int:
        return sum(grid.get_height() - o.row for o in os)

    def part_1(self, grid: Input) -> Output1:
        os = self.tilt_up(grid)
        grid = self.redraw(grid, os)
        return self.calc_load(grid, os)

    def part_2(self, grid: Input) -> Output2:
        start_grid = CharGrid.from_strings(
            [row for row in grid.get_rows_as_strings()]
        )
        d = defaultdict[tuple[Cell, ...], list[int]](list)
        i = 0
        while True:
            if i % 100 == 0:
                log(i)
            grid, os = self.spin_cycle(grid)
            key = tuple(o for o in sorted(os))
            if i > 100 and key in d and len(d[key]) > 2:
                cycle = d[key]
                period = cycle[1] - cycle[0]
                offset = cycle[0]
                log(f"{i=}, {d[key]=}, {offset=}, {period=}")
                break
            d[key].append(i)
            i += 1
        grid = start_grid
        for i in range(offset):
            grid, os = self.spin_cycle(grid)
        for i in range((1_000_000_000 - offset) % period):
            grid, os = self.spin_cycle(grid)
        return self.calc_load(grid, os)

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

#! /usr/bin/env python3
#
# Advent of Code 2024 Day 20
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
from aoc.geometry import Direction
from aoc.graph import bfs
from aoc.grid import Cell
from aoc.grid import CharGrid

Input = CharGrid
Output1 = int
Output2 = int


TEST = """\
###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings(list(input_data))

    def solve_1(self, grid: CharGrid, target: int) -> int:
        for cell in grid.get_cells():
            if grid.get_value(cell) == "S":
                start = cell
            if grid.get_value(cell) == "E":
                end = cell
        time, path = bfs(
            start,
            lambda cell: cell == end,
            lambda cell: (
                n
                for n in grid.get_capital_neighbours(cell)
                if grid.get_value(n) != "#"
            ),
        )
        log(f"{time=}")
        seen = set[Cell]()
        ans = 0
        for p in path:
            for dir in Direction.capitals():
                it = grid.get_cells_dir(p, dir)
                first = next(it)
                try:
                    second = next(it)
                except StopIteration:
                    continue
                if (
                    grid.get_value(first) == "#"
                    and grid.get_value(second) == "."
                ):
                    if first in seen:
                        continue
                    seen.add(first)
                    cheat, _ = bfs(
                        start,
                        lambda cell: cell == end,
                        lambda cell: (
                            n
                            for n in grid.get_capital_neighbours(cell)
                            if grid.get_value(n) != "#" or n == first
                        ),
                    )
                    if time - cheat >= target:
                        log(f"{p=}, {dir=}, {time - cheat}")
                        ans += 1
        return ans

    def part_1(self, grid: Input) -> Output1:
        return self.solve_1(grid, 100)

    def part_2(self, grid: Input) -> Output2:
        return 0

    # @aoc_samples(
    #     (
    #         ("part_1", TEST, 0),
    #         # ("part_2", TEST, "TODO"),
    #     )
    # )
    def samples(self) -> None:
        input = self.parse_input(TEST.splitlines())
        assert self.solve_1(input, 20) == 5


solution = Solution(2024, 20)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

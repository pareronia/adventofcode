#! /usr/bin/env python3
#
# Advent of Code 2024 Day 20
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.graph import bfs
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

    def solve(self, grid: CharGrid, cheat_len: int, target: int) -> int:
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

        ans = 0
        for i1 in range(len(path) - cheat_len):
            for i2 in range(i1 + cheat_len, len(path)):
                p1, p2 = path[i1], path[i2]
                md = abs(p1.row - p2.row) + abs(p1.col - p2.col)
                if md <= cheat_len and i2 - i1 - md >= target:
                    ans += 1
        return ans

    def part_1(self, grid: Input) -> Output1:
        return self.solve(grid, 2, 100)

    def part_2(self, grid: Input) -> Output2:
        return self.solve(grid, 20, 100)

    def samples(self) -> None:
        input = self.parse_input(TEST.splitlines())
        assert self.solve(input, 2, 2) == 44
        assert self.solve(input, 20, 50) == 285


solution = Solution(2024, 20)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

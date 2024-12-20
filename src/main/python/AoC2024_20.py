#! /usr/bin/env python3
#
# Advent of Code 2024 Day 20
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.graph import bfs_full
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
        start = next(
            cell for cell in grid.get_cells() if grid.get_value(cell) == "S"
        )
        distances, _ = bfs_full(
            start,
            lambda cell: grid.get_value(cell) != "#",
            lambda cell: (
                n
                for n in grid.get_capital_neighbours(cell)
                if grid.get_value(n) != "#"
            ),
        )
        dist = {(k.row, k.col): v for k, v in distances.items()}
        ans = 0
        for r, c in dist.keys():
            for md in range(2, cheat_len + 1):
                for dr in range(md + 1):
                    dc = md - dr
                    for rr, cc in {
                        (r + dr, c + dc),
                        (r + dr, c - dc),
                        (r - dr, c + dc),
                        (r - dr, c - dc),
                    }:
                        if (rr, cc) not in dist:
                            continue
                        if dist[(rr, cc)] - dist[(r, c)] >= target + md:
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

#! /usr/bin/env python3
#
# Advent of Code 2024 Day 10
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.graph import flood_fill
from aoc.grid import IntGrid
from aoc.grid import Cell

Input = InputData
Output1 = int
Output2 = int


TEST = """\
89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, input: Input) -> Output1:
        grid = IntGrid.from_strings(list(input))
        starts = [cell for cell in grid.get_all_equal_to(0)]
        ans = 0
        for s in starts:
            path = flood_fill(
                s,
                lambda cell: (
                    n
                    for n in grid.get_capital_neighbours(cell)
                    if grid.get_value(n) == grid.get_value(cell) + 1
                ),
            )
            ans += sum(1 for cell in path if grid.get_value(cell) == 9)
        return ans

    def part_2(self, input: Input) -> Output2:
        grid = IntGrid.from_strings(list(input))
        starts = [cell for cell in grid.get_all_equal_to(0)]
        paths = set[frozenset[Cell]]()

        def dfs(path: frozenset[Cell], start: Cell) -> None:
            if len(path) == 10 and path not in paths:
                paths.add(path)
                return
            val = grid.get_value(start)
            for n in grid.get_capital_neighbours(start):
                if grid.get_value(n) == val + 1:
                    new_path = {_ for _ in path}
                    new_path.add(n)
                    dfs(frozenset(new_path), n)

        for s in starts:
            dfs(frozenset({s}), s)
        return len(paths)

    @aoc_samples(
        (
            ("part_1", TEST, 36),
            ("part_2", TEST, 81),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 10)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

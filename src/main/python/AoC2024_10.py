#! /usr/bin/env python3
#
# Advent of Code 2024 Day 10
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.grid import Cell
from aoc.grid import IntGrid

Input = IntGrid
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
        return IntGrid.from_strings(list(input_data))

    def get_trails(self, grid: IntGrid) -> list[list[Cell]]:
        trails = list[list[Cell]]()

        def dfs(trail: list[Cell]) -> None:
            if len(trail) == 10:
                trails.append(trail)
                return
            nxt = grid.get_value(trail[-1]) + 1
            for n in grid.get_capital_neighbours(trail[-1]):
                if grid.get_value(n) == nxt:
                    dfs(trail + [n])

        list(map(lambda s: dfs([s]), grid.get_all_equal_to(0)))
        return trails

    def part_1(self, grid: Input) -> Output1:
        trails = self.get_trails(grid)
        return sum(
            len({trail[9] for trail in trails if trail[0] == zero})
            for zero in {trail[0] for trail in trails}
        )

    def part_2(self, grid: Input) -> Output2:
        return len(self.get_trails(grid))

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

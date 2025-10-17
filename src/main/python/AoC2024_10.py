#! /usr/bin/env python3
#
# Advent of Code 2024 Day 10
#

import sys
from collections import deque
from enum import Enum
from enum import auto
from enum import unique

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


@unique
class Grading(Enum):
    SCORE = auto()
    RATING = auto()

    def get(self, trails: list[list[Cell]]) -> int:
        match self:
            case Grading.SCORE:
                return len({trail[-1] for trail in trails})
            case Grading.RATING:
                return len(trails)


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return IntGrid.from_strings(list(input_data))

    def solve(self, grid: IntGrid, grading: Grading) -> int:
        def bfs(trail_head: Cell) -> list[list[Cell]]:
            trails = list[list[Cell]]()
            q = deque([[trail_head]])
            while q:
                trail = q.pop()
                nxt = len(trail)
                if nxt == 10:
                    trails.append(trail)
                    continue
                for n in grid.get_capital_neighbours(trail[-1]):
                    if grid.get_value(n) == nxt:
                        q.append([*trail, n])
            return trails

        return sum(
            grading.get(bfs(trail_head))
            for trail_head in grid.get_all_equal_to(0)
        )

    def part_1(self, grid: Input) -> Output1:
        return self.solve(grid, Grading.SCORE)

    def part_2(self, grid: Input) -> Output2:
        return self.solve(grid, Grading.RATING)

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

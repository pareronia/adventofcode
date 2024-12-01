#! /usr/bin/env python3
#
# Advent of Code 2015 Day 18
#

import functools
import sys
from collections import Counter
from typing import Iterable
from typing import cast

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.game_of_life import ClassicRules
from aoc.game_of_life import GameOfLife
from aoc.grid import Cell


class FixedGrid(GameOfLife.Universe[Cell]):
    def __init__(self, input: InputData):
        lines = list(input)
        self.height = len(lines)
        self.width = len(lines[0])
        self.initial_alive = {
            Cell(r, c)
            for r in range(self.height)
            for c in range(self.width)
            if lines[r][c] == "#"
        }
        self.stuck_positions = {
            Cell(0, 0),
            Cell(0, self.width - 1),
            Cell(self.height - 1, 0),
            Cell(self.height - 1, self.width - 1),
        }

    def neighbour_count(self, alive: Iterable[Cell]) -> dict[Cell, int]:
        return Counter(n for cell in alive for n in self._neighbours(cell))

    @functools.cache
    def _neighbours(self, cell: Cell) -> set[Cell]:
        return {
            n
            for n in cell.get_all_neighbours()
            if n.row >= 0
            and n.row < self.height
            and n.col >= 0
            and n.col < self.width
        }


Input = FixedGrid
Output1 = int
Output2 = int


TEST = """\
.#.#.#
...##.
#....#
..#...
#.#..#
####..
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return FixedGrid(input_data)

    def solve_1(self, grid: Input, generations: int) -> Output1:
        game_of_life = GameOfLife(
            grid.initial_alive, grid, ClassicRules()
        )  # type:ignore[misc]
        for _ in range(generations):
            game_of_life.next_generation()
        return len(set(game_of_life.alive))

    def solve_2(self, grid: Input, generations: int) -> Output2:
        game_of_life = GameOfLife(
            grid.initial_alive | grid.stuck_positions, grid, ClassicRules()
        )  # type:ignore[misc]
        for _ in range(generations):
            game_of_life.next_generation()
            alive = set(cast(Iterable[Cell], game_of_life.alive))
            alive |= grid.stuck_positions
            game_of_life = GameOfLife(
                alive, grid, ClassicRules()
            )  # type:ignore[misc]
        return len(set(game_of_life.alive))

    def part_1(self, input: Input) -> Output1:
        return self.solve_1(input, 100)

    def part_2(self, input: Input) -> Output2:
        return self.solve_2(input, 100)

    def samples(self) -> None:
        assert self.solve_1(self.parse_input(TEST.splitlines()), 4) == 4
        assert self.solve_2(self.parse_input(TEST.splitlines()), 5) == 17


solution = Solution(2015, 18)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

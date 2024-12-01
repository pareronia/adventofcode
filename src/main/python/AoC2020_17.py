#! /usr/bin/env python3
#
# Advent of Code 2020 Day 17
#

import sys
from typing import Callable

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.game_of_life import ClassicRules
from aoc.game_of_life import GameOfLife
from aoc.game_of_life import InfiniteGrid

TEST = """\
.#.
..#
###
"""

ON = "#"
GENERATIONS = 6
CellFactory = Callable[[int, int], tuple[int, ...]]
Input = InputData
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def solve(self, inputs: Input, cell_factory: CellFactory) -> int:
        alive = {
            cell_factory(r, c)
            for r, row in enumerate(inputs)
            for c, state in enumerate(row)
            if state == ON
        }
        game_of_life = GameOfLife(alive, InfiniteGrid(), ClassicRules())
        for i in range(GENERATIONS):
            game_of_life.next_generation()
        return sum(1 for _ in game_of_life.alive)

    def part_1(self, inputs: Input) -> int:
        return self.solve(inputs, cell_factory=lambda r, c: (0, r, c))

    def part_2(self, inputs: Input) -> int:
        return self.solve(inputs, cell_factory=lambda r, c: (0, 0, r, c))

    @aoc_samples(
        (
            ("part_1", TEST, 112),
            ("part_2", TEST, 848),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2020, 17)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

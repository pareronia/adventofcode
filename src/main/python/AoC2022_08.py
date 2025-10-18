#! /usr/bin/env python3
#
# Advent of Code 2022 Day 8
#

import sys
from collections.abc import Iterator
from dataclasses import dataclass
from math import prod
from typing import Self

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.grid import Cell
from aoc.grid import IntGrid

TEST = """\
30373
25512
65332
33549
33548
"""


@dataclass(frozen=True)
class Trees:
    grid: IntGrid

    @classmethod
    def from_input(cls, inputs: InputData) -> Self:
        return cls(IntGrid([[int(c) for c in line] for line in inputs]))

    def capital_directions(self, cell: Cell) -> Iterator[Iterator[Cell]]:
        yield self.grid.get_cells_n(cell)
        yield self.grid.get_cells_e(cell)
        yield self.grid.get_cells_s(cell)
        yield self.grid.get_cells_w(cell)

    def count_visible_from_outside(self) -> int:
        return (
            2 * (self.grid.get_height() + self.grid.get_width())
            - 4
            + sum(
                any(
                    all(
                        self.grid.get_value(c) < self.grid.get_value(cell)
                        for c in cells
                    )
                    for cells in self.capital_directions(cell)
                )
                for cell in self.grid.get_cells_without_border()
            )
        )

    def max_scenic_score(self) -> int:
        def viewing_distance(direction: Iterator[Cell], val: int) -> int:
            n = 0
            stop = False
            for c in direction:
                if stop:
                    break
                n += 1
                stop = self.grid.get_value(c) >= val
            return n

        return max(
            prod(
                viewing_distance(direction, self.grid.get_value(cell))
                for direction in self.capital_directions(cell)
            )
            for cell in self.grid.get_cells_without_border()
        )


Input = Trees
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Trees.from_input(input_data)

    def part_1(self, trees: Input) -> Output1:
        return trees.count_visible_from_outside()

    def part_2(self, trees: Input) -> Output2:
        return trees.max_scenic_score()

    @aoc_samples(
        (
            ("part_1", TEST, 21),
            ("part_2", TEST, 8),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 8)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

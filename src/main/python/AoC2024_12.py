#! /usr/bin/env python3
#
# Advent of Code 2024 Day 12
#

import sys
from collections import defaultdict
from collections.abc import Iterator
from enum import Enum
from enum import auto
from enum import unique
from typing import Self

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.graph import flood_fill
from aoc.grid import Cell

Input = InputData
Output1 = int
Output2 = int

CORNER_DIRS = [
    [Direction.LEFT_AND_UP, Direction.LEFT, Direction.UP],
    [Direction.RIGHT_AND_UP, Direction.RIGHT, Direction.UP],
    [Direction.RIGHT_AND_DOWN, Direction.RIGHT, Direction.DOWN],
    [Direction.LEFT_AND_DOWN, Direction.LEFT, Direction.DOWN],
]

TEST1 = """\
AAAA
BBCD
BBCC
EEEC
"""
TEST2 = """\
OOOOO
OXOXO
OOOOO
OXOXO
OOOOO
"""
TEST3 = """\
RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE
"""
TEST4 = """\
EEEEE
EXXXX
EEEEE
EXXXX
EEEEE
"""
TEST5 = """\
AAAAAA
AAABBA
AAABBA
ABBAAA
ABBAAA
AAAAAA
"""


@unique
class Pricing(Enum):
    PERIMETER = auto()
    NUMBER_OF_SIDES = auto()

    def calculate(self, plot: Cell, region: set[Cell]) -> int:
        match self:
            case Pricing.PERIMETER:
                return 4 - sum(
                    n in region for n in plot.get_capital_neighbours()
                )
            case Pricing.NUMBER_OF_SIDES:
                return sum(
                    tuple(
                        1 if plot.at(d[i]) in region else 0 for i in range(3)
                    )
                    in ((0, 0, 0), (1, 0, 0), (0, 1, 1))
                    for d in CORNER_DIRS
                )


class RegionIterator(Iterator[set[Cell]]):
    def __init__(self, plots_by_plant: dict[str, set[Cell]]) -> None:
        self.plots_by_plant = plots_by_plant
        self.keys = iter(plots_by_plant.keys())
        self.key: str | None = next(self.keys)
        self.all_plots_with_plant = plots_by_plant[self.key]

    def __iter__(self) -> Self:
        return self

    def has_next(self) -> bool:
        if len(self.all_plots_with_plant) > 0:
            return True
        self.key = next(self.keys, None)
        return self.key is not None

    def __next__(self) -> set[Cell]:
        if not self.has_next():
            raise StopIteration
        assert self.key is not None
        if len(self.all_plots_with_plant) == 0:
            self.all_plots_with_plant = self.plots_by_plant[self.key]
        region = flood_fill(
            next(iter(self.all_plots_with_plant)),
            lambda cell: (
                n
                for n in cell.get_capital_neighbours()
                if n in self.all_plots_with_plant
            ),
        )
        self.all_plots_with_plant -= region
        return region


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def solve(self, garden_map: Input, pricing: Pricing) -> int:
        plots_by_plant = defaultdict[str, set[Cell]](set)
        for r, row in enumerate(garden_map):
            for c, plant in enumerate(row):
                plots_by_plant[plant].add(Cell(r, c))
        return sum(
            sum(pricing.calculate(plot, region) for plot in region)
            * len(region)
            for region in RegionIterator(plots_by_plant)
        )

    def part_1(self, garden_map: Input) -> Output1:
        return self.solve(garden_map, Pricing.PERIMETER)

    def part_2(self, garden_map: Input) -> Output2:
        return self.solve(garden_map, Pricing.NUMBER_OF_SIDES)

    @aoc_samples(
        (
            ("part_1", TEST1, 140),
            ("part_1", TEST2, 772),
            ("part_1", TEST3, 1930),
            ("part_2", TEST1, 80),
            ("part_2", TEST2, 436),
            ("part_2", TEST3, 1206),
            ("part_2", TEST4, 236),
            ("part_2", TEST5, 368),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 12)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2024 Day 12
#

import sys
from collections import defaultdict
from enum import Enum
from enum import auto
from enum import unique
from typing import Iterator

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


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def solve(self, input: Input, pricing: Pricing) -> int:
        def get_regions(input: Input) -> Iterator[set[Cell]]:
            plots_by_plant = defaultdict[str, set[Cell]](set)
            for r, row in enumerate(input):
                for c, plant in enumerate(row):
                    plots_by_plant[plant].add(Cell(r, c))
            for all_plots_with_plant in plots_by_plant.values():
                while all_plots_with_plant:
                    region = flood_fill(
                        next(iter(all_plots_with_plant)),
                        lambda cell: (
                            n
                            for n in cell.get_capital_neighbours()
                            if n in all_plots_with_plant
                        ),
                    )
                    yield region
                    all_plots_with_plant -= region

        return sum(
            sum(pricing.calculate(plot, region) for plot in region)
            * len(region)
            for region in get_regions(input)
        )

    def part_1(self, input: Input) -> Output1:
        return self.solve(input, Pricing.PERIMETER)

    def part_2(self, input: Input) -> Output2:
        return self.solve(input, Pricing.NUMBER_OF_SIDES)

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

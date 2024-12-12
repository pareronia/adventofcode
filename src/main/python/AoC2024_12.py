#! /usr/bin/env python3
#
# Advent of Code 2024 Day 12
#

import sys
from collections import defaultdict

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.graph import flood_fill
from aoc.grid import Cell
from aoc.grid import CharGrid

Input = CharGrid
Output1 = int
Output2 = int


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


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings(list(input_data))

    def get_regions(self, grid: CharGrid) -> dict[str, list[set[Cell]]]:
        p = defaultdict[str, set[Cell]](set)
        for plot in grid.get_cells():
            p[grid.get_value(plot)].add(plot)
        regions = defaultdict[str, list[set[Cell]]](list)
        for k, all_p in p.items():
            while all_p:
                r = flood_fill(
                    next(iter(all_p)),
                    lambda cell: (
                        n
                        for n in grid.get_capital_neighbours(cell)
                        if n in all_p
                    ),
                )
                regions[k].append(r)
                all_p -= r
        return regions

    def part_1(self, grid: Input) -> Output1:
        regions = self.get_regions(grid)
        d = list[list[int]]()
        for plant in regions:
            for region in regions[plant]:
                dd = list[int]()
                for plot in region:
                    c = 4
                    for n in grid.get_capital_neighbours(plot):
                        if grid.get_value(n) == plant:
                            c -= 1
                    dd.append(c)
                d.append(dd)
        return sum(len(dd) * sum(dd) for dd in d)

    def part_2(self, grid: Input) -> Output2:
        def count_corners(plot: Cell, region: set[Cell]) -> int:
            ans = 0
            DO = [
                [Direction.LEFT, Direction.LEFT_AND_UP, Direction.UP],
                [Direction.UP, Direction.RIGHT_AND_UP, Direction.RIGHT],
                [Direction.RIGHT, Direction.RIGHT_AND_DOWN, Direction.DOWN],
                [Direction.DOWN, Direction.LEFT_AND_DOWN, Direction.LEFT],
            ]
            for d in DO:
                o = list(
                    filter(
                        lambda n: n in region, map(lambda dd: plot.at(dd), d)
                    )
                )
                if len(o) == 0:
                    ans += 1
            if (
                plot.at(Direction.LEFT_AND_DOWN) not in region
                and plot.at(Direction.LEFT) in region
                and plot.at(Direction.DOWN) in region
            ):
                ans += 1
            if (
                plot.at(Direction.LEFT_AND_UP) not in region
                and plot.at(Direction.LEFT) in region
                and plot.at(Direction.UP) in region
            ):
                ans += 1
            if (
                plot.at(Direction.RIGHT_AND_DOWN) not in region
                and plot.at(Direction.RIGHT) in region
                and plot.at(Direction.DOWN) in region
            ):
                ans += 1
            if (
                plot.at(Direction.RIGHT_AND_UP) not in region
                and plot.at(Direction.RIGHT) in region
                and plot.at(Direction.UP) in region
            ):
                ans += 1
            if (
                plot.at(Direction.LEFT_AND_DOWN) in region
                and plot.at(Direction.LEFT) not in region
                and plot.at(Direction.DOWN) not in region
            ):
                ans += 1
            if (
                plot.at(Direction.LEFT_AND_UP) in region
                and plot.at(Direction.LEFT) not in region
                and plot.at(Direction.UP) not in region
            ):
                ans += 1
            if (
                plot.at(Direction.RIGHT_AND_DOWN) in region
                and plot.at(Direction.RIGHT) not in region
                and plot.at(Direction.DOWN) not in region
            ):
                ans += 1
            if (
                plot.at(Direction.RIGHT_AND_UP) in region
                and plot.at(Direction.RIGHT) not in region
                and plot.at(Direction.UP) not in region
            ):
                ans += 1
            return ans

        d = list[list[int]]()
        regions = self.get_regions(grid)
        for plant in regions:
            for region in regions[plant]:
                dd = list[int]()
                for plot in region:
                    dd.append(count_corners(plot, region))
                d.append(dd)
        return sum(len(dd) * sum(dd) for dd in d)

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

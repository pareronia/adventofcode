#! /usr/bin/env python3
#
# Advent of Code 2019 Day 10
#

from __future__ import annotations

import math
import sys
from functools import reduce
from typing import Iterator

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.grid import CharGrid

Input = CharGrid
Output1 = int
Output2 = int
Position = tuple[int, int]
ASTEROID = "#"


TEST1 = """\
.#..#
.....
#####
....#
...##
"""
TEST2 = """\
......#.#.
#..#.#....
..#######.
.#.#.###..
.#..#.....
..#....#.#
#..#....#.
.##.#..###
##...#..#.
.#....####
"""
TEST3 = """\
#.#...#.#.
.###....#.
.#....#...
##.#.#.#.#
....#.#.#.
.##..###.#
..#...##..
..##....##
......#...
.####.###.
"""
TEST4 = """\
.#..#..###
####.###.#
....###.#.
..###.##.#
##.##.#.#.
....###..#
..#.#..#.#
#..#.#.###
.##...##.#
.....#.#..
"""
TEST5 = """\
.#..##.###...#######
##.############..##.
.#.######.########.#
.###.#######.####.#.
#####.##.#.##.###.##
..#####..#.#########
####################
#.####....###.#.#.##
##.#################
#####.##.###..####..
..######..##.#######
####.##.####...##..#
.#####..#.######.###
##...#.##########...
#.##########.#######
.####.#.###.###.#.##
....##.##.###..#####
.#.#.###########.###
#.#.#.#####.####.###
###.##.####.##.#..##
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings(list(input_data))

    def part_1(self, grid: Input) -> Output1:
        return len(self.best(grid))

    def part_2(self, grid: Input) -> Output2:
        d = self.best(grid)
        x, y = d[sorted(d.keys())[199]]
        return x * 100 + y

    def best(self, grid: CharGrid) -> dict[float, Position]:
        def asteroids() -> Iterator[Position]:
            for r in range(grid.get_height()):
                for c in range(grid.get_width()):
                    if grid.get_value_at(r, c) == ASTEROID:
                        yield (c, r)

        def angles(asteroid: Position) -> dict[float, Position]:
            def merge(
                d: dict[float, Position], other: Position
            ) -> dict[float, Position]:
                def angle(asteroid: Position, other: Position) -> float:
                    angle = (
                        math.atan2(
                            other[1] - asteroid[1], other[0] - asteroid[0]
                        )
                        + math.pi / 2
                    )
                    return angle + 2 * math.pi if angle < 0 else angle

                def distance_squared(a: Position, b: Position) -> int:
                    dx = a[0] - b[0]
                    dy = a[1] - b[1]
                    return dx**2 + dy**2

                a = angle(asteroid, other)
                d[a] = (
                    min(
                        other,
                        d[a],
                        key=lambda x: distance_squared(x, asteroid),
                    )
                    if a in d
                    else other
                )
                return d

            return reduce(
                merge,
                filter(lambda x: x != asteroid, asteroids()),
                dict[float, Position](),
            )

        return max((angles((x, y)) for x, y in asteroids()), key=len)

    @aoc_samples(
        (
            ("part_1", TEST1, 8),
            ("part_1", TEST2, 33),
            ("part_1", TEST3, 35),
            ("part_1", TEST4, 41),
            ("part_1", TEST5, 210),
            ("part_2", TEST5, 802),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2019, 10)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

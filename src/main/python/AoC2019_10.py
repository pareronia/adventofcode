#! /usr/bin/env python3
#
# Advent of Code 2019 Day 10
#

from __future__ import annotations

import math
import sys
from functools import reduce
from typing import Iterator
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.grid import CharGrid

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


def asteroids(grid: CharGrid) -> Iterator[Position]:
    return (
        (c, r)
        for r in range(grid.get_height())
        for c in range(grid.get_width())
        if grid.get_value_at(r, c) == ASTEROID
    )


class Asteroid(NamedTuple):
    position: Position
    others: dict[float, Position]

    @classmethod
    def from_(cls, position: Position, grid: CharGrid) -> Asteroid:
        return Asteroid(position, Asteroid.angles(grid, position))

    @classmethod
    def angles(
        cls, grid: CharGrid, asteroid: Position
    ) -> dict[float, Position]:
        def merge(
            d: dict[float, Position], other: Position
        ) -> dict[float, Position]:

            def angle(asteroid: Position, other: Position) -> float:
                angle = (
                    math.atan2(other[1] - asteroid[1], other[0] - asteroid[0])
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
            filter(lambda x: x != asteroid, asteroids(grid)),
            dict[float, Position](),
        )


Input = list[Asteroid]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        grid = CharGrid.from_strings(list(input_data))
        return [Asteroid.from_(p, grid) for p in asteroids(grid)]

    def best(self, asteroids: list[Asteroid]) -> Asteroid:
        return max(asteroids, key=lambda a: len(a.others))

    def part_1(self, asteroids: Input) -> Output1:
        return len(self.best(asteroids).others)

    def part_2(self, asteroids: Input) -> Output2:
        d = self.best(asteroids).others
        x, y = d[sorted(d.keys())[199]]
        return x * 100 + y

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

#! /usr/bin/env python3
#
# Advent of Code 2024 Day 8
#

import itertools
import sys
from collections import defaultdict
from enum import Enum
from enum import auto
from enum import unique
from functools import reduce
from operator import ior
from typing import Iterator

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Position
from aoc.geometry import Vector

Antenna = Position
AntennaPair = tuple[Antenna, Antenna]
Input = tuple[int, int, list[set[Antenna]]]
Output1 = int
Output2 = int


TEST = """\
............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............
"""


@unique
class Mode(Enum):
    MODE_1 = auto()
    MODE_2 = auto()

    def collect_antinodes(
        self, h: int, w: int, pair: AntennaPair
    ) -> set[Position]:
        def get_antinodes(max_count: int) -> Iterator[Antenna]:
            vec = Vector.of(pair[0].x - pair[1].x, pair[0].y - pair[1].y)
            for pos, d in itertools.product(pair, {-1, 1}):
                for a in range(1, max_count + 1):
                    antinode = pos.translate(vec, d * a)
                    if 0 <= antinode.x < w and 0 <= antinode.y < h:
                        yield antinode
                    else:
                        break

        match self:
            case Mode.MODE_1:
                return set(get_antinodes(max_count=1)) - {
                    pair[0],
                    pair[1],
                }
            case Mode.MODE_2:
                return set(get_antinodes(max_count=sys.maxsize))


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        lines = list(input_data)
        h, w = len(lines), len(lines[0])
        antennae = defaultdict[str, set[Antenna]](set)
        for r, c in itertools.product(range(h), range(w)):
            if (freq := lines[r][c]) != ".":
                antennae[freq].add(Antenna(c, h - r - 1))
        return h, w, list(antennae.values())

    def solve(
        self, h: int, w: int, antennae: list[set[Antenna]], mode: Mode
    ) -> int:
        return len(
            reduce(
                ior,
                (
                    mode.collect_antinodes(h, w, pair)
                    for same_frequency in antennae
                    for pair in itertools.combinations(same_frequency, 2)
                ),
            )
        )

    def part_1(self, input: Input) -> Output1:
        return self.solve(*input, Mode.MODE_1)

    def part_2(self, input: Input) -> Output2:
        return self.solve(*input, Mode.MODE_2)

    @aoc_samples(
        (
            ("part_1", TEST, 14),
            ("part_2", TEST, 34),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 8)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2020 Day 24
#
"""
Coordinate system:
    NW: 1,-1    NE: 0,1
    W : -1,0    0,0         E: 1,0
                SW: 0,-1    SE: 1,-1
"""

from __future__ import annotations

import re
import sys
from collections import Counter
from typing import Iterable
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.game_of_life import GameOfLife


class Direction(NamedTuple):
    q: int
    r: int


DIRS = {
    "ne": Direction(1, -1),
    "nw": Direction(0, -1),
    "se": Direction(0, 1),
    "sw": Direction(-1, 1),
    "e": Direction(1, 0),
    "w": Direction(-1, 0),
}
Tile = tuple[int, int]


class Floor(NamedTuple):
    tiles: set[Tile]

    @classmethod
    def from_input(self, input: list[str]) -> Floor:
        p = re.compile(r"(n?(e|w)|s?(e|w))")
        tiles = set[Tile]()
        for line in input:
            tile = (0, 0)
            for m in p.finditer(line):
                d = DIRS[m.group(0)]
                tile = (tile[0] + d.q, tile[1] + d.r)
            if tile in tiles:
                tiles.remove(tile)
            else:
                tiles.add(tile)
        return Floor(tiles)


class HexGrid(GameOfLife.Universe[Tile]):
    def neighbour_count(self, alive: Iterable[Tile]) -> dict[Tile, int]:
        return Counter(
            (t[0] + d.q, t[1] + d.r) for d in DIRS.values() for t in alive
        )


class Rules(GameOfLife.Rules[Tile]):
    def alive(self, tile: Tile, count: int, alive: Iterable[Tile]) -> bool:
        return count == 2 or (count == 1 and tile in alive)


TEST = """\
sesenwnenenewseeswwswswwnenewsewsw
neeenesenwnwwswnenewnwwsewnenwseswesw
seswneswswsenwwnwse
nwnwneseeswswnenewneswwnewseswneseene
swweswneswnenwsewnwneneseenw
eesenwseswswnenwswnwnwsewwnwsene
sewnenenenesenwsewnenwwwse
wenwwweseeeweswwwnwwe
wsweesenenewnwwnwsenewsenwwsesesenwne
neeswseenwwswnwswswnw
nenwswwsewswnenenewsenwsenwnesesenew
enewnwewneswsewnwswenweswnenwsenwsw
sweneswneswneneenwnewenewwneswswnese
swwesenesewenwneswnwwneseswwne
enesenwswwswneneswsenwnewswseenwsese
wnwnesenesenenwwnenwsewesewsesesew
nenewswnwewswnenesenwnesewesw
eneswnwswnwsenenwnwnwwseeswneewsenese
neswnwewnwnwseenwseesewsenwsweewe
wseweeenwnesenwwwswnew
"""


Input = Floor
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Floor.from_input(list(input_data))

    def part_1(self, floor: Floor) -> int:
        return len(floor.tiles)

    def part_2(self, floor: Floor) -> int:
        gol = GameOfLife(floor.tiles, HexGrid(), Rules())
        for _ in range(100):
            gol.next_generation()
        return sum(1 for _ in gol.alive)

    @aoc_samples(
        (
            ("part_1", TEST, 10),
            ("part_2", TEST, 2208),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2020, 24)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

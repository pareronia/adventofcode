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
import aocd
import re
from functools import lru_cache
from dataclasses import dataclass
from aoc import my_aocd
from aoc.common import log
from aoc.geometry import Position
from aoc.navigation import Heading, Waypoint, NavigationWithWaypoint


E = "e"
SE = "se"
SW = "sw"
W = "w"
NW = "nw"
NE = "ne"


@dataclass(frozen=True)
class NavigationInstruction:
    heading: Heading
    value: int


@dataclass(frozen=True)
class Floor:
    tiles: set[tuple[int, int]]

    def flip(self, tile: tuple[int, int]):
        if tile in self.tiles:
            self.tiles.remove(tile)
        else:
            self.tiles.add(tile)


def _parse(inputs: tuple[str]) -> list[list[NavigationInstruction]]:
    navs = list[list[NavigationInstruction]]()
    for input_ in inputs:
        nav = list[NavigationInstruction]()
        m = re.findall(r"(n?(e|w)|s?(e|w))", input_)
        for _ in m:
            heading = _[0]
            if heading == E:
                nav.append(NavigationInstruction(Heading.EAST, 1))
            elif heading == SE:
                nav.append(NavigationInstruction(Heading.SOUTH, 1))
                nav.append(NavigationInstruction(Heading.EAST, 1))
            elif heading == W:
                nav.append(NavigationInstruction(Heading.WEST, 1))
            elif heading == SW:
                nav.append(NavigationInstruction(Heading.SOUTH, 1))
            elif heading == NW:
                nav.append(NavigationInstruction(Heading.NORTH, 1))
                nav.append(NavigationInstruction(Heading.WEST, 1))
            elif heading == NE:
                nav.append(NavigationInstruction(Heading.NORTH, 1))
            else:
                raise ValueError("invalid input")
        navs.append(nav)
    return navs


def _navigate_with_waypoint(
    navigation: NavigationWithWaypoint, nav: NavigationInstruction
) -> None:
    navigation.update_waypoint(nav.heading, nav.value)


def _build_floor(navs: list[list[NavigationInstruction]]) -> Floor:
    floor = Floor(set())
    for nav in navs:
        navigation = NavigationWithWaypoint(Position(0, 0), Waypoint(0, 0))
        [_navigate_with_waypoint(navigation, n) for n in nav]
        floor.flip((navigation.waypoint.x, navigation.waypoint.y))
    return floor


def part_1(inputs: tuple[str]) -> int:
    navs = _parse(inputs)
    floor = _build_floor(navs)
    return len(floor.tiles)


@lru_cache(maxsize=11000)
def _get_neighbours(x: int, y: int) -> list[Position]:
    return [
        ((x + dx, y + dy))
        for dx, dy in [(-1, 1), (0, 1), (-1, 0), (1, 0), (0, -1), (1, -1)]
    ]


def _run_cycle(floor: Floor) -> Floor:
    def to_check():
        for tile in floor.tiles:
            # check the positions of all existing black tiles
            yield tile
            # also check all their neighbour positions (a position can only
            #  become black if it is adjacent to at least 1 black tile)
            for n in _get_neighbours(*tile):
                yield n

    new_tiles = set()
    # for each position:
    for p in to_check():
        # does it have black neighbour? yes: if that neighbour
        #  is an existing black tile; no: if it isn't
        bn = 0
        for n in _get_neighbours(*p):
            if n in floor.tiles:
                bn += 1
        # apply rules
        if p in floor.tiles and bn in {1, 2}:
            new_tiles.add(p)
        if p not in floor.tiles and bn == 2:
            new_tiles.add(p)
    return Floor(new_tiles)


def part_2(inputs: tuple[str]) -> int:
    navs = _parse(inputs)
    floor = _build_floor(navs)
    log(len(floor.tiles))
    for _ in range(100):
        floor = _run_cycle(floor)
        log(len(floor.tiles))
    log(_get_neighbours.cache_info())
    return len(floor.tiles)


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
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2020, 24)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 10
    assert part_2(TEST) == 2208

    inputs = my_aocd.get_input_data(puzzle, 316)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2020 Day 24
#

from __future__ import annotations
import re
from dataclasses import dataclass
import my_aocd
from common import log
from geometry import Position
from navigation import Headings, Waypoint, NavigationWithWaypoint


E = "e"
SE = "se"
SW = "sw"
W = "w"
NW = "nw"
NE = "ne"
NORTH = "N"
EAST = "E"
SOUTH = "S"
WEST = "W"
BLACK = "B"
WHITE = "W"


@dataclass(frozen=True)
class NavigationInstruction:
    action: str
    value: int


@dataclass
class Tile:
    x: int
    y: int
    color: str

    def __init__(self, x: int, y: int, color: str):
        self.x = x
        self.y = y
        self.color = color

    def flip(self):
        if self.color == WHITE:
            self.color = BLACK
        else:
            self.color = WHITE

    def __eq__(self, other: Tile):
        return self.x == other.x and self.y == other.y

    def get_neighbours(self) -> list[Position]:
        neighbours = []
        neighbours.append(Position(self.x-1, self.y+2))
        neighbours.append(Position(self.x+1, self.y+2))
        neighbours.append(Position(self.x+2, self.y))
        neighbours.append(Position(self.x+1, self.y-2))
        neighbours.append(Position(self.x-1, self.y-2))
        neighbours.append(Position(self.x-2, self.y))
        return neighbours


@dataclass(frozen=True)
class Floor:
    tiles: list[Tile]

    def get(self, x: int, y: int):
        for tile in self.tiles:
            if tile.x == x and tile.y == y:
                return tile

    def flip(self, position: Position):
        tile = self.get(position.x, position.y)
        if tile is None:
            tile = Tile(position.x, position.y, WHITE)
            self.tiles.append(tile)
            log("new tile")
        if tile.color == WHITE:
            tile.color = BLACK
            log("flip to black")
        else:
            tile.color = WHITE
            log("flip to white")

    def count_black(self):
        return sum([1 for tile in self.tiles if tile.color == BLACK])


def _parse(inputs: tuple[str]) -> list[list[NavigationInstruction]]:
    navs = list[list[NavigationInstruction]]()
    for input_ in inputs:
        nav = list[NavigationInstruction]()
        m = re.findall(r"(n?(e|w)|s?(e|w))", input_)
        for _ in m:
            heading = _[0]
            if heading == E:
                nav.append(NavigationInstruction(EAST, 2))
            elif heading == SE:
                nav.append(NavigationInstruction(SOUTH, 2))
                nav.append(NavigationInstruction(EAST, 1))
            elif heading == W:
                nav.append(NavigationInstruction(WEST, 2))
            elif heading == SW:
                nav.append(NavigationInstruction(SOUTH, 2))
                nav.append(NavigationInstruction(WEST, 1))
            elif heading == NW:
                nav.append(NavigationInstruction(NORTH, 2))
                nav.append(NavigationInstruction(WEST, 1))
            elif heading == NE:
                nav.append(NavigationInstruction(NORTH, 2))
                nav.append(NavigationInstruction(EAST, 1))
            else:
                raise ValueError("invalid input")
        navs.append(nav)
    return navs


def _navigate_with_waypoint(navigation: NavigationWithWaypoint,
                            nav: NavigationInstruction) -> None:
    if nav.action in {NORTH, EAST, SOUTH, WEST}:
        navigation.update_waypoint(heading=Headings[nav.action].value,
                                   amount=nav.value)
    else:
        raise ValueError("invalid input")


def _build_floor(navs: list[list[NavigationInstruction]]) -> Floor:
    floor = Floor([])
    for nav in navs:
        start = Waypoint(0, 0)
        navigation = NavigationWithWaypoint(Position(0, 0), start)
        for n in nav:
            _navigate_with_waypoint(navigation, n)
        floor.flip(navigation.waypoint)
    return floor


def part_1(inputs: tuple[str]) -> int:
    navs = _parse(inputs)
    floor = _build_floor(navs)
    for tile in floor.tiles:
        log(tile)
    return floor.count_black()


def _run_cycle(floor: Floor) -> Floor:
    new_tiles = [Tile(t.x, t.y, t.color) for t in floor.tiles
                 if t.color == BLACK]
    for tile in new_tiles:
        for p in tile.get_neighbours():
            if tile not in new_tiles:
                new_tiles.append(Tile(p.x, p.y, WHITE))
    new_floor = Floor(new_tiles)
    flips = []
    for new_tile in new_floor.tiles:
        neighbours = [new_floor.get(p.x, p.y)
                      for p in new_tile.get_neighbours()]
        bn = sum([1 for t in neighbours
                  if t is not None and t.color == BLACK])
        if new_tile.color == BLACK:
            if bn == 0 or bn > 2:
                flips.append((new_tile.x, new_tile.y))
        elif new_tile.color == WHITE:
            if bn == 2:
                flips.append((new_tile.x, new_tile.y))
        else:
            raise ValueError("color should only be black or white")
    for flip in flips:
        new_floor.get(flip[0], flip[1]).flip()
    return new_floor


def part_2(inputs: tuple[str]) -> int:
    navs = _parse(inputs)
    floor = _build_floor(navs)
    log(len(floor.tiles))
    log(floor.count_black())
    for i in range(1):
        floor = _run_cycle(floor)
        log(len(floor.tiles))
        log(floor.count_black())
        print(".", end="", flush=True)
    return floor.count_black()


test = """\
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
    my_aocd.print_header(2020, 24)

    assert part_1(test) == 10
    assert part_2(test) == 15

    # inputs = my_aocd.get_input_as_tuple(2020, 24, 316)
    # result1 = part_1(inputs)
    # print(f"Part 1: {result1}")
    # # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

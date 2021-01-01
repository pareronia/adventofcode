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


@dataclass(frozen=True)
class NavigationInstruction:
    action: str
    value: int


@dataclass(frozen=True)
class Tile:
    x: int
    y: int

    def get_neighbours(self) -> list[Position]:
        neighbours = []
        neighbours.append(Position(self.x-1, self.y+1))
        neighbours.append(Position(self.x, self.y+1))
        neighbours.append(Position(self.x-1, self.y))
        neighbours.append(Position(self.x+1, self.y))
        neighbours.append(Position(self.x, self.y-1))
        neighbours.append(Position(self.x+1, self.y-1))
        return neighbours


@dataclass(frozen=True)
class Floor:
    tiles: set[Tile]

    def get(self, x: int, y: int):
        for tile in self.tiles:
            if tile.x == x and tile.y == y:
                return tile

    def flip(self, position: Position):
        tile = self.get(position.x, position.y)
        if tile is None:
            self.tiles.add(Tile(position.x, position.y))
        else:
            self.tiles.remove(tile)


def _parse(inputs: tuple[str]) -> list[list[NavigationInstruction]]:
    """
    Coordinate system:
        NW: 1,-1    NE: 0,1
        W : -1,0    0,0         E: 1,0
                    SW: 0,-1    SE: 1,-1
    """
    navs = list[list[NavigationInstruction]]()
    for input_ in inputs:
        nav = list[NavigationInstruction]()
        m = re.findall(r"(n?(e|w)|s?(e|w))", input_)
        for _ in m:
            heading = _[0]
            if heading == E:
                nav.append(NavigationInstruction(EAST, 1))
            elif heading == SE:
                nav.append(NavigationInstruction(SOUTH, 1))
                nav.append(NavigationInstruction(EAST, 1))
            elif heading == W:
                nav.append(NavigationInstruction(WEST, 1))
            elif heading == SW:
                nav.append(NavigationInstruction(SOUTH, 1))
            elif heading == NW:
                nav.append(NavigationInstruction(NORTH, 1))
                nav.append(NavigationInstruction(WEST, 1))
            elif heading == NE:
                nav.append(NavigationInstruction(NORTH, 1))
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
    floor = Floor(set())
    for nav in navs:
        navigation = NavigationWithWaypoint(Position(0, 0), Waypoint(0, 0))
        [_navigate_with_waypoint(navigation, n) for n in nav]
        floor.flip(navigation.waypoint)
    return floor


def part_1(inputs: tuple[str]) -> int:
    navs = _parse(inputs)
    floor = _build_floor(navs)
    return len(floor.tiles)


def _run_cycle(floor: Floor) -> Floor:
    # check the positions of all existing black tiles
    check = [Position(tile.x, tile.y) for tile in floor.tiles]
    # also check all their neighbour positions (a position can only
    #  become black if it is adjacent to at least 1 black tile)
    for tile in floor.tiles:
        for n in tile.get_neighbours():
            check.append(n)
    new_tiles = set()
    # for each position:
    for p in check:
        # does it have black neighbour? yes: if that neighbour
        #  is an existing black tile; no: if it isn't
        bn = 0
        for n in Tile(p.x, p.y).get_neighbours():
            if Tile(n.x, n.y) in floor.tiles:
                bn += 1
        # apply rules
        tile = Tile(p.x, p.y)
        if tile in floor.tiles and bn in {1, 2}:
            new_tiles.add(tile)
        if tile not in floor.tiles and bn == 2:
            new_tiles.add(tile)
    new_floor = Floor(new_tiles)
    return new_floor


def part_2(inputs: tuple[str]) -> int:
    navs = _parse(inputs)
    floor = _build_floor(navs)
    log(len(floor.tiles))
    for i in range(100):
        floor = _run_cycle(floor)
        log(len(floor.tiles))
        print(".", end="", flush=True)
    print("")
    return len(floor.tiles)


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
    assert part_2(test) == 2208

    inputs = my_aocd.get_input_as_tuple(2020, 24, 316)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

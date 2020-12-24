#! /usr/bin/env python3
#
# Advent of Code 2020 Day 24
#

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


@dataclass
class Tile(Position):
    color: str

    def __init__(self, x: int, y: int, color: str):
        super().__init__(x, y)
        self.color = color


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
            tile = Tile(position.x, position.y, "white")
            self.tiles.append(tile)
            log("new tile")
        if tile.color == "white":
            tile.color = "black"
            log("flip to black")
        else:
            tile.color = "white"
            log("flip to white")

    def count_black(self):
        return sum([1 for tile in self.tiles if tile.color == "black"])


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


def part_1(inputs: tuple[str]) -> int:
    navs = _parse(inputs)
    # assert len(navs) == 20
    floor = Floor([])
    for nav in navs:
        start = Waypoint(0, 0)
        navigation = NavigationWithWaypoint(Position(0, 0), start)
        for n in nav:
            _navigate_with_waypoint(navigation, n)
        floor.flip(navigation.waypoint)
    for tile in floor.tiles:
        log(tile)
    return floor.count_black()


def part_2(inputs: tuple[str]) -> int:
    return 0


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
    assert part_2(test) == 0

    inputs = my_aocd.get_input_as_tuple(2020, 24, 316)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

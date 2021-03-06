#! /usr/bin/env python3
#
# Advent of Code 2020 Day 12
#

from typing import NamedTuple
from aoc import my_aocd
from aoc.common import log
from aoc.geometry import Position
from aoc.navigation import Headings, Waypoint,\
        NavigationWithHeading, NavigationWithWaypoint


NORTH = "N"
EAST = "E"
SOUTH = "S"
WEST = "W"
RIGHT = "R"
LEFT = "L"
FORWARD = "F"


class NavigationInstruction(NamedTuple):
    action: str
    value: int


def _parse(inputs: tuple[str]) -> list[NavigationInstruction]:
    return [NavigationInstruction(i[0], int(i[1:])) for i in inputs]


def _navigate_with_heading(navigation: NavigationWithHeading,
                           nav: NavigationInstruction) -> None:
    if nav.action == RIGHT:
        navigation.right(nav.value)
    elif nav.action == LEFT:
        navigation.left(nav.value)
    elif nav.action == FORWARD:
        navigation.forward(nav.value)
    elif nav.action in {NORTH, EAST, SOUTH, WEST}:
        navigation.drift(heading=Headings[nav.action].value,
                         amount=nav.value)
    else:
        raise ValueError("invalid input")


def part_1(inputs: tuple[str]) -> int:
    navs = _parse(inputs)
    start = EAST
    navigation = NavigationWithHeading(
        position=Position(0, 0),
        heading=Headings[start].value)
    log(navigation)
    for nav in navs:
        _navigate_with_heading(navigation, nav)
        log(navigation)
    return abs(navigation.position.x) + abs(navigation.position.y)


def _navigate_with_waypoint(navigation: NavigationWithWaypoint,
                            nav: NavigationInstruction) -> None:
    if nav.action == RIGHT:
        navigation.right(nav.value)
    elif nav.action == LEFT:
        navigation.left(nav.value)
    elif nav.action == FORWARD:
        navigation.forward(nav.value)
    elif nav.action in {NORTH, EAST, SOUTH, WEST}:
        navigation.update_waypoint(heading=Headings[nav.action].value,
                                   amount=nav.value)
    else:
        raise ValueError("invalid input")


def part_2(inputs: tuple[str]) -> int:
    navs = _parse(inputs)
    start = Waypoint(10, 1)
    navigation = NavigationWithWaypoint(Position(0, 0), start)
    log(navigation)
    for nav in navs:
        _navigate_with_waypoint(navigation, nav)
        log(navigation)
    return abs(navigation.position.x) + abs(navigation.position.y)


TEST = """\
F10
N3
F7
R90
F11
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 12)

    assert part_1(TEST) == 25
    assert part_2(TEST) == 286

    inputs = my_aocd.get_input(2020, 12, 785)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

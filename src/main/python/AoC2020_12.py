#! /usr/bin/env python3
#
# Advent of Code 2020 Day 12
#

from __future__ import annotations
from typing import NamedTuple
import aocd
from aoc import my_aocd
from aoc.common import log
from aoc.geometry import Position, Turn
from aoc.navigation import (
    Heading,
    Waypoint,
    NavigationWithHeading,
    NavigationWithWaypoint,
)
from enum import Enum


class Action(Enum):
    NORTH = "N"
    EAST = "E"
    SOUTH = "S"
    WEST = "W"
    RIGHT = "R"
    LEFT = "L"
    FORWARD = "F"

    @classmethod
    def from_str(cls, s: str) -> Action:
        for v in Action:
            if v.value == s:
                return v
        raise ValueError


class NavigationInstruction(NamedTuple):
    action: Action
    value: int


def _parse(inputs: tuple[str, ...]) -> list[NavigationInstruction]:
    return [
        NavigationInstruction(Action.from_str(i[0]), int(i[1:]))
        for i in inputs
    ]


def _navigate_with_heading(
    navigation: NavigationWithHeading, nav: NavigationInstruction
) -> None:
    if nav.action == Action.RIGHT:
        navigation.turn(Turn.from_degrees(nav.value))
    elif nav.action == Action.LEFT:
        navigation.turn(Turn.from_degrees(360 - nav.value))
    elif nav.action == Action.FORWARD:
        navigation.forward(nav.value)
    elif nav.action in {Action.NORTH, Action.EAST, Action.SOUTH, Action.WEST}:
        navigation.drift(heading=Heading[nav.action.name], amount=nav.value)


def part_1(inputs: tuple[str, ...]) -> int:
    navs = _parse(inputs)
    navigation = NavigationWithHeading(
        position=Position(0, 0), heading=Heading.EAST
    )
    log(navigation)
    for nav in navs:
        _navigate_with_heading(navigation, nav)
        log(navigation)
    return abs(navigation.position.x) + abs(navigation.position.y)


def _navigate_with_waypoint(
    navigation: NavigationWithWaypoint, nav: NavigationInstruction
) -> None:
    if nav.action == Action.RIGHT:
        navigation.right(nav.value)
    elif nav.action == Action.LEFT:
        navigation.left(nav.value)
    elif nav.action == Action.FORWARD:
        navigation.forward(nav.value)
    elif nav.action in {Action.NORTH, Action.EAST, Action.SOUTH, Action.WEST}:
        navigation.update_waypoint(
            heading=Heading[nav.action.name], amount=nav.value
        )
    else:
        raise ValueError("invalid input")


def part_2(inputs: tuple[str, ...]) -> int:
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
    puzzle = aocd.models.Puzzle(2020, 12)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 25  # type:ignore[arg-type]
    assert part_2(TEST) == 286  # type:ignore[arg-type]

    inputs = my_aocd.get_input_data(puzzle, 785)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

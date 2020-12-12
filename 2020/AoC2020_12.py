#! /usr/bin/env python3
#
# Advent of Code 2020 Day 12
#

from __future__ import annotations
from dataclasses import dataclass
import my_aocd
from common import log
from geometry import Position, Vector


NORTH = "N"
EAST = "E"
SOUTH = "S"
WEST = "W"
RIGHT = "R"
LEFT = "L"
FORWARD = "F"


@dataclass
class NavigationInstruction:
    action: str
    value: int


@dataclass
class Waypoint(Vector):
    pass


@dataclass
class Heading(Vector):
    name: str

    def __init__(self, x: int, y: int, name: str):
        if x not in {-1, 0, 1} or y not in {-1, 0, 1}:
            raise ValueError(f"Invalid Heading: {x}, {y}")
        super().__init__(x, y)
        self.name = name

    @classmethod
    def get(cls, x: int, y: int) -> Heading:
        for h in HEADINGS.values():
            if h.x == x and h.y == y:
                return h
        raise ValueError(f"Invalid Heading: {x}, {y}")

    @classmethod
    def rotate(cls, heading: Heading, degrees: int) -> Heading:
        v = Vector(heading.x, heading.y)
        v.rotate(degrees)
        return Heading.get(v.x, v.y)


HEADINGS = {NORTH: Heading(0, 1, NORTH),
            EAST: Heading(1, 0, EAST),
            SOUTH: Heading(0, -1, SOUTH),
            WEST: Heading(-1, 0, WEST)}


def _parse(inputs: tuple[str]) -> list[NavigationInstruction]:
    return [NavigationInstruction(i[0], int(i[1:])) for i in inputs]


def _navigate_1(position: Position, heading: Heading,
                nav: NavigationInstruction) -> (Position, Heading):
    if nav.action == RIGHT:
        heading = Heading.rotate(heading, nav.value)
    elif nav.action == LEFT:
        heading = Heading.rotate(heading, -nav.value)
    elif nav.action == FORWARD:
        position.translate(vector=heading, amplitude=nav.value)
    elif nav.action in {NORTH, EAST, SOUTH, WEST}:
        position.translate(vector=HEADINGS[nav.action], amplitude=nav.value)
    else:
        raise ValueError("invalid input")
    return position, heading


def part_1(inputs: tuple[str], start: str) -> int:
    navs = _parse(inputs)
    position = Position(0, 0)
    heading = HEADINGS[start]
    log({"position": position,
         "heading": Heading.get(heading.x, heading.y).name})
    for nav in navs:
        position, heading = _navigate_1(position, heading, nav)
        heading_s = Heading.get(heading.x, heading.y).name
        log(f"{nav} ->"
            f"{{'position': {position}, 'heading': {heading_s}}}")
    return abs(position.x) + abs(position.y)


def _navigate_2(position: Position, waypoint: Waypoint,
                nav: NavigationInstruction) -> (Position, Waypoint):
    if nav.action == RIGHT:
        waypoint.rotate(nav.value)
    elif nav.action == LEFT:
        waypoint.rotate(-nav.value)
    elif nav.action == FORWARD:
        position.translate(vector=waypoint, amplitude=nav.value)
    elif nav.action in {NORTH, EAST, SOUTH, WEST}:
        waypoint.add(vector=HEADINGS[nav.action], amplitude=nav.value)
    else:
        raise ValueError("invalid input")
    return position, waypoint


def part_2(inputs: tuple[str], start: Waypoint) -> int:
    navs = _parse(inputs)
    position = Position(0, 0)
    waypoint = start
    log({"position": position, "waypoint": waypoint})
    for nav in navs:
        position, waypoint = _navigate_2(position, waypoint, nav)
        log(f"{nav} -> {{'position': {position}, 'waypoint': {waypoint}}}")
    return abs(position.x) + abs(position.y)


test = """\
F10
N3
F7
R90
F11
"""


def main() -> None:
    my_aocd.print_header(2020, 12)

    assert part_1(test.split(), EAST) == 25
    assert part_2(test.split(), Waypoint(10, 1)) == 286

    inputs = my_aocd.get_input_as_tuple(2020, 12, 785)
    result1 = part_1(inputs, start=EAST)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs, start=Waypoint(10, 1))
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

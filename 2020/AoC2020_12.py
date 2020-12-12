#! /usr/bin/env python3
#
# Advent of Code 2020 Day 12
#
from dataclasses import dataclass
import my_aocd
from common import log


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
class Position:
    x: int
    y: int


@dataclass
class Vector:
    x: int
    y: int


def _parse(inputs: tuple[str]) -> list[NavigationInstruction]:
    return [NavigationInstruction(i[0], int(i[1:])) for i in inputs]


def _new_orientation(start: str, action: str, value: int) -> str:
    if value == 180:
        if start == NORTH:
            return SOUTH
        elif start == EAST:
            return WEST
        elif start == SOUTH:
            return NORTH
        elif start == WEST:
            return EAST
        else:
            raise ValueError("invalid input")
    if value == 270:
        if action == LEFT:
            action = RIGHT
            value = 90
        elif action == RIGHT:
            action = LEFT
            value = 90
        else:
            raise ValueError("invalid input")
    results = dict()
    results[NORTH + "/" + RIGHT] = EAST
    results[NORTH + "/" + LEFT] = WEST
    results[EAST + "/" + RIGHT] = SOUTH
    results[EAST + "/" + LEFT] = NORTH
    results[SOUTH + "/" + RIGHT] = WEST
    results[SOUTH + "/" + LEFT] = EAST
    results[WEST + "/" + RIGHT] = NORTH
    results[WEST + "/" + LEFT] = SOUTH
    return results[start + "/" + action]


def _move(position: Position, direction: str, value: int) -> Position:
    if direction == NORTH:
        vector = Vector(0, value)
    elif direction == EAST:
        vector = Vector(value, 0)
    elif direction == SOUTH:
        vector = Vector(0, -value)
    elif direction == WEST:
        vector = Vector(-value, 0)
    else:
        raise ValueError("invalid input")
    return _translate(position, vector)


def _translate(position: Position, vector: Vector) -> Position:
    return Position(position.x+vector.x, position.y+vector.y)


def _rotate(position: Position, degrees: int) -> Position:
    if degrees < 0:
        degrees = 360 + degrees
    if degrees == 90:
        return Position(position.y, -position.x)
    elif degrees == 180:
        return Position(-position.x, -position.y)
    elif degrees == 270:
        return Position(-position.y, position.x)
    else:
        raise ValueError("invalid input")


def _navigate_1(position: Position, orientation: str,
                nav: NavigationInstruction) -> (Position, str):
    if nav.action in {RIGHT, LEFT}:
        orientation = _new_orientation(orientation, nav.action, nav. value)
    elif nav.action == FORWARD:
        position = _move(position, orientation, nav.value)
    else:
        position = _move(position, nav.action, nav.value)
    return position, orientation


def part_1(inputs: tuple[str], start: str) -> int:
    navs = _parse(inputs)
    position = Position(0, 0)
    orientation = start
    log({"position": position, "orientation": orientation})
    for nav in navs:
        position, orientation = _navigate_1(position, orientation, nav)
        log(f"{nav} ->"
            f"{{'position': {position}, 'orientation': {orientation}}}")
    return abs(position.x) + abs(position.y)


def _navigate_2(position: Position, waypoint: Position,
                nav: NavigationInstruction) -> (Position, Position):
    if nav.action == FORWARD:
        vector = Vector(waypoint.x*nav.value, waypoint.y*nav.value)
        position = _translate(position, vector)
    elif nav.action in {NORTH, EAST, SOUTH, WEST}:
        waypoint = _move(waypoint, nav.action, nav.value)
    elif nav.action == RIGHT:
        waypoint = _rotate(waypoint, nav.value)
    elif nav.action == LEFT:
        waypoint = _rotate(waypoint, -nav.value)
    else:
        raise ValueError("invalid input")
    return position, waypoint


def part_2(inputs: tuple[str], waypoint: Position) -> int:
    navs = _parse(inputs)
    position = Position(0, 0)
    waypoint = Position(waypoint.x, waypoint.y)
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
    assert part_2(test.split(), Position(10, 1)) == 286

    inputs = my_aocd.get_input_as_tuple(2020, 12, 785)
    result1 = part_1(inputs, EAST)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs, Position(10, 1))
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

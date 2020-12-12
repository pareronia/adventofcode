#! /usr/bin/env python3
#
# Advent of Code 2020 Day 12
#
from dataclasses import dataclass
import my_aocd
from common import log


@dataclass
class NavigationInstruction:
    action: str
    value: int


@dataclass
class Position:
    x: int
    y: int


def _parse(inputs: tuple[str]) -> list[NavigationInstruction]:
    return [NavigationInstruction(i[0], int(i[1:])) for i in inputs]


def _new_orientation(start: str, action: str, value: int) -> str:
    if value == 180:
        if start == "N":
            return "S"
        elif start == "E":
            return "W"
        elif start == "S":
            return "N"
        elif start == "W":
            return "E"
        else:
            raise ValueError("invalid input")
    if value == 270:
        if action == "L":
            action = "R"
            value = 90
        elif action == "R":
            action = "L"
            value = 90
        else:
            raise ValueError("invalid input")
    results = dict()
    results["NR90"] = "E"
    results["NL90"] = "W"
    results["ER90"] = "S"
    results["EL90"] = "N"
    results["SR90"] = "W"
    results["SL90"] = "E"
    results["WR90"] = "N"
    results["WL90"] = "S"
    return results[start+action+str(value)]


def part_1(inputs: tuple[str], start: str) -> int:
    navs = _parse(inputs)
    log(navs)
    orientation = start
    position = Position(0, 0)
    for nav in navs:
        if nav.action in ("R", "L"):
            orientation = _new_orientation(orientation, nav.action, nav. value)
            log(orientation)
        elif nav.action == "F":
            if orientation == "N":
                position.y += nav.value
            elif orientation == "E":
                position.x += nav.value
            elif orientation == "S":
                position.y -= nav.value
            elif orientation == "W":
                position.x -= nav.value
            else:
                raise ValueError("invalid input")
            log(position)
        elif nav.action == "N":
            position.y += nav.value
        elif nav.action == "E":
            position.x += nav.value
        elif nav.action == "S":
            position.y -= nav.value
        elif nav.action == "W":
            position.x -= nav.value
        else:
            raise ValueError("invalid input")
    return abs(position.x) + abs(position.y)


def part_2(inputs: tuple[str]) -> int:
    pass


test = """\
F10
N3
F7
R90
F11
"""


def main() -> None:
    my_aocd.print_header(2020, 12)

    assert part_1(test.split(), "E") == 25
    # assert part_2(test) == 0

    inputs = my_aocd.get_input_as_tuple(2020, 12, 785)
    result1 = part_1(inputs, "E")
    print(f"Part 1: {result1}")
    # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

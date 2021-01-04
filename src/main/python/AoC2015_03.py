#! /usr/bin/env python3
#
# Advent of Code 2015 Day 3
#

from aoc import my_aocd
from aoc.navigation import NavigationWithHeading, Headings
from aoc.geometry import Position


NORTH = Headings["N"].value
EAST = Headings["E"].value
SOUTH = Headings["S"].value
WEST = Headings["W"].value


def part_1(inputs: tuple[str]) -> int:
    assert len(inputs) == 1
    navigation = NavigationWithHeading(Position(0, 0), NORTH)
    for c in inputs[0]:
        if c == ">":
            navigation.drift(EAST, 1)
        elif c == "v":
            navigation.drift(SOUTH, 1)
        elif c == "<":
            navigation.drift(WEST, 1)
        elif c == "^":
            navigation.drift(NORTH, 1)
        else:
            raise ValueError("Invalid input")
    visited = navigation.get_visited_positions(True)
    visited = {(p.x, p.y) for p in visited}
    return len(visited)


def part_2(inputs: tuple[str]) -> int:
    return 0


TEST1 = ">".splitlines()
TEST2 = "^>v<".splitlines()
TEST3 = "^v^v^v^v^v".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 3)

    assert part_1(TEST1) == 2
    assert part_1(TEST2) == 4
    assert part_1(TEST3) == 2

    inputs = my_aocd.get_input_as_tuple(2015, 3, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

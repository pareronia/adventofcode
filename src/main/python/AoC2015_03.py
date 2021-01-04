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


# TODO: make geometry.Position hashable ?
def _count_unique_positions(positions: list[Position]) -> int:
    return len({(p.x, p.y) for p in positions})


def _add_navigation_instruction(navigation: NavigationWithHeading, c: str):
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


def part_1(inputs: tuple[str]) -> int:
    assert len(inputs) == 1
    navigation = NavigationWithHeading(Position(0, 0), NORTH)
    for c in inputs[0]:
        _add_navigation_instruction(navigation, c)
    return _count_unique_positions(navigation.get_visited_positions(True))


def part_2(inputs: tuple[str]) -> int:
    assert len(inputs) == 1
    santa_navigation = NavigationWithHeading(Position(0, 0), NORTH)
    robot_navigation = NavigationWithHeading(Position(0, 0), NORTH)
    for i, c in enumerate(inputs[0]):
        if i % 2 != 0:
            _add_navigation_instruction(robot_navigation, c)
        else:
            _add_navigation_instruction(santa_navigation, c)
    visited = santa_navigation.get_visited_positions(True)
    visited.extend(robot_navigation.get_visited_positions(True))
    return _count_unique_positions(visited)


TEST1 = ">".splitlines()
TEST2 = "^>v<".splitlines()
TEST3 = "^v^v^v^v^v".splitlines()
TEST4 = "^v".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 3)

    assert part_1(TEST1) == 2
    assert part_1(TEST2) == 4
    assert part_1(TEST3) == 2
    assert part_2(TEST4) == 3
    assert part_2(TEST2) == 3
    assert part_2(TEST3) == 11

    inputs = my_aocd.get_input_as_tuple(2015, 3, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

#! /usr/bin/env python3
#
# Advent of Code 2016 Day 1
#

from aoc import my_aocd
from aoc.navigation import NavigationWithHeading, Headings
from aoc.geometry import Position


def _parse(inputs: tuple[str]) -> list[str]:
    assert len(inputs) == 1
    return inputs[0].split(", ")


def _navigate(steps: list[str]) -> NavigationWithHeading:
    navigation = NavigationWithHeading(Position(0, 0), Headings["N"].value)
    for step in steps:
        if step[0] == 'R':
            navigation.right(90)
        elif step[0] == 'L':
            navigation.left(90)
        else:
            raise ValueError("Invalid input")
        for i in range(int(step[1:])):
            navigation.forward(1)
    return navigation


def part_1(inputs: tuple[str]) -> int:
    steps = _parse(inputs)
    navigation = _navigate(steps)
    return abs(navigation.position.x) + abs(navigation.position.y)


def part_2(inputs: tuple[str]) -> int:
    steps = _parse(inputs)
    navigation = _navigate(steps)
    seen = set()
    for pos in navigation.get_visited_positions(True):
        _pos = (pos.x, pos.y)
        if _pos in seen:
            return abs(pos.x) + abs(pos.y)
        else:
            seen.add(_pos)
    raise ValueError("Unsolvable")


TEST1 = "R2, L3".splitlines()
TEST2 = "R2, R2, R2".splitlines()
TEST3 = "R5, L5, R5, R3".splitlines()
TEST4 = "R8, R4, R4, R8".splitlines()


def main() -> None:
    my_aocd.print_header(2016, 1)

    assert part_1(TEST1) == 5
    assert part_1(TEST2) == 2
    assert part_1(TEST3) == 12
    assert part_2(TEST4) == 4

    inputs = my_aocd.get_input(2016, 1, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

#! /usr/bin/env python3
#
# Advent of Code 2016 Day 1
#

from aoc import my_aocd
from aoc.common import log
from aoc.navigation import NavigationWithHeading, Headings
from aoc.geometry import Position


def _parse(inputs: tuple[str]) -> list[str]:
    assert len(inputs) == 1
    return inputs[0].split(", ")


def part_1(inputs: tuple[str]) -> int:
    steps = _parse(inputs)
    log(steps)
    navigation = NavigationWithHeading(Position(0, 0), Headings["N"].value)
    for step in steps:
        if step[0] == 'R':
            navigation.right(90)
        elif step[0] == 'L':
            navigation.left(90)
        else:
            raise ValueError("Invalid input")
        navigation.forward(int(step[1:]))
    return abs(navigation.position.x) + abs(navigation.position.y)


def part_2(inputs: tuple[str]) -> int:
    return 0


TEST1 = "R2, L3".splitlines()
TEST2 = "R2, R2, R2".splitlines()
TEST3 = "R5, L5, R5, R3".splitlines()


def main() -> None:
    my_aocd.print_header(2016, 1)

    assert part_1(TEST1) == 5
    assert part_1(TEST2) == 2
    assert part_1(TEST3) == 12
    assert part_2(TEST1) == 0
    assert part_2(TEST2) == 0
    assert part_2(TEST3) == 0

    inputs = my_aocd.get_input(2016, 1, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

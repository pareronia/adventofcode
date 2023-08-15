#! /usr/bin/env python3
#
# Advent of Code 2016 Day 1
#

import aocd
from aoc import my_aocd
from aoc.navigation import NavigationWithHeading, Heading
from aoc.geometry import Position, Turn


def _parse(inputs: tuple[str]) -> list[str]:
    assert len(inputs) == 1
    return inputs[0].split(", ")


def _navigate(steps: list[str]) -> NavigationWithHeading:
    navigation = NavigationWithHeading(Position(0, 0), Heading.NORTH)
    for step in steps:
        navigation.turn(Turn.from_str(step[0]))
        for _ in range(int(step[1:])):
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
    puzzle = aocd.models.Puzzle(2016, 1)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 5
    assert part_1(TEST2) == 2
    assert part_1(TEST3) == 12
    assert part_2(TEST4) == 4

    inputs = my_aocd.get_input_data(puzzle, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

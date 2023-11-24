#! /usr/bin/env python3
#
# Advent of Code 2016 Day 2
#

from typing import Callable
import aocd
from aoc import my_aocd
from aoc.navigation import NavigationWithHeading, Heading
from aoc.geometry import Position
from aoc.common import log


KEYPAD_1 = {
    (-1, 1): "1",
    (0, 1): "2",
    (1, 1): "3",
    (-1, 0): "4",
    (0, 0): "5",
    (1, 0): "6",
    (-1, -1): "7",
    (0, -1): "8",
    (1, -1): "9",
}
KEYPAD_2 = {
    (2, 2): "1",
    (1, 1): "2",
    (2, 1): "3",
    (3, 1): "4",
    (0, 0): "5",
    (1, 0): "6",
    (2, 0): "7",
    (3, 0): "8",
    (4, 0): "9",
    (1, -1): "A",
    (2, -1): "B",
    (3, -1): "C",
    (2, -2): "D",
}


def _navigate(
    ins: str, start: Position, in_bounds: Callable[[Position], bool]
) -> NavigationWithHeading:
    navigation = NavigationWithHeading(start, Heading.NORTH, in_bounds)
    for c in ins:
        navigation.drift(Heading.from_str(c), 1)
    return navigation


def _solve(
    inputs: tuple[str, ...],
    in_bounds: Callable[[Position], bool],
    get: Callable[[Position], str],
) -> str:
    code = str()
    start = Position.of(0, 0)
    for ins in inputs:
        navigation = _navigate(ins, start, in_bounds)
        last = navigation.get_visited_positions()[-1]
        log(last)
        code += get(last)
        log(code)
        start = last
    return code


def part_1(inputs: tuple[str, ...]) -> str:
    return _solve(
        inputs,
        lambda pos: (pos.x, pos.y) in KEYPAD_1,
        lambda pos: KEYPAD_1[(pos.x, pos.y)],
    )


def part_2(inputs: tuple[str, ...]) -> str:
    return _solve(
        inputs,
        lambda pos: (pos.x, pos.y) in KEYPAD_2,
        lambda pos: KEYPAD_2[(pos.x, pos.y)],
    )


TEST = """\
ULL
RRDDD
LURDL
UUUUD
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2016, 2)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == "1985"  # type:ignore[arg-type]
    assert part_2(TEST) == "5DB3"  # type:ignore[arg-type]

    inputs = my_aocd.get_input_data(puzzle, 5)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

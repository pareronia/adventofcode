#! /usr/bin/env python3
#
# Advent of Code 2016 Day 2
#

from aoc import my_aocd
from aoc.navigation import NavigationWithHeading, Headings
from aoc.geometry import Position
from aoc.common import log


KEYPAD_1 = {
    (-1, 1): '1',
    (0, 1): '2',
    (1, 1): '3',
    (-1, 0): '4',
    (0, 0): '5',
    (1, 0): '6',
    (-1, -1): '7',
    (0, -1): '8',
    (1, -1): '9',
}
KEYPAD_2 = {
    (2, 2): '1',
    (1, 1): '2',
    (2, 1): '3',
    (3, 1): '4',
    (0, 0): '5',
    (1, 0): '6',
    (2, 0): '7',
    (3, 0): '8',
    (4, 0): '9',
    (1, -1): 'A',
    (2, -1): 'B',
    (3, -1): 'C',
    (2, -2): 'D',
}


def _navigate(ins: str, start: Position, in_bounds) -> NavigationWithHeading:
    navigation = NavigationWithHeading(start,
                                       Headings["N"].value,
                                       in_bounds)
    for i in range(len(ins)):
        step = ins[i]
        if step == 'R':
            navigation.drift(Headings["E"].value, 1)
        elif step == 'L':
            navigation.drift(Headings["W"].value, 1)
        elif step == 'U':
            navigation.drift(Headings["N"].value, 1)
        elif step == 'D':
            navigation.drift(Headings["S"].value, 1)
        else:
            raise ValueError("Invalid input")
    return navigation


def _solve(inputs: tuple[str], in_bounds, get) -> str:
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


def part_1(inputs: tuple[str]) -> str:
    return _solve(
        inputs,
        lambda pos: (pos.x, pos.y) in KEYPAD_1,
        lambda pos: KEYPAD_1[(pos.x, pos.y)]
    )


def part_2(inputs: tuple[str]) -> str:
    return _solve(
        inputs,
        lambda pos: (pos.x, pos.y) in KEYPAD_2,
        lambda pos: KEYPAD_2[(pos.x, pos.y)]
    )


TEST = '''\
ULL
RRDDD
LURDL
UUUUD
'''.splitlines()


def main() -> None:
    my_aocd.print_header(2016, 2)

    assert part_1(TEST) == "1985"
    assert part_2(TEST) == "5DB3"

    inputs = my_aocd.get_input(2016, 2, 5)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

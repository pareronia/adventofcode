#! /usr/bin/env python3
#
# Advent of Code 2016 Day 2
#

from aoc import my_aocd
from aoc.navigation import NavigationWithHeading, Headings
from aoc.geometry import Position, Rectangle
from aoc.common import log


KEYPAD = (('7', '8', '9'), ('4', '5', '6'), ('1', '2', '3'))


def _navigate(ins: str, start: Position) -> NavigationWithHeading:
    navigation = NavigationWithHeading(start,
                                       Headings["N"].value,
                                       Rectangle.of(Position.of(0, 0),
                                                    Position.of(2, 2)))
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


def part_1(inputs: tuple[str]) -> int:
    code = str()
    start = Position.of(1, 1)
    for ins in inputs:
        navigation = _navigate(ins, start)
        last = navigation.get_visited_positions(False)[-1]
        log(last)
        code += KEYPAD[last.y][last.x]
        log(code)
        start = last
    return int(code)


def part_2(inputs: tuple[str]) -> int:
    return 0


TEST = '''\
ULL
RRDDD
LURDL
UUUUD
'''.splitlines()


def main() -> None:
    my_aocd.print_header(2016, 2)

    assert part_1(TEST) == 1985

    inputs = my_aocd.get_input(2016, 2, 5)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

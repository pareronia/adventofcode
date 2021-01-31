#! /usr/bin/env python3
#
# Advent of Code 2019 Day 3
#

import re
from aoc import my_aocd


def _parse(inputs: tuple[str]) -> (int, int):
    return tuple(int(_) for _ in inputs[0].split("-"))


def _is_valid(passw: int) -> bool:
    passw = str(passw)
    if len(re.findall(r'([0-9])\1', passw)) < 1:
        return False
    for i, c in enumerate(passw):
        if i == len(passw) - 1:
            continue
        if int(passw[i+1]) < int(c):
            return False
    return True


def part_1(inputs: tuple[str]) -> int:
    min_, max_ = _parse(inputs)
    return len([_ for _ in range(min_, max_ + 1) if _is_valid(_)])


def part_2(inputs: tuple[str]) -> int:
    return 0


def main() -> None:
    my_aocd.print_header(2019, 4)

    assert _is_valid(122345) is True
    assert _is_valid(111123) is True
    assert _is_valid(111111) is True
    assert _is_valid(223450) is False
    assert _is_valid(123789) is False

    inputs = my_aocd.get_input(2019, 4, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

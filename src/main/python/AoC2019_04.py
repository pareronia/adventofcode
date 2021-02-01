#! /usr/bin/env python3
#
# Advent of Code 2019 Day 3
#

from typing import Callable
from collections import Counter
from aoc import my_aocd


def _parse(inputs: tuple[str]) -> (int, int):
    return tuple(int(_) for _ in inputs[0].split("-"))


def _does_not_decrease(passw: str) -> bool:
    return list(passw) == sorted(passw)


def _is_valid_1(passw: int) -> bool:
    passw = str(passw)
    return _does_not_decrease(passw) and len(set(passw)) != len(passw)


def _is_valid_2(passw: int) -> bool:
    passw = str(passw)
    return _does_not_decrease(passw) and 2 in Counter(passw).values()


def _count_valid(inputs: tuple[str], check: Callable) -> int:
    min_, max_ = _parse(inputs)
    return len([_ for _ in range(min_, max_ + 1) if check(_)])


def part_1(inputs: tuple[str]) -> int:
    return _count_valid(inputs, _is_valid_1)


def part_2(inputs: tuple[str]) -> int:
    return _count_valid(inputs, _is_valid_2)


def main() -> None:
    my_aocd.print_header(2019, 4)

    assert _is_valid_1(122345) is True
    assert _is_valid_1(111123) is True
    assert _is_valid_1(111111) is True
    assert _is_valid_1(223450) is False
    assert _is_valid_1(123789) is False
    assert _is_valid_2(112233) is True
    assert _is_valid_2(123444) is False
    assert _is_valid_2(111122) is True

    inputs = my_aocd.get_input(2019, 4, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

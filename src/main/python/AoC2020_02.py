#! /usr/bin/env python3
#
# Advent of Code 2020 Day 2
#
import re
from operator import xor
from typing import Callable

import aocd

from aoc import my_aocd


def _solve(
    inputs: tuple[str], check_valid: Callable[[int, int, str, str], bool]
) -> str:
    def _parse(line: str) -> (int, int, str, str):
        m = re.search(r"^(\d{1,2})-(\d{1,2}) ([a-z]{1}): ([a-z]+)$", line)
        return (int(m.group(1)), int(m.group(2)), m.group(3), m.group(4))

    return str(sum(1 for line in inputs if check_valid(*_parse(line))))


def part_1(inputs: tuple[str]) -> str:
    def check_valid(first: int, second: int, wanted: str, passw: str):
        return passw.count(wanted) in range(first, second + 1)

    return _solve(inputs, check_valid)


def part_2(inputs: tuple[str]) -> str:
    def check_valid(first: int, second: int, wanted: str, passw: str):
        first_matched = passw[first - 1] == wanted
        second_matched = passw[second - 1] == wanted
        return xor(first_matched, second_matched)

    return _solve(inputs, check_valid)


TEST = """\
1-3 a: abcde
2-9 c: ccccccccc
1-3 b: cdefg
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2020, 2)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 2
    assert part_2(TEST) == 1

    inputs = my_aocd.get_input_data(puzzle, 1000)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

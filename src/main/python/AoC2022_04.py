#! /usr/bin/env python3
#
# Advent of Code 2022 Day 4
#


from __future__ import annotations

import re
from typing import Callable

import aocd
from aoc import my_aocd


def _solve(inputs: tuple[str], f: Callable[[set[int], set[int]], bool]) -> int:
    return sum(
        f(
            set(range(n1, n2 + 1)),
            set(range(n3, n4 + 1)),
        )
        for n1, n2, n3, n4 in (
            (int(n) for n in re.findall(r"[0-9]+", line)) for line in inputs
        )
    )


def part_1(inputs: tuple[str]) -> int:
    return _solve(inputs, lambda s1, s2: s1.issubset(s2) or s2.issubset(s1))


def part_2(inputs: tuple[str]) -> int:
    return _solve(inputs, lambda s1, s2: not s1.isdisjoint(s2))


TEST = """\
2-4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 4)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 2
    assert part_2(TEST) == 4

    inputs = my_aocd.get_input_data(puzzle, 1000)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

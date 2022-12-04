#! /usr/bin/env python3
#
# Advent of Code 2022 Day 4
#


from __future__ import annotations

import re
from typing import Callable
from typing import NamedTuple

import aocd
from aoc import my_aocd


class Range(NamedTuple):
    minimum: int
    maximum: int

    def contains(self, n: int) -> bool:
        return self.minimum <= n <= self.maximum

    def contains_range(self, other: Range) -> bool:
        return self.contains(other.minimum) and self.contains(other.maximum)

    def is_overlapped_by(self, other: Range) -> bool:
        return (
            other.contains(self.minimum)
            or other.contains(self.maximum)
            or self.contains(other.minimum)
        )


def _solve(
    inputs: tuple[str], f: Callable[[tuple[Range, Range]], bool]
) -> int:
    return sum(
        1
        for _ in (
            filter(
                lambda nums: f(
                    Range(nums[0], nums[1]), Range(nums[2], nums[3])
                ),
                map(
                    lambda line: [int(n) for n in re.findall(r"[0-9]+", line)],
                    inputs,
                ),
            )
        )
    )


def part_1(inputs: tuple[str]) -> int:
    return _solve(
        inputs,
        lambda range1, range2: range1.contains_range(range2)
        or range2.contains_range(range1),
    )


def part_2(inputs: tuple[str]) -> int:
    return _solve(
        inputs, lambda range1, range2: range1.is_overlapped_by(range2)
    )


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

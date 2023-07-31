#! /usr/bin/env python3
#
# Advent of Code 2019 Day 5
#

import aocd

from aoc import my_aocd
from aoc.intcode import IntCode


def _parse(inputs: tuple[str]) -> list[int]:
    return [int(_) for _ in inputs[0].split(",")]


def _solve(ints: list[int], inp: int) -> int:
    IntCode(ints).run([inp], out := [])
    return out[-1]


def part_1(inputs: tuple[str]) -> int:
    return _solve(_parse(inputs), 1)


def part_2(inputs: tuple[str]) -> int:
    return _solve(_parse(inputs), 5)


def main() -> None:
    puzzle = aocd.models.Puzzle(2019, 5)
    my_aocd.print_header(puzzle.year, puzzle.day)

    inputs = my_aocd.get_input_data(puzzle, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

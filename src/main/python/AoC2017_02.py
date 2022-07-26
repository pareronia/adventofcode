#! /usr/bin/env python3
#
# Advent of Code 2017 Day 2
#
from typing import Callable
from itertools import combinations
from aoc import my_aocd
import aocd


def _difference_highest_lowest(line: list[int]) -> int:
    return max(line) - min(line)


def _evenly_divisible_quotient(line: list[int]) -> int:
    for n1, n2 in combinations(line, 2):
        if n1 > n2:
            if n1 % n2 == 0:
                return n1 // n2
        elif n2 % n1 == 0:
            return n2 // n1
    raise ValueError


def _solve(strategy: Callable, inputs: tuple[str]) -> int:
    return sum(strategy([int(_) for _ in line.split()])
               for line in inputs)


def part_1(inputs: tuple[str]) -> int:
    return _solve(_difference_highest_lowest, inputs)


def part_2(inputs: tuple[str]) -> int:
    return _solve(_evenly_divisible_quotient, inputs)


TEST1 = """\
5 1 9 5
7 5 3
2 4 6 8
""".splitlines()
TEST2 = """\
5 9 2 8
9 4 7 3
3 8 6 5
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2017, 2)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 18
    assert part_2(TEST2) == 9

    inputs = my_aocd.get_input(2017, 2, 16)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()

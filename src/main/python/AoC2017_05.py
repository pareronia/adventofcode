#! /usr/bin/env python3
#
# Advent of Code 2017 Day 5
#
from typing import Callable
from aoc import my_aocd
import aocd


def _parse(inputs: tuple[str]) -> list[int]:
    return [int(_) for _ in inputs]


def _count_jumps(inputs: tuple[str], strategy: Callable) -> int:
    offsets = _parse(inputs)
    i = 0
    cnt = 0
    while i < len(offsets):
        jump = offsets[i]
        offsets[i] = strategy(jump)
        i += jump
        cnt += 1
    return cnt


def part_1(inputs: tuple[str]) -> int:
    return _count_jumps(inputs, lambda j: j + 1)


def part_2(inputs: tuple[str]) -> int:
    return _count_jumps(inputs, lambda j: j - 1 if j >= 3 else j + 1)


TEST = """\
0
3
0
1
-3
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2017, 5)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 5
    assert part_2(TEST) == 10

    inputs = my_aocd.get_input(2017, 5, 1033)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()

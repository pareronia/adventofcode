#! /usr/bin/env python3
#
# Advent of Code 2017 Day 15
#
from aoc import my_aocd
import aocd
from aoc.common import log


FACTOR_A = 16807
FACTOR_B = 48271
MOD = 2147483647


def _parse(inputs: tuple[str]) -> tuple[int]:
    assert len(inputs) == 2
    return (int(inputs[i].split()[-1]) for i in (0, 1))


def _next(prev: int, factor: int, condition) -> int:
    val = prev
    while True:
        val = (val * factor) % MOD
        if condition(val):
            return val


def _solve(reps: int, start_a: int, start_b: int,
           condition_a, condition_b) -> int:
    cnt = 0
    prev_a = start_a
    prev_b = start_b
    for i in range(reps):
        prev_a = _next(prev_a, FACTOR_A, condition_a)
        prev_b = _next(prev_b, FACTOR_B, condition_b)
        if i < 5:
            log(f"{i}: {prev_a} | {prev_b}")
        if (prev_a & 0xffff) == (prev_b & 0xffff):
            cnt += 1
    return cnt


def part_1(inputs: tuple[str]) -> int:
    start_a, start_b = _parse(inputs)
    return _solve(40_000_000, start_a, start_b,
                  lambda x: True, lambda x: True)


def part_2(inputs: tuple[str]) -> int:
    start_a, start_b = _parse(inputs)
    return _solve(5_000_000, start_a, start_b,
                  lambda x: x % 4 == 0, lambda x: x % 8 == 0)


TEST = """\
Generator A starts with 65
Generator B starts with 8921
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2017, 15)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 588
    assert part_2(TEST) == 309

    inputs = my_aocd.get_input(2017, 15, 2)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()

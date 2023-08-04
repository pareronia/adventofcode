#! /usr/bin/env python3
#
# Advent of Code 2019 Day 7
#

import itertools
from collections.abc import Iterable

import aocd

from aoc import my_aocd
from aoc.intcode import IntCode


def _parse(inputs: tuple[str]) -> tuple[int]:
    return [int(_) for _ in inputs[0].split(",")]


def _run(prog: list[int], phase_settings: tuple[int]) -> int:
    assert len(phase_settings) == 5
    queues = [[phase_settings[i]] for i in range(5)]
    queues[0].append(0)
    int_codes = [IntCode(prog) for _ in range(5)]
    while True:
        for i in range(5):
            int_codes[i].run_till_input_required(
                queues[i], queues[(i + 1) % 5]
            )
        if all(int_code.halted for int_code in int_codes):
            break
    return queues[0][-1]


def _solve(prog: list[int], iterable: Iterable[int]) -> int:
    return max(_run(prog, _) for _ in itertools.permutations(iterable))


def part_1(inputs: tuple[str]) -> int:
    return _solve(_parse(inputs), range(5))


def part_2(inputs: tuple[str]) -> int:
    return _solve(_parse(inputs), range(5, 10, 1))


def main() -> None:
    puzzle = aocd.models.Puzzle(2019, 7)
    my_aocd.print_header(puzzle.year, puzzle.day)

    inputs = my_aocd.get_input_data(puzzle, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

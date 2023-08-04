#! /usr/bin/env python3
#
# Advent of Code 2019 Day 2
#

import itertools
from copy import deepcopy

import aocd

from aoc import my_aocd
from aoc.intcode import IntCode


def _parse(inputs: tuple[str]) -> tuple[int]:
    assert len(inputs) == 1
    return [int(_) for _ in inputs[0].split(",")]


def _run_prog(prog: list[int], noun: int, verb: int) -> int:
    prog[1] = noun
    prog[2] = verb
    int_code = IntCode(prog)
    int_code.run()
    return int_code.get_program()[0]


def part_1(inputs: tuple[str]) -> int:
    return _run_prog(_parse(inputs), 12, 2)


def part_2(inputs: tuple[str]) -> int:
    prog = _parse(inputs)
    for noun, verb in itertools.product(range(100), repeat=2):
        if _run_prog(deepcopy(prog), noun, verb) == 19_690_720:
            return 100 * noun + verb
    raise RuntimeError("Unsolved")


def main() -> None:
    puzzle = aocd.models.Puzzle(2019, 2)
    my_aocd.print_header(puzzle.year, puzzle.day)

    inputs = my_aocd.get_input_data(puzzle, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

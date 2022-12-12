#! /usr/bin/env python3
#
# Advent of Code 2022 Day 12
#

from collections import deque
from itertools import product
from typing import Callable

import aocd
from aoc import my_aocd
from aoc.common import log

Cell = tuple[int, int]


def _solve(inputs: tuple[str], is_end: Callable[[bool], Cell]) -> int:
    def get_value(cell: Cell) -> int:
        ch = inputs[cell[0]][cell[1]]
        return ord("a") if ch == "S" else ord("z") if ch == "E" else ord(ch)

    height = len(inputs)
    width = len(inputs[0])
    start = [
        (r, c)
        for r, c in product(range(height), range(width))
        if inputs[r][c] == "E"
    ][0]
    log(f"start: {start}")
    q = deque[tuple[Cell, int]]()
    q.append((start, 0))
    seen = set[Cell]()
    seen.add(start)
    while q:
        (r, c), d = q.popleft()
        if is_end((r, c)):
            log(f"end: {(r, c)}")
            return d
        for rr, cc in [(r + 1, c), (r - 1, c), (r, c + 1), (r, c - 1)]:
            if rr < 0 or rr >= height or cc < 0 or cc >= width:
                continue
            if (rr, cc) in seen:
                continue
            if get_value((r, c)) - get_value((rr, cc)) > 1:
                continue
            seen.add((rr, cc))
            q.append(((rr, cc), d + 1))
    raise ValueError("Unsolvable")


def part_1(inputs: tuple[str]) -> int:
    return _solve(inputs, lambda cell: inputs[cell[0]][cell[1]] == "S")


def part_2(inputs: tuple[str]) -> int:
    return _solve(
        inputs,
        lambda cell: inputs[cell[0]][cell[1]] == "S"
        or inputs[cell[0]][cell[1]] == "a",
    )


TEST = """\
Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 12)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 31
    assert part_2(TEST) == 29

    inputs = my_aocd.get_input_data(puzzle, 41)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

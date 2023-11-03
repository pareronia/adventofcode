#! /usr/bin/env python3
#
# Advent of Code 2022 Day 12
#
# TODO CharGrid

import aocd
from itertools import product
from typing import Iterator
from aoc import my_aocd
from aoc.common import log
from aoc.graph import bfs

Cell = tuple[int, int]
DIRS = [
    (-1, 0),
    (1, 0),
    (0, -1),
    (0, 1),
]


def _solve(inputs: tuple[str, ...], end_points: set[str]) -> int:
    height = len(inputs)
    width = len(inputs[0])

    def is_end(cell: Cell) -> bool:
        return inputs[cell[0]][cell[1]] in end_points

    def adjacent(cell: Cell) -> Iterator[Cell]:
        def get_value(cell: Cell) -> int:
            ch = inputs[cell[0]][cell[1]]
            return (
                ord("a") if ch == "S" else ord("z") if ch == "E" else ord(ch)
            )

        r, c = cell
        for dr, dc in DIRS:
            rr, cc = r + dr, c + dc
            if (
                0 <= rr < height
                and 0 <= cc < width
                and get_value((r, c)) - get_value((rr, cc)) <= 1
            ):
                yield (rr, cc)

    start = [
        (r, c)
        for r, c in product(range(height), range(width))
        if inputs[r][c] == "E"
    ][0]
    log(f"start: {start}")
    return bfs(start, is_end, adjacent)


def part_1(inputs: tuple[str, ...]) -> int:
    return _solve(inputs, {"S"})


def part_2(inputs: tuple[str, ...]) -> int:
    return _solve(inputs, {"S", "a"})


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

    assert part_1(TEST) == 31  # type:ignore[arg-type]
    assert part_2(TEST) == 29  # type:ignore[arg-type]

    inputs = my_aocd.get_input_data(puzzle, 41)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

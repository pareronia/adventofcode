#! /usr/bin/env python3
#
# Advent of Code 2022 Day 12
#

import aocd
from aoc import my_aocd
from aoc.graph import bfs
from aoc.grid import CharGrid, Cell


def _solve(inputs: tuple[str, ...], end_points: set[str]) -> int:
    grid = CharGrid([_ for _ in inputs])

    def get_value(cell: Cell) -> int:
        ch = grid.get_value(cell)
        return ord("a") if ch == "S" else ord("z") if ch == "E" else ord(ch)

    return bfs(
        next(grid.get_all_equal_to("E")),
        lambda cell: grid.get_value(cell) in end_points,
        lambda cell: (
            n
            for n in grid.get_capital_neighbours(cell)
            if get_value(cell) - get_value(n) <= 1
        ),
    )


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

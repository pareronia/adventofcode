#! /usr/bin/env python3
#
# Advent of Code 2022 Day 14
#


import aocd
from aoc import my_aocd

Position = tuple[int, int]
SOURCE = Position((500, 0))


def _parse(inputs: tuple[str]) -> tuple[set[Position], int]:
    rocks = set[Position]()
    max_y = 0
    for line in inputs:
        c = [list(map(int, p.split(","))) for p in line.strip().split(" -> ")]
        for (x1, y1), (x2, y2) in zip(c, c[1:]):
            x1, x2 = sorted([x1, x2])
            y1, y2 = sorted([y1, y2])
            for x in range(x1, x2 + 1):
                for y in range(y1, y2 + 1):
                    rocks.add(Position((x, y)))
                    max_y = max(max_y, y)
    return rocks, max_y


def _drop(rocks: set[Position], sand: set[Position], max_y: int) -> Position:
    curr = SOURCE
    while True:
        curr_x, curr_y = curr
        for dx, dy in [(0, 1), (-1, 1), (1, 1)]:
            test = Position((curr_x + dx, curr_y + dy))
            if test not in sand and test not in rocks:
                curr = test
                break
        if curr == (curr_x, curr_y):
            return curr
        if curr[1] > max_y:
            return None


def _solve(rocks: set[Position], max_y: int) -> set[Position]:
    sand = set[Position]()
    while True:
        p = _drop(rocks, sand, max_y)
        if p is None:
            break
        sand.add(p)
        if p == SOURCE:
            break
    return sand


def part_1(inputs: tuple[str]) -> int:
    rocks, max_y = _parse(inputs)
    sand = _solve(rocks, max_y)
    return len(sand)


def part_2(inputs: tuple[str]) -> int:
    rocks, max_y = _parse(inputs)
    xs = sorted(x for (x, y) in rocks)
    the_max = max_y + 2
    for x in range(xs[0] - the_max, xs[-1] + the_max + 1):
        rocks.add(Position((x, the_max)))
    sand = _solve(rocks, the_max)
    return len(sand)


TEST = tuple(
    """\
498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9
""".splitlines()
)


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 14)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 24
    assert part_2(TEST) == 93

    inputs = my_aocd.get_input_data(puzzle, 164)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

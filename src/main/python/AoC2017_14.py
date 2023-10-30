#! /usr/bin/env python3
#
# Advent of Code 2017 Day 14
#

from collections import deque
from collections.abc import Iterator
from aoc import my_aocd
from aoc.knothash import KnotHash
from aoc.grid import IntGrid, Cell

import aocd

ON = 1


def _hashes(input_: str) -> Iterator[str]:
    for i in range(128):
        yield KnotHash.bin_string(f"{input_}-{i}")


def _find_region(grid: IntGrid, start: Cell) -> set[Cell]:
    seen = set[Cell]()
    q = deque[Cell]()
    q.append(start)
    while len(q) > 0:
        cell = q.popleft()
        for n in grid.get_capital_neighbours(cell):
            if grid.get_value(n) == ON and n not in seen:
                q.append(n)
                seen.add(n)
    return seen


def part_1(inputs: tuple[str, ...]) -> int:
    return sum(1 for h in _hashes(inputs[0]) for c in h if int(c) == ON)


def part_2(inputs: tuple[str, ...]) -> int:
    grid = IntGrid([[int(c) for c in h] for h in _hashes(inputs[0])])
    regions = list[set[Cell]]()
    ans = 0
    for cell in grid.get_all_equal_to(ON):
        if not any(cell in r for r in regions):
            regions.append(_find_region(grid, cell))
            ans += 1
    return ans


TEST = """flqrgnkx""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2017, 14)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 8108  # type:ignore[arg-type]
    assert part_2(TEST) == 1242  # type:ignore[arg-type]

    inputs = my_aocd.get_input(2017, 14, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

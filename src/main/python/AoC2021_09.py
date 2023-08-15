#! /usr/bin/env python3
#
# Advent of Code 2021 Day 9
#

from collections.abc import Generator
from math import prod
from collections import deque
from aoc import my_aocd
from aoc.grid import Cell, IntGrid
from aoc.common import log
import aocd


def _parse(inputs: tuple[str]) -> IntGrid:
    return IntGrid([[int(_) for _ in list(r)] for r in inputs])


def _find_lows(grid: IntGrid) -> Generator[Cell]:
    for low in (c for c in grid.get_cells()
                if (all(grid.get_value(c) < grid.get_value(n)
                        for n in grid.get_capital_neighbours(c)))):
        log(low)
        yield low


def part_1(inputs: tuple[str]) -> int:
    grid = _parse(inputs)
    log(grid)
    return sum([grid.get_value(c) + 1
                for c in _find_lows(grid)])


def _size_of_basin_around_low(grid: IntGrid, c: Cell) -> int:
    basin = set[Cell]()
    q = deque[Cell]()
    q.append(c)
    while len(q) > 0:
        cell = q.popleft()
        basin.add(cell)
        for n in grid.get_capital_neighbours(cell):
            if n not in basin and grid.get_value(n) != 9:
                q.append(n)
    log(basin)
    log(len(basin))
    return len(basin)


def part_2(inputs: tuple[str]) -> int:
    grid = _parse(inputs)
    return prod(sorted(_size_of_basin_around_low(grid, low)
                       for low in _find_lows(grid))
                [-3:])


TEST = """\
2199943210
3987894921
9856789892
8767896789
9899965678
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 9)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 15
    assert part_2(TEST) == 1134

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 100)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()

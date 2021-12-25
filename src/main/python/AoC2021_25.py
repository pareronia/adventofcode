#! /usr/bin/env python3
#
# Advent of Code 2021 Day 25
#

from __future__ import annotations
from collections.abc import Generator
from aoc import my_aocd
import aocd


EMPTY = "."
EAST = ">"
SOUTH = "v"


class Grid:
    rows: int
    cols: int
    south_herd: set[tuple[int, int]]
    east_herd: set[tuple[int, int]]

    def __init__(self,
                 rows: int,
                 cols: int,
                 south_herd: set[tuple[int, int]],
                 east_herd: set[tuple[int, int]]
                 ) -> Grid:
        self.rows = rows
        self.cols = cols
        self.south_herd = south_herd
        self.east_herd = east_herd


def _parse(inputs: tuple[str]) -> Grid:
    rows = len(inputs)
    cols = len(inputs[0])
    south_herd = set[tuple[int, int]]()
    east_herd = set[tuple[int, int]]()
    for row, line in enumerate(inputs):
        for col, c in enumerate(line):
            if c == SOUTH:
                south_herd.add((row, col))
            elif c == EAST:
                east_herd.add((row, col))
            elif c != EMPTY:
                raise ValueError
    return Grid(rows, cols, south_herd, east_herd)


def _destination_south(grid: Grid, row: int, col: int) -> tuple[int, int]:
    return ((row + 1) % grid.rows, col)


def _destination_east(grid: Grid, row: int, col: int) -> tuple[int, int]:
    return (row, (col + 1) % grid.cols)


def _find_south(grid: Grid) -> Generator[tuple[int, int]]:
    for row, col in grid.south_herd:
        destination = _destination_south(grid, row, col)
        if destination not in grid.south_herd \
           and destination not in grid.east_herd:
            yield (row, col)


def _find_east(grid: Grid) -> Generator[tuple[int, int]]:
    for row, col in grid.east_herd:
        destination = _destination_east(grid, row, col)
        if destination not in grid.south_herd \
           and destination not in grid.east_herd:
            yield (row, col)


def _move_south(grid: Grid, to_move: Generator[tuple[int, int]]) -> int:
    cnt = 0
    new_herd = {cell for cell in grid.south_herd}
    for cell in to_move:
        new_herd.remove(cell)
        new_herd.add(_destination_south(grid, *cell))
        cnt += 1
    grid.south_herd = new_herd
    return cnt


def _move_east(grid: Grid, to_move: Generator[tuple[int, int]]) -> int:
    cnt = 0
    new_herd = {cell for cell in grid.east_herd}
    for cell in to_move:
        new_herd.remove(cell)
        new_herd.add(_destination_east(grid, *cell))
        cnt += 1
    grid.east_herd = new_herd
    return cnt


def _print(grid: Grid) -> None:
    if not __debug__:
        return
    for row in range(grid.rows):
        for col in range(grid.cols):
            if (row, col) in grid.south_herd:
                print(SOUTH, end='')
            elif (row, col) in grid.east_herd:
                print(EAST, end='')
            else:
                print(EMPTY, end='')
        print()
    print()


def part_1(inputs: tuple[str]) -> int:
    grid = _parse(inputs)
    cnt = 0
    moved = -1
    while moved:
        moved = _move_east(grid, _find_east(grid)) \
                + _move_south(grid, _find_south(grid))
        cnt += 1
    return cnt


def part_2(inputs: tuple[str]) -> int:
    return None


TEST = """\
v...>>.vv>
.vv>>.vv..
>>.>v>...v
>>v>>.>.v.
v>v.vv.v..
>.>>..v...
.vv..>.>v.
v.v..>>v.v
....v..v.>
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 25)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 58

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 137)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()

#! /usr/bin/env python3
#
# Advent of Code 2021 Day 11
#

from collections.abc import Generator
from typing import NamedTuple
from aoc import my_aocd
from aoc.navigation import Headings
import aocd


class Cell(NamedTuple):
    row: int
    col: int


# TODO numpy?
class Grid(NamedTuple):
    values: list[list[int]]

    def get_width(self) -> int:
        assert len(self.values) > 0
        return len(self.values[0])

    def get_height(self) -> int:
        return len(self.values)

    def size(self) -> int:
        return self.get_height() * self.get_width()

    def get_value(self, c: Cell) -> int:
        return self.values[c.row][c.col]

    def set_value(self, c: Cell, value: int) -> None:
        self.values[c.row][c.col] = value

    def increment(self, c: Cell) -> None:
        self.values[c.row][c.col] += 1

    def get_cells(self) -> Generator[Cell]:
        return (Cell(r, c)
                for r in range(self.get_height())
                for c in range(self.get_width()))


class Flashes():
    value: int

    def __init__(self):
        self.value = 0

    def increment(self) -> None:
        self.value += 1

    def get(self) -> int:
        return self.value


def _find_neighbours(grid: Grid, c: Cell) -> Generator[Cell]:
    return (Cell(c.row + d.x, c.col + d.y)
            for d in Headings.OCTANTS()
            if c.row + d.x >= 0
            and c.row + d.x < grid.get_height()
            and c.col + d.y >= 0
            and c.col + d.y < grid.get_width())


def _parse(inputs: tuple[str]) -> Grid:
    return Grid([[int(_) for _ in list(r)] for r in inputs])


def _flash(grid: Grid, c: Cell, flashes: Flashes) -> None:
    grid.set_value(c, 0)
    flashes.increment()
    for n in _find_neighbours(grid, c):
        if grid.get_value(n) == 0:
            continue
        grid.increment(n)
        if grid.get_value(n) > 9:
            _flash(grid, n, flashes)


def _cycle(grid: Grid) -> int:
    [grid.increment(c) for c in grid.get_cells()]
    flashes = Flashes()
    [_flash(grid, c, flashes)
     for c in grid.get_cells()
     if grid.get_value(c) > 9]
    return flashes.get()


def part_1(inputs: tuple[str]) -> int:
    grid = _parse(inputs)
    flashes = 0
    for cycle in range(100):
        flashes += _cycle(grid)
    return flashes


def part_2(inputs: tuple[str]) -> int:
    grid = _parse(inputs)
    cycle = 1
    while True:
        flashes = _cycle(grid)
        if flashes == grid.size():
            break
        cycle += 1
    return cycle


TEST = """\
5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 11)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 1656
    assert part_2(TEST) == 195

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 10)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()

#! /usr/bin/env python3
#
# Advent of Code 2021 Day 15
#

from collections.abc import Generator
from aoc import my_aocd
from aoc.geometry import Direction
from aoc.graph import a_star
from aoc.grid import IntGrid
import aocd

START = (0, 0)
Cell = tuple[int, int]


def _parse(inputs: tuple[str]) -> IntGrid:
    return IntGrid([[int(_) for _ in list(r)] for r in inputs])


def _get_risk(grid: IntGrid, row: int, col: int) -> int:
    value = (
        grid.get_value_at(row % grid.get_height(), col % grid.get_width())
        + row // grid.get_height()
        + col // grid.get_width()
    )
    while value > 9:
        value -= 9
    return value


def _find_neighbours(
    grid: IntGrid, row: int, col: int, tiles: int, seen: set[Cell]
) -> Generator[Cell]:
    seen.add((row, col))
    return (
        (row + d.x, col + d.y)
        for d in Direction.CAPITAL
        if (row + d.x, col + d.y) not in seen
        and row + d.x >= 0
        and row + d.x < tiles * grid.get_height()
        and col + d.y >= 0
        and col + d.y < tiles * grid.get_width()
    )


def _solve(grid: IntGrid, tiles: int) -> int:
    seen = set[Cell]()
    end = (tiles * grid.get_height() - 1, tiles * grid.get_width() - 1)
    risk, _ = a_star(
        START,
        lambda cell: cell == end,
        lambda cell: _find_neighbours(grid, *cell, tiles, seen),
        lambda cell: _get_risk(grid, *cell),
    )
    return risk


def part_1(inputs: tuple[str]) -> int:
    grid = _parse(inputs)
    return _solve(grid, 1)


def part_2(inputs: tuple[str]) -> int:
    grid = _parse(inputs)
    return _solve(grid, 5)


TEST = """\
1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 15)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 40
    assert part_2(TEST) == 315

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 100)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

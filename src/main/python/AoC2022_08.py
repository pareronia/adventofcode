#! /usr/bin/env python3
#
# Advent of Code 2022 Day 8
#

from collections.abc import Iterator
from math import prod

import aocd

from aoc import my_aocd
from aoc.grid import Cell, IntGrid


def capital_directions(grid: IntGrid, cell: Cell) -> list[Iterator[Cell]]:
    return [
        grid.get_cells_n(cell),
        grid.get_cells_e(cell),
        grid.get_cells_s(cell),
        grid.get_cells_w(cell),
    ]


def ignoring_borders(grid: IntGrid) -> Iterator[Cell]:
    return (
        c
        for c in grid.get_cells()
        if 1 <= c.row
        and c.row < grid.get_max_row_index()
        and 1 <= c.col
        and c.col < grid.get_max_col_index()
    )


def visible_from_outside(grid: IntGrid, cell: Cell) -> bool:
    val = grid.get_value(cell)
    return any(
        all(grid.get_value(c) < val for c in cells)
        for cells in capital_directions(grid, cell)
    )


def viewing_distance(
    grid: IntGrid, direction: Iterator[Cell], val: int
) -> int:
    n = 0
    stop = False
    for c in direction:
        if stop:
            break
        n += 1
        stop = grid.get_value(c) >= val
    return n


def scenic_score(grid: IntGrid, cell: Cell) -> int:
    return prod(
        viewing_distance(grid, direction, grid.get_value(cell))
        for direction in capital_directions(grid, cell)
    )


def part_1(inputs: tuple[str, ...]) -> int:
    grid = IntGrid([[int(c) for c in line] for line in inputs])
    ans = 2 * (grid.get_height() + grid.get_width()) - 4
    return ans + sum(
        1 for c in ignoring_borders(grid) if visible_from_outside(grid, c)
    )


def part_2(inputs: tuple[str, ...]) -> int:
    grid = IntGrid([[int(c) for c in line] for line in inputs])
    return max(scenic_score(grid, c) for c in ignoring_borders(grid))


TEST = """\
30373
25512
65332
33549
33548
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 8)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 21  # type:ignore[arg-type]
    assert part_2(TEST) == 8  # type:ignore[arg-type]

    inputs = my_aocd.get_input_data(puzzle, 99)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

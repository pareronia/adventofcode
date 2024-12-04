#! /usr/bin/env python3
#
# Advent of Code 2022 Day 8
#

from collections.abc import Iterator
from math import prod

from aoc.common import aoc_main
from aoc.grid import Cell
from aoc.grid import IntGrid


def capital_directions(grid: IntGrid, cell: Cell) -> list[Iterator[Cell]]:
    return [
        grid.get_cells_n(cell),
        grid.get_cells_e(cell),
        grid.get_cells_s(cell),
        grid.get_cells_w(cell),
    ]


def ignoring_borders(grid: IntGrid) -> Iterator[Cell]:
    return grid.get_cells_without_border()


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


@aoc_main(2022, 8, part_1, part_2)
def main() -> None:
    assert part_1(TEST) == 21  # type:ignore[arg-type]
    assert part_2(TEST) == 8  # type:ignore[arg-type]


if __name__ == "__main__":
    main()

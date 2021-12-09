#! /usr/bin/env python3
#
# Advent of Code 2021 Day 9
#

from typing import NamedTuple
from aoc import my_aocd
from aoc.common import log


class Cell(NamedTuple):
    row: int
    col: int


class Grid(NamedTuple):
    heights: list[list[int]]

    def get_width(self) -> int:
        assert len(self.heights) > 0
        return len(self.heights[0])

    def get_height(self) -> int:
        return len(self.heights)

    def get_value(self, c: Cell) -> int:
        return self.heights[c.row][c.col]

    def get_cells(self):
        return (Cell(r, c)
                for r in range(self.get_height())
                for c in range(self.get_width()))


def _parse(inputs: tuple[str]) -> Grid:
    return Grid([[int(_) for _ in list(r)] for r in inputs])


def _find_neighbours(grid: Grid, c: Cell):
    return (Cell(c.row + dr, c.col + dc)
            for dr, dc in ((-1, 0), (0, 1), (1, 0), (0, -1))
            if c.row + dr >= 0
            and c.row + dr < grid.get_height()
            and c.col + dc >= 0
            and c.col + dc < grid.get_width())


def _find_lows(grid: Grid):
    for low in (c for c in grid.get_cells()
                if (all(grid.get_value(c) < grid.get_value(n)
                        for n in _find_neighbours(grid, c)))):
        log(low)
        yield low


def part_1(inputs: tuple[str]) -> int:
    grid = _parse(inputs)
    log(grid)
    return sum([grid.get_value(c) + 1
                for c in _find_lows(grid)])


def part_2(inputs: tuple[str]) -> int:
    return None


TEST = """\
2199943210
3987894921
9856789892
8767896789
9899965678
""".splitlines()


def main() -> None:
    my_aocd.print_header(2021, 9)

    assert part_1(TEST) == 15
    assert part_2(TEST) == 1134

    inputs = my_aocd.get_input(2021, 9, 100)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

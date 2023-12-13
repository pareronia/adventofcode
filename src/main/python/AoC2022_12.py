#! /usr/bin/env python3
#
# Advent of Code 2022 Day 12
#

from aoc.common import aoc_main
from aoc.graph import bfs
from aoc.grid import Cell
from aoc.grid import CharGrid


def _solve(inputs: tuple[str, ...], end_points: set[str]) -> int:
    grid = CharGrid([_ for _ in inputs])

    def get_value(cell: Cell) -> int:
        ch = grid.get_value(cell)
        return ord("a") if ch == "S" else ord("z") if ch == "E" else ord(ch)

    distance, _ = bfs(
        next(grid.get_all_equal_to("E")),
        lambda cell: grid.get_value(cell) in end_points,
        lambda cell: (
            n
            for n in grid.get_capital_neighbours(cell)
            if get_value(cell) - get_value(n) <= 1
        ),
    )
    return distance


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


@aoc_main(2022, 12, part_1, part_2)
def main() -> None:
    assert part_1(TEST) == 31  # type:ignore[arg-type]
    assert part_2(TEST) == 29  # type:ignore[arg-type]


if __name__ == "__main__":
    main()

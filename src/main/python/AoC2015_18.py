#! /usr/bin/env python3
#
# Advent of Code 2015 Day 18
#

from aoc import my_aocd
from aoc.common import log

ON = "#"
OFF = "."


def _parse(inputs: tuple[str]) -> list[str]:
    return list(inputs)


def _log_grid(grid: list[str]):
    if len(grid) > 6:
        return
    [log(line) for line in grid]
    log("")


def _next_generation(grid: list[str],
                     stuck_positions: list[tuple[int, int]],
                     stuck_value: str) -> list[str]:
    new_grid = list[str]()
    for r, row in enumerate(grid):
        new_row = ""
        for c, cell in enumerate(row):
            c1 = max(0, c-1)
            c2 = min(c+1, len(row)-1)
            r1 = max(0, r-1)
            r2 = min(r+1, len(grid)-1)
            neighbours = tuple(grid[rr][cc]
                               for rr in range(r1, r2+1)
                               for cc in range(c1, c2+1)
                               if not (r == rr and c == cc))
            neighbours_on = neighbours.count(ON)
            if cell == ON and neighbours_on in {2, 3} \
                    or cell == OFF and neighbours_on == 3 \
                    or stuck_value == ON and (r, c) in stuck_positions:
                new_row += ON
            else:
                new_row += OFF
        new_grid.append(new_row)
    return new_grid


def _run_generations(grid: list[str], generations: int,
                     stuck_positions: list[tuple[int, int]] = [],
                     stuck_value: str = ON) -> list[str]:
    for i in range(generations):
        grid = _next_generation(grid, stuck_positions, stuck_value)
        _log_grid(grid)
    return grid


def _do_part_1(inputs: tuple[str], generations: int) -> int:
    grid = _parse(inputs)
    _log_grid(grid)
    grid = _run_generations(grid, generations)
    return sum(line.count(ON) for line in grid)


def part_1(inputs: tuple[str]) -> int:
    return _do_part_1(inputs, 100)


def _do_part_2(inputs: tuple[str], generations: int) -> int:
    grid = _parse(inputs)
    _log_grid(grid)
    max_r = len(grid) - 1
    max_c = len(grid[0]) - 1
    stuck_positions = [(0, 0), (0, max_c), (max_r, 0), (max_r, max_c)]
    new_grid = list[str]()
    for r, row in enumerate(grid):
        new_row = ""
        for c, cell in enumerate(row):
            if (r, c) in stuck_positions:
                new_row += ON
            else:
                new_row += cell
        new_grid.append(new_row)
    grid = new_grid
    grid = _run_generations(grid, generations, stuck_positions, stuck_value=ON)
    return sum(line.count(ON) for line in grid)


def part_2(inputs: tuple[str]) -> int:
    return _do_part_2(inputs, 100)


TEST = """\
.#.#.#
...##.
#....#
..#...
#.#..#
####..
""".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 18)

    assert _do_part_1(TEST, 4) == 4
    assert _do_part_2(TEST, 5) == 17

    inputs = my_aocd.get_input(2015, 18, 100)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

#! /usr/bin/env python3
#
# Advent of Code 2015 Day 18
#

from functools import lru_cache
from aoc import my_aocd
from aoc.common import log

ON = "#"
OFF = "."


def _parse_dict(inputs: tuple[str]) -> dict:
    grid = dict()
    for r, row in enumerate(inputs):
        for c, cell in enumerate(row):
            if cell == ON:
                grid[(r, c)] = ()
    return grid, (len(inputs), len(inputs[0]))


def _parse_array(inputs: tuple[str]) -> list[str]:
    return list(inputs)


def _log_grid_array(grid: list[str]):
    if len(grid) > 6:
        return
    [log(line) for line in grid]
    log("")


def _log_grid_dict(grid: dict, size: tuple[int, int]):
    array = []
    for r in range(size[0]):
        row = ""
        for c in range(size[1]):
            if (r, c) in grid:
                row += ON
            else:
                row += OFF
        array.append(row)
    return _log_grid_array(array)


@lru_cache(maxsize=10000)
def _find_neighbours(r: int, c: int, num_rows: int, num_cols: int) -> tuple:
    return tuple((rr, cc)
                 for rr in range(r-1, r+2)
                 for cc in range(c-1, c+2)
                 if not (r == rr and c == cc)
                 and 0 <= rr < num_rows and 0 <= cc < num_cols)


def _next_generation_dict(grid: dict,
                          size: tuple[int, int],
                          stuck_positions: list[tuple[int, int]],
                          stuck_value: str) -> dict:
    to_on = set()
    to_off = set()
    for cell in grid.keys():
        neighbours = _find_neighbours(*cell, *size)
        neighbours_on = sum(1 for _ in neighbours if _ in grid)
        if neighbours_on in {2, 3} \
                or stuck_value == ON and cell in stuck_positions:
            to_on.add(cell)
        else:
            to_off.add(cell)
        for n in neighbours:
            if n in grid:
                continue
            n_neighbours = _find_neighbours(*n, *size)
            n_neighbours_on = sum(1 for _ in n_neighbours if _ in grid)
            if n_neighbours_on == 3:
                to_on.add(n)
    for cell in to_on:
        grid[cell] = ()
    for cell in to_off:
        del grid[cell]
    return grid


def _next_generation_array(grid: list[str],
                           stuck_positions: list[tuple[int, int]],
                           stuck_value: str) -> list[str]:
    size = (len(grid), len(grid[0]))
    new_grid = list[str]()
    for r, row in enumerate(grid):
        new_row = ""
        for c, cell in enumerate(row):
            neighbours = _find_neighbours(r, c, *size)
            neighbours_on = sum(1 for n in neighbours
                                if grid[n[0]][n[1]] == ON)
            if cell == ON and neighbours_on in {2, 3} \
                    or cell == OFF and neighbours_on == 3 \
                    or stuck_value == ON and (r, c) in stuck_positions:
                new_row += ON
            else:
                new_row += OFF
        new_grid.append(new_row)
    return new_grid


def _run_generations_array(grid: list[str], generations: int,
                           stuck_positions: list[tuple[int, int]] = [],
                           stuck_value: str = ON) -> list[str]:
    for i in range(generations):
        grid = _next_generation_array(grid, stuck_positions, stuck_value)
        _log_grid_array(grid)
    return grid


def _run_generations_dict(grid: list[str],
                          size: tuple[int, int],
                          generations: int,
                          stuck_positions: list[tuple[int, int]] = [],
                          stuck_value: str = ON) -> list[str]:
    for i in range(generations):
        grid = _next_generation_dict(grid, size, stuck_positions, stuck_value)
        _log_grid_dict(grid, size)
    return grid


def _do_part_1_dict(inputs: tuple[str], generations: int) -> int:
    grid, size = _parse_dict(inputs)
    _log_grid_dict(grid, size)
    grid = _run_generations_dict(grid, size, generations, [], ON)
    log(_find_neighbours.cache_info())
    return len(grid)


def _do_part_1_array(inputs: tuple[str], generations: int) -> int:
    grid = _parse_array(inputs)
    _log_grid_array(grid)
    grid = _run_generations_array(grid, generations)
    log(_find_neighbours.cache_info())
    return sum(line.count(ON) for line in grid)


def part_1_array(inputs: tuple[str]) -> int:
    return _do_part_1_array(inputs, 100)


def part_1_dict(inputs: tuple[str]) -> int:
    return _do_part_1_dict(inputs, 100)


def _do_part_2_dict(inputs: tuple[str], generations: int) -> int:
    grid, size = _parse_dict(inputs)
    max_r = size[0] - 1
    max_c = size[1] - 1
    stuck_positions = [(0, 0), (0, max_c), (max_r, 0), (max_r, max_c)]
    for cell in stuck_positions:
        grid[cell] = ()
    _log_grid_dict(grid, size)
    grid = _run_generations_dict(grid, size, generations,
                                 stuck_positions, stuck_value=ON)
    log(_find_neighbours.cache_info())
    return len(grid)


def _do_part_2_array(inputs: tuple[str], generations: int) -> int:
    grid = _parse_array(inputs)
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
    _log_grid_array(grid)
    grid = _run_generations_array(grid, generations,
                                  stuck_positions, stuck_value=ON)
    log(_find_neighbours.cache_info())
    return sum(line.count(ON) for line in grid)


def part_2_dict(inputs: tuple[str]) -> int:
    return _do_part_2_dict(inputs, 100)


def part_2_array(inputs: tuple[str]) -> int:
    return _do_part_2_array(inputs, 100)


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

    assert _do_part_1_array(TEST, 4) == 4
    assert _do_part_1_dict(TEST, 4) == 4
    assert _do_part_2_array(TEST, 5) == 17
    assert _do_part_2_dict(TEST, 5) == 17

    inputs = my_aocd.get_input(2015, 18, 100)
    result1 = part_1_dict(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2_dict(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

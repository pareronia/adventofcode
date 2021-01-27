#! /usr/bin/env python3
#
# Advent of Code 2020 Day 11
#
from functools import lru_cache
from aoc import my_aocd
from aoc.common import log


FLOOR = "."
EMPTY = "L"
OCCUPIED = "#"

the_grid: list[str]


@lru_cache(maxsize=8648)
def _find_adjacent(r: int, c: int,
                   num_rows: int, num_cols: int) -> tuple[tuple[int, int]]:
    return tuple((rr, cc)
                 for rr in range(r-1, r+2)
                 for cc in range(c-1, c+2)
                 if not (r == rr and c == cc)
                 and 0 <= rr < num_rows and 0 <= cc < num_cols)


@lru_cache(maxsize=8648)
def _find_visible(r: int, c: int,
                  num_rows: int, num_cols: int) -> list[tuple[int, int]]:
    global the_grid
    grid = the_grid
    vis = []
    for rg in [range(c-1, -1, -1), range(c+1, num_cols)]:
        for i in rg:
            if grid[r][i] != FLOOR:
                vis.append((r, i))
                break
    for rg in [range(r-1, -1, -1), range(r+1, num_rows)]:
        for i in rg:
            if grid[i][c] != FLOOR:
                vis.append((i, c))
                break
    cnt_right = num_cols-1-c
    cnt_up = r
    cnt_down = num_rows-1-r
    cnt_left = c
    i = 0
    while (i := i + 1) <= min(cnt_right, cnt_down):
        if grid[r+i][c+i] != FLOOR:
            vis.append((r+i, c+i))
            break
    i = 0
    while (i := i + 1) <= min(cnt_left, cnt_down):
        if grid[r+i][c-i] != FLOOR:
            vis.append((r+i, c-i))
            break
    i = 0
    while (i := i + 1) <= min(cnt_left, cnt_up):
        if grid[r-i][c-i] != FLOOR:
            vis.append((r-i, c-i))
            break
    i = 0
    while (i := i + 1) <= min(cnt_right, cnt_up):
        if grid[r-i][c+i] != FLOOR:
            vis.append((r-i, c+i))
            break
    return vis


def _run_cycle(grid: list[str], strategy, tolerance: int) -> list[str]:
    new_grid = list[str]()
    changed = False
    num_rows = len(grid)
    num_cols = len(grid[0])
    for r in range(num_rows):
        new_row = ""
        for c in range(num_cols):
            if grid[r][c] == EMPTY:
                to_check = strategy(r, c, num_rows, num_cols)
                if sum(1 for rr, cc in to_check
                       if grid[rr][cc] == OCCUPIED) == 0:
                    new_row += OCCUPIED
                    changed = True
                    continue
            elif grid[r][c] == OCCUPIED:
                to_check = strategy(r, c, num_rows, num_cols)
                if sum(1 for rr, cc in to_check
                       if grid[rr][cc] == OCCUPIED) >= tolerance:
                    new_row += EMPTY
                    changed = True
                    continue
            elif grid[r][c] != FLOOR:
                raise ValueError("invalid grid")
            new_row += grid[r][c]
        new_grid.append(new_row)
    return new_grid, changed


def _find_count_of_equilibrium(inputs: tuple[str], strategy,
                               tolerance: int) -> int:
    global the_grid
    the_grid = list(inputs)
    grid = the_grid
    log(grid)
    while True:
        new_grid, changed = _run_cycle(grid, strategy, tolerance)
        log(new_grid)
        if not changed:
            return sum(row.count(OCCUPIED) for row in grid)
        grid = new_grid


def part_1(inputs: tuple[str]) -> int:
    return _find_count_of_equilibrium(inputs, _find_adjacent, tolerance=4)


def part_2(inputs: tuple[str]) -> int:
    return _find_count_of_equilibrium(inputs, _find_visible, tolerance=5)


TEST = """\
L.LL.LL.LL
LLLLLLL.LL
L.L.L..L..
LLLL.LL.LL
L.LL.LL.LL
L.LLLLL.LL
..L.L.....
LLLLLLLLLL
L.LLLLLL.L
L.LLLLL.LL
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 11)

    assert part_1(TEST) == 37
    assert part_2(TEST) == 26

    inputs = my_aocd.get_input(2020, 11, 94)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

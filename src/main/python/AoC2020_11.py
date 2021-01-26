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


@lru_cache(maxsize=8648)
def _find_neighbours(r: int, c: int, num_rows: int, num_cols: int) -> tuple:
    return tuple((rr, cc)
                 for rr in range(r-1, r+2)
                 for cc in range(c-1, c+2)
                 if not (r == rr and c == cc)
                 and 0 <= rr < num_rows and 0 <= cc < num_cols)


def _find_adjacent(grid: list[str], seat: tuple[int, int],
                   len_x: int, len_y: int) -> tuple[tuple[int, int]]:
    return _find_neighbours(seat[0], seat[1], len_x, len_y)


def _find_visible(grid: list[str], seat: tuple[int, int],
                  len_x: int, len_y: int) -> list[tuple[int, int]]:
    x = seat[0]
    y = seat[1]
    vis = []
    for i in range(x-1, -1, -1):
        if grid[y][i] != FLOOR:
            vis.append((i, y))
            break
    for i in range(x+1, len_x):
        if grid[y][i] != FLOOR:
            vis.append((i, y))
            break
    for i in range(y-1, -1, -1):
        if grid[i][x] != FLOOR:
            vis.append((x, i))
            break
    for i in range(y+1, len_y):
        if grid[i][x] != FLOOR:
            vis.append((x, i))
            break
    cnt_right = len_x-1-x
    cnt_up = y
    cnt_down = len_y-1-y
    cnt_left = x
    i = 1
    while i <= min(cnt_right, cnt_down):
        if grid[y+i][x+i] != FLOOR:
            vis.append((x+i, y+i))
            break
        i += 1
    i = 1
    while i <= min(cnt_left, cnt_down):
        if grid[y+i][x-i] != FLOOR:
            vis.append((x-i, y+i))
            break
        i += 1
    i = 1
    while i <= min(cnt_left, cnt_up):
        if grid[y-i][x-i] != FLOOR:
            vis.append((x-i, y-i))
            break
        i += 1
    i = 1
    while i <= min(cnt_right, cnt_up):
        if grid[y-i][x+i] != FLOOR:
            vis.append((x+i, y-i))
            break
        i += 1
    return vis


def _run_cycle(grid: list[str], strategy, tolerance: int) -> list[str]:
    new_grid = list[str]()
    changed = False
    len_y = len(grid)
    len_x = len(grid[0])
    for y in range(len_y):
        new_row = ""
        for x in range(len_x):
            if grid[y][x] == EMPTY:
                to_check = strategy(grid, (x, y), len_x, len_y)
                if sum(1 for s in to_check
                       if grid[s[1]][s[0]] == OCCUPIED) == 0:
                    new_row += OCCUPIED
                    changed = True
                    continue
            elif grid[y][x] == OCCUPIED:
                to_check = strategy(grid, (x, y), len_x, len_y)
                if sum(1 for s in to_check
                       if grid[s[1]][s[0]] == OCCUPIED) >= tolerance:
                    new_row += EMPTY
                    changed = True
                    continue
            elif grid[y][x] != FLOOR:
                raise ValueError("invalid grid")
            new_row += grid[y][x]
        new_grid.append(new_row)
    return new_grid, changed


def _find_count_of_equilibrium(inputs: tuple[str], strategy,
                               tolerance: int) -> int:
    grid = list(inputs)
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

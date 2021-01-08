#! /usr/bin/env python3
#
# Advent of Code 2020 Day 11
#
from aoc import my_aocd
from aoc.common import log


FLOOR = "."
EMPTY = "L"
OCCUPIED = "#"


# TODO: check grid limits in _find_adjacent, so this check can be simpler
def _check_value(grid: list[str], seat: tuple[int, int], value) -> bool:
    x = seat[0]
    y = seat[1]
    max_x = len(grid[0])-1
    max_y = len(grid)-1
    if x < 0 or x > max_x or y < 0 or y > max_y:
        return None
    return grid[y][x] == value


def _find_adjacent(grid: list[str],
                   seat: tuple[int, int]) -> list[tuple[int, int]]:
    x = seat[0]
    y = seat[1]
    adj = []
    for i in range(x-1, x+2):
        for j in range(y-1, y+2):
            adj.append((i, j))
    adj.remove((x, y))
    return adj


def _find_visible(grid: list[str],
                  seat: tuple[int, int]) -> list[tuple[int, int]]:
    x = seat[0]
    y = seat[1]
    len_x = len(grid[0])
    len_y = len(grid)
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


def _select_seats_to_check(strategy, grid: list[str],
                           seat: tuple[int, int]) -> list[tuple[int, int]]:
    return strategy(grid, seat)


def _count_with_value(grid: list[str], seats: list[tuple[int, int]],
                      value: str) -> int:
    return sum([1 for s in seats if _check_value(grid, s, value)])


def _run_cycle(grid: list[str], strategy, tolerance: int) -> list[str]:
    new_grid = list[str]()
    changed = False
    for y in range(len(grid)):
        new_row = ""
        for x in range(len(grid[y])):
            if grid[y][x] == EMPTY:
                to_check = _select_seats_to_check(strategy, grid, seat=(x, y))
                if _count_with_value(grid, to_check, OCCUPIED) == 0:
                    new_row += OCCUPIED
                    changed = True
                    continue
            elif grid[y][x] == OCCUPIED:
                to_check = _select_seats_to_check(strategy, grid, seat=(x, y))
                if _count_with_value(grid, to_check, OCCUPIED) >= tolerance:
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
            return sum([row.count(OCCUPIED) for row in grid])
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

#! /usr/bin/env python3
#
# Advent of Code 2020 Day 11
#
import my_aocd
from common import log


def _parse(inputs: tuple[str]) -> [str]:
    return list(inputs)


def _count_occupied_in_grid(grid: list[str]) -> int:
    return sum([row.count("#") for row in grid])


def _is_occupied(grid: list[str], seat: tuple[int, int]) -> bool:
    x = seat[0]
    y = seat[1]
    max_x = len(grid[0])-1
    max_y = len(grid)-1
    if x < 0 or x > max_x or y < 0 or y > max_y:
        return None
    return grid[y][x] == "#"


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
    adj = []
    for i in range(x-1, -1, -1):
        if grid[y][i] != ".":
            adj.append((i, y))
            break
    for i in range(x+1, len_x):
        if grid[y][i] != ".":
            adj.append((i, y))
            break
    for i in range(y-1, -1, -1):
        if grid[i][x] != ".":
            adj.append((x, i))
            break
    for i in range(y+1, len_y):
        if grid[i][x] != ".":
            adj.append((x, i))
            break
    cnt_right = len_x-1-x
    cnt_up = y
    cnt_down = len_y-1-y
    cnt_left = x
    i = 1
    while i <= min(cnt_right, cnt_down):
        if grid[y+i][x+i] != ".":
            adj.append((x+i, y+i))
            break
        i += 1
    i = 1
    while i <= min(cnt_left, cnt_down):
        if grid[y+i][x-i] != ".":
            adj.append((x-i, y+i))
            break
        i += 1
    i = 1
    while i <= min(cnt_left, cnt_up):
        if grid[y-i][x-i] != ".":
            adj.append((x-i, y-i))
            break
        i += 1
    i = 1
    while i <= min(cnt_right, cnt_up):
        if grid[y-i][x+i] != ".":
            adj.append((x+i, y-i))
            break
        i += 1
    return adj


def _count_occupied(strategy, *args) -> int:
    grid = args[0]
    seat = args[1]
    to_check = strategy(grid, seat)
    return sum([1 for s in to_check if _is_occupied(grid, s)])


def _run_cycle(grid: list[str], strategy, tolerance: int) -> list[str]:
    new_grid = list[str]()
    for y in range(len(grid)):
        new_row = ""
        for x in range(len(grid[y])):
            if grid[y][x] == ".":
                new_row += "."
            elif grid[y][x] == "L":
                if _count_occupied(strategy, grid, (x, y)) == 0:
                    new_row += "#"
                else:
                    new_row += "L"
            elif grid[y][x] == "#":
                if _count_occupied(strategy, grid, (x, y)) >= tolerance:
                    new_row += "L"
                else:
                    new_row += "#"
            else:
                raise ValueError("invalid grid")
        new_grid.append(new_row)
    return new_grid


def _find_count_of_equilibrium(inputs: tuple[str], strategy,
                               tolerance: int) -> int:
    grid = _parse(inputs)
    log(grid)
    cnt = _count_occupied_in_grid(grid)
    log(cnt)
    while True:
        new_grid = _run_cycle(grid, strategy, tolerance)
        log(new_grid)
        if (new_grid == grid):
            return _count_occupied_in_grid(new_grid)
        grid = new_grid


def part_1(inputs: tuple[str]) -> int:
    return _find_count_of_equilibrium(inputs, _find_adjacent, tolerance=4)


def part_2(inputs: tuple[str]) -> int:
    return _find_count_of_equilibrium(inputs, _find_visible, tolerance=5)


test = ("L.LL.LL.LL",
        "LLLLLLL.LL",
        "L.L.L..L..",
        "LLLL.LL.LL",
        "L.LL.LL.LL",
        "L.LLLLL.LL",
        "..L.L.....",
        "LLLLLLLLLL",
        "L.LLLLLL.L",
        "L.LLLLL.LL"
        )


def main() -> None:
    my_aocd.print_header(2020, 11)

    assert part_1(test) == 37
    assert part_2(test) == 26

    inputs = my_aocd.get_input_as_tuple(2020, 11, 94)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

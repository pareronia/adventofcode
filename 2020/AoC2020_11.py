#! /usr/bin/env python3
#
# Advent of Code 2020 Day 11
#
import my_aocd
from common import log


def _parse(inputs: tuple[str]) -> [str]:
    return list(inputs)


def _count_occupied(grid: list[str]) -> int:
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


def _count_occupied_adjacent(grid: list[str], seat: tuple[int, int]) -> int:
    adj = _find_adjacent(grid, seat)
    return sum([1 for s in adj if _is_occupied(grid, s)])


def _run_cycle(grid: list[str]) -> list[str]:
    new_grid = list[str]()
    for y in range(len(grid)):
        new_row = ""
        for x in range(len(grid[y])):
            if grid[y][x] == ".":
                new_row += "."
            elif grid[y][x] == "L":
                if _count_occupied_adjacent(grid, (x, y)) == 0:
                    new_row += "#"
                else:
                    new_row += "L"
            elif grid[y][x] == "#":
                if _count_occupied_adjacent(grid, (x, y)) >= 4:
                    new_row += "L"
                else:
                    new_row += "#"
            else:
                raise ValueError("invalid grid")
        new_grid.append(new_row)
    return new_grid


def part_1(inputs: tuple[str]) -> int:
    grid = _parse(inputs)
    log(grid)
    cnt = _count_occupied(grid)
    log(cnt)
    while True:
        new_grid = _run_cycle(grid)
        log(new_grid)
        if (new_grid == grid):
            return _count_occupied(new_grid)
        grid = new_grid


def part_2(inputs: tuple[str]) -> int:
    return 0


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
    assert part_2(test) == 0

    inputs = my_aocd.get_input_as_tuple(2020, 11, 94)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

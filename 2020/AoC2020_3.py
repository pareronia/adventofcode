#! /usr/bin/env python3
#
# Advent of Code 2020 Day 3
#
# import aocd


# def _get_input() -> list[str]:
#     inputs = aocd.get_data(year=2020, day=3).splitlines()
#     assert len(inputs) == 323
#     return inputs


def _create_grid(inputs: list[str], step_x: int) -> list[str]:
    grid = list()
    for input_ in inputs:
        line = ""
        for i in range(0, step_x):
            line += input_
        grid.append(line)
    return grid


def _flatten_grid(grid: list[str]) -> str:
    flat = ""
    for line in grid:
        flat += line
    return flat


def _find_indices(max_: int, step_x: int, step_y: int) -> list[int]:
    indices = []
    i = 0
    while i <= max_:
        indices.append(i)
        i += (step_x+step_y)
    return indices


def _do_run(flat: str, indices: list[int]) -> str:
    run = ""
    for i in range(0, len(flat)-1):
        if i in indices:
            if flat[i] == ".":
                run += "O"
            else:
                run += "X"
        else:
            run += flat[i]
    return run


def _unflatten(flat: str, width: int) -> list[str]:
    grid = []
    i = 0
    while i <= len(flat):
        grid.append(flat[i:i+width])
        i += width
    return grid


def _count_hits(run: str) -> int:
    return run.count("X")


test = ["..##.......",
        "#...#...#..",
        ".#....#..#.",
        "..#.#...#.#",
        ".#...##..#.",
        "..#.##.....",
        ".#.#.#....#",
        ".#........#",
        "#.##...#...",
        "#...##....#",
        ".#..#...#.#",
        ]


def main() -> None:
    print("====================================================")
    print("AoC 2020 Day 3 - https://adventofcode.com/2020/day/3")
    print("====================================================")
    print("")

    # inputs = _get_input()
    # [print(input_) for input_ in inputs]
    grid = _create_grid(test, 3)
    [print(input_) for input_ in grid]
    flat = _flatten_grid(grid)
    print(flat)
    indices = _find_indices(len(flat)-1, len(grid[0]), 3)
    print(indices)
    run = _do_run(flat, indices)
    result = _unflatten(run, len(grid[0]))
    [print(x) for x in result]
    assert _count_hits(run) == 7
    # [print(x) for x in _unflatten("abcdefghijkabcdefghijk", 11)]


if __name__ == '__main__':
    main()

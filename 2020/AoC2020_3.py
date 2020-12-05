#! /usr/bin/env python3
#
# Advent of Code 2020 Day 3
#
import math
import aocd


open_square = "."
tree = "#"
hit = "X"
miss = "O"


def _get_input() -> list[str]:
    inputs = aocd.get_data(year=2020, day=3).splitlines()
    assert len(inputs) == 323
    return inputs


def _create_grid(inputs: list[str], step_x: int) -> list[str]:
    length = len(inputs)
    width = len(inputs[0])
    width_required = length*step_x
    width_divmod = divmod(width_required, width)
    times_x = width_divmod[0] \
        if width_divmod[1] == 0 \
        else width_divmod[0]+1
    grid = list()
    for input_ in inputs:
        line = ""
        for i in range(0, times_x):
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
    for i in range(0, len(flat)):
        if i in indices:
            if flat[i] == open_square:
                run += miss
            elif flat[i] == tree:
                run += hit
            else:
                raise ValueError("Invalid input")
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


def _part_1(inputs: list[str],
            step_x: int,
            step_y: int
            ) -> tuple[list[str], int]:
    grid = _create_grid(inputs, step_x)
    width = len(grid[0])
    flat = _flatten_grid(grid)
    indices = _find_indices(len(flat)-1, step_y*width, step_x)
    run = _do_run(flat, indices)
    result = _unflatten(run, width)
    hits = run.count(hit)
    return result, hits


def _part_2(inputs: list[str],
            steps_list: list[tuple[int, int]]
            ) -> None:
    results = [_part_1(inputs, steps[0], steps[1])[1] for steps in steps_list]
    return math.prod(results)


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

    assert _part_1(test, 1, 1)[1] == 2
    assert _part_1(test, 3, 1)[1] == 7
    assert _part_1(test, 5, 1)[1] == 3
    assert _part_1(test, 7, 1)[1] == 4
    assert _part_1(test, 1, 2)[1] == 2
    assert _part_2(test, [(1, 1),
                          (3, 1),
                          (5, 1),
                          (7, 1),
                          (1, 2),
                          ]) == 336

    inputs = _get_input()
    result1 = _part_1(inputs, 3, 1)
    print(f"Part 1: {result1[1]}")
    result2 = _part_2(inputs, [(1, 1),
                               (3, 1),
                               (5, 1),
                               (7, 1),
                               (1, 2),
                               ])
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

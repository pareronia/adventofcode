#! /usr/bin/env python3
#
# Advent of Code 2020 Day 3
#
import math
from aoc import my_aocd


OPEN_SQUARE = "."
TREE = "#"
HIT = "X"
MISS = "O"


def _create_grid(inputs: tuple[str], step_x: int) -> list[str]:
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
            if flat[i] == OPEN_SQUARE:
                run += MISS
            elif flat[i] == TREE:
                run += HIT
            else:
                raise ValueError("Invalid input")
        else:
            run += flat[i]
    return run


def _run(inputs: tuple[str], step_x: int, step_y: int) -> int:
    grid = _create_grid(inputs, step_x)
    width = len(grid[0])
    flat = _flatten_grid(grid)
    indices = _find_indices(len(flat)-1, step_y*width, step_x)
    run = _do_run(flat, indices)
    return run.count(HIT)


def _do_part_1(inputs: tuple[str], step_x: int, step_y: int) -> int:
    return _run(inputs, step_x, step_y)


def part_1(inputs: tuple[str]) -> int:
    return _do_part_1(inputs, 3, 1)


def part_2(inputs: tuple[str]) -> int:
    return math.prod([_run(inputs, *steps) for steps in ((1, 1),
                                                         (3, 1),
                                                         (5, 1),
                                                         (7, 1),
                                                         (1, 2))])


TEST = """\
..##.......
#...#...#..
.#....#..#.
..#.#...#.#
.#...##..#.
..#.##.....
.#.#.#....#
.#........#
#.##...#...
#...##....#
.#..#...#.#
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 3)

    assert _do_part_1(TEST, 1, 1) == 2
    assert _do_part_1(TEST, 3, 1) == 7
    assert _do_part_1(TEST, 5, 1) == 3
    assert _do_part_1(TEST, 7, 1) == 4
    assert _do_part_1(TEST, 1, 2) == 2
    assert part_2(TEST) == 336

    inputs = my_aocd.get_input(2020, 3, 323)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

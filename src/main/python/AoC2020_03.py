#! /usr/bin/env python3
#
# Advent of Code 2020 Day 3
#
import math
from aoc import my_aocd


def _parse(inputs: tuple[str]):
    trees = {(r, c)
             for r, row in enumerate(inputs)
             for c, _ in enumerate(row)
             if _ == "#"}
    return trees, (len(inputs), len(inputs[0]))


def _run(trees: set[tuple[int, int]], rows: int, cols: int,
         step_c: int, step_r: int) -> int:
    cnt = c = 0
    for r in range(0, rows, step_r):
        if (r, c) in trees:
            cnt += 1
        c = (c + step_c) % cols
    return cnt


def _do_part_1(inputs: tuple[str], steps: tuple[int]) -> int:
    trees, size = _parse(inputs)
    return _run(trees, *size, *steps)


def part_1(inputs: tuple[str]) -> int:
    return _do_part_1(inputs, (3, 1))


def part_2(inputs: tuple[str]) -> int:
    trees, size = _parse(inputs)
    return math.prod([_run(trees, *size, *steps) for steps in ((1, 1),
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

    assert _do_part_1(TEST, (1, 1)) == 2
    assert _do_part_1(TEST, (3, 1)) == 7
    assert _do_part_1(TEST, (5, 1)) == 3
    assert _do_part_1(TEST, (7, 1)) == 4
    assert _do_part_1(TEST, (1, 2)) == 2
    assert part_2(TEST) == 336

    inputs = my_aocd.get_input(2020, 3, 323)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

#! /usr/bin/env python3
#
# Advent of Code 2022 Day 1
#

import aocd
from aoc import my_aocd


def _solve(inputs: tuple[str], count: int):
    blocks = my_aocd.to_blocks(inputs)
    sums = [sum(int(line) for line in block) for block in blocks]
    return sum(_ for _ in sorted(sums)[-count:])


def part_1(inputs: tuple[str]) -> int:
    return _solve(inputs, 1)


def part_2(inputs: tuple[str]) -> int:
    return _solve(inputs, 3)


TEST = """\
1000
2000
3000

4000

5000
6000

7000
8000
9000

10000
""".splitlines()


def main() -> None:
    aocd.get_data(year=2022, day=1, block=True)
    puzzle = aocd.models.Puzzle(2022, 1)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 24_000
    assert part_2(TEST) == 45_000

    inputs = my_aocd.get_input_data(puzzle, 2264)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

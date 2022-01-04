#! /usr/bin/env python3
#
# Advent of Code 2021 Day 6
#

from collections import deque
from aoc import my_aocd


def _solve(inputs: tuple[str], days: int) -> int:
    assert len(inputs) == 1
    fishies = deque([0] * 9)
    for n in inputs[0].split(','):
        fishies[int(n)] += 1
    for i in range(days):
        zeroes = fishies[0]
        fishies.rotate(-1)
        fishies[6] += zeroes
    return sum(fishies)


def part_1(inputs: tuple[str]) -> int:
    return _solve(inputs, 80)


def part_2(inputs: tuple[str]) -> int:
    return _solve(inputs, 256)


TEST = """\
3,4,3,1,2
""".splitlines()


def main() -> None:
    my_aocd.print_header(2021, 6)

    assert part_1(TEST) == 5_934
    assert part_2(TEST) == 26_984_457_539

    inputs = my_aocd.get_input(2021, 6, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

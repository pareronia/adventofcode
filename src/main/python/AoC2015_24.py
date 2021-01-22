#! /usr/bin/env python3
#
# Advent of Code 2015 Day 24
#

import math
# from copy import deepcopy
from aoc import my_aocd
from aoc.common import log


def _parse(inputs: tuple[str]) -> list[int]:
    return [int(input_.split(",")[0]) for input_ in inputs]


def solve(weights: list[int], groups: int) -> int:
    target = sum(weights) // groups
    log(target)
    weights.sort(reverse=True)
    minimal = []
    for w in weights:
        minimal.append(w)
        if sum(minimal) >= target:
            break
    log(minimal)
    if sum(minimal) == target:
        return math.prod(minimal)
    remainder = target - sum(minimal[:-1])
    if remainder in weights:
        return math.prod(minimal[:-1]) * remainder
    remainder = target - 1 - sum(minimal[:-1])
    if remainder in weights:
        return math.prod(minimal[:-1]) * remainder
    new_ = minimal[:-1]
    skip = new_[-1]
    new_ = new_[:-1]
    new_.append(max(w for w in weights if w not in new_ and w < skip))
    log(new_)
    remainder = target - 1 - sum(new_)
    if remainder in weights:
        return math.prod(new_) * remainder
    raise RuntimeError


def part_1(inputs: tuple[str]) -> int:
    return solve(_parse(inputs), 3)


def part_2(inputs: tuple[str]) -> int:
    return solve(_parse(inputs), 4)


TEST = """\
1,
2,
3,
4,
5,
7,
8
9
10,
11
""".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 24)

    assert part_1(TEST) == 99
    assert part_2(TEST) == 44

    inputs = my_aocd.get_input(2015, 24, 28)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

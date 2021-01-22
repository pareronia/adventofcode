#! /usr/bin/env python3
#
# Advent of Code 2015 Day 24
#

import math
from aoc import my_aocd
from aoc.common import log


def _parse(inputs: tuple[str]) -> list[int]:
    return [int(input_.split(",")[0]) for input_ in inputs]


# works, but only for the first group, and probably depends on specific input
def solve(weights: list[int], groups: int) -> int:
    target = sum(weights) // groups
    log(target)
    weights.sort(reverse=True)
    candidates = []
    for w in weights:
        if sum(candidates) + w >= target:
            break
        candidates.append(w)
    seen = set[tuple[int]]()
    while tuple(candidates) not in seen:
        seen.add(tuple(candidates))
        total = sum(candidates)
        remainder = target - total
        if remainder in weights:
            group = [c for c in candidates]
            group.append(remainder)
            log(group)
            return math.prod(group)
        remainder = target - 1 - total
        if remainder in weights:
            group = [c for c in candidates]
            group.append(remainder)
            group.append(1)
            log(group)
            return math.prod(group)
        skip = candidates[-1]
        candidates = candidates[:-1]
        candidates.append(max(w for w in weights
                              if w not in candidates and w < skip))
    raise RuntimeError("Unsolvable")


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

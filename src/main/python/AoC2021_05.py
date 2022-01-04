#! /usr/bin/env python3
#
# Advent of Code 2021 Day 5
#

import re
from collections import defaultdict
from aoc import my_aocd
from aoc.common import log


def _solve(inputs: tuple[str], diag: bool) -> int:
    d = defaultdict(int)
    for s in inputs:
        x1, y1, x2, y2 = tuple(map(int, re.findall(r'\d+', s)))
        assert sum(_ < 0 for _ in (x1, y1, x2, y2)) == 0
        log((x1, y1, x2, y2))
        mx = 0 if x1 == x2 else 1 if x1 < x2 else -1
        my = 0 if y1 == y2 else 1 if y1 < y2 else -1
        if not diag and mx != 0 and my != 0:
            continue
        length = max(abs(x1 - x2), abs(y1 - y2))
        for i in range(0, length + 1):
            d[(x1 + mx * i, y1 + my * i)] += 1
    log(d)
    return sum(x > 1 for x in d.values())


def part_1(inputs: tuple[str]) -> int:
    return _solve(inputs, False)


def part_2(inputs: tuple[str]) -> int:
    return _solve(inputs, True)


TEST = """\
0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2
""".splitlines()


def main() -> None:
    my_aocd.print_header(2021, 5)

    assert part_1(TEST) == 5
    assert part_2(TEST) == 12

    inputs = my_aocd.get_input(2021, 5, 500)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

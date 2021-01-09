#! /usr/bin/env python3
#
# Advent of Code 2015 Day 17
#

import itertools
from aoc import my_aocd


def _parse(inputs: tuple[str]) -> tuple[int]:
    return tuple([int(input_) for input_ in inputs])


def _do_part_1(inputs: tuple[str], eggnog_volume: int) -> int:
    inputs = _parse(inputs)
    cocos = 0
    for i in range(1, len(inputs) + 1):
        for c in itertools.combinations(inputs, i):
            if sum(c) == eggnog_volume:
                cocos += 1
    return cocos


def part_1(inputs: tuple[str]) -> int:
    return _do_part_1(inputs, 150)


def part_2(inputs: tuple[str]) -> int:
    return 0


TEST = """\
20
15
10
5
5
""".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 17)

    assert _do_part_1(TEST, 25) == 4

    inputs = my_aocd.get_input(2015, 17, 20)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

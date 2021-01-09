#! /usr/bin/env python3
#
# Advent of Code 2015 Day 17
#

import itertools
from aoc import my_aocd


def _parse(inputs: tuple[str]) -> tuple[int]:
    return tuple([int(input_) for input_ in inputs])


def _get_cocos(containers: tuple[int], eggnog_volume: int) -> list[tuple[int]]:
    cocos = list[tuple[int]]()
    for i in range(1, len(containers) + 1):
        for c in itertools.combinations(containers, i):
            if sum(c) == eggnog_volume:
                cocos.append(c)
    return cocos


def _do_part_1(inputs: tuple[str], eggnog_volume: int) -> int:
    return len(_get_cocos(_parse(inputs), eggnog_volume))


def part_1(inputs: tuple[str]) -> int:
    return _do_part_1(inputs, 150)


def _do_part_2(inputs: tuple[str], eggnog_volume: int) -> int:
    cocos = _get_cocos(_parse(inputs), eggnog_volume)
    min_cont = min([len(coco) for coco in cocos])
    return sum([1 for coco in cocos if len(coco) == min_cont])


def part_2(inputs: tuple[str]) -> int:
    return _do_part_2(inputs, 150)


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
    assert _do_part_2(TEST, 25) == 3

    inputs = my_aocd.get_input(2015, 17, 20)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

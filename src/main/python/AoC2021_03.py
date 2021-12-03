#! /usr/bin/env python3
#
# Advent of Code 2021 Day 3
#

from typing import NamedTuple
from aoc import my_aocd


class BitCount(NamedTuple):
    ones: int
    zeroes: int

    def most_common(self) -> str:
        return '1' if self.ones >= self.zeroes else '0'

    def least_common(self) -> str:
        return '1' if self.ones < self.zeroes else '0'


def _ans(value1: str, value2: str) -> int:
    return int(value1, 2) * int(value2, 2)


def _bit_counts(strings: list[str], pos: int) -> BitCount:
    zeroes = sum(s[pos] == '0' for s in strings)
    return BitCount(len(strings) - zeroes, zeroes)


def part_1(inputs: tuple[str]) -> int:
    gamma = ""
    epsilon = ""
    for i in range(len(inputs[0])):
        bit_count = _bit_counts(inputs, i)
        gamma += bit_count.most_common()
        epsilon += bit_count.least_common()
    return _ans(gamma, epsilon)


def _reduce(strings: list[str], keep) -> str:
    pos = 0
    while len(strings) > 1:
        to_keep = keep(_bit_counts(strings, pos))
        strings = [s for s in strings if s[pos] == to_keep]
        pos += 1
    return strings[0]


def part_2(inputs: tuple[str]) -> int:
    o2 = _reduce(list(inputs), lambda x: x.most_common())
    co2 = _reduce(list(inputs), lambda x: x.least_common())
    return _ans(o2, co2)


TEST = """\
00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010
""".splitlines()


def main() -> None:
    my_aocd.print_header(2021, 3)

    assert part_1(TEST) == 198
    assert part_2(TEST) == 230

    inputs = my_aocd.get_input(2021, 3, 1000)
    print(f"Part 1: {part_1(inputs)}")
    print(f"Part 2: {part_2(inputs)}")


if __name__ == '__main__':
    main()

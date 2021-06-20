#! /usr/bin/env python3
#
# Advent of Code 2016 Day 15
#

from __future__ import annotations
import re
from typing import NamedTuple
from aoc import my_aocd


class Disc(NamedTuple):
    period: int
    offset: int
    delay: int

    @classmethod
    def of(cls, period: str, position: str, delay: str) -> Disc:
        period = int(period)
        return Disc(period, (period - int(position)) % period, int(delay))

    def aligned_at(self, time: int) -> bool:
        return (time + self.delay) % self.period == self.offset


def _parse(inputs: tuple[str]) -> list[Disc]:
    return [Disc.of(m[1], m[3], m[0])
            for m in [re.findall(r'[0-9]+', i) for i in inputs]
            ]


def _solve(discs: list[Disc]) -> int:
    i = 0
    while (not all([True if d.aligned_at(i) else False for d in discs])):
        i += 1
    return i


def part_1(inputs: tuple[str]) -> int:
    discs = _parse(inputs)
    return _solve(discs)


def part_2(inputs: tuple[str]) -> int:
    discs = _parse(inputs)
    discs.insert(len(discs), Disc.of("11", "0", str(len(discs)+1)))
    return _solve(discs)


TEST1 = '''\
Disc #1 has 5 positions; at time=0, it is at position 4.
Disc #2 has 2 positions; at time=0, it is at position 1.
'''.splitlines()
TEST2 = '''\
Disc #1 has 5 positions; at time=0, it is at position 3.
Disc #2 has 2 positions; at time=0, it is at position 1.
Disc #3 has 3 positions; at time=0, it is at position 2.
'''.splitlines()


def main() -> None:
    my_aocd.print_header(2016, 15)

    assert part_1(TEST1) == 5
    assert part_1(TEST2) == 1

    inputs = my_aocd.get_input(2016, 15, 6)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

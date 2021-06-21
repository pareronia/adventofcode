#! /usr/bin/env python3
#
# Advent of Code 2016 Day 20
#

from __future__ import annotations
from typing import NamedTuple
from aoc import my_aocd
from functools import reduce


class Range(NamedTuple):
    minimum: int
    maximum: int

    @classmethod
    def between(cls, from_inclusive: int, to_inclusive: int) -> Range:
        return Range(from_inclusive, to_inclusive)

    def contains(self, element: int) -> bool:
        if element is None:
            return False
        return self.minimum <= element and element <= self.maximum

    def is_overlapped_by(self, other: Range) -> bool:
        if other is None:
            return False
        return other.contains(self.minimum) \
            or other.contains(self.maximum) \
            or self.contains(other.minimum)


def _parse(inputs: tuple[str]) -> list[Range]:
    return [Range.between(int(s[0]), int(s[1]))
            for s in [i.split('-') for i in inputs]]


def _combine_ranges(ranges: list[Range]) -> list[Range]:
    combines = list[Range]()
    ranges = sorted(ranges, key=lambda r: r.minimum)
    combines.append(ranges[0])
    for i in range(1, len(ranges)):
        r = ranges[i]
        combined = False
        for j, combine in enumerate(combines):
            if combine.is_overlapped_by(r):
                min_ = min(combine.minimum, r.minimum)
                max_ = max(combine.maximum, r.maximum)
                combines[j] = Range.between(min_, max_)
                combined = True
            elif r.maximum + 1 == combine.minimum:
                combines[j] = Range.between(r.minimum, combine.maximum)
                combined = True
            elif combine.maximum + 1 == r.minimum:
                combines[j] = Range.between(combine.minimum, r.maximum)
                combined = True
        if not combined:
            combines.append(r)
    return combines


def part_1(inputs: tuple[str]) -> int:
    combined = sorted(_combine_ranges(_parse(inputs)), key=lambda c: c.minimum)
    return combined[0].maximum + 1


def part_2(inputs: tuple[str]) -> int:
    combined = _combine_ranges(_parse(inputs))
    return reduce(lambda a, b: a - b,
                  map(lambda c: c.maximum - c.minimum + 1, combined),
                  2 ** 32)


TEST1 = '''\
5-8
0-2
4-7
'''.splitlines()
TEST2 = '''\
0-90000000
100000005-110000005
90000001-90000100
1000000054-1000000057
'''.splitlines()


def main() -> None:
    my_aocd.print_header(2016, 20)

    assert part_1(TEST1) == 3
    assert part_1(TEST2) == 90000101
    assert part_2(TEST1) == 4294967288
    assert part_2(TEST2) == 4194967190

    inputs = my_aocd.get_input(2016, 20, 1075)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

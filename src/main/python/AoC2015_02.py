#! /usr/bin/env python3
#
# Advent of Code 2015 Day 2
#

from __future__ import annotations
from dataclasses import dataclass
from aoc import my_aocd


@dataclass(frozen=True)
class Present:
    l: int
    w: int
    h: int

    @classmethod
    def create(self, length: str, width: str, height: str) -> Present:
        return Present(int(length), int(width), int(height))


def _parse(inputs: tuple[str]) -> list[Present]:
    return [Present.create(*input_.split("x")) for input_ in inputs]


def _calculate_required_area(present: Present) -> int:
    sides = (2 * present.l * present.w,
             2 * present.w * present.h,
             2 * present.h * present.l)
    return sum(sides) + min(sides) // 2


def _calculate_required_length(present: Present) -> int:
    circumferences = (2 * (present.l + present.w),
                      2 * (present.w + present.h),
                      2 * (present.h + present.l))
    return min(circumferences) + present.l * present.w * present.h


def part_1(inputs: tuple[str]) -> int:
    presents = _parse(inputs)
    return sum([_calculate_required_area(p) for p in presents])


def part_2(inputs: tuple[str]) -> int:
    presents = _parse(inputs)
    return sum([_calculate_required_length(p) for p in presents])


TEST1 = "2x3x4".splitlines()
TEST2 = "1x1x10".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 2)

    assert part_1(TEST1) == 58
    assert part_1(TEST2) == 43
    assert part_2(TEST1) == 34
    assert part_2(TEST2) == 14

    inputs = my_aocd.get_input(2015, 2, 1000)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

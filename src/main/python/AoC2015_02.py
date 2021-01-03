#! /usr/bin/env python3
#
# Advent of Code 2015 Day 2
#

from dataclasses import dataclass
from aoc import my_aocd
from aoc.common import log


@dataclass
class Present:
    l: int
    w: int
    h: int

    def __init__(self, length, w, h):
        self.l = int(length)  # noqa
        self.w = int(w)
        self.h = int(h)


def _parse(inputs: tuple[str]) -> list[Present]:
    return [Present(*input_.split("x")) for input_ in inputs]


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
    log(presents)
    return sum([_calculate_required_area(p) for p in presents])


def part_2(inputs: tuple[str]) -> int:
    presents = _parse(inputs)
    return sum([_calculate_required_length(p) for p in presents])


test1 = "2x3x4".splitlines()
test2 = "1x1x10".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 2)

    assert part_1(test1) == 58
    assert part_1(test2) == 43
    assert part_2(test1) == 34
    assert part_2(test2) == 14

    inputs = my_aocd.get_input_as_tuple(2015, 2, 1000)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

#! /usr/bin/env python3
#
# Advent of Code 2015 Day 2
#

from aoc import my_aocd


def _calculate_required_area(present: tuple[int, int, int]) -> int:
    l, w, h = present
    sides = (2 * l * w, 2 * w * h, 2 * h * l)
    return sum(sides) + min(sides) // 2


def _calculate_required_length(present: tuple[int, int, int]) -> int:
    l, w, h = present
    circumferences = (2 * (l + w), 2 * (w + h), 2 * (h + l))
    return min(circumferences) + l * w * h


def part_1(inputs: tuple[str]) -> int:
    return sum(_calculate_required_area((map(int, line.split('x'))))
               for line in inputs)


def part_2(inputs: tuple[str]) -> int:
    return sum(_calculate_required_length((map(int, line.split('x'))))
               for line in inputs)


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

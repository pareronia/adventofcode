#! /usr/bin/env python3
#
# Advent of Code 2015 Day 9
#

from aoc import my_aocd
from aoc.common import log


def part_1(inputs: tuple[str]) -> int:
    assert len(inputs) == 1
    cnt = 0
    skip = 0
    in_marker = False
    marker = ""
    for i in range(len(inputs[0])):
        if skip > 0:
            skip -= 1
            continue
        c = inputs[0][i]
        if c == '(':
            in_marker = True
        elif c == ')':
            in_marker = False
            splits = marker.split('x')
            add = int(splits[0]) * int(splits[1])
            log("adding: " + str(add))
            cnt += add
            skip = int(splits[0])
            log("skipping: " + str(skip))
            marker = ""
        else:
            if in_marker:
                marker += c
            else:
                cnt += 1
    return cnt


def part_2(inputs: tuple[str]) -> int:
    return 0


TEST1 = "ADVENT".splitlines()
TEST2 = "A(1x5)BC".splitlines()
TEST3 = "(3x3)XYZ".splitlines()
TEST4 = "A(2x2)BCD(2x2)EFG".splitlines()
TEST5 = "(6x1)(1x3)A".splitlines()
TEST6 = "X(8x2)(3x3)ABCY".splitlines()


def main() -> None:
    my_aocd.print_header(2016, 9)

    assert part_1(TEST1) == 6
    assert part_1(TEST2) == 7
    assert part_1(TEST3) == 9
    assert part_1(TEST4) == 11
    assert part_1(TEST5) == 6
    assert part_1(TEST6) == 18

    inputs = my_aocd.get_input(2016, 9, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

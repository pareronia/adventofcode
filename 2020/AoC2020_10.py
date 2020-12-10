#! /usr/bin/env python3
#
# Advent of Code 2020 Day 10
#
import my_aocd
from common import log


def _get_sorted(inputs: tuple[int]) -> tuple[int]:
    sorted_ = list(inputs)
    sorted_.sort()
    return tuple(sorted_)


def part_1(inputs: tuple[int]) -> int:
    inputs = _get_sorted(inputs) 
    log(inputs)
    prev = 0
    jumps_1 = 0
    jumps_3 = 0
    for n in inputs:
        if n-prev == 1:
            jumps_1 += 1
        elif n-prev == 3:
            jumps_3 += 1
        else:
            raise ValueError("Invalid input")
        prev = n
    jumps_3 += 1
    return jumps_1*jumps_3


def part_2(inputs: tuple[int]) -> int:
    return 0


test_1 = (16,
          10,
          15,
          5,
          1,
          11,
          7,
          19,
          6,
          12,
          4
          )
test_2 = (28,
          33,
          18,
          42,
          31,
          14,
          46,
          20,
          48,
          47,
          24,
          23,
          49,
          45,
          19,
          38,
          39,
          11,
          1,
          32,
          25,
          35,
          8,
          17,
          7,
          9,
          4,
          2,
          34,
          10,
          3
          )


def main() -> None:
    my_aocd.print_header(2020, 10)

    assert part_1(test_1) == 35
    assert part_1(test_2) == 220
    assert part_2(test_1) == 0
    assert part_2(test_2) == 0

    inputs = my_aocd.get_input_as_ints_tuple(2020, 10, 101)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

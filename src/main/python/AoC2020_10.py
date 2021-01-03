#! /usr/bin/env python3
#
# Advent of Code 2020 Day 10
#
from collections import defaultdict
from aoc import my_aocd
from aoc.common import log


def _get_sorted(inputs: tuple[int]) -> tuple[int]:
    sorted_ = list(inputs)
    sorted_.sort()
    return tuple(sorted_)


def _find_jumps(inputs: tuple[int]):
    jumps_1 = []
    jumps_3 = []
    for i in range(1, len(inputs)):
        if inputs[i]-inputs[i-1] != 1:
            jumps_3.append(i)
        else:
            jumps_1.append(i)
    return jumps_1, jumps_3


def part_1(inputs: tuple[int]) -> int:
    inputs = _get_sorted(inputs)
    log(inputs)
    jumps_1, jumps_3 = _find_jumps(inputs)
    log((jumps_1, jumps_3))
    return (len(jumps_1)+1)*(len(jumps_3)+1)


def part_2(inputs: tuple[int]) -> int:
    inputs = _get_sorted(inputs)
    log(inputs)
    seen = defaultdict(int)
    seen[0] = 1
    seen[inputs[0]] = 1
    log(seen)
    for i in inputs[1:]:
        for j in range(inputs.index(i)-1, -2, -1):
            if j == -1:
                j_ = 0
            else:
                j_ = inputs[j]
            if i - j_ <= 3:
                seen[i] = seen[i] + seen[j_]
        log(seen)
    log(seen[inputs[-1]])
    return seen[inputs[-1]]


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
    assert part_2(test_1) == 8
    assert part_2(test_2) == 19208

    inputs = my_aocd.get_input_as_ints_tuple(2020, 10, 101)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

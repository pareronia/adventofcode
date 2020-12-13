#! /usr/bin/env python3
#
# Advent of Code 2020 Day 13
#

import my_aocd
from common import log


def parse(inputs: tuple[str]):
    buses = [int(b) for b in inputs[1].split(",") if b != "x"]
    return int(inputs[0]), buses


def part_1(inputs: tuple[int]) -> int:
    target, buses = parse(inputs)
    log((target, buses))
    cnt = 0
    while True:
        t = target + cnt
        for b in buses:
            if t % b == 0:
                return b * cnt
        cnt += 1


def part_2(inputs: tuple[int]) -> int:
    return 0


test = ("939",
        "7,13,x,x,59,x,31,19"
        )


def main() -> None:
    my_aocd.print_header(2020, 13)

    assert part_1(test) == 295
    assert part_2(test) == 0

    inputs = my_aocd.get_input_as_tuple(2020, 13, 2)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

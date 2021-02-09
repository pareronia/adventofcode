#! /usr/bin/env python3
#
# Advent of Code 2019 Day 8
#

from aoc import my_aocd


def part_1(inputs: tuple[str]) -> int:
    assert len(inputs) == 1
    c = []
    for i in range(0, 15000, 150):
        s = inputs[0][i:i+150]
        c.append((s.count("0"), s.count("1"), s.count("2")))
    min0 = min(zeroes for zeroes, _, _ in c)
    return [ones * twos for zeroes, ones, twos in c if zeroes == min0][0]


def part_2(inputs: tuple[str]) -> int:
    return 0


def main() -> None:
    my_aocd.print_header(2019, 8)

    inputs = my_aocd.get_input(2019, 8, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

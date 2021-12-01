#! /usr/bin/env python3
#
# Advent of Code 2021 Day 1
#

from aoc import my_aocd


def _parse(inputs: tuple[str]) -> tuple[int]:
    return tuple(int(_) for _ in inputs)


def part_1(inputs: tuple[str]) -> int:
    depths = _parse(inputs)
    count = 0
    prev = depths[0]
    for i in range(1, len(depths)):
        curr = depths[i]
        if curr > prev:
            count += 1
        prev = curr
    return count


def part_2(inputs: tuple[str]) -> int:
    depths = _parse(inputs)
    count = 0
    prev = depths[0:3]
    for i in range(1, len(depths) - 2):
        curr = depths[i:i+3]
        if sum(curr) > sum(prev):
            count += 1
        prev = curr
    return count


TEST = """\
199
200
208
210
200
207
240
269
260
263
""".splitlines()


def main() -> None:
    my_aocd.print_header(2021, 1)

    assert part_1(TEST) == 7
    assert part_2(TEST) == 5

    inputs = my_aocd.get_input(2021, 1, 2000)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

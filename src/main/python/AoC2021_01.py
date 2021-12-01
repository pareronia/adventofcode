#! /usr/bin/env python3
#
# Advent of Code 2021 Day 1
#

from aoc import my_aocd


def part_1(inputs: tuple[str]) -> int:
    count = 0
    prev = int(inputs[0])
    for i in range(1, len(inputs)):
        curr = int(inputs[i])
        if curr > prev:
            count += 1
        prev = curr
    return count


def part_2(inputs: tuple[str]) -> int:
    count = 0
    prev = [int(inputs[0]), int(inputs[1]), int(inputs[2])]
    for i in range(1, len(inputs) - 2):
        curr = [int(inputs[i]), int(inputs[i + 1]), int(inputs[i + 2])]
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

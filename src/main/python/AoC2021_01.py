#! /usr/bin/env python3
#
# Advent of Code 2021 Day 1
#

from aoc import my_aocd


def _parse(inputs: tuple[str]) -> tuple[int]:
    return tuple(int(_) for _ in inputs)


def _solve(depths: tuple[int], window: int) -> int:
    return sum([depths[i] > depths[i-window]
                for i in range(window, len(depths))])


def part_1(inputs: tuple[str]) -> int:
    return _solve(_parse(inputs), 1)


def part_2(inputs: tuple[str]) -> int:
    return _solve(_parse(inputs), 3)


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

#! /usr/bin/env python3
#
# Advent of Code 2016 Day 3
#

from aoc import my_aocd


def _parse(inputs: tuple[str]) -> tuple[tuple[int]]:
    return ((int(_[0]), int(_[1]), int(_[2]))
            for _ in (line.split() for line in inputs)
            )


def _valid(t: tuple[int]) -> bool:
    return t[0] + t[1] > t[2] \
            and t[0] + t[2] > t[1] \
            and t[1] + t[2] > t[0]


def part_1(inputs: tuple[str]) -> int:
    return sum(1 for t in _parse(inputs) if _valid(t))


def part_2(inputs: tuple[str]) -> int:
    ts = list(_parse(inputs))
    return sum(int(_valid((ts[i][0], ts[i+1][0], ts[i+2][0])))
               + int(_valid((ts[i][1], ts[i+1][1], ts[i+2][1])))
               + int(_valid((ts[i][2], ts[i+1][2], ts[i+2][2])))
               for i in range(0, len(ts), 3)
               )


TEST1 = "5 10 25".splitlines()
TEST2 = "3 4 5".splitlines()
TEST3 = '''\
5 3 6
10 4 8
25 5 10
'''.splitlines()


def main() -> None:
    my_aocd.print_header(2016, 3)

    assert part_1(TEST1) == 0
    assert part_1(TEST2) == 1
    assert part_2(TEST3) == 2

    inputs = my_aocd.get_input(2016, 3, 1911)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

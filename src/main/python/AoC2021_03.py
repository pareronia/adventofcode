#! /usr/bin/env python3
#
# Advent of Code 2021 Day 3
#

from aoc import my_aocd


def part_1(inputs: tuple[str]) -> int:
    gamma = ""
    epsilon = ""
    for i in range(len(inputs[0])):
        cnt0 = 0
        cnt1 = 0
        for b in inputs:
            if b[i] == '0':
                cnt0 += 1
            else:
                cnt1 += 1
        if cnt0 > cnt1:
            gamma += '0'
            epsilon += '1'
        else:
            gamma += '1'
            epsilon += '0'
    return int(gamma, 2) * int(epsilon, 2)


def part_2(inputs: tuple[str]) -> int:
    return 0


TEST = """\
00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010
""".splitlines()


def main() -> None:
    my_aocd.print_header(2021, 3)

    assert part_1(TEST) == 198
    assert part_2(TEST) == 0

    inputs = my_aocd.get_input(2021, 3, 1000)
    print(f"Part 1: {part_1(inputs)}")
    print(f"Part 2: {part_2(inputs)}")


if __name__ == '__main__':
    main()

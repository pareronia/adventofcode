#! /usr/bin/env python3
#
# Advent of Code 2021 Day 2
#

from aoc import my_aocd


def part_1(inputs: tuple[str]) -> int:
    hor = 0
    ver = 0
    for input in inputs:
        dir, amount = input.split()
        if dir == "forward":
            hor += int(amount)
        elif dir == "down":
            ver += int(amount)
        elif dir == "up":
            ver -= int(amount)
        else:
            raise ValueError
    return hor * ver


def part_2(inputs: tuple[str]) -> int:
    return 0


TEST = """\
forward 5
down 5
forward 8
up 3
down 8
forward 2
""".splitlines()


def main() -> None:
    my_aocd.print_header(2021, 2)

    assert part_1(TEST) == 150
    assert part_2(TEST) == 0

    inputs = my_aocd.get_input(2021, 2, 1000)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

#! /usr/bin/env python3
#
# Advent of Code 2015 Day 10
#

from aoc import my_aocd
from aoc.common import spinner


def _look_and_say(string: str) -> str:
    result = ""
    i = 0
    while i < len(string):
        digit = string[i]
        j = 0
        while i+j < len(string) and string[i+j] == digit:
            j += 1
        result += str(j) + digit
        i += j
    return result


def _look_and_say_iterations(string: str, iterations: int) -> str:
    for i in range(iterations):
        string = _look_and_say(string)
        spinner(i, iterations // 4)
    return string


def part_1(inputs: tuple[str]) -> int:
    assert len(inputs) == 1
    return len(_look_and_say_iterations(inputs[0], 40))


def part_2(inputs: tuple[str]) -> int:
    assert len(inputs) == 1
    return len(_look_and_say_iterations(inputs[0], 50))


TEST = "1".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 10)

    assert _look_and_say_iterations(TEST, 5) == "312211"

    inputs = my_aocd.get_input(2015, 10, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

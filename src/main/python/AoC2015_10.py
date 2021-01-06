#! /usr/bin/env python3
#
# Advent of Code 2015 Day 10
#

from aoc import my_aocd


def _look_and_say(string: str) -> str:
    if len(string) == 1:
        return "1" + string[0]
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


def part_1(inputs: tuple[str], iterations: int) -> int:
    assert len(inputs) == 1
    string = inputs[0]
    for i in range(iterations):
        string = _look_and_say(string)
    return len(string)


def part_2(inputs: tuple[str]) -> int:
    return 0


TEST = """\
1
""".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 10)

    assert part_1(TEST, 5) == 6
    assert part_2(TEST) == 0

    inputs = my_aocd.get_input_as_tuple(2015, 10, 1)
    result1 = part_1(inputs, 40)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

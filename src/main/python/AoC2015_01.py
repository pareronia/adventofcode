#! /usr/bin/env python3
#
# Advent of Code 2015 Day 1
#

from aoc import my_aocd


def part_1(inputs: tuple[str]) -> int:
    assert len(inputs) == 1
    return len(inputs[0]) - 2 * inputs[0].count(")")


def part_2(inputs: tuple[str]) -> int:
    assert len(inputs) == 1
    sum_ = int()
    for i, c in enumerate(inputs[0]):
        if c == "(":
            sum_ += 1
        elif c == ")":
            sum_ -= 1
        else:
            raise ValueError("Invalid input")
        if sum_ == -1:
            return i + 1
    raise RuntimeError("Unreachable")


TEST1 = "(())".splitlines()
TEST2 = "()()".splitlines()
TEST3 = "(((".splitlines()
TEST4 = "(()(()(".splitlines()
TEST5 = "))(((((".splitlines()
TEST6 = "())".splitlines()
TEST7 = "))(".splitlines()
TEST8 = ")))".splitlines()
TEST9 = ")())())".splitlines()
TEST10 = ")".splitlines()
TEST11 = "()())".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 1)

    assert part_1(TEST1) == 0
    assert part_1(TEST2) == 0
    assert part_1(TEST3) == 3
    assert part_1(TEST4) == 3
    assert part_1(TEST5) == 3
    assert part_1(TEST6) == -1
    assert part_1(TEST7) == -1
    assert part_1(TEST8) == -3
    assert part_1(TEST9) == -3
    assert part_2(TEST10) == 1
    assert part_2(TEST11) == 5

    inputs = my_aocd.get_input(2015, 1, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

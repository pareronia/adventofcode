#! /usr/bin/env python3
#
# Advent of Code 2015 Day 1
#

from aoc import my_aocd


def part_1(inputs: tuple[str]) -> int:
    return len(inputs[0]) - 2 * inputs[0].count(")")


test1 = "(())".splitlines()
test2 = "()()".splitlines()
test3 = "(((".splitlines()
test4 = "(()(()(".splitlines()
test5 = "))(((((".splitlines()
test6 = "())".splitlines()
test7 = "))(".splitlines()
test8 = ")))".splitlines()
test9 = ")())())".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 1)

    assert part_1(test1) == 0
    assert part_1(test2) == 0
    assert part_1(test3) == 3
    assert part_1(test4) == 3
    assert part_1(test5) == 3
    assert part_1(test6) == -1
    assert part_1(test7) == -1
    assert part_1(test8) == -3
    assert part_1(test9) == -3

    inputs = my_aocd.get_input_as_tuple(2015, 1, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")


if __name__ == '__main__':
    main()

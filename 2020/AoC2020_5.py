#! /usr/bin/env python3
#
# Advent of Code 2020 Day 5
#
import aocd


def _get_input() -> list[str]:
    inputs = aocd.get_data(year=2020, day=5).splitlines()
    assert len(inputs) == 839
    return inputs


def part_1(inputs: list[str]) -> int:
    table = str.maketrans("FBLR", "0101")
    return max([int(input_.translate(table), 2) for input_ in inputs])


test = ["FBFBBFFRLR",
        "BFFFBBFRRR",
        "FFFBBBFRRR",
        "BBFFBBFRLL"
        ]


def main() -> None:
    print("====================================================")
    print("AoC 2020 Day 5 - https://adventofcode.com/2020/day/5")
    print("====================================================")
    print("")

    assert part_1(test) == 820

    inputs = _get_input()
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")


if __name__ == '__main__':
    main()

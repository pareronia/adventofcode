#! /usr/bin/env python3
#
# Advent of Code 2020 Day 5
#
from aoc import my_aocd


def _translate(input_: str) -> str:
    table = str.maketrans("FBLR", "0101")
    return input_.translate(table)


def _as_int(binary: str) -> int:
    return int(binary, 2)


def part_1(inputs: tuple[str]) -> int:
    return max([_as_int(_translate(input_)) for input_ in inputs])


def part_2(inputs: tuple[str]) -> int:
    sorted_inputs = list(inputs)
    sorted_inputs.sort()
    for i in range(len(sorted_inputs)):
        if i+1 == len(sorted_inputs):
            raise ValueError("Unsolvable")
        if sorted_inputs[i][-1] == sorted_inputs[i+1][-1]:
            return _as_int(_translate(sorted_inputs[i])) + 1


test_1 = ["FBFBBFFRLR",
          "BFFFBBFRRR",
          "FFFBBBFRRR",
          "BBFFBBFRLL"
          ]


test_2 = ["FFFFFFFFFF",
          "FFFFFFFFFR",
          "FFFFFFFFRF",
          # "FFFFFFFFRR",
          "FFFFFFFRFF"
          ]


def main() -> None:
    my_aocd.print_header(2020, 5)

    assert part_1(test_1) == 820
    assert part_2(test_2) == 3

    inputs = my_aocd.get_input_as_tuple(2020, 5, 839)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

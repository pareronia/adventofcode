#! /usr/bin/env python3
#
# Advent of Code 2020 Day 5
#
import my_aocd


def _translate(input_: str) -> str:
    table = str.maketrans("FBLR", "0101")
    return input_.translate(table)


def _as_int(binary: str) -> int:
    return int(binary, 2)


def part_1(inputs: list[str]) -> int:
    translated = [_translate(input_) for input_ in inputs]
    return max([_as_int(translated) for translated in translated])


def part_2(inputs: list[str]) -> int:
    sorted_inputs = list(inputs)
    sorted_inputs.sort()
    for i in range(0, len(sorted_inputs)):
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
    print("====================================================")
    print("AoC 2020 Day 5 - https://adventofcode.com/2020/day/5")
    print("====================================================")
    print("")

    assert part_1(test_1) == 820
    assert part_2(test_2) == 3

    inputs = my_aocd.get_input_as_list(2020, 5, 839)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

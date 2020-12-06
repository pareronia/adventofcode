#! /usr/bin/env python3
#
# Advent of Code 2020 Day 6
#
import aocd


def _get_input() -> list[str]:
    inputs = aocd.get_data(year=2020, day=6).splitlines()
    assert len(inputs) == 2042
    return inputs


def part_1(inputs: list[str]) -> int:
    inputs.append("")
    all_answers = []
    answers = set()
    for input_ in inputs:
        if len(input_) > 0:
            answers.update(input_)
            continue
        all_answers.append(answers)
        answers = set()
    all_answers.append(answers)
    return sum([len(answers) for answers in all_answers])


test = ["abc",
        "",
        "a",
        "b",
        "c",
        "",
        "ab",
        "ac",
        "",
        "a",
        "a",
        "a",
        "a",
        "",
        "b",
        ]


def main() -> None:
    print("====================================================")
    print("AoC 2020 Day 6 - https://adventofcode.com/2020/day/6")
    print("====================================================")
    print("")

    assert part_1(test) == 11

    inputs = _get_input()
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")


if __name__ == '__main__':
    main()

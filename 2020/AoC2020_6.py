#! /usr/bin/env python3
#
# Advent of Code 2020 Day 6
#
import aocd


def _get_input() -> list[str]:
    inputs = aocd.get_data(year=2020, day=6).splitlines()
    assert len(inputs) == 2042
    return inputs


def _append_empty_line(lst: list[str]) -> list[str]:
    new_lst = list(lst)
    new_lst.append("")
    return new_lst


def _sum_of_counts(lists: list[list]) -> int:
    return sum([len(lst) for lst in lists])


def part_1(inputs: list[str]) -> int:
    inputs = _append_empty_line(inputs)
    unique_anwers_per_group = []
    unique_answers_for_group = set()
    for input_ in inputs:
        if len(input_) > 0:
            unique_answers_for_group.update(input_)
            continue
        unique_anwers_per_group.append(unique_answers_for_group)
        unique_answers_for_group = set()
    return _sum_of_counts(unique_anwers_per_group)


alfabet = "abcdefghijklmnopqrstuvwxyz"


def part_2(inputs: list[str]) -> int:
    inputs = _append_empty_line(inputs)
    common_answers_per_group = []
    common_answers_for_group = set(alfabet)
    for input_ in inputs:
        if len(input_) > 0:
            common_answers_for_group = common_answers_for_group\
                    .intersection(set(input_))
            continue
        common_answers_per_group.append(common_answers_for_group)
        common_answers_for_group = set(alfabet)
    return _sum_of_counts(common_answers_per_group)


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
    assert part_2(test) == 6

    inputs = _get_input()
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

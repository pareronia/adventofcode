#! /usr/bin/env python3
#
# Advent of Code 2020 Day 6
#
import my_aocd


def _append_empty_line(lst: tuple[str]) -> list[str]:
    new_lst = list(lst)
    new_lst.append("")
    return new_lst


def _sum_of_counts(lists: list[list]) -> int:
    return sum([len(lst) for lst in lists])


def part_1(inputs: tuple[str]) -> int:
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


def part_2(inputs: tuple[str]) -> int:
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
    my_aocd.print_header(2020, 6)

    assert part_1(test) == 11
    assert part_2(test) == 6

    inputs = my_aocd.get_input_as_tuple(2020, 6, 2042)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

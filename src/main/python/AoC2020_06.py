#! /usr/bin/env python3
#
# Advent of Code 2020 Day 6
#
from aoc import my_aocd


def _sum_of_counts(lists: list[list]) -> int:
    return sum([len(lst) for lst in lists])


def part_1(inputs: tuple[str]) -> int:
    blocks = my_aocd.to_blocks(inputs)
    unique_anwers_per_group = []
    for block in blocks:
        unique_answers_for_group = set()
        for input_ in block:
            unique_answers_for_group.update(input_)
        unique_anwers_per_group.append(unique_answers_for_group)
    return _sum_of_counts(unique_anwers_per_group)


alfabet = "abcdefghijklmnopqrstuvwxyz"


def part_2(inputs: tuple[str]) -> int:
    blocks = my_aocd.to_blocks(inputs)
    common_answers_per_group = []
    for block in blocks:
        common_answers_for_group = set(alfabet)
        for input_ in block:
            common_answers_for_group = common_answers_for_group\
                    .intersection(set(input_))
        common_answers_per_group.append(common_answers_for_group)
    return _sum_of_counts(common_answers_per_group)


TEST = """\
abc

a
b
c

ab
ac

a
a
a
a

b
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 6)

    assert part_1(TEST) == 11
    assert part_2(TEST) == 6

    inputs = my_aocd.get_input(2020, 6, 2042)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

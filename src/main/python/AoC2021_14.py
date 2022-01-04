#! /usr/bin/env python3
#
# Advent of Code 2021 Day 14
#

from collections import Counter
from aoc import my_aocd
import aocd


def _parse(inputs: tuple[str]) -> tuple[str, dict[str, str]]:
    template = inputs[0]
    rules = dict[str, str]()
    for line in inputs[2:]:
        k, v = line.split(' -> ')
        rules[k] = v
    return template, rules


def _solve(inputs: tuple[str], cycles: int) -> int:
    template, rules = _parse(inputs)
    pair_counter = Counter()
    elem_counter = Counter()
    for i in range(len(template)):
        elem_counter[template[i]] += 1
        if i == 0:
            continue
        pair_counter[template[i-1] + template[i]] += 1
    for _ in range(cycles):
        pair_counter_2 = Counter()
        for pair, count in pair_counter.items():
            elem = rules[pair]
            elem_counter[elem] += count
            pair_counter_2[pair[0] + elem] += count
            pair_counter_2[elem + pair[1]] += count
        pair_counter = pair_counter_2
    return elem_counter.most_common()[0][1] - elem_counter.most_common()[-1][1]


def part_1(inputs: tuple[str]) -> int:
    return _solve(inputs, 10)


def part_2(inputs: tuple[str]) -> int:
    return _solve(inputs, 40)


TEST = """\
NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 14)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 1_588
    assert part_2(TEST) == 2_188_189_693_529

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 102)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()

#! /usr/bin/env python3
#
# Advent of Code 2015 Day 6
#

from collections import Counter
from aoc import my_aocd


def _get_counters(inputs: tuple[str]) -> list[Counter]:
    def count(i: int) -> Counter:
        cnt = Counter()
        for input_ in inputs:
            cnt[input_[i]] += 1
        return cnt
    return [count(i) for i in range(len(inputs[0]))]


def part_1(inputs: tuple[str]) -> str:
    return "".join([counter.most_common()[0][0]
                    for counter in _get_counters(inputs)])


def part_2(inputs: tuple[str]) -> str:
    return "".join([counter.most_common()[-1][0]
                    for counter in _get_counters(inputs)])


TEST = '''\
eedadn
drvtee
eandsr
raavrd
atevrs
tsrnev
sdttsa
rasrtv
nssdts
ntnada
svetve
tesnvt
vntsnd
vrdear
dvrsen
enarar
'''.splitlines()


def main() -> None:
    my_aocd.print_header(2016, 6)

    assert part_1(TEST) == "easter"
    assert part_2(TEST) == "advent"

    inputs = my_aocd.get_input(2016, 6, 624)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

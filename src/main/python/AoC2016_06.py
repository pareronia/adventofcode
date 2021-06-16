#! /usr/bin/env python3
#
# Advent of Code 2015 Day 6
#

from collections import defaultdict
from aoc import my_aocd


def part_1(inputs: tuple[str]) -> str:
    hists = list()
    for i in range(len(inputs[0])):
        hists.append(defaultdict(lambda: 0))
    for input_ in inputs:
        for i in range(len(input_)):
            hists[i][input_[i]] = hists[i][input_[i]] + 1
    return "".join([max(hist, key=hist.get) for hist in hists])


def part_2(inputs: tuple[str]) -> str:
    return ""


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

    inputs = my_aocd.get_input(2016, 6, 624)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

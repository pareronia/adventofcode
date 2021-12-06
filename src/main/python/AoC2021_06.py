#! /usr/bin/env python3
#
# Advent of Code 2021 Day 6
#

from collections import Counter
from aoc import my_aocd
from aoc.common import log


def _solve(inputs: tuple[str], days: int) -> int:
    assert len(inputs) == 1
    fishies = Counter([int(_) for _ in inputs[0].split(',')])
    log(fishies)
    for i in range(days):
        new = Counter()
        new[0] = fishies[1]
        new[1] = fishies[2]
        new[2] = fishies[3]
        new[3] = fishies[4]
        new[4] = fishies[5]
        new[5] = fishies[6]
        new[6] = fishies[7] + fishies[0]
        new[7] = fishies[8]
        new[8] = fishies[0]
        fishies = new
        log(f"({i + 1}) {sum(new.values())}: {new}")
    return sum(fishies.values())


def part_1(inputs: tuple[str]) -> int:
    return _solve(inputs, 80)


def part_2(inputs: tuple[str]) -> int:
    return _solve(inputs, 256)


TEST = """\
3,4,3,1,2
""".splitlines()


def main() -> None:
    my_aocd.print_header(2021, 6)

    assert part_1(TEST) == 5_934
    assert part_2(TEST) == 26_984_457_539

    inputs = my_aocd.get_input(2021, 6, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

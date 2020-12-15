#! /usr/bin/env python3
#
# Advent of Code 2020 Day 15
#

from collections import defaultdict
import my_aocd
from common import log


def part_1(inputs: list) -> int:
    nums = [int(n) for n in inputs]
    log(nums)
    turns = defaultdict(list)
    for i, n in enumerate(nums):
        turns[n].append(i+1)
    last = n
    # log(f"turn: {i+1}, last: {last}, turns: {turns}")
    for i in range(len(nums)+1, 2021):
        if len(turns[last]) <= 1:
            last = 0
        else:
            last = turns[last][-1] - turns[last][-2]
        turns[last].append(i)
        # log(f"turn: {i}, last: {last}, turns: {turns}")
    return last


def part_2(inputs: str) -> int:
    return 0


test_1 = "0,3,6"
test_2 = "1,3,2"
test_3 = "2,1,3"


def main() -> None:
    my_aocd.print_header(2020, 15)

    assert part_1(test_1.split(",")) == 436
    assert part_1(test_2.split(",")) == 1
    assert part_1(test_3.split(",")) == 10
    assert part_2(test_1) == 0

    inputs = my_aocd.get_input_as_list(2020, 15, 1)[0].split(",")
    log(inputs)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

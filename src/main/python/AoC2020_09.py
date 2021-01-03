#! /usr/bin/env python3
#
# Advent of Code 2020 Day 9
#
from aoc import my_aocd
from aoc.common import log


def find_two_summands(numbers: list[int], sum_: int):
    for n1 in numbers:
        n2 = sum_ - n1
        if n2 in numbers:
            return (n1, n2)
    return None


def part_1(inputs: tuple[int], window_size: int = 25) -> int:
    log(inputs)
    _range = range(window_size, len(inputs))
    for i in _range:
        n = inputs[i]
        search_window = inputs[i-window_size:i]
        log(search_window)
        if find_two_summands(search_window, sum_=n) is None:
            return n
    raise ValueError("Unsolvable")


def _sublists(inputs: tuple[int], min_size: int) -> [[int]]:
    sublists = [[]]
    for i in range(len(inputs) + 1):
        for j in range(i + 1, len(inputs) + 1):
            if j-i < min_size:
                continue
            sli = inputs[i:j]
            sublists.append(sli)
    return sublists


def _collect_all_sublists_before_target_with_minimum_size_2(
        inputs: tuple[int], target: int) -> [[]]:
    return _sublists(inputs[:target], min_size=2)


def part_2(inputs: tuple[int], target: int,
           window_size: int = 25) -> int:
    log(inputs)
    target_pos = inputs.index(target)
    log(target_pos)
    sublists = _collect_all_sublists_before_target_with_minimum_size_2(
        inputs, target_pos)
    ss = [s for s in sublists if sum(s) == target]
    log(f"Found: {ss}")
    if len(ss) == 1:
        return min(ss[0])+max(ss[0])
    raise ValueError("Unsolvable")


test = (35,
        20,
        15,
        25,
        47,
        40,
        62,
        55,
        65,
        95,
        102,
        117,
        150,
        182,
        127,
        219,
        299,
        277,
        309,
        576
        )


def main() -> None:
    my_aocd.print_header(2020, 9)

    assert part_1(test, 5) == 127
    assert part_2(test, 127, 5) == 62

    inputs = my_aocd.get_input_as_ints_tuple(2020, 9, 1000)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs, result1)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

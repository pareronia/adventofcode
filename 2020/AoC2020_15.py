#! /usr/bin/env python3
#
# Advent of Code 2020 Day 15
#

from collections import defaultdict, deque
import my_aocd
from common import log


class Turns:
    turns: dict

    def new_deque(self) -> deque:
        return deque(maxlen=2)

    def __init__(self):
        self.turns = defaultdict(self.new_deque)

    def get_two_latest_turns(self, num: int) -> (int, int):
        if len(self.turns[num]) <= 1:
            return None
        else:
            return (self.turns[num][-1], self.turns[num][-2])

    def add_latest_turn(self, num: int, turn: int) -> None:
        self.turns[num].append(turn)

    def get_size(self):
        return len(self.turns)


def _play(starting_numbers: list[int], number_of_turns: int) -> int:
    nums = [int(n) for n in starting_numbers]
    log(nums)
    turns = Turns()
    for i, n in enumerate(nums):
        turns.add_latest_turn(n, i+1)
    last = n
    for i in range(len(nums)+1, number_of_turns+1):
        latest_turns = turns.get_two_latest_turns(last)
        if latest_turns is None:
            last = 0
        else:
            last = latest_turns[0] - latest_turns[1]
        turns.add_latest_turn(last, i)
    log(turns.get_size())
    return last


def part_1(inputs: list) -> int:
    return _play(starting_numbers=inputs, number_of_turns=2020)


def part_2(inputs: list) -> int:
    return _play(starting_numbers=inputs, number_of_turns=30000000)


test_1 = "0,3,6"
test_2 = "1,3,2"
test_3 = "2,1,3"
test_4 = "1,2,3"
test_5 = "2,3,1"
test_6 = "3,2,1"
test_7 = "3,1,2"


def main() -> None:
    my_aocd.print_header(2020, 15)

    assert part_1(test_1.split(",")) == 436
    assert part_1(test_2.split(",")) == 1
    assert part_1(test_3.split(",")) == 10
    assert part_1(test_4.split(",")) == 27
    assert part_1(test_5.split(",")) == 78
    assert part_1(test_6.split(",")) == 438
    assert part_1(test_7.split(",")) == 1836
    assert part_2(test_1.split(",")) == 175594
    assert part_2(test_2.split(",")) == 2578
    assert part_2(test_3.split(",")) == 3544142
    assert part_2(test_4.split(",")) == 261214
    assert part_2(test_5.split(",")) == 6895259
    assert part_2(test_6.split(",")) == 18
    assert part_2(test_7.split(",")) == 362

    inputs = my_aocd.get_input_as_list(2020, 15, 1)[0].split(",")
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

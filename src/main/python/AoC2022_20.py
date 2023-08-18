#! /usr/bin/env python3
#
# Advent of Code 2022 Day 20
#


from __future__ import annotations

import aocd

from aoc import my_aocd
from aoc.common import clog, log


class Nums:
    def __init__(self, inputs: tuple[str], factor: int):
        self.nums = list(map(lambda line: int(line) * factor, inputs))
        size = len(self.nums)
        self.next: list[int] = [None] * size
        self.prev: list[int] = [None] * size
        for i in range(1, size + 1, 1):
            self.prev[i % size] = (i - 1) % size
            self.next[i % size] = (i + 1) % size

    def print(self, zero: int) -> list[int]:
        tmp = zero
        s = []
        while True:
            s.append(self.nums[tmp])
            tmp = self.next[tmp]
            if self.nums[tmp] == 0:
                break
        return s

    def find_zero(self) -> int:
        return next(i for i, num in enumerate(self.nums) if num == 0)

    def round(self) -> None:
        size = len(self.nums)
        for i, to_move in enumerate(self.nums):
            clog(lambda: f"to_move: {to_move}")
            if to_move == 0:
                continue
            self.prev[self.next[i]] = self.prev[i]
            self.next[self.prev[i]] = self.next[i]
            if to_move > 0:
                move_to = self.prev[i]
                amount = to_move % (size - 1)
                for _ in range(amount):
                    move_to = self.next[move_to]
            else:
                move_to = self.next[i]
                amount = abs(to_move) % (size - 1) + 1
                for _ in range(amount):
                    move_to = self.prev[move_to]
            clog(lambda: f"move to: {self.nums[move_to]} ({amount})")
            before, after = move_to, self.next[move_to]
            self.next[before] = i
            self.prev[i] = before
            self.prev[after] = i
            self.next[i] = after


def _solve(inputs: tuple[str], rounds: int, factor: int = 1) -> int:
    nums = Nums(inputs, factor)
    zero = nums.find_zero()
    for round in range(rounds):
        log(f"Round {round}")
        nums.round()
        clog(lambda: f"{nums.print(zero)}")
    ans = 0
    n = zero
    for i in range(1, 3001):
        n = nums.next[n]
        if i % 1000 == 0:
            ans += nums.nums[n]
    return ans


def part_1(inputs: tuple[str]) -> int:
    return _solve(inputs, rounds=1)


def part_2(inputs: tuple[str]) -> int:
    return _solve(inputs, rounds=10, factor=811_589_153)


TEST1 = tuple(
    """\
1
2
-3
3
-2
0
4
""".splitlines()
)
TEST2 = tuple(
    """\
3
1
0
""".splitlines()
)


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 20)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 3
    assert part_1(TEST2) == 4
    assert part_2(TEST1) == 1_623_178_306

    inputs = my_aocd.get_input_data(puzzle, 5000)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

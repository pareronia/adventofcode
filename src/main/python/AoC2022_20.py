#! /usr/bin/env python3
#
# Advent of Code 2022 Day 20
#


from __future__ import annotations

import aocd
from aoc import my_aocd
from aoc.common import clog


class Num:
    def __init__(self, num: int) -> None:
        self.num: int = num
        self.next: Num | None = None
        self.prev: Num | None = None


def _print(zero: Num) -> None:
    tmp = zero
    s = []
    while True:
        s.append(tmp.num)
        tmp = tmp.next
        if tmp.num == 0:
            break
    return s


def _do_mix(nums: list[Num], zero: Num) -> None:
    def _do_move(to_move: Num, move_to: Num) -> None:
        before, after = move_to, move_to.next
        before.next = to_move
        to_move.prev = before
        after.prev = to_move
        to_move.next = after

    size = len(nums)
    for to_move in nums:
        clog(lambda: f"to_move: {to_move.num}")
        if to_move.num > 0:
            move_to = to_move.prev
            amount = to_move.num % (size - 1)
            to_move.next.prev = to_move.prev
            to_move.prev.next = to_move.next
            for j in range(amount):
                move_to = move_to.next
            clog(lambda: f"move to: {move_to.num}")
            _do_move(to_move, move_to)
        elif to_move.num < 0:
            move_to = to_move.next
            amount = abs(to_move.num) % (size - 1) + 1
            to_move.next.prev = to_move.prev
            to_move.prev.next = to_move.next
            for j in range(amount):
                move_to = move_to.prev
            clog(lambda: f"move to: {move_to.num}")
            _do_move(to_move, move_to)
        clog(lambda: _print(zero))


def _solve(inputs: tuple[str], rounds: int, factor: int = 1) -> int:
    nums = list(map(lambda line: Num(int(line) * factor), inputs))
    size = len(nums)
    for i in range(1, size + 1):
        if nums[i - 1].num == 0:
            zero = nums[i - 1]
        nums[i % size].prev = nums[(i - 1) % size]
        nums[i % size].next = nums[(i + 1) % size]
    for i in range(rounds):
        _do_mix(nums, zero)
    ans = 0
    n = zero
    for i in range(1, 3001):
        n = n.next
        if i % 1000 == 0:
            ans += n.num
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
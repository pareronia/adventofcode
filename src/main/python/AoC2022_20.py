#! /usr/bin/env python3
#
# Advent of Code 2022 Day 20
#


from __future__ import annotations


import aocd
from aoc import my_aocd

from aoc.common import clog
from aoc.common import log


class Num:
    def __init__(self, num: int) -> None:
        self.num: int = num
        self.next: Num | None = None
        self.prev: Num | None = None


def _parse(inputs: tuple[str]) -> tuple[dict[int, Num], Num]:
    pos = dict[int, Num]()
    for i, num in enumerate(list(map(int, inputs))):
        n = Num(num)
        if num == 0:
            zero = n
        pos[i] = n
        if i == 0:
            head = n
        else:
            n.prev = pos[i - 1]
            n.prev.next = n
            tail = n
    head.prev = tail
    tail.next = head
    return pos, zero


def _print(zero) -> None:
    tmp = zero
    s = []
    while True:
        s.append(tmp.num)
        tmp = tmp.next
        if tmp.num == 0:
            break
    return s


def _do_mix(pos, zero) -> None:
    def _do_move(to_move, move_to) -> None:
        before, after = move_to, move_to.next
        before.next = to_move
        to_move.prev = before
        after.prev = to_move
        to_move.next = after

    size = len(pos)
    for i in range(size):
        to_move = pos[i]
        clog(lambda: f"to_move: {to_move.num}")
        amount = to_move.num
        if amount == 0:
            log("Nothing to do")
            continue
        move_to = to_move
        to_move.next.prev = to_move.prev
        to_move.prev.next = to_move.next
        if amount > 0:
            for j in range(amount % (size - 1)):
                move_to = move_to.next
            clog(lambda: f"move to: {move_to.num}")
            _do_move(to_move, move_to)
        else:
            for j in range(abs(amount) % (size - 1) + 1):
                move_to = move_to.prev
            clog(lambda: f"move to: {move_to.num}")
            _do_move(to_move, move_to)
        clog(lambda: _print(zero))


def _solve(pos: dict[int, Num], zero: Num, rounds: int) -> int:
    for i in range(rounds):
        _do_mix(pos, zero)
    ans = 0
    n = zero
    for i in range(1, 3001):
        n = n.next
        if i % 1000 == 0:
            ans += n.num
    return ans


def part_1(inputs: tuple[str]) -> int:
    pos, zero = _parse(inputs)
    return _solve(pos, zero, 1)


def part_2(inputs: tuple[str]) -> int:
    pos, zero = _parse(inputs)
    for i in pos:
        pos[i].num *= 811_589_153
    return _solve(pos, zero, 10)


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

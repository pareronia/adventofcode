#! /usr/bin/env python3
#
# Advent of Code 2020 Day 23
#

from collections import deque
import my_aocd
from common import log


def _parse(inputs: tuple[str]) -> list[int]:
    assert len(inputs) == 1
    return [int(c) for c in inputs[0]]


def _log(cups: list[int], msg):
    if len(cups) <= 10:
        log(msg)


def _do_move(move: int, cups: deque[int], current: int) -> list[int]:
    _log(cups, f"-- move {move+1} --")
    _log(cups, f"cups: {cups}")
    c = cups[current]
    _log(cups, f"current: {c}")
    p1 = cups[(current+1) % len(cups)]
    p2 = cups[(current+2) % len(cups)]
    p3 = cups[(current+3) % len(cups)]
    cups.remove(p1)
    cups.remove(p2)
    cups.remove(p3)
    pick_up = [p1, p2, p3]
    _log(cups, f"pick up: {pick_up}")
    d = c-1
    if d < min(cups):
        d = max(cups)
    while d in pick_up:
        d -= 1
        if d < min(cups):
            d = max(cups)
    _log(cups, f"destination: {d}")
    d_i = cups.index(d)
    for i, p in enumerate(pick_up):
        cups.insert(d_i+i+1, p)
    current = cups.index(c)
    current = (current+1) % len(cups)
    _log(cups, "")
    return cups, current


def part_1(inputs: tuple[str]) -> int:
    cups = deque(_parse(inputs))
    current = 0
    for move in range(100):
        cups, current = _do_move(move, cups, current)
    log("-- final --")
    log(f"cups: {cups}")
    one = cups.index(1)
    result = 0
    for i in range(len(cups)):
        cup = cups[(one+1+i) % len(cups)]
        if cup != 1:
            result += cup*10**(len(cups)-2-i)
    log(result)
    return result


def part_2(inputs: tuple[str]) -> int:
    cups = _parse(inputs)
    cups.extend([i for i in range(max(cups)+1, 1_000_001)])
    cups = deque(cups)
    current = 0
    for move in range(10_000_000):
        cups, current = _do_move(move, cups, current)
        print('.', end='', flush=True)
    one = cups.index(1)
    star1 = cups[one+1]
    star2 = cups[one+2]
    return star1 * star2


test = """\
389125467
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 23)

    assert part_1(test) == 67384529
    assert part_2(test) == 149245887792

    result1 = part_1(["974618352"])
    print(f"Part 1: {result1}")
    # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

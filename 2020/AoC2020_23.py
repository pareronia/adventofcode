#! /usr/bin/env python3
#
# Advent of Code 2020 Day 23
#

from __future__ import annotations
from dataclasses import dataclass
import my_aocd
from common import log


@dataclass
class Cup:
    label: int
    prev: Cup
    next_: Cup

    def move_after(self, cup: Cup):
        self.next_.prev = self.prev
        self.prev.next_ = self.next_
        cup.next_.prev = self
        self.next_ = cup.next_
        self.prev = cup
        cup.next_ = self


def _parse(inputs: tuple[str]) -> list[int]:
    assert len(inputs) == 1
    return [int(c) for c in inputs[0]]


def _log(size: int, msg):
    if size <= 10:
        log(msg)


def _print_cups(move: int, cups: dict[int, Cup], current: int) -> str:
    if len(cups) > 10:
        return
    c = cups[current]
    start = c
    for i in range(move):
        start = start.prev
    p = start
    result = ""
    while True:
        result += f"{p.label} "
        p = p.next_
        if p == start:
            break
    result = result.replace(str(c.label), "(" + str(c.label) + ")")
    return result


def _prepare_cups(labels: list[int]) -> (dict[int, Cup], int, int, int):
    size = len(labels)
    min_val = min(labels)
    max_val = max(labels)
    cups = dict[int, Cup]()
    for label in labels:
        cups[label] = Cup(label, None, None)
    for i, label in enumerate(labels):
        prev = labels[(i-1) % len(labels)]
        next_ = labels[(i+1) % len(labels)]
        cups[label].prev = cups[prev] if prev in cups else None
        cups[label].next_ = cups[next_] if next_ in cups else None
    cups[labels[0]].prev = cups[labels[-1]]
    cups[labels[-1]].next_ = cups[labels[0]]
    return cups, size, min_val, max_val


def _do_move(move: int, cups: dict[int, Cup],
             current: int, size: int, min_val: int,
             max_val: int) -> (dict[int, Cup], int):
    _log(size, f"-- move {move+1} --")
    _log(size, f"cups: {_print_cups(move, cups, current)}")
    c = cups[current]
    p1 = c.next_
    p2 = p1.next_
    p3 = p2.next_
    pickup = (p1.label, p2.label, p3.label)
    _log(size, f"pick up: {p1.label}, {p2.label}, {p3.label}")
    d = c.label-1
    if d < min_val:
        d = max_val
    while d in pickup:
        d -= 1
        if d < min_val:
            d = max_val
    _log(size, f"destination: {d}")
    p1.move_after(cups[d])
    p2.move_after(p1)
    p3.move_after(p2)
    current = c.next_.label
    _log(size, "")
    return cups, current


def part_1(inputs: tuple[str]) -> int:
    cups = _parse(inputs)
    cd, size, min_val, max_val = _prepare_cups(cups)
    current = cups[0]
    for move in range(100):
        cd, current = _do_move(move, cd, current, size, min_val, max_val)
    cup = cd[1]
    result = ""
    while cup.next_ != cd[1]:
        result += str(cup.next_.label)
        cup = cup.next_
    log(result)
    return int(result)


def part_2(inputs: tuple[str]) -> int:
    cups = _parse(inputs)
    cups.extend([i for i in range(max(cups)+1, 1_000_001)])
    cd, size, min_val, max_val = _prepare_cups(cups)
    current = cups[0]
    for move in range(10_000_000):
        cd, current = _do_move(move, cd, current, size, min_val, max_val)
        if move % 100_000 == 0:
            print('.', end='', flush=True)
    print("")
    one = cd[1]
    star1 = one.next_.label
    star2 = one.next_.next_.label
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
    result2 = part_2(["974618352"])
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

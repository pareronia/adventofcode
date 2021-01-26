#! /usr/bin/env python3
#
# Advent of Code 2020 Day 23
#

from __future__ import annotations
from dataclasses import dataclass
from aoc import my_aocd
from aoc.common import log


@dataclass
class Cup:
    label: int
    next_: Cup


def _parse(inputs: tuple[str]) -> list[int]:
    assert len(inputs) == 1
    return [int(c) for c in inputs[0]]


def _log(size: int, msg):
    if size <= 10:
        log(msg)


def _print_cups(move: int, cups: dict[int, Cup], current: Cup) -> str:
    if len(cups) > 10:
        return
    p = current
    result = ""
    while True:
        result += f"{p.label} "
        p = p.next_
        if p == current:
            break
    result = result.replace(str(current.label), "(" + str(current.label) + ")")
    return result


def _prepare_cups(labels: list[int]) -> (dict[int, Cup], int, int, int):
    size = len(labels)
    min_val = min(labels)
    max_val = max(labels)
    cups = dict[int, Cup]()
    for label in labels:
        cups[label] = Cup(label, None)
    for i, label in enumerate(labels):
        next_ = labels[(i+1) % len(labels)]
        cups[label].next_ = cups[next_] if next_ in cups else None
    cups[labels[-1]].next_ = cups[labels[0]]
    return cups, size, min_val, max_val


def _do_move(move: int, cups: dict[int, Cup],
             current: Cup, size: int, min_val: int,
             max_val: int) -> (dict[int, Cup], int):
    # _log(size, f"-- move {move+1} --")
    # _log(size, f"cups: {_print_cups(move, cups, current)}")
    c = current
    p1 = c.next_
    p2 = p1.next_
    p3 = p2.next_
    c.next_ = p3.next_
    pickup = (p1.label, p2.label, p3.label)
    # _log(size, f"pick up: {p1.label}, {p2.label}, {p3.label}")
    d = c.label-1
    if d < min_val:
        d = max_val
    while d in pickup:
        d -= 1
        if d < min_val:
            d = max_val
    # _log(size, f"destination: {d}")
    destination = cups[d]
    p3.next_ = destination.next_
    destination.next_ = p1
    current = c.next_
    # _log(size, "")
    return cups, current


def part_1(inputs: tuple[str]) -> int:
    cups = _parse(inputs)
    cd, size, min_val, max_val = _prepare_cups(cups)
    current = cd[cups[0]]
    for move in range(100):
        cd, current = _do_move(move, cd, current, size, min_val, max_val)
    cup = cd[1]
    result = ""
    while cup.next_.label != 1:
        result += str(cup.next_.label)
        cup = cup.next_
    log(result)
    return int(result)


def part_2(inputs: tuple[str]) -> int:
    cups = _parse(inputs)
    cups.extend([i for i in range(max(cups)+1, 1_000_001)])
    cd, size, min_val, max_val = _prepare_cups(cups)
    current = cd[cups[0]]
    for move in range(10_000_000):
        cd, current = _do_move(move, cd, current, size, min_val, max_val)
        if move % 100_000 == 0:
            print('.', end='', flush=True)
    print("")
    one = cd[1]
    star1 = one.next_.label
    star2 = one.next_.next_.label
    return star1 * star2


TEST = """\
389125467
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 23)

    assert part_1(TEST) == 67384529
    assert part_2(TEST) == 149245887792

    inputs = my_aocd.get_input(2020, 23, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

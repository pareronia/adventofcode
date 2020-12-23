#! /usr/bin/env python3
#
# Advent of Code 2020 Day 23
#

from copy import deepcopy
import my_aocd
from common import log


def _parse(inputs: tuple[str]) -> list[int]:
    assert len(inputs) == 1
    return [int(c) for c in inputs[0]]


def _log(cups: list[int], msg):
    if len(cups) <= 10:
        log(msg)


def _do_move(move: int, cups: list[int], current: int) -> list[int]:
    _log(cups, f"-- move {move+1} --")
    _log(cups, f"cups: {cups}")
    pickup_cups = deepcopy(cups)
    pickup_cups.extend(pickup_cups)
    pick_up = pickup_cups[current+1:current+4]
    _log(cups, f"pick up: {pick_up}")
    cups_ordered = deepcopy(cups)
    cups_ordered.sort(key=lambda x: x, reverse=True)
    d = (cups_ordered.index(cups[current]) + 1) % len(cups_ordered)
    while cups_ordered[d] in pick_up:
        d = (d + 1) % len(cups_ordered)
    destination = cups_ordered[d]
    _log(cups, f"destination: {destination}")
    result = []
    for k in range(len(cups)):
        result.append(None)
    result[current] = cups[current]
    j = (current + 1) % len(cups)
    for i in range(current+1, current+1+len(cups)):
        i_ = i % len(cups)
        if cups[i_] == destination:
            result[j] = cups[i_]
            j = (j + 1) % len(cups)
            for p in pick_up:
                result[j] = p
                j = (j + 1) % len(cups)
        elif cups[i_] not in pick_up:
            result[j] = cups[i_]
            j = (j + 1) % len(cups)
    _log(cups, "")
    return result


def part_1(inputs: tuple[str]) -> int:
    cups = _parse(inputs)
    for move in range(100):
        cups = _do_move(move, cups, move % len(cups))
    one = cups.index(1)
    final = cups[one+1:] + cups[:one]
    result = sum([c*10**(len(final)-1-i)
                  for i, c in enumerate(final)])
    log(result)
    return result


def part_2(inputs: tuple[str]) -> int:
    cups = _parse(inputs)
    cups.extend([i for i in range(max(cups)+1, 1_000_001)])
    for move in range(10_000_000):
        cups = _do_move(move, cups, move % len(cups))
        print('.', end='', flush=True)
    return 0


test = """\
389125467
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 23)

    assert part_1(test) == 67384529
    assert part_2(test) == 0

    result1 = part_1(["974618352"])
    print(f"Part 1: {result1}")
    # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

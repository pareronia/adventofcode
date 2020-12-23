#! /usr/bin/env python3
#
# Advent of Code 2020 Day 23
#

import my_aocd
from common import log


def _parse(inputs: tuple[str]) -> list[int]:
    assert len(inputs) == 1
    return [int(c) for c in inputs[0]]


def _log(cups: dict, msg):
    if len(cups) <= 10:
        log(msg)


def _print_cups(val: dict[int], current: int) -> str:
    if len(val) > 10:
        return
    cups = [str(val[i]) for i in sorted(val.keys(), key=lambda k: k)]
    result = " ".join(cups)
    result = result.replace(str(val[current]), "(" + str(val[current]) + ")")
    return result


def _do_move(move: int, val: dict[int], idx: dict[int],
             current: int) -> (dict[int], dict[int]):
    min_val = min(val.values())
    max_val = max(val.values())
    size = len(val)
    _log(val, f"-- move {move+1} --")
    _log(val, f"cups: {_print_cups(val, current)}")
    _log(val, idx)
    _log(val, val)
    cv = val[current]
    p1v = val[(current+1) % size]
    p2v = val[(current+2) % size]
    p3v = val[(current+3) % size]
    pick_up = [p1v, p2v, p3v]
    _log(val, f"pick up: {pick_up}")
    dv = cv-1
    if dv < min_val:
        dv = max_val
    while dv in pick_up:
        dv -= 1
        if dv < min_val:
            dv = max_val
    _log(val, f"destination: {dv}")
    for pv in reversed(pick_up):
        di = idx[dv]
        pvi = idx[pv]
        if di > pvi:
            for i in range(pvi+1, di+1):
                i1 = i % size
                v1 = val[i1]
                i2 = (i-1) % size
                val[i2], idx[v1] = v1, i2
            val[di], idx[pv] = pv, di
        else:
            for i in range(pvi-1, di, -1):
                i1 = i % size
                v1 = val[i1]
                i2 = (i+1) % size
                val[i2], idx[v1] = v1, i2
            val[(di+1) % size], idx[pv] = pv, (di+1) % size
    current = (idx[cv]+1) % size
    _log(val, "")
    return val, idx, current


def _prepare_bidi(cups: list[int]) -> (dict[int], dict[int]):
    val = dict[int]()
    idx = dict[int]()
    for i, cup in enumerate(cups):
        val[i] = cup
        idx[cup] = i
    return val, idx


def part_1(inputs: tuple[str]) -> int:
    cups = _parse(inputs)
    val, idx = _prepare_bidi(cups)
    current = 0
    for move in range(100):
        val, idx, current = _do_move(move, val, idx, current)
    log("-- final --")
    _log(val, f"cups: {_print_cups(val, current)}")
    one = idx[1]
    result = sum([val[(one+1+i) % len(val)] * 10**(len(val)-2-i)
                  for i in range(len(val)-1)])
    log(result)
    return result


def part_2(inputs: tuple[str]) -> int:
    cups = _parse(inputs)
    cups.extend([i for i in range(max(cups)+1, 1_000_001)])
    val, idx = _prepare_bidi(cups)
    current = 0
    for move in range(10_000_000):
        val, idx, current = _do_move(move, val, idx, current)
        print('.', end='', flush=True)
    one = idx[1]
    star1 = val[one+1]
    star2 = val[one+2]
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

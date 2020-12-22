#! /usr/bin/env python3
#
# Advent of Code 2020 Day 22
#

import my_aocd
from common import log


def _parse(inputs: tuple[str]) -> list[list[int]]:
    blocks = my_aocd.to_blocks(inputs)
    pl1 = []
    for i, line in enumerate(blocks[0]):
        if i == 0:
            continue
        pl1.append(int(line))
    pl2 = []
    for i, line in enumerate(blocks[1]):
        if i == 0:
            continue
        pl2.append(int(line))
    return pl1, pl2


def part_1(inputs: tuple[str]) -> int:
    pl1, pl2 = _parse(inputs)
    log(pl1)
    log(pl2)
    while len(pl1) != 0 and len(pl2) != 0:
        n1 = pl1.pop(0)
        n2 = pl2.pop(0)
        if n1 > n2:
            log("Player 1 wins")
            pl1.append(n1)
            pl1.append(n2)
        else:
            log("Player 2 wins")
            pl2.append(n2)
            pl2.append(n1)
        log(pl1)
        log(pl2)
    winner = pl1 if len(pl2) == 0 else pl2
    total = 0
    for i, c in enumerate(winner):
        total += (len(winner)-i) * c
    return total


def part_2(inputs: tuple[str]) -> int:
    return 0


test = """\
Player 1:
9
2
6
3
1

Player 2:
5
8
4
7
10
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 21)

    assert part_1(test) == 306
    assert part_2(test) == 0

    inputs = my_aocd.get_input_as_tuple(2020, 22, 53)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

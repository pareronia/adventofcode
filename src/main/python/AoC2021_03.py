#! /usr/bin/env python3
#
# Advent of Code 2021 Day 3
#

from aoc import my_aocd
from aoc.common import log


def _to_dec(s: str) -> int:
    return int(s, 2)


def _mcb(inputs: list[str], pos: int) -> tuple[int, int]:
    cnt0 = 0
    cnt1 = 0
    for b in inputs:
        if b[pos] == '0':
            cnt0 += 1
        else:
            cnt1 += 1
    return cnt0, cnt1


def part_1(inputs: tuple[str]) -> int:
    gamma = ""
    epsilon = ""
    for i in range(len(inputs[0])):
        cnt0, cnt1 = _mcb(inputs, i)
        if cnt0 > cnt1:
            gamma += '0'
            epsilon += '1'
        else:
            gamma += '1'
            epsilon += '0'
    return _to_dec(gamma) * _to_dec(epsilon)


def _filter(inputs: list[str], bit: str, pos: int) -> list[str]:
    return [s for s in inputs if s[pos] == bit]


def part_2(inputs: tuple[str]) -> int:
    sso2 = list(inputs)
    io2 = 0
    while len(sso2) > 1:
        cnt0, cnt1 = _mcb(sso2, io2)
        if cnt0 > cnt1:
            sso2 = _filter(sso2, '0', io2)
        else:
            sso2 = _filter(sso2, '1', io2)
        io2 += 1
    oxygen = _to_dec(sso2[0])
    log(oxygen)
    ssco2 = list(inputs)
    ico2 = 0
    while len(ssco2) > 1:
        cnt0, cnt1 = _mcb(ssco2, ico2)
        if cnt0 > cnt1:
            ssco2 = _filter(ssco2, '1', ico2)
        else:
            ssco2 = _filter(ssco2, '0', ico2)
        ico2 += 1
    co2 = _to_dec(ssco2[0])
    log(co2)
    return oxygen * co2


TEST = """\
00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010
""".splitlines()


def main() -> None:
    my_aocd.print_header(2021, 3)

    assert part_1(TEST) == 198
    assert part_2(TEST) == 230

    inputs = my_aocd.get_input(2021, 3, 1000)
    print(f"Part 1: {part_1(inputs)}")
    print(f"Part 2: {part_2(inputs)}")


if __name__ == '__main__':
    main()

#! /usr/bin/env python3
#
# Advent of Code 2020 Day 13
#

import my_aocd
from common import log


def parse(inputs: tuple[str]):
    buses = []
    for b in inputs[1].split(","):
        if b == "x":
            buses.append(-1)
        else:
            buses.append(int(b))
    return int(inputs[0]), buses


def part_1(inputs: tuple[int]) -> int:
    target, buses = parse(inputs)
    cnt = 0
    while True:
        t = target + cnt
        for b in buses:
            if b == -1:
                continue
            if t % b == 0:
                return b * cnt
        cnt += 1


def _find_offsets(buses: list[int]) -> list[tuple[int, int]]:
    i = []
    for j in range(len(buses)):
        b = buses[j]
        if b != -1:
            i.append((b, j))
    return i


def _find_largest_and_recalc_offsets(i: list[tuple[int, int]]):
    m = (0, 0)
    for j in i:
        if j[0] > m[0]:
            m = j
    new_i = []
    for j in i:
        if j != m:
            new_i.append((j[0], j[1]-m[1]))
    return m, new_i


def _init_multiples(buses: list[tuple[int, int]], start: int) -> dict:
    multiples = {}
    for b in buses:
        multiples[b[0]] = start * b[0]
    return multiples


def _lookup_multiple(multiples: dict, bus: int, wanted: int):
    # return wanted % bus == 0
    last = multiples[bus]
    while True:
        next_ = last + bus
        if next_ > wanted:
            return False
        else:
            multiples[bus] = next_
            last = next_
            if next_ == wanted:
                return True


def part_2(inputs: tuple[int], start: int) -> int:
    target, buses = parse(inputs)
    i = _find_offsets(buses)
    log(i)
    m, i = _find_largest_and_recalc_offsets(i)
    log(m)
    log(i)
    a = start // m[0]
    multiples = _init_multiples(i, start=a)
    a_m = a * m[0]
    cnt = 0
    while True:
        cnt += 1
        ok = True
        for j in range(len(i)):
            found = _lookup_multiple(multiples, bus=i[j][0],
                                     wanted=a_m + i[j][1])
            if not found:
                ok = False
                break
        if ok:
            log(cnt)
            return a_m if m[1] == 0 else a_m + i[0][1]
        a_m += m[0]
    return 0


test_1 = ("939",
          "7,13,x,x,59,x,31,19"
          )
test_2 = ("0",
          "17,x,13,19"
          )
test_3 = ("0",
          "67,7,59,61"
          )
test_4 = ("0",
          "67,x,7,59,61"
          )
test_5 = ("0",
          "67,7,x,59,61"
          )
test_6 = ("0",
          "1789,37,47,1889"
          )


def main() -> None:
    my_aocd.print_header(2020, 13)

    assert part_1(test_1) == 295
    assert part_2(("0", "3,5,7"), 7) == 54
    assert part_2(test_1, 1000000) == 1068781
    assert part_2(test_2, 3000) == 3417
    assert part_2(test_3, 75000) == 754018
    assert part_2(test_4, 75000) == 779210
    assert part_2(test_5, 1000000) == 1261476
    assert part_2(test_6, 1000000000) == 1202161486

    inputs = my_aocd.get_input_as_tuple(2020, 13, 2)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs, 100000000000000)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

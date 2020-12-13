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


def part_2(inputs: tuple[int], start: int) -> int:
    target, buses = parse(inputs)
    i = []
    for j in range(len(buses)):
        b = buses[j]
        if b != -1:
            i.append((b, j))
    log(i)
    m = (0, 0)
    for j in i:
        if j[0] > m[0]:
            m = j
    log(m)
    new_i = []
    for j in i:
        if j != m:
            new_i.append((j[0], j[1]-m[1]))
    i = new_i
    log(i)
    a = start // m[0]
    while True:
        a_m = a * m[0]
        ok = True
        for j in range(len(i)):
            if (a_m + i[j][1]) % i[j][0] != 0:
                ok = False
                break
        if ok:
            return a_m if m[1] == 0 else a_m + i[0][1]
        a += 1
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

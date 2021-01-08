#! /usr/bin/env python3
#
# Advent of Code 2020 Day 13
#
# FIXME: Make part2 bis into algorithm

from aoc import my_aocd
from aoc.common import log


def parse(inputs: tuple[str]):
    assert len(inputs) == 2
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
            new_i.sort(key=lambda t: t[0], reverse=True)
    return m, new_i


def _init_multiples(buses: list[tuple[int, int]], start: int) -> dict:
    multiples = {}
    for b in buses:
        multiples[b[0]] = start // b[0] * b[0]
    log(f"init multiples: {multiples}")
    return multiples


def _lookup_multiple(multiples: dict, bus: int, wanted: int):
    # log(f"{wanted} for bus {bus}")
    # return wanted % bus == 0
    last = multiples[bus]
    while True:
        next_ = last + bus
        # if (not next_ % (10000*bus)):
        #     print((last, next_, wanted))
        if next_ > wanted:
            return False
        else:
            multiples[bus] = next_
            last = next_
            if next_ == wanted:
                # log(f"found; {next_}={wanted} for bus {bus}")
                return True


def part_2_bis(inputs: tuple[int], start: int) -> int:
    target, buses = parse(inputs)
    i = _find_offsets(buses)
    log(i)
    m, i = _find_largest_and_recalc_offsets(i)
    log(m)
    log(i)
    a = start // m[0]
    a_m = a * m[0]
    log(f"start: {start}")
    multiples = _init_multiples(i, start=a_m)
    cnt = 0
    while True:
        cnt += 1
        ok = True
        for j in range(len(i)):
            bus = i[j][0]
            wanted = a_m + i[j][1]
            # if not cnt % 10000:
            #     log((cnt, bus, wanted))
            found = _lookup_multiple(multiples, bus, wanted)
            if not found:
                ok = False
                break
        if ok:
            i.sort(key=lambda t: t[1])
            result = a_m if m[1] == 0 else a_m + i[0][1]
            log(f"Result: {(cnt, result)}")
            return result
        a_m += m[0]
    return 0


def part_2(inputs: tuple[int]) -> int:
    cnt = 1
    r = 0
    while True:
        r = cnt * 23
        if (r + 13) % 41 == 0:
            break
        cnt += 1
    lcm1 = 23 * 41
    while True:
        r = r + lcm1
        if (r + 23) % 509 == 0:
            break
    lcm2 = 23 * 41 * 509
    while True:
        r = r + lcm2
        if (r + 36) % 13 == 0:
            break
    lcm3 = 23 * 41 * 509 * 13
    while True:
        r = r + lcm3
        if (r + 37) % 17 == 0:
            break
    lcm4 = 23 * 41 * 509 * 13 * 17
    while True:
        r = r + lcm4
        if (r + 52) % 29 == 0:
            break
    lcm5 = 23 * 41 * 509 * 13 * 17 * 29
    while True:
        r = r + lcm5
        if (r + 54) % 401 == 0:
            break
    lcm6 = 23 * 41 * 509 * 13 * 17 * 29 * 401
    while True:
        r = r + lcm6
        if (r + 60) % 37 == 0:
            break
    lcm7 = 23 * 41 * 509 * 13 * 17 * 29 * 401 * 37
    while True:
        r = r + lcm7
        if (r + 73) % 19 == 0:
            break
    return r


TEST1 = """\
939
7,13,x,x,59,x,31,19
""".splitlines()
TEST2 = """\
0
17,x,13,19
""".splitlines()
TEST3 = """\
0
67,7,59,61
""".splitlines()
TEST4 = """\
0
67,x,7,59,61
""".splitlines()
TEST5 = """\
0
67,7,x,59,61
""".splitlines()
TEST6 = """\
0
1789,37,47,1889
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 13)

    assert part_1(TEST1) == 295
    assert part_2_bis(TEST1, 1000000) == 1068781
    assert part_2_bis(TEST2, 3000) == 3417
    assert part_2_bis(TEST3, 75000) == 754018
    assert part_2_bis(TEST4, 75000) == 779210
    assert part_2_bis(TEST5, 1000000) == 1261476
    assert part_2_bis(TEST6, 1000000000) == 1202161486

    inputs = my_aocd.get_input(2020, 13, 2)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    # result2 = part_2_bis(inputs, 100000000000000)
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

#! /usr/bin/env python3
#
# Advent of Code 2020 Day 13
#

from aoc import my_aocd


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


def part_2(inputs: tuple[int]) -> int:
    _, buses = parse(inputs)
    bs = {buses.index(b): b for b in buses if b != -1}
    idxs = [i for i in bs]
    r = 0
    lcm = 1
    for i in range(len(idxs) - 1):
        cur = idxs[i]
        nxt = idxs[i+1]
        lcm *= bs[cur]
        offset = nxt
        while True:
            r += lcm
            if (r + offset) % bs[nxt] == 0:
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
    assert part_2(TEST1) == 1068781
    assert part_2(TEST2) == 3417
    assert part_2(TEST3) == 754018
    assert part_2(TEST4) == 779210
    assert part_2(TEST5) == 1261476
    assert part_2(TEST6) == 1202161486

    inputs = my_aocd.get_input(2020, 13, 2)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

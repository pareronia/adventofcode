#! /usr/bin/env python3
#
# Advent of Code 2020 Day 10
#
from collections import defaultdict, Counter
from aoc import my_aocd
from aoc.common import log


def _parse(inputs: tuple[str]) -> tuple[int]:
    sorted_ = [int(_) for _ in inputs]
    sorted_.append(0)
    sorted_.append(max(sorted_) + 3)
    sorted_.sort()
    return tuple(sorted_)


def part_1(inputs: tuple[str]) -> int:
    inputs = _parse(inputs)
    log(inputs)
    cnt = Counter((inputs[i] - inputs[i-1]
                   for i in range(1, len(inputs))))
    return cnt[1] * cnt[3]


def part_2(inputs: tuple[str]) -> int:
    inputs = _parse(inputs)
    log(inputs)
    seen = defaultdict(lambda: 0)
    seen[0] = 1
    for i in inputs[1:]:
        for j in (inputs[k]
                  for k in range(inputs.index(i) - 1, -1, -1)
                  if i - inputs[k] <= 3):
            seen[i] += seen[j]
        log(seen)
    log(seen[inputs[-1]])
    return seen[inputs[-1]]


TEST1 = """\
16
10
15
5
1
11
7
19
6
12
4
""".splitlines()
TEST2 = """\
28
33
18
42
31
14
46
20
48
47
24
23
49
45
19
38
39
11
1
32
25
35
8
17
7
9
4
2
34
10
3
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 10)

    assert part_1(TEST1) == 35
    assert part_1(TEST2) == 220
    assert part_2(TEST1) == 8
    assert part_2(TEST2) == 19208

    inputs = my_aocd.get_input(2020, 10, 101)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

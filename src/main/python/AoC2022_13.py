#! /usr/bin/env python3
#
# Advent of Code 2022 Day 13
#

from functools import cmp_to_key
from math import prod

from aoc import my_aocd
from aoc.common import aoc_main
from aoc.common import log


def _compare(r1: int | list[int], r2: int | list[int]) -> int:
    log(f">Compare {r1} vs {r2}")
    if isinstance(r1, int) and isinstance(r2, int):
        return -1 if r1 < r2 else 1 if r1 > r2 else 0
    elif isinstance(r1, list) and isinstance(r2, int):
        log("Mixed types; convert right to list and retry comparison")
        return _compare(r1, [r2])
    elif isinstance(r1, int) and isinstance(r2, list):
        log("Mixed types; convert left to list and retry comparison")
        return _compare([r1], r2)
    elif isinstance(r1, list) and isinstance(r2, list):
        log(f"Compare lists {r1} vs {r2}")
        size1 = len(r1)
        size2 = len(r2)
        for i in range(size1):
            n1 = r1[i]
            try:
                n2 = r2[i]
            except IndexError:
                log("Right side ran out of items")
                return 1
            res = _compare(n1, n2)
            if res == 0:
                continue
            return res
        log("Left side ran out of items")
        return -1 if size1 < size2 else 0
    raise ValueError


def part_1(inputs: tuple[str, ...]) -> int:
    return sum(
        i + 1
        for i, block in enumerate(my_aocd.to_blocks(inputs))
        if _compare(eval(block[0]), eval(block[1])) < 1  # nosec
    )


def part_2(inputs: tuple[str, ...]) -> int:
    dividers = [[[2]], [[6]]]
    packets = [eval(line) for line in inputs if len(line) > 0]  # nosec
    packets.extend(dividers)
    packets.sort(key=cmp_to_key(_compare))
    return prod(
        i + 1 for i, packet in enumerate(packets) if packet in dividers
    )


TEST = tuple(
    """\
[1,1,3,1,1]
[1,1,5,1,1]

[[1],[2,3,4]]
[[1],4]

[9]
[[8,7,6]]

[[4,4],4,4]
[[4,4],4,4,4]

[7,7,7,7]
[7,7,7]

[]
[3]

[[[]]]
[[]]

[1,[2,[3,[4,[5,6,7]]]],8,9]
[1,[2,[3,[4,[5,6,0]]]],8,9]
""".splitlines()
)


@aoc_main(2022, 13, part_1, part_2)
def main() -> None:
    assert part_1(TEST) == 13
    assert part_2(TEST) == 140


if __name__ == "__main__":
    main()

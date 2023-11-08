#! /usr/bin/env python3
#
# Advent of Code 2022 Day 1
#

from aoc import my_aocd
from aoc.common import aoc_main


def _solve(inputs: tuple[str, ...], count: int) -> int:
    blocks = my_aocd.to_blocks(inputs)
    sums = [sum(int(line) for line in block) for block in blocks]
    return sum(_ for _ in sorted(sums)[-count:])


def part_1(inputs: tuple[str, ...]) -> int:
    return _solve(inputs, 1)


def part_2(inputs: tuple[str, ...]) -> int:
    return _solve(inputs, 3)


TEST = """\
1000
2000
3000

4000

5000
6000

7000
8000
9000

10000
""".splitlines()


@aoc_main(2022, 1, part_1, part_2)
def main() -> None:
    assert part_1(TEST) == 24_000  # type:ignore[arg-type]
    assert part_2(TEST) == 45_000  # type:ignore[arg-type]


if __name__ == "__main__":
    main()

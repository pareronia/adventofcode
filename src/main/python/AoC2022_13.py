#! /usr/bin/env python3
#
# Advent of Code 2022 Day 13
#

import sys
from functools import cmp_to_key
from math import prod

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

TEST = """\
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
"""

Input = InputData
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def compare(self, r1: int | list[int], r2: int | list[int]) -> int:
        log(f">Compare {r1} vs {r2}")
        if isinstance(r1, int) and isinstance(r2, int):
            return -1 if r1 < r2 else 1 if r1 > r2 else 0
        if isinstance(r1, list) and isinstance(r2, int):
            log("Mixed types; convert right to list and retry comparison")
            return self.compare(r1, [r2])
        if isinstance(r1, int) and isinstance(r2, list):
            log("Mixed types; convert left to list and retry comparison")
            return self.compare([r1], r2)
        if isinstance(r1, list) and isinstance(r2, list):
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
                res = self.compare(n1, n2)
                if res == 0:
                    continue
                return res
            log("Left side ran out of items")
            return -1 if size1 < size2 else 0
        raise AssertionError

    def part_1(self, inputs: Input) -> Output1:
        return sum(
            i + 1
            for i, block in enumerate(my_aocd.to_blocks(inputs))
            if self.compare(eval(block[0]), eval(block[1])) < 1  # noqa:S307
        )

    def part_2(self, inputs: Input) -> Output2:
        dividers = [[[2]], [[6]]]
        packets = [eval(line) for line in inputs if len(line) > 0]  # noqa:S307
        packets.extend(dividers)
        packets.sort(key=cmp_to_key(self.compare))
        return prod(
            i + 1 for i, packet in enumerate(packets) if packet in dividers
        )

    @aoc_samples(
        (
            ("part_1", TEST, 13),
            ("part_2", TEST, 140),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 13)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

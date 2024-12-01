#! /usr/bin/env python3
#
# Advent of Code 2019 Day 3
#

import sys
from collections import Counter
from typing import Callable

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.range import RangeInclusive

Input = RangeInclusive
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        a, b = list(input_data)[0].split("-")
        return RangeInclusive.between(int(a), int(b))

    def does_not_decrease(self, passw: str) -> bool:
        return list(passw) == sorted(passw)

    def is_valid_1(self, passw: str) -> bool:
        return self.does_not_decrease(passw) and len(set(passw)) != len(passw)

    def is_valid_2(self, passw: str) -> bool:
        return self.does_not_decrease(passw) and 2 in Counter(passw).values()

    def count_valid(self, range: Input, check: Callable[[str], bool]) -> int:
        return sum(check(str(i)) for i in range.iterator())

    def part_1(self, range: Input) -> int:
        return self.count_valid(range, self.is_valid_1)

    def part_2(self, range: Input) -> int:
        return self.count_valid(range, self.is_valid_2)

    def samples(self) -> None:
        assert self.is_valid_1("122345")
        assert self.is_valid_1("111123")
        assert self.is_valid_1("111111")
        assert not self.is_valid_1("223450")
        assert not self.is_valid_1("123789")
        assert self.is_valid_2("112233")
        assert not self.is_valid_2("123444")
        assert self.is_valid_2("111122")


solution = Solution(2019, 4)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

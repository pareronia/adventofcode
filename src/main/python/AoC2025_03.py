#! /usr/bin/env python3
#
# Advent of Code 2025 Day 3
#

import sys
from functools import cache

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

Input = InputData
Output1 = int
Output2 = int


TEST = """\
987654321111111
811111111111119
234234234234278
818181911112111
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, inputs: Input) -> Output1:
        ans = 0
        for line in inputs:
            first = max(enumerate(line[:-1]), key=lambda e: (e[1], -e[0]))
            second = max(
                enumerate(line[first[0] + 1 :]), key=lambda e: (e[1], -e[0])
            )
            ans += int(first[1] + second[1])
        return ans

    def part_2(self, inputs: Input) -> Output2:
        @cache
        def dfs(left: int, start: int) -> int:
            if start >= len(line):
                return 0
            if left == 1:
                if start == len(line) - 1:
                    return int(line[-1])
                return max(int(line[start]), dfs(1, start + 1))
            cur = dfs(left - 1, start + 1)
            nxt = dfs(left, start + 1)
            if cur > 0:
                return max(int(line[start] + str(cur)), nxt)
            return nxt

        dfs.cache_clear()
        line = "1"
        test = dfs(1, 0)
        assert test == 1, test
        dfs.cache_clear()
        line = "21"
        test = dfs(1, 0)
        assert test == 2, test
        dfs.cache_clear()
        line = "12"
        test = dfs(1, 0)
        assert test == 2, test
        dfs.cache_clear()
        line = "12"
        test = dfs(2, 0)
        assert test == 12, test
        dfs.cache_clear()
        line = "123"
        test = dfs(2, 0)
        assert test == 23, test
        dfs.cache_clear()
        line = "321"
        test = dfs(2, 0)
        assert test == 32, test
        ans = 0
        for line in inputs:  # noqa:B007
            dfs.cache_clear()
            best = dfs(12, 0)
            log(best)
            ans += best
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 357),
            ("part_2", TEST, 3121910778619),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2025, 3)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2024 Day 22
#

import sys
from collections import defaultdict

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

Input = list[int]
Output1 = int
Output2 = int


TEST1 = """\
1
10
100
2024
"""
TEST2 = """\
1
2
3
2024
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(map(int, input_data))

    def part_1(self, seeds: Input) -> Output1:
        nums = seeds[:]
        for _ in range(2000):
            for i in range(len(nums)):
                nums[i] = (nums[i] ^ (nums[i] * 64)) % 16777216
                nums[i] = (nums[i] ^ (nums[i] // 32)) % 16777216
                nums[i] = (nums[i] ^ (nums[i] * 2048)) % 16777216
        return sum(nums)

    def part_2(self, seeds: Input) -> Output2:
        d = defaultdict[tuple[int, int, int, int], int](int)
        for i in range(len(seeds)):
            secrets = list[int]()
            secrets.append(seeds[i])
            for j in range(2000):
                num = secrets[-1]
                num = (num ^ (num * 64)) % 16777216
                num = (num ^ (num // 32)) % 16777216
                num = (num ^ (num * 2048)) % 16777216
                secrets.append(num)
            diffs = list[tuple[int, int]]()
            # prices: list[list[tuple[int, tuple[int, int, int, int]]]] = [
            #     [] for _ in range(len(secrets))
            # ]
            prev = secrets[0]
            for price in secrets[1:]:
                diffs.append(((price % 10) - (prev % 10), price % 10))
                prev = price
            seen = set[tuple[int, int, int, int]]()
            for j in range(len(diffs) - 3):
                key = (
                    diffs[j][0],
                    diffs[j + 1][0],
                    diffs[j + 2][0],
                    diffs[j + 3][0],
                )
                if key in seen:
                    continue
                seen.add(key)
                d[key] += diffs[j + 3][1]
        # best = 0
        # for n in tqdm(range(len(prices[0]))):
        #     seq = prices[0][n][1]
        #     bananas = 0
        #     for i in range(len(prices)):
        #         for j in range(4, len(prices[i])):
        #             if prices[i][j][1] == seq:
        #                 bananas += prices[i][j][0]
        #                 break
        #     best = max(best, bananas)
        # for i in tqdm(range(len(prices))):
        #     e = dict[tuple[int, int, int, int], int]()
        #     for j in range(len(prices[i])):
        #         seq = prices[i][j][1]
        #         # if seq == (0, 0, 0, 0):
        #         #     continue
        #         if seq in e:
        #             continue
        #         e[seq] = prices[i][j][0]
        #     for k, v in e.items():
        #         d[k] += v
        log([(k, v) for k, v in d.items()][0:10])
        log([(k, v) for k, v in d.items()][-9:])
        return max(d.values())

    @aoc_samples(
        (
            ("part_1", TEST1, 37327623),
            ("part_2", TEST2, 23),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 22)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

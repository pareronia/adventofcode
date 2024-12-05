#! /usr/bin/env python3
#
# Advent of Code 2024 Day 5
#

import sys
from collections import defaultdict
from functools import cmp_to_key

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = InputData
Output1 = int
Output2 = int


TEST = """\
47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, input: Input) -> Output1:
        blocks = my_aocd.to_blocks(input)
        order = defaultdict[int, list[int]](list)
        for line in blocks[0]:
            a, b = map(int, line.split("|"))
            order[a].append(b)
        ans = 0
        for line in blocks[1]:
            nums = list(map(int, line.split(",")))
            for i in range(1, len(nums)):
                a = nums[i - 1]
                b = nums[i]
                if b not in order[a]:
                    break
            else:
                ans += nums[len(nums) // 2]
        return ans

    def part_2(self, input: Input) -> Output2:
        blocks = my_aocd.to_blocks(input)
        order = defaultdict[int, list[int]](list)
        for line in blocks[0]:
            a, b = map(int, line.split("|"))
            order[a].append(b)
        ans = 0
        wrong = list[list[int]]()
        for line in blocks[1]:
            nums = list(map(int, line.split(",")))
            for i in range(1, len(nums)):
                a = nums[i - 1]
                b = nums[i]
                if b not in order[a]:
                    wrong.append(nums)
                    break

        def f(a: int, b: int) -> int:
            if b in order[a]:
                return 1
            else:
                return -1

        for w in wrong:
            w.sort(key=cmp_to_key(f))
            ans += w[len(w) // 2]
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 143),
            ("part_2", TEST, 123),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 5)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

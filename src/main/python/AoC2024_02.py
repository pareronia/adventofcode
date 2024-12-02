#! /usr/bin/env python3
#
# Advent of Code 2024 Day 2
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = InputData
Output1 = int
Output2 = int


TEST = """\
7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def safe(self, nums: list[int]) -> bool:
        if nums[0] == nums[-1]:
            return False
        if nums[0] < nums[-1]:
            for i in range(len(nums) - 1):
                diff = nums[i + 1] - nums[i]
                if diff < 1 or diff > 3:
                    break
            else:
                return True
        if nums[0] > nums[-1]:
            for i in range(len(nums) - 1):
                diff = nums[i] - nums[i + 1]
                if diff < 1 or diff > 3:
                    break
            else:
                return True
        return False

    def part_1(self, input: Input) -> Output1:
        ans = 0
        for line in input:
            nums = list(map(int, line.split()))
            if self.safe(nums):
                ans += 1
        return ans

    def part_2(self, input: Input) -> Output2:
        ans = 0
        for line in input:
            nums = list(map(int, line.split()))
            if self.safe(nums):
                ans += 1
            else:
                for i in range(len(nums)):
                    if self.safe(nums[:i] + nums[i + 1 :]):  # noqa E203
                        ans += 1
                        break
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 2),
            ("part_2", TEST, 4),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 2)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

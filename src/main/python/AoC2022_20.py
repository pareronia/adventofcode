#! /usr/bin/env python3
#
# Advent of Code 2022 Day 20
#


import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST1 = """\
1
2
-3
3
-2
0
4
"""
TEST2 = """\
3
1
0
"""

Input = InputData
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def solve(self, inputs: Input, rounds: int, factor: int = 1) -> int:
        nums = [int(line) * factor for line in inputs]
        idxs = list(range(len(nums)))
        for _ in range(rounds):
            for i, num in enumerate(nums):
                idx = idxs.index(i)
                idxs.remove(i)
                new_idx = (idx + num) % len(idxs)
                idxs.insert(new_idx, i)
        zero_idx = idxs.index(nums.index(0))
        return sum(
            nums[idxs[(zero_idx + i) % len(idxs)]] for i in [1000, 2000, 3000]
        )

    def part_1(self, inputs: Input) -> Output1:
        return self.solve(inputs, rounds=1)

    def part_2(self, inputs: Input) -> Output2:
        return self.solve(inputs, rounds=10, factor=811_589_153)

    @aoc_samples(
        (
            ("part_1", TEST1, 3),
            ("part_1", TEST2, 4),
            ("part_2", TEST1, 1_623_178_306),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 20)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

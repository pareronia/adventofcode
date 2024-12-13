#! /usr/bin/env python3
#
# Advent of Code 2024 Day 9
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[int]
Output1 = int
Output2 = int

TRIANGLE = [0, 0, 1, 3, 6, 10, 15, 21, 28, 36]

TEST = """\
2333133121414131402
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    """https://github.com/maneatingape/advent-of-code-rust/blob/main/src/year2024/day09.rs"""  # noqa E501

    def parse_input(self, input_data: InputData) -> Input:
        return list(map(int, list(input_data)[0]))

    def update(
        self, ans: int, block: int, idx: int, size: int
    ) -> tuple[int, int]:
        id = idx // 2
        extra = block * size + TRIANGLE[size]
        return (ans + id * extra, block + size)

    def part_1(self, disk: Input) -> Output1:
        left = 0
        right = len(disk) - 2 + len(disk) % 2
        need = disk[right]
        block = 0
        ans = 0
        while left < right:
            ans, block = self.update(ans, block, left, disk[left])
            available = disk[left + 1]
            left += 2
            while available > 0:
                if need == 0:
                    if left == right:
                        break
                    right -= 2
                    need = disk[right]
                size = min(need, available)
                ans, block = self.update(ans, block, right, size)
                available -= size
                need -= size
        ans, _ = self.update(ans, block, right, need)
        return ans

    def part_2(self, disk: Input) -> Output2:
        block = 0
        ans = 0
        free: list[list[int]] = list()
        for i in range(10):
            free.append(list())
        for idx, size in enumerate(disk):
            if idx % 2 and size > 0:
                free[size].append(block)
            block += size
        for i in range(10):
            free[i].append(block)
            free[i].reverse()
        for idx, size in reversed(list(enumerate(disk))):
            block -= size
            if idx % 2:
                continue
            nxt_block = block
            nxt_idx = sys.maxsize
            for i in range(size, len(free)):
                first = free[i][-1]
                if first < nxt_block:
                    nxt_block = first
                    nxt_idx = i
            if len(free):
                if free[-1][-1] > block:
                    free.pop()
            id = idx // 2
            extra = nxt_block * size + TRIANGLE[size]
            ans += id * extra
            if nxt_idx != sys.maxsize:
                free[nxt_idx].pop()
                to = nxt_idx - size
                if to > 0:
                    i = len(free[to])
                    val = nxt_block + size
                    while free[to][i - 1] < val:
                        i -= 1
                    free[to].insert(i, val)
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 1928),
            ("part_2", TEST, 2858),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 9)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

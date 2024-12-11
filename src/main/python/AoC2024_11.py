#! /usr/bin/env python3
#
# Advent of Code 2024 Day 11
#

import sys
from collections import defaultdict
from collections import deque

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
from tqdm import tqdm

Input = InputData
Output1 = int
Output2 = int


TEST = """\
125 17
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, input: Input) -> Output1:
        stones = list(input)[0].split()
        for _ in range(25):
            new_stones = list[str]()
            for s in stones:
                if s == "0":
                    new_stones.append("1")
                elif not len(s) % 2:
                    new_stones.append(s[: len(s) // 2])
                    new_stones.append(str(int(s[len(s) // 2 :])))
                else:
                    new_stones.append(str(int(s) * 2024))
            stones = new_stones
        return len(stones)

    def part_2(self, input: Input) -> Output2:
        stones = list(input)[0].split()
        # stones = ["0"]
        # seen = dict[tuple[int, int], int]()
        seen = defaultdict[tuple[int, int], int](int)
        target = 25
        ans = 0
        q = deque((int(s), 0) for s in stones)
        while q:
            s, i = q.pop()
            if i == target:
                continue
            nxt = []
            if s == 0:
                seen[(s, i)] += seen[(s, i - 1)]
                nxt.append((1, i + 1))
            elif not len(str(s)) % 2:
                ss = str(s)
                ss1, ss2 = ss[: len(ss) // 2], ss[len(ss) // 2 :]
                seen[(s, i)] += (
                    seen[(int(ss1), i - 1)] + seen[(int(ss2), i - 1)] + 2
                )

                nxt.append((int(ss1), i + 1))
                nxt.append((int(ss2), i + 1))
            else:
                seen[(s, i)] += seen[(s * 2024, i - 1)]
                nxt.append((s * 2024, i + 1))
            for n in nxt:
                # if n in seen:
                #     log("seen: {n}")
                #     ans += seen[n]
                # else:
                q.append(n)
        log(seen)
        for x in stones:
            ans += seen[(int(x), target - 1)]
        return ans

    @aoc_samples((("part_1", TEST, 55312),))
    def samples(self) -> None:
        pass


solution = Solution(2024, 11)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

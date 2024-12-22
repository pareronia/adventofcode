#! /usr/bin/env python3
#
# Advent of Code 2024 Day 22
#

import multiprocessing
import os
import sys
from collections import defaultdict
from typing import Callable

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

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

    def secret(self, num: int) -> int:
        num = (num ^ (num * 64)) % 16777216
        num = (num ^ (num // 32)) % 16777216
        num = (num ^ (num * 2048)) % 16777216
        return num

    def solve(
        self, seeds: Input, worker: Callable[[str, list[int]], None]
    ) -> None:
        if sys.platform.startswith("win"):
            worker("main", seeds)
        else:
            n = os.process_cpu_count()
            batch = [list[int]() for _ in range(n)]
            cnt = 0
            for seed in seeds:
                batch[cnt % n].append(seed)
                cnt += 1
            jobs = []
            for i in range(n):
                p = multiprocessing.Process(
                    target=worker, args=(str(i), batch[i])
                )
                jobs.append(p)
                p.start()
            for p in jobs:
                p.join()

    def part_1(self, seeds: Input) -> Output1:
        m_ans = multiprocessing.Manager().dict()

        def worker(id: str, seeds: list[int]) -> None:
            ans = 0
            for num in seeds:
                for _ in range(2000):
                    num = self.secret(num)
                ans += num
            m_ans[id] = ans

        self.solve(seeds, worker)
        return sum(m_ans.values())

    def part_2(self, seeds: Input) -> Output2:
        m_ans = multiprocessing.Array("i", [0 for _ in range(19**4)])

        def worker(id: str, seeds: list[int]) -> None:
            p = defaultdict[int, int](int)
            seen = [-1 for _ in range(19**4)]
            for i, num in enumerate(seeds):
                na = num
                nb = self.secret(na)
                nc = self.secret(nb)
                nd = self.secret(nc)
                b, c, d = (
                    na % 10 - nb % 10 + 9,
                    nb % 10 - nc % 10 + 9,
                    nc % 10 - nd % 10 + 9,
                )
                num = nd
                prev_price = num % 10
                for _ in range(3, 2000):
                    num = self.secret(num)
                    price = num % 10
                    a, b, c, d = b, c, d, prev_price - price + 9
                    prev_price = price
                    key = (a * 19**3) + (b * 19**2) + (c * 19) + d
                    if seen[key] == i:
                        continue
                    seen[key] = i
                    p[key] += price
            for k, v in p.items():
                m_ans[k] += v

        self.solve(seeds, worker)
        return max(m_ans)  # type:ignore

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

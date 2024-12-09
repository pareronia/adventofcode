#! /usr/bin/env python3
#
# Advent of Code 2024 Day 9
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

Input = InputData
Output1 = int
Output2 = int


TEST = """\
2333133121414131402
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, input: Input) -> Output1:
        line = next(iter(input))
        blocks = []
        cnt = 0
        for ch in line:
            v = int(ch)
            for _ in range(v):
                blocks.append(-1 if cnt % 2 else cnt // 2)
            cnt += 1
        # log("".join("." if b == -1 else str(b) for b in blocks))
        new = []
        for i in range(len(blocks)):
            if i == len(blocks):
                break
            b = blocks[i]
            if b != -1:
                new.append(b)
            else:
                while (x := blocks.pop()) == -1:
                    continue
                new.append(x)
        # log("".join("." if b == -1 else str(b) for b in new))
        ans = 0
        for i, n in enumerate(new):
            ans += i * n
        return ans

    def part_2(self, input: Input) -> Output2:
        line = next(iter(input))
        blocks = []
        cnt = 0
        for ch in line:
            v = int(ch)
            blocks.append((-1 if cnt % 2 else cnt // 2, v))
            cnt += 1
        log(blocks)
        log(
            "".join(
                "." * cnt if val == -1 else str(val) * cnt
                for val, cnt in blocks
            )
        )
        x = 0
        # while x <= 10:
        while x <= 20000:
            x += 1
            new = list[tuple[int, int]]()
            for j in range(1, len(blocks) - 1):
                valj, cntj = blocks[-j]
                if valj == -1:
                    continue
                for i in range(len(blocks) - j):
                    val, cnt = blocks[i]
                    if val != -1:
                        continue
                    if cntj > cnt:
                        continue
                    else:
                        break
                else:
                    continue
                break
            else:
                log("stop")
                self.log(blocks)
                ans = 0
                # breakpoint()
                x = 0
                for i, n in enumerate(blocks):
                    val, cnt = n
                    if val != -1:
                        for j in range(x, x + cnt):
                            ans += j * val
                    x += cnt
                return ans
            for ii in range(len(blocks)):
                if ii == len(blocks):
                    break
                if ii != i:
                    new.append(blocks[ii])
                    continue
                blocks[-j] = (-1, cntj)
                new.append((valj, cntj))
                if cntj < cnt:
                    new.append((-1, cnt - cntj))
            if new == blocks:
                break
            blocks = new
            self.log(blocks)
        raise RuntimeError()
        # ans = 0
        # for i, n in enumerate(new):
        #     val, cnt = n
        #     for j in range(i, i + cnt):
        #         ans += j * val
        # return ans

    def log(self, blocks: list[tuple[int, int]]) -> None:
        log(
            "".join(
                "." * cnt if val == -1 else str(val) * cnt
                for val, cnt in blocks
            )
        )

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

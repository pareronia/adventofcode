#! /usr/bin/env python3
#
# Advent of Code 2023 Day 15
#

import sys
from collections import defaultdict

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

Input = list[str]
Output1 = int
Output2 = int


TEST = """\
rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)[0].split(",")

    def hash(self, s: str) -> int:
        ans = 0
        for ch in s:
            ans += ord(ch)
            ans *= 17
            ans %= 256
        return ans

    def part_1(self, steps: Input) -> Output1:
        ans = 0
        for step in steps:
            ans += self.hash(step)
        return ans

    def part_2(self, steps: Input) -> Output2:
        boxes = defaultdict[int, list[tuple[str, int]]](list)
        for step in steps:
            if "=" in step:
                label, fl = step.split("=")
                box = self.hash(label)
                lst = boxes[box]
                for x in lst:
                    if x[0] == label:
                        idx = lst.index(x)
                        lst[idx] = (label, int(fl))
                        break
                else:
                    lst.append((label, int(fl)))
            else:
                label = step[:-1]
                box = self.hash(label)
                lst = boxes[box]
                for x in lst:
                    if x[0] == label:
                        lst.remove(x)
            log(boxes)
        ans = 0
        for box in boxes:
            for i, x in enumerate(boxes[box]):
                ans += (box + 1) * (i + 1) * x[1]
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 1320),
            ("part_2", TEST, 145),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 15)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

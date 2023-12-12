#! /usr/bin/env python3
#
# Advent of Code 2023 Day 12
#

import sys
from itertools import product

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

Input = InputData
Output1 = int
Output2 = int


TEST = """\
???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def count(self, s: str, counts: tuple[int, ...]) -> int:
        def match(s: str, counts: tuple[int, ...]) -> bool:
            c = tuple(len(_) for _ in s.split(".") if len(_) != 0)
            return c == counts

        ans = 0
        cq = s.count("?")
        pp = product("#.", repeat=cq)
        for p in pp:
            pi = iter(p)
            test = "".join(
                s[i] if s[i] != "?" else next(pi) for i in range(len(s))
            )
            if match(test, counts):
                ans += 1
        return ans

    def part_1(self, input: Input) -> Output1:
        ans = 0
        for line in input:
            s, w = line.split()
            counts = tuple(int(_) for _ in w.split(","))
            log((s, counts))
            ans += self.count(s, counts)
        return ans

    def part_2(self, input: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 21),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 12)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

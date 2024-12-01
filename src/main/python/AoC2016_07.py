#! /usr/bin/env python3
#
# Advent of Code 2015 Day 7
#

import re
import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

ABBA = re.compile(r"([a-z])(?!\1)([a-z])\2\1")
HYPERNET = re.compile(r"\[([a-z]*)\]")
ABA = re.compile(r"([a-z])(?!\1)[a-z]\1")

TEST1 = """\
abba[mnop]qrst
abcd[bddb]xyyx
aaaa[qwer]tyui
ioxxoj[asdfgh]zxcvbn
abcox[ooooo]xocba
"""
TEST2 = """\
aba[bab]xyz
xyx[xyx]xyx
aaa[kek]eke
zazbz[bzb]cdb
"""

Input = InputData
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, inputs: InputData) -> int:
        def is_tls(ip: str) -> bool:
            for b in HYPERNET.findall(ip):
                if ABBA.search(b) is not None:
                    return False
                ip = ip.replace("[" + b + "]", "/")
            return ABBA.search(ip) is not None

        return sum(is_tls(line) for line in inputs)

    def part_2(self, inputs: InputData) -> int:
        def is_sls(ip: str) -> bool:
            bs = HYPERNET.findall(ip)
            for b in bs:
                ip = ip.replace("[" + b + "]", "/")
            return any(
                any(bab in b for b in bs)
                for bab in (
                    m.group()[1] + m.group()[0] + m.group()[1]
                    for i in range(len(ip) - 3)
                    for m in ABA.finditer(ip[i:])
                )
            )

        return sum(is_sls(line) for line in inputs)

    @aoc_samples(
        (
            ("part_1", TEST1, 2),
            ("part_2", TEST2, 3),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2016, 7)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2015 Day 6
#

import sys
from collections import Counter

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[Counter[str]]
Output1 = str
Output2 = str


TEST = """\
eedadn
drvtee
eandsr
raavrd
atevrs
tsrnev
sdttsa
rasrtv
nssdts
ntnada
svetve
tesnvt
vntsnd
vrdear
dvrsen
enarar
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        inputs = list(input_data)
        return [
            Counter(line[i] for line in inputs) for i in range(len(inputs[0]))
        ]

    def part_1(self, counters: Input) -> str:
        return "".join(counter.most_common()[0][0] for counter in counters)

    def part_2(self, counters: Input) -> str:
        return "".join(counter.most_common()[-1][0] for counter in counters)

    @aoc_samples(
        (
            ("part_1", TEST, "easter"),
            ("part_2", TEST, "advent"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2016, 6)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

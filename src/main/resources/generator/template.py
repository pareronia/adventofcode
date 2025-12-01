#! /usr/bin/env python3
#
# Advent of Code ${year} Day ${day}
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = InputData
Output1 = int
Output2 = int


TEST = """\
TODO
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, inputs: Input) -> Output1:  # noqa:ARG002
        return 0

    def part_2(self, inputs: Input) -> Output2:  # noqa:ARG002
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, "TODO"),
            ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(${year}, ${day})


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

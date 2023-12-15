#! /usr/bin/env python3
#
# Advent of Code 2023 Day 15
#

import sys
from collections import defaultdict
from functools import reduce

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[str]
Output1 = int
Output2 = int


TEST = """\
rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
"""


def hash(s: str) -> int:
    return reduce(
        lambda acc, ch: ((acc + ord(ch)) * 17) % 256, (ch for ch in s), 0
    )


class Boxes:
    def __init__(self) -> None:
        self.boxes = defaultdict[int, list[tuple[str, int]]](list)

    def add_lens(self, label: str, focal_length: int) -> None:
        lenses = self.boxes[hash(label)]
        for lens in lenses:
            if lens[0] == label:
                idx = lenses.index(lens)
                lenses[idx] = (label, focal_length)
                break
        else:
            lenses.append((label, focal_length))

    def remove_lens(self, label: str) -> None:
        lenses = self.boxes[hash(label)]
        for lens in lenses:
            if lens[0] == label:
                lenses.remove(lens)

    def get_total_focusing_power(self) -> int:
        return sum(
            (box + 1) * i * lens[1]
            for box in self.boxes
            for i, lens in enumerate(self.boxes[box], start=1)
        )


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)[0].split(",")

    def part_1(self, steps: Input) -> Output1:
        return sum(hash(step) for step in steps)

    def part_2(self, steps: Input) -> Output2:
        boxes = Boxes()
        for step in steps:
            if "=" in step:
                label, fl = step.split("=")
                boxes.add_lens(label, int(fl))
            else:
                label = step[:-1]
                boxes.remove_lens(label)
        return boxes.get_total_focusing_power()

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

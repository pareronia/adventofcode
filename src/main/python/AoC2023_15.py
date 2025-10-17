#! /usr/bin/env python3
#
# Advent of Code 2023 Day 15
#

import sys
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


def calc_hash(s: str) -> int:
    return reduce(
        lambda acc, ch: ((acc + ord(ch)) * 17) % 256, (ch for ch in s), 0
    )


class Boxes:
    def __init__(self) -> None:
        self.boxes: list[dict[str, int]] = [{} for _ in range(256)]

    def add_lens(self, label: str, focal_length: int) -> None:
        self.boxes[calc_hash(label)][label] = focal_length

    def remove_lens(self, label: str) -> None:
        box = self.boxes[calc_hash(label)]
        if label in box:
            del box[label]

    def get_total_focusing_power(self) -> int:
        return sum(
            b * i * focal_length
            for b, box in enumerate(self.boxes, start=1)
            for i, focal_length in enumerate(box.values(), start=1)
        )


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return next(iter(input_data)).split(",")

    def part_1(self, steps: Input) -> Output1:
        return sum(calc_hash(step) for step in steps)

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

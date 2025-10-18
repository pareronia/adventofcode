#! /usr/bin/env python3
#
# Advent of Code 2022 Day 10
#


import sys

import advent_of_code_ocr as ocr
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

TEST = """\
addx 15
addx -11
addx 6
addx -3
addx 5
addx -1
addx -8
addx 13
addx 4
noop
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx -35
addx 1
addx 24
addx -19
addx 1
addx 16
addx -11
noop
noop
addx 21
addx -15
noop
noop
addx -3
addx 9
addx 1
addx -3
addx 8
addx 1
addx 5
noop
noop
noop
noop
noop
addx -36
noop
addx 1
addx 7
noop
noop
noop
addx 2
addx 6
noop
noop
noop
noop
noop
addx 1
noop
noop
addx 7
addx 1
noop
addx -13
addx 13
addx 7
noop
addx 1
addx -33
noop
noop
noop
addx 2
noop
noop
noop
addx 8
noop
addx -1
addx 2
addx 1
noop
addx 17
addx -9
addx 1
addx 1
addx -3
addx 11
noop
noop
addx 1
noop
addx 1
noop
noop
addx -13
addx -19
addx 1
addx 3
addx 26
addx -30
addx 12
addx -1
addx 3
addx 1
noop
noop
noop
addx -9
addx 18
addx 1
addx 2
noop
noop
addx 9
noop
noop
noop
addx -1
addx 2
addx -37
addx 1
addx 3
noop
addx 15
addx -21
addx 22
addx -6
addx 1
noop
addx 2
addx 1
noop
addx -10
noop
noop
addx 20
addx 1
addx 2
addx 2
addx -6
addx -11
noop
noop
noop
"""

FILL = "▒"
EMPTY = " "

Input = tuple[int, ...]
Output1 = int
Output2 = str


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        ans = list[int]()
        x = 1
        for line in input_data:
            splits = line.split()
            if splits[0] == "noop":
                ans.append(x)
            elif splits[0] == "addx":
                ans.append(x)
                ans.append(x)
                x += int(splits[1])
        return tuple(ans)

    def get_pixels(self, xs: tuple[int, ...]) -> tuple[str, ...]:
        pixels = "".join(
            FILL if i % 40 in range(x - 1, x + 2) else EMPTY
            for i, x in enumerate(xs)
        )
        return tuple(pixels[i * 40 : i * 40 + 40] for i in range(6))

    def part_1(self, xs: Input) -> Output1:
        return sum(
            x * i if i % 40 == 20 else 0 for i, x in enumerate(xs, start=1)
        )

    def part_2(self, xs: Input) -> Output2:
        pixels = self.get_pixels(xs)
        log(pixels)
        return str(
            ocr.convert_6(
                "\n".join(pixels), fill_pixel=FILL, empty_pixel=EMPTY
            )
        )

    @aoc_samples((("part_1", TEST, 13_140),))
    def samples(self) -> None:
        xs = self.parse_input(TEST.splitlines())
        assert self.get_pixels(xs) == (
            "▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ",
            "▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒ ",
            "▒▒▒▒    ▒▒▒▒    ▒▒▒▒    ▒▒▒▒    ▒▒▒▒    ",
            "▒▒▒▒▒     ▒▒▒▒▒     ▒▒▒▒▒     ▒▒▒▒▒     ",
            "▒▒▒▒▒▒      ▒▒▒▒▒▒      ▒▒▒▒▒▒      ▒▒▒▒",
            "▒▒▒▒▒▒▒       ▒▒▒▒▒▒▒       ▒▒▒▒▒▒▒     ",
        )


solution = Solution(2022, 10)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2019 Day 8
#

import sys
from typing import cast

from advent_of_code_ocr import convert_6
from aoc.common import InputData
from aoc.common import SolutionBase

WIDTH = 25
HEIGHT = 6

Input = str
Output1 = int
Output2 = str


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)[0]

    def get_layers(self, input: str, width: int, height: int) -> list[str]:
        layer_size = width * height
        return [
            input[i : i + layer_size]  # noqa E203
            for i in range(0, len(input), layer_size)
        ]

    def part_1(self, input: str) -> int:
        layers = self.get_layers(input, WIDTH, HEIGHT)
        least_zeroes = min(layers, key=lambda lyr: lyr.count("0"))
        return least_zeroes.count("1") * least_zeroes.count("2")

    def get_image(self, input: str, width: int, height: int) -> str:
        layers = self.get_layers(input, width, height)
        return "".join(
            next(lyr[i] for lyr in layers if lyr[i] != "2")
            for i in range(0, len(layers[0]))
        )

    def part_2(self, input: str) -> str:
        image = self.get_image(input, WIDTH, HEIGHT)
        to_ocr = "\n".join(
            image[i : i + WIDTH]  # noqa E203
            for i in range(0, WIDTH * HEIGHT, WIDTH)
        )
        return cast(str, convert_6(to_ocr, fill_pixel="1", empty_pixel="0"))

    def samples(self) -> None:
        assert self.get_image("0222112222120000", 2, 2) == "0110"


solution = Solution(2019, 8)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

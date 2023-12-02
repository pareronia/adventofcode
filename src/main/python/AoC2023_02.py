#! /usr/bin/env python3
#
# Advent of Code 2023 Day 2
#

import sys
from math import prod

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = InputData
Output1 = int
Output2 = int


TEST = """\
Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, input: InputData) -> Output1:
        ans = 0
        for game in input:
            splits = game.split(":")
            id = int(splits[0].split()[1])
            draws = splits[1].split(";")
            for draw in draws:
                colors = draw.split(",")
                for c in colors:
                    count, color = c.split()
                    if color == "red" and int(count) > 12:
                        break
                    if color == "green" and int(count) > 13:
                        break
                    if color == "blue" and int(count) > 14:
                        break
                else:
                    continue
                break
            else:
                ans += id
                continue
        return ans

    def part_2(self, input: InputData) -> Output2:
        ans = 0
        for game in input:
            splits = game.split(":")
            draws = splits[1].split(";")
            max_red = max_green = max_blue = 0
            for draw in draws:
                colors = draw.split(",")
                for c in colors:
                    count, color = c.split()
                    if color == "red":
                        max_red = max(max_red, int(count))
                    if color == "green":
                        max_green = max(max_green, int(count))
                    if color == "blue":
                        max_blue = max(max_blue, int(count))
            assert max_red or max_green or max_blue
            ans += prod(m for m in [max_red, max_green, max_blue] if m)
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 8),
            ("part_2", TEST, 2286),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 2)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

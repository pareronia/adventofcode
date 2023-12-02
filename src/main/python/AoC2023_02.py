#! /usr/bin/env python3
#
# Advent of Code 2023 Day 2
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

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
            log(id)
            draws = splits[1].split(";")
            for draw in draws:
                colors = draw.split(",")
                for c in colors:
                    count, color = c.split()
                    if color == "red" and int(count) > 12:
                        log(str(id) + ": impossible")
                        break
                    if color == "green" and int(count) > 13:
                        log(str(id) + ": impossible")
                        break
                    if color == "blue" and int(count) > 14:
                        log(str(id) + ": impossible")
                        break
                else:
                    continue
                break
            else:
                log(str(id) + ": possible")
                ans += id
                continue
        return ans

    def part_2(self, input: InputData) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 8),
            ("part_2", TEST, 0),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 2)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2023 Day 2
#

from __future__ import annotations

import sys
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
"""


class Draw(NamedTuple):
    red: int
    green: int
    blue: int


class Game(NamedTuple):
    id: int
    draws: list[Draw]


Input = list[Game]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        def parse_draw(s: str) -> Draw:
            red = green = blue = None
            for cc in s.split(","):
                count, color = cc.split()
                if color == "red":
                    red = int(count)
                if color == "green":
                    green = int(count)
                if color == "blue":
                    blue = int(count)
            return Draw(red or 0, green or 0, blue or 0)

        return [
            Game(
                i + 1,
                [parse_draw(draw) for draw in line.split(":")[1].split(";")],
            )
            for i, line in enumerate(input_data)
        ]

    def part_1(self, games: Input) -> Output1:
        def possible(draw: Draw) -> bool:
            return draw.red <= 12 and draw.green <= 13 and draw.blue <= 14

        return sum(
            game.id
            for game in games
            if all(possible(draw) for draw in game.draws)
        )

    def part_2(self, games: Input) -> Output2:
        def power(game: Game) -> int:
            return (
                max(draw.red for draw in game.draws)
                * max(draw.green for draw in game.draws)
                * max(draw.blue for draw in game.draws)
            )

        return sum(power(game) for game in games)

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

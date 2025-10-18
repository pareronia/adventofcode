#! /usr/bin/env python3
#
# Advent of Code 2022 Day 2
#

import sys
from collections.abc import Callable

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = InputData
Output1 = int
Output2 = int
ROCK = "rock"
PAPER = "paper"
SCISSORS = "scissors"
WIN = "win"
DRAW = "draw"
LOSS = "lose"
shapes = {
    "A": ROCK,
    "B": PAPER,
    "C": SCISSORS,
    "X": ROCK,
    "Y": PAPER,
    "Z": SCISSORS,
}
response_points = {ROCK: 1, PAPER: 2, SCISSORS: 3}
outcome_points = {LOSS: 0, DRAW: 3, WIN: 6}


TEST = """\
A Y
B X
C Z
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def _solve(
        self, inputs: InputData, f: Callable[[str, str], tuple[str, str]]
    ) -> int:
        ans = 0
        for line in inputs:
            response, outcome = f(*line.split())
            ans += response_points[response] + outcome_points[outcome]
        return ans

    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, inputs: Input) -> Output1:
        outcomes = {
            (ROCK, ROCK): DRAW,
            (ROCK, SCISSORS): LOSS,
            (ROCK, PAPER): WIN,
            (SCISSORS, ROCK): WIN,
            (SCISSORS, SCISSORS): DRAW,
            (SCISSORS, PAPER): LOSS,
            (PAPER, ROCK): LOSS,
            (PAPER, SCISSORS): WIN,
            (PAPER, PAPER): DRAW,
        }

        def f(col1: str, col2: str) -> tuple[str, str]:
            play, response = shapes[col1], shapes[col2]
            outcome = outcomes[(play, response)]
            return response, outcome

        return self._solve(inputs, f)

    def part_2(self, inputs: Input) -> Output2:
        outcomes = {"X": LOSS, "Y": DRAW, "Z": WIN}
        responses = {
            (ROCK, LOSS): SCISSORS,
            (ROCK, DRAW): ROCK,
            (ROCK, WIN): PAPER,
            (PAPER, LOSS): ROCK,
            (PAPER, DRAW): PAPER,
            (PAPER, WIN): SCISSORS,
            (SCISSORS, LOSS): PAPER,
            (SCISSORS, DRAW): SCISSORS,
            (SCISSORS, WIN): ROCK,
        }

        def f(col1: str, col2: str) -> tuple[str, str]:
            play, outcome = shapes[col1], outcomes[col2]
            response = responses[(play, outcome)]
            return response, outcome

        return self._solve(inputs, f)

    @aoc_samples(
        (
            ("part_1", TEST, 15),
            ("part_2", TEST, 12),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 2)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

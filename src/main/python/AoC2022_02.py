#! /usr/bin/env python3
#
# Advent of Code 2022 Day 2
#

from typing import Callable

from aoc.common import aoc_main

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


def _solve(
    inputs: tuple[str, ...], f: Callable[[str, str], tuple[str, str]]
) -> int:
    ans = 0
    for line in inputs:
        response, outcome = f(*line.split())
        ans += response_points[response] + outcome_points[outcome]
    return ans


def part_1(inputs: tuple[str, ...]) -> int:
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

    return _solve(inputs, f)


def part_2(inputs: tuple[str, ...]) -> int:
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

    return _solve(inputs, f)


TEST = """\
A Y
B X
C Z
""".splitlines()


@aoc_main(2022, 2, part_1, part_2)
def main() -> None:
    assert part_1(TEST) == 15  # type:ignore[arg-type]
    assert part_2(TEST) == 12  # type:ignore[arg-type]


if __name__ == "__main__":
    main()

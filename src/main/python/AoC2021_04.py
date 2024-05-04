#! /usr/bin/env python3
#
# Advent of Code 2021 Day 4
#

from __future__ import annotations

import itertools
import sys
from typing import NamedTuple

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples


class Board:
    MARKED = -1

    numbers: list[list[int]]
    complete: bool

    def __init__(self, numbers: list[str]):
        self.complete = False
        self.numbers = [
            [int(_) for _ in s.split()] for i, s in enumerate(numbers)
        ]

    def __repr__(self) -> str:
        return f"Board(complete: {self.complete}, numbers: {self.numbers})"

    def set_complete(self) -> None:
        self.complete = True

    def is_complete(self) -> bool:
        return self.complete

    def mark(self, number: int) -> None:
        for row in self.numbers:
            for i, c in enumerate(row):
                if c == number:
                    row[i] = Board.MARKED

    def win(self) -> bool:
        return any(
            all(val == Board.MARKED for val in rc)
            for rc in itertools.chain(
                (row for row in self.numbers),
                (self._get_column(col) for col in range(self._get_width())),
            )
        )

    def value(self) -> int:
        return sum(c for row in self.numbers for c in row if c != Board.MARKED)

    def _get_column(self, col: int) -> list[int]:
        return [self.numbers[row][col] for row in range(self._get_height())]

    def _get_height(self) -> int:
        return len(self.numbers)

    def _get_width(self) -> int:
        return len(self.numbers[0])


class Bingo(NamedTuple):
    draw: int
    board: Board


class BingoGame(NamedTuple):
    draws: list[int]
    boards: list[Board]

    @classmethod
    def from_input(cls, inputs: list[str]) -> BingoGame:
        blocks = my_aocd.to_blocks(inputs)
        draws = [int(_) for _ in blocks[0][0].split(",")]
        boards = [Board(_) for _ in blocks[1:]]
        return BingoGame(draws, boards)

    def play(self, stop_count: int) -> list[Bingo]:
        bingoes = list[Bingo]()
        for draw in self.draws:
            for b in self.boards:
                b.mark(draw)
            winners = [
                b for b in self.boards if not b.is_complete() and b.win()
            ]
            for winner in winners:
                winner.set_complete()
                bingo = Bingo(draw, winner)
                bingoes.append(bingo)
                if len(bingoes) == stop_count:
                    return bingoes
        raise RuntimeError("unreachable")


Input = list[str]
Output1 = int
Output2 = int


TEST = """\
7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)

    def solve(self, game: BingoGame, stop_count: int) -> int:
        bingoes = game.play(stop_count)
        return bingoes[-1].draw * bingoes[-1].board.value()

    def part_1(self, inputs: Input) -> int:
        game = BingoGame.from_input(inputs)
        return self.solve(game, 1)

    def part_2(self, inputs: Input) -> int:
        game = BingoGame.from_input(inputs)
        return self.solve(game, len(game.boards))

    @aoc_samples(
        (
            ("part_1", TEST, 4512),
            ("part_2", TEST, 1924),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2021, 4)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

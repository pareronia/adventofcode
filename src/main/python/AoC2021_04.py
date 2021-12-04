#! /usr/bin/env python3
#
# Advent of Code 2021 Day 4
#

from typing import NamedTuple
from aoc import my_aocd


class Board:
    MARKED = -1

    numbers: list[list[int]]
    complete: bool

    def __init__(self, numbers: list[str]):
        self.complete = False
        self.numbers = list()
        for i, s in enumerate(numbers):
            self.numbers.append([int(_) for _ in s.split()])

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
        for row in self.numbers:
            if all(_ == Board.MARKED for _ in row):
                return True
        for i in range(self._get_width()):
            if all(_ == Board.MARKED for _ in self._get_column(i)):
                return True
        return False

    def value(self) -> int:
        return sum(c
                   for row in self.numbers
                   for c in row
                   if c != Board.MARKED)

    def _get_column(self, col: int) -> list[int]:
        return [self.numbers[row][col]
                for row in range(self._get_height())]

    def _get_height(self) -> int:
        return len(self.numbers)

    def _get_width(self) -> int:
        return len(self.numbers[0])


class Bingo(NamedTuple):
    draw: int
    board: Board


def _parse(inputs: tuple[str]) -> tuple[list[int], list[Board]]:
    blocks = my_aocd.to_blocks(inputs)
    draws = [int(_) for _ in blocks[0][0].split(',')]
    boards = [Board(_) for _ in blocks[1:]]
    return draws, boards


def _play(draws: list[int], boards: list[Board], stop) -> None:
    for draw in draws:
        [b.mark(draw) for b in boards]
        winners = [b for b in boards if not b.is_complete() and b.win()]
        for winner in winners:
            winner.set_complete()
            if stop(Bingo(draw, winner)):
                return


def _solve(draws: list[int], boards: list[Board], stop_count: int) -> int:
    bingoes = []

    def stop(bingo: Bingo) -> bool:
        bingoes.append(bingo)
        return len(bingoes) == stop_count

    _play(draws, boards, stop)
    return bingoes[-1].draw * bingoes[-1].board.value()


def part_1(inputs: tuple[str]) -> int:
    draws, boards = _parse(inputs)
    return _solve(draws, boards, 1)


def part_2(inputs: tuple[str]) -> int:
    draws, boards = _parse(inputs)
    return _solve(draws, boards, len(boards))


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
""".splitlines()


def main() -> None:
    my_aocd.print_header(2021, 4)

    assert part_1(TEST) == 4512
    assert part_2(TEST) == 1924

    inputs = my_aocd.get_input(2021, 4, 601)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

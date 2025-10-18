#! /usr/bin/env python3
#
# Advent of Code 2022 Day 23
#


import sys
from collections import defaultdict

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

TEST = """\
....#..
..###.#
#...#.#
.#...##
#.###..
##.#.##
.#..#..
"""


ELF = "#"
GROUND = "."
Tile = tuple[int, int]
Direction = tuple[int, int]
N = (-1, 0)
NW = (-1, -1)
NE = (-1, 1)
S = (1, 0)
SW = (1, -1)
SE = (1, 1)
W = (0, -1)
E = (0, 1)
ALL_DIRS = {N, NE, E, SE, S, SW, W, NW}
DIRS = {N: {N, NE, NW}, S: {S, SE, SW}, W: {W, NW, SW}, E: {E, NE, SE}}

Input = InputData
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def get_elves(self, inputs: InputData) -> set[Tile]:
        return {
            (r, c)
            for r, line in enumerate(inputs)
            for c, ch in enumerate(line)
            if ch == ELF
        }

    def calculate_moves(
        self, elves: set[Tile], order: list[Direction]
    ) -> dict[Tile, list[Tile]]:
        moves: dict[Tile, list[Tile]] = defaultdict(list[Tile])
        for r, c in elves:
            if all((r + dr, c + dc) not in elves for dr, dc in ALL_DIRS):
                continue
            for d in order:
                if all((r + dr, c + dc) not in elves for dr, dc in DIRS[d]):
                    moves[(r + d[0], c + d[1])].append((r, c))
                    break
        return moves

    def execute_moves(
        self, elves: set[Tile], moves: dict[Tile, list[Tile]]
    ) -> set[Tile]:
        for move, candidates in moves.items():
            if len(candidates) > 1:
                continue
            elves.remove(candidates[0])
            elves.add(move)
        return elves

    def part_1(self, inputs: Input) -> Output1:
        def bounds(elves: set[Tile]) -> tuple[int, int, int, int]:
            min_r = min(r for r, _ in elves)
            min_c = min(c for _, c in elves)
            max_r = max(r for r, _ in elves)
            max_c = max(c for _, c in elves)
            return min_r, min_c, max_r, max_c

        def draw(elves: set[Tile]) -> None:
            if not __debug__:
                return
            min_r, min_c, max_r, max_c = bounds(elves)
            for r in range(min_r, max_r + 1):
                [
                    print(ELF if (r, c) in elves else GROUND, end="")
                    for c in range(min_c, max_c + 1)
                ]
                print()

        elves = self.get_elves(inputs)
        order = [N, S, W, E]
        for i in range(10):
            log(f"Round {i + 1}:")
            moves = self.calculate_moves(elves, order)
            elves = self.execute_moves(elves, moves)
            order.append(order.pop(0))
            draw(elves)
            log(order)
        min_r, min_c, max_r, max_c = bounds(elves)
        return (max_r - min_r + 1) * (max_c - min_c + 1) - len(elves)

    def part_2(self, inputs: Input) -> Output2:
        elves = self.get_elves(inputs)
        order = [N, S, W, E]
        for i in range(1000):
            log(f"Round {i + 1}:")
            moves = self.calculate_moves(elves, order)
            if len(moves) == 0:
                return i + 1
            elves = self.execute_moves(elves, moves)
            order.append(order.pop(0))
        raise AssertionError

    @aoc_samples(
        (
            ("part_1", TEST, 110),
            ("part_2", TEST, 20),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 23)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

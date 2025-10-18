#! /usr/bin/env python3
#
# Advent of Code 2022 Day 9
#


import itertools
import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST1 = """\
R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2
"""
TEST2 = """\
R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20
"""

MOVES = {"U": (0, 1), "D": (0, -1), "L": (-1, 0), "R": (1, 0)}

Position = tuple[int, int]
Move = tuple[int, int]
Input = tuple[Move, ...]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return tuple(
            MOVES[m]
            for move, amount in (line.split() for line in input_data)
            for m in itertools.repeat(move, int(amount))
        )

    def solve(self, moves: tuple[Move, ...], size: int) -> int:
        def _move_rope(rope: list[Position], move: Move) -> list[Position]:
            def _catchup(head: Position, tail: Position) -> Position:
                dx = head[0] - tail[0]
                dy = head[1] - tail[1]
                if abs(dx) > 1 or abs(dy) > 1:
                    return (
                        tail[0] + (-1 if dx < 0 else 1 if dx > 0 else 0),
                        tail[1] + (-1 if dy < 0 else 1 if dy > 0 else 0),
                    )
                return tail

            rope[0] = (rope[0][0] + move[0], rope[0][1] + move[1])
            for i in range(1, len(rope)):
                rope[i] = _catchup(rope[i - 1], rope[i])
            return rope

        rope = [(0, 0)] * size
        seen = {(rope := _move_rope(rope, move))[-1] for move in moves}
        return len(seen)

    def part_1(self, moves: Input) -> Output1:
        return self.solve(moves, size=2)

    def part_2(self, moves: Input) -> Output2:
        return self.solve(moves, size=10)

    @aoc_samples(
        (
            ("part_1", TEST1, 13),
            ("part_2", TEST1, 1),
            ("part_2", TEST2, 36),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 9)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2022 Day 14
#


import itertools
import sys
from dataclasses import dataclass
from typing import Self

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9
"""

Position = tuple[int, int]
SOURCE: Position = (500, 0)


@dataclass(frozen=True)
class Cave:
    rocks: set[Position]
    max_x: int
    max_y: int

    @classmethod
    def from_inputs(cls, inputs: InputData) -> Self:
        rocks = set[Position]()
        max_x = 0
        max_y = 0
        for line in inputs:
            c = [
                list(map(int, p.split(",")))
                for p in line.strip().split(" -> ")
            ]
            for (x_1, y_1), (x_2, y_2) in itertools.pairwise(c):
                x1, x2 = sorted([x_1, x_2])
                y1, y2 = sorted([y_1, y_2])
                for x in range(x1, x2 + 1):
                    for y in range(y1, y2 + 1):
                        rocks.add((x, y))
                        max_x = max(max_x, x)
                        max_y = max(max_y, y)
        return cls(rocks, max_x, max_y)

    def get_occupied(self) -> list[list[bool]]:
        occupied = [
            [False] * (self.max_x + 150) for _ in range(self.max_y + 2)
        ]
        for x, y in self.rocks:
            occupied[y][x] = True
        return occupied


Input = Cave
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Cave.from_inputs(input_data)

    def solve(self, occupied: list[list[bool]], max_y: int) -> int:  # noqa:C901
        def drop() -> Position | None:
            curr = SOURCE
            while True:
                p = curr
                curr_x, curr_y = curr
                for dx, dy in [(0, 1), (-1, 1), (1, 1)]:
                    try:
                        if not occupied[curr_y + dy][curr_x + dx]:
                            curr = (curr_x + dx, curr_y + dy)
                            break
                    except IndexError:
                        pass
                if curr == p:
                    return curr
                if curr[1] > max_y:
                    return None

        cnt = 0
        while True:
            p = drop()
            if p is None:
                break
            occupied[p[1]][p[0]] = True
            cnt += 1
            if p == SOURCE:
                break
        return cnt

    def part_1(self, cave: Input) -> Output1:
        return self.solve(cave.get_occupied(), cave.max_y)

    def part_2(self, cave: Input) -> Output2:
        return self.solve(cave.get_occupied(), cave.max_y + 2)

    @aoc_samples(
        (
            ("part_1", TEST, 24),
            ("part_2", TEST, 93),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 14)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

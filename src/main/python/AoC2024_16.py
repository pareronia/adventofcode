#! /usr/bin/env python3
#
# Advent of Code 2024 Day 16
#

from __future__ import annotations

import itertools
import sys
from typing import Iterator
from typing import NamedTuple
from typing import Self

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.graph import Dijkstra
from aoc.grid import Cell

IDX_TO_DIR = {
    0: Direction.UP,
    1: Direction.RIGHT,
    2: Direction.DOWN,
    3: Direction.LEFT,
}
DIR_TO_IDX = {
    Direction.UP: 0,
    Direction.RIGHT: 1,
    Direction.DOWN: 2,
    Direction.LEFT: 3,
}
TURNS = {
    Direction.UP: [Direction.LEFT, Direction.RIGHT],
    Direction.RIGHT: [Direction.UP, Direction.DOWN],
    Direction.DOWN: [Direction.LEFT, Direction.RIGHT],
    Direction.LEFT: [Direction.UP, Direction.DOWN],
}


TEST1 = """\
###############
#.......#....E#
#.#.###.#.###.#
#.....#.#...#.#
#.###.#####.#.#
#.#.#.......#.#
#.#.#####.###.#
#...........#.#
###.#.#####.#.#
#...#.....#.#.#
#.#.#.###.#.#.#
#.....#...#.#.#
#.###.#.#.#.#.#
#S..#.....#...#
###############
"""
TEST2 = """\
#################
#...#...#...#..E#
#.#.#.#.#.#.#.#.#
#.#.#.#...#...#.#
#.#.#.#.###.#.#.#
#...#.#.#.....#.#
#.#.#.#.#.#####.#
#.#...#.#.#.....#
#.#.#####.#.###.#
#.#.#.......#...#
#.#.###.#####.###
#.#.#...#.....#.#
#.#.#.#####.###.#
#.#.#.........#.#
#.#.#.#########.#
#S#.............#
#################
"""


class ReindeerMaze(NamedTuple):
    grid: list[list[str]]
    start: Cell
    end: Cell

    @classmethod
    def from_grid(cls, strings: list[str]) -> Self:
        grid = [[ch for ch in line] for line in strings]
        start = Cell(len(grid) - 2, 1)
        end = Cell(1, len(grid[0]) - 2)
        return cls(grid, start, end)

    def adjacent(self, state: State) -> Iterator[State]:
        r, c, dir = state
        direction = IDX_TO_DIR[dir]
        states = (
            (r - d.y, c + d.x, DIR_TO_IDX[d])
            for d in [direction] + TURNS[direction]
        )
        for state in states:
            if self.grid[state[0]][state[1]] != "#":
                yield state


Input = ReindeerMaze
Output1 = int
Output2 = int
State = tuple[int, int, int]


class Solution(SolutionBase[Input, Output1, Output2]):

    def parse_input(self, input_data: InputData) -> Input:
        return ReindeerMaze.from_grid(list(input_data))

    def part_1(self, maze: Input) -> Output1:
        return Dijkstra.distance(
            (maze.start.row, maze.start.col, DIR_TO_IDX[Direction.RIGHT]),
            lambda state: (state[0], state[1]) == (maze.end.row, maze.end.col),
            lambda state: maze.adjacent(state),
            lambda curr, nxt: 1 if curr[2] == nxt[2] else 1001,
        )

    def part_2(self, maze: Input) -> Output2:
        result = Dijkstra.all(
            (maze.start.row, maze.start.col, DIR_TO_IDX[Direction.RIGHT]),
            lambda state: (state[0], state[1]) == (maze.end.row, maze.end.col),
            lambda state: maze.adjacent(state),
            lambda curr, nxt: 1 if curr[2] == nxt[2] else 1001,
        )
        return len(
            {
                (state[0], state[1])
                for end, dir in itertools.product(
                    [maze.end], Direction.capitals()
                )
                for paths in result.get_paths(
                    (end.row, end.col, DIR_TO_IDX[dir])
                )
                for state in paths
            }
        )

    @aoc_samples(
        (
            ("part_1", TEST1, 7036),
            ("part_1", TEST2, 11048),
            ("part_2", TEST1, 45),
            ("part_2", TEST2, 64),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 16)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

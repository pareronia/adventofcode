#! /usr/bin/env python3
#
# Advent of Code 2024 Day 16
#

from __future__ import annotations

import itertools
import sys
from collections import defaultdict
from queue import PriorityQueue
from typing import Iterator
from typing import NamedTuple
from typing import Self

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.grid import Cell
from aoc.grid import CharGrid

Input = CharGrid
Output1 = int
Output2 = int
State = tuple[int, int, int]

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
    Direction.UP: {Direction.LEFT, Direction.RIGHT},
    Direction.RIGHT: {Direction.UP, Direction.DOWN},
    Direction.DOWN: {Direction.LEFT, Direction.RIGHT},
    Direction.LEFT: {Direction.UP, Direction.DOWN},
}
START_DIR = Direction.RIGHT
FORWARD, BACKWARD = 1, -1


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


class Solution(SolutionBase[Input, Output1, Output2]):
    class ReindeerMaze(NamedTuple):
        grid: CharGrid
        start: Cell
        end: Cell

        @classmethod
        def from_grid(cls, grid: CharGrid) -> Self:
            for cell in grid.get_cells():
                val = grid.get_value(cell)
                if val == "S":
                    start = cell
                if val == "E":
                    end = cell
            return cls(grid, start, end)

        def adjacent(self, state: State, mode: int) -> Iterator[State]:
            r, c, dir = state
            direction = IDX_TO_DIR[dir]
            for d in TURNS[direction]:
                yield (r, c, DIR_TO_IDX[d])
            nr, nc = r - mode * direction.y, c + mode * direction.x
            if self.grid.values[nr][nc] != "#":
                yield (nr, nc, dir)

        def dijkstra(self, starts: set[State], mode: int) -> dict[State, int]:
            q: PriorityQueue[tuple[int, State]] = PriorityQueue()
            for s in starts:
                q.put((0, s))
            dists: defaultdict[State, int] = defaultdict(lambda: sys.maxsize)
            for s in starts:
                dists[s] = 0
            while not q.empty():
                dist, node = q.get()
                curr_dist = dists[node]
                for n in self.adjacent(node, mode):
                    new_dist = curr_dist + (1 if node[2] == n[2] else 1000)
                    if new_dist < dists[n]:
                        dists[n] = new_dist
                        q.put((new_dist, n))
            return dists

        def forward_distances(self) -> tuple[dict[State, int], int]:
            starts = {(self.start.row, self.start.col, DIR_TO_IDX[START_DIR])}
            distances = self.dijkstra(starts, FORWARD)
            best = next(
                v
                for k, v in distances.items()
                if (k[0], k[1]) == (self.end.row, self.end.col)
            )
            return distances, best

        def backward_distances(self) -> dict[State, int]:
            starts = itertools.product(
                [self.end],
                (DIR_TO_IDX[dir] for dir in Direction.capitals()),
            )
            return self.dijkstra(
                set((s[0].row, s[0].col, s[1]) for s in starts), BACKWARD
            )

    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings(list(input_data))

    def part_1(self, grid: Input) -> Output1:
        maze = Solution.ReindeerMaze.from_grid(grid)
        _, best = maze.forward_distances()
        return best

    def part_2(self, grid: Input) -> Output2:
        maze = Solution.ReindeerMaze.from_grid(grid)
        forward_distances, best = maze.forward_distances()
        backward_distances = maze.backward_distances()
        all_tile_states = itertools.product(
            grid.find_all_matching(lambda cell: grid.get_value(cell) != "#"),
            (DIR_TO_IDX[dir] for dir in Direction.capitals()),
        )
        best_tiles = {
            cell
            for cell, dir in all_tile_states
            if forward_distances[(cell.row, cell.col, dir)]
            + backward_distances[(cell.row, cell.col, dir)]
            == best
        }
        return len(best_tiles)

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

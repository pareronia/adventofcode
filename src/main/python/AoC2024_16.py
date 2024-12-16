#! /usr/bin/env python3
#
# Advent of Code 2024 Day 16
#

from __future__ import annotations

import itertools
import sys
from collections import defaultdict
from queue import PriorityQueue
from typing import Callable
from typing import Iterator
from typing import NamedTuple
from typing import Self

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.geometry import Turn
from aoc.grid import Cell
from aoc.grid import CharGrid

Input = CharGrid
Output1 = int
Output2 = int
State = tuple[Cell, str]

DIRS = {"U", "R", "D", "L"}
START_DIR = "R"


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

        def get_turns(self, direction: Direction) -> Iterator[str]:
            for turn in (Turn.LEFT, Turn.RIGHT):
                new_letter = direction.turn(turn).letter
                assert new_letter is not None
                yield new_letter

        def adjacent_forward(self, state: State) -> Iterator[State]:
            cell, letter = state
            direction = Direction.from_str(letter)
            for d in self.get_turns(direction):
                yield (cell, d)
            nxt = cell.at(direction)
            if self.grid.get_value(nxt) != "#":
                yield (nxt, letter)

        def adjacent_backward(self, state: State) -> Iterator[State]:
            cell, letter = state
            direction = Direction.from_str(letter)
            for d in self.get_turns(direction):
                yield (cell, d)
            nxt = cell.at(direction.turn(Turn.AROUND))
            if self.grid.get_value(nxt) != "#":
                yield (nxt, letter)

        def dijkstra(
            self,
            starts: set[State],
            is_end: Callable[[State], bool],
            adjacent: Callable[[State], Iterator[State]],
            get_distance: Callable[[State, State], int],
        ) -> dict[State, int]:
            q: PriorityQueue[tuple[int, State]] = PriorityQueue()
            for s in starts:
                q.put((0, s))
            dists: defaultdict[State, int] = defaultdict(lambda: sys.maxsize)
            for s in starts:
                dists[s] = 0
            while not q.empty():
                dist, node = q.get()
                curr_dist = dists[node]
                for n in adjacent(node):
                    new_dist = curr_dist + get_distance(node, n)
                    if new_dist < dists[n]:
                        dists[n] = new_dist
                        q.put((new_dist, n))
            return dists

        def forward_distances(self) -> dict[State, int]:
            return self.dijkstra(
                {(self.start, START_DIR)},
                lambda node: node[0] == self.end,
                self.adjacent_forward,
                lambda curr, nxt: 1 if curr[1] == nxt[1] else 1000,
            )

        def backward_distances(self) -> dict[State, int]:
            return self.dijkstra(
                {_ for _ in itertools.product([self.end], DIRS)},
                lambda node: node[0] == self.start,
                self.adjacent_backward,
                lambda curr, nxt: 1 if curr[1] == nxt[1] else 1000,
            )

    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings(list(input_data))

    def part_1(self, grid: Input) -> Output1:
        maze = Solution.ReindeerMaze.from_grid(grid)
        distances = maze.forward_distances()
        return next(v for k, v in distances.items() if k[0] == maze.end)

    def part_2(self, grid: Input) -> Output2:
        maze = Solution.ReindeerMaze.from_grid(grid)
        forw_dists = maze.forward_distances()
        best = next(v for k, v in forw_dists.items() if k[0] == maze.end)
        backw_dists = maze.backward_distances()
        all_tile_states = itertools.product(
            grid.find_all_matching(lambda cell: grid.get_value(cell) != "#"),
            DIRS,
        )
        best_tiles = {
            cell
            for cell, dir in all_tile_states
            if forw_dists[(cell, dir)] + backw_dists[(cell, dir)] == best
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

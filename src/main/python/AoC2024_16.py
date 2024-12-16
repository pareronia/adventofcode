#! /usr/bin/env python3
#
# Advent of Code 2024 Day 16
#

import sys
from collections import defaultdict
from queue import PriorityQueue
from typing import Callable
from typing import Iterator
from typing import TypeVar

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.geometry import Turn

# from aoc.graph import a_star
from aoc.grid import Cell
from aoc.grid import CharGrid

T = TypeVar("T")

Input = CharGrid
Output1 = int
Output2 = int


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


def dijkstra(
    starts: set[T],
    is_end: Callable[[T], bool],
    adjacent: Callable[[T], Iterator[T]],
    get_cost: Callable[[T, T], int],
) -> dict[T, int]:
    q: PriorityQueue[tuple[int, T]] = PriorityQueue()
    for s in starts:
        q.put((0, s))
    best: defaultdict[T, int] = defaultdict(lambda: sys.maxsize)
    for s in starts:
        best[s] = 0
    while not q.empty():
        cost, node = q.get()
        c_total = best[node]
        for n in adjacent(node):
            new_risk = c_total + get_cost(node, n)
            if new_risk < best[n]:
                best[n] = new_risk
                q.put((new_risk, n))
    return best


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings(list(input_data))

    def part_1(self, grid: Input) -> Output1:
        def adjacent(state: tuple[Cell, str]) -> Iterator[tuple[Cell, str]]:
            cell, letter = state
            dir = Direction.from_str(letter)
            for turn in (Turn.LEFT, Turn.RIGHT):
                new_letter = dir.turn(turn).letter
                assert new_letter is not None
                yield (cell, new_letter)
            nxt = cell.at(dir)
            if grid.get_value(nxt) != "#":
                yield (cell.at(dir), letter)

        def cost(curr: tuple[Cell, str], next: tuple[Cell, str]) -> int:
            if curr[1] == next[1]:
                return 1
            else:
                return 1000

        start = next(grid.get_all_equal_to("S"))
        end = next(grid.get_all_equal_to("E"))
        distance = dijkstra(
            {(start, "R")},
            lambda node: node[0] == end,
            adjacent,
            cost,
        )
        key = next(k for k in distance.keys() if k[0] == end)
        return distance[key]

    def part_2(self, grid: Input) -> Output2:
        def adjacent_1(state: tuple[Cell, str]) -> Iterator[tuple[Cell, str]]:
            cell, letter = state
            dir = Direction.from_str(letter)
            for turn in (Turn.LEFT, Turn.RIGHT):
                new_letter = dir.turn(turn).letter
                assert new_letter is not None
                yield (cell, new_letter)
            nxt = cell.at(dir)
            if grid.get_value(nxt) != "#":
                yield (nxt, letter)

        def adjacent_2(state: tuple[Cell, str]) -> Iterator[tuple[Cell, str]]:
            cell, letter = state
            dir = Direction.from_str(letter)
            for turn in (Turn.LEFT, Turn.RIGHT):
                new_letter = dir.turn(turn).letter
                assert new_letter is not None
                yield (cell, new_letter)
            nxt = cell.at(dir.turn(Turn.AROUND))
            if grid.get_value(nxt) != "#":
                yield (nxt, letter)

        def cost(curr: tuple[Cell, str], next: tuple[Cell, str]) -> int:
            if curr[1] == next[1]:
                return 1
            else:
                return 1000

        start = next(grid.get_all_equal_to("S"))
        end = next(grid.get_all_equal_to("E"))
        distance_1 = dijkstra(
            {(start, "R")},
            lambda node: node[0] == end,
            adjacent_1,
            cost,
        )
        key = next(k for k in distance_1.keys() if k[0] == end)
        best = distance_1[key]
        distance_2 = dijkstra(
            {(end, "U"), (end, "R"), (end, "D"), (end, "L")},
            lambda node: node[0] == start,
            adjacent_2,
            cost,
        )
        ans = set[Cell]([end])
        for cell in grid.find_all_matching(
            lambda cell: grid.get_value(cell) != "#"
        ):
            for dir in ("U", "R", "D", "L"):
                if (
                    (cell, dir) in distance_1
                    and (cell, dir) in distance_2
                    and distance_1[(cell, dir)] + distance_2[(cell, dir)]
                    == best
                ):
                    ans.add(cell)
        return len(ans)

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

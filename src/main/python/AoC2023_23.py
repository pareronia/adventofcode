#! /usr/bin/env python3
#
# Advent of Code 2023 Day 23
#

import sys
from collections import defaultdict
from collections import deque

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.grid import Cell
from aoc.grid import CharGrid

Input = CharGrid
Output1 = int
Output2 = int
Edge = tuple[Cell, int]


TEST = """\
#.#####################
#.......#########...###
#######.#########.#.###
###.....#.>.>.###.#.###
###v#####.#v#.###.#.###
###.>...#.#.#.....#...#
###v###.#.#.#########.#
###...#.#.#.......#...#
#####.#.#.#######.#.###
#.....#.#.#.......#...#
#.#####.#.#.#########v#
#.#...#...#...###...>.#
#.#.#v#######v###.###v#
#...#.>.#...>.>.#.###.#
#####v#.#.###v#.#.###.#
#.....#...#...#.#.#...#
#.#########.###.#.#.###
#...###...#...#...#.###
###.###.#.###v#####v###
#...#...#.#.>.>.#.>.###
#.###.###.#.###.#.#v###
#.....###...###...#...#
#####################.#
"""


class PathFinder:
    def __init__(self, grid: CharGrid) -> None:
        self.grid = grid
        self.start = Cell(0, 1)
        self.end = Cell(grid.get_height() - 1, grid.get_width() - 2)

    def find_longest_hike_length_with_only_downward_slopes(self) -> int:
        forks = self._find_forks()
        graph = self._build_graph(forks, downward_slope_only=True)
        return self._find_longest(graph, self.start, set[Cell]())

    def find_longest_hike_length(self) -> int:
        forks = self._find_forks()
        graph = self._build_graph(forks, downward_slope_only=False)
        return self._find_longest(graph, self.start, set[Cell]())

    def _find_forks(self) -> set[Cell]:
        def is_fork(cell: Cell) -> bool:
            return (
                cell == self.start
                or cell == self.end
                or self.grid.get_value(cell) != "#"
                and sum(
                    1
                    for n in self.grid.get_capital_neighbours(cell)
                    if self.grid.get_value(n) != "#"
                )
                > 2
            )

        return {cell for cell in self.grid.get_cells() if is_fork(cell)}

    def _build_graph(
        self, forks: set[Cell], downward_slope_only: bool
    ) -> dict[Cell, set[Edge]]:
        graph = defaultdict[Cell, set[Edge]](set)
        for fork in forks:
            q = deque([(fork, 0)])
            seen = set[Cell]({fork})
            while len(q) > 0:
                cell, distance = q.popleft()
                if cell in forks and cell != fork:
                    graph[fork].add((cell, distance))
                    continue
                for d in Direction.capitals():
                    n = cell.at(d)
                    if not self.grid.is_in_bounds(n):
                        continue
                    val = self.grid.get_value(n)
                    if val == "#" or (
                        downward_slope_only
                        and val in {"^", ">", "v", "<"}
                        and Direction.from_str(val) != d
                    ):
                        continue
                    if n not in seen:
                        seen.add(n)
                        q.append((n, distance + 1))
        return graph

    def _find_longest(
        self, graph: dict[Cell, set[Edge]], curr: Cell, seen: set[Cell]
    ) -> int:
        if curr == self.end:
            return 0
        ans = -1_000_000_000
        seen.add(curr)
        for vertex, distance in graph[curr]:
            if vertex in seen:
                continue
            ans = max(ans, distance + self._find_longest(graph, vertex, seen))
        seen.remove(curr)
        return ans


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings([line for line in input_data])

    def part_1(self, grid: Input) -> Output1:
        return PathFinder(
            grid
        ).find_longest_hike_length_with_only_downward_slopes()

    def part_2(self, grid: Input) -> Output2:
        return PathFinder(grid).find_longest_hike_length()

    @aoc_samples(
        (
            ("part_1", TEST, 94),
            ("part_2", TEST, 154),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 23)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

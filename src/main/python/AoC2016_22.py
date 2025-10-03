#! /usr/bin/env python3
#
# Advent of Code 2016 Day 22
#

from __future__ import annotations

import sys
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Position

TEST = """\
root@ebhq-gridcenter# df -h
Filesystem            Size  Used  Avail  Use%
/dev/grid/node-x0-y0   10T    8T     2T   80%
/dev/grid/node-x0-y1   11T    6T     5T   54%
/dev/grid/node-x0-y2   32T   28T     4T   87%
/dev/grid/node-x1-y0    9T    7T     2T   77%
/dev/grid/node-x1-y1    8T    0T     8T    0%
/dev/grid/node-x1-y2   11T    7T     4T   63%
/dev/grid/node-x2-y0   10T    6T     4T   60%
/dev/grid/node-x2-y1    9T    8T     1T   88%
/dev/grid/node-x2-y2    9T    6T     3T   66%
"""


class Node(NamedTuple):
    x: int
    y: int
    used: int
    free: int

    @classmethod
    def from_input(cls, line: str) -> Node:
        fs, size, used, avail, pct = line.split()
        x, y = fs.split("-")[-2:]
        return Node(int(x[1:]), int(y[1:]), int(used[:-1]), int(avail[:-1]))

    def __eq__(self, other: object) -> bool:
        if other is None or type(other) is not Node:
            return False
        return self.x == other.x and self.y == other.y

    def is_not_empty(self) -> bool:
        return self.used > 0


class Cluster(NamedTuple):
    nodes: list[Node]

    @classmethod
    def from_input(cls, input: InputData) -> Cluster:
        return Cluster([Node.from_input(line) for line in list(input)[2:]])

    def get_unusable_nodes(self) -> set[Node]:
        max_free = max(self.nodes, key=lambda n: n.free).free
        return {n for n in self.nodes if n.used > max_free}

    def get_max_x(self) -> int:
        return max(self.nodes, key=lambda n: n.x).x

    def get_empty_node(self) -> Node:
        empty_nodes = [n for n in self.nodes if n.used == 0]
        assert len(empty_nodes) == 1, "Expected 1 empty node"
        return empty_nodes[0]

    def get_goal_node(self) -> Node:
        return [n for n in self.nodes if n.x == self.get_max_x() and n.y == 0][
            0
        ]


Input = Cluster
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, inputs: InputData) -> Input:
        return Cluster.from_input(inputs)

    def part_1(self, cluster: Input) -> int:
        return sum(
            1
            for b in cluster.nodes
            for a in filter(lambda a: a.is_not_empty(), cluster.nodes)
            if a != b and a.used <= b.free
        )

    def part_2(self, cluster: Input) -> int:
        unusable = cluster.get_unusable_nodes()
        hole_ys = {n.y for n in unusable}
        assert len(hole_ys) == 1, "Expected all unusable nodes in 1 row"
        hole_y = next(_ for _ in hole_ys)
        if hole_y <= 1:
            raise RuntimeError("Unsolvable")
        assert not (
            max(unusable, key=lambda n: n.x).x != cluster.get_max_x()
        ), "Expected unusable row to touch side"
        hole_x = min(unusable, key=lambda n: n.x).x
        hole = Position(hole_x - 1, hole_y)
        empty_node = cluster.get_empty_node()
        d1 = Position(empty_node.x, empty_node.y).manhattan_distance(hole)
        goal_node = cluster.get_goal_node()
        d2 = Position(goal_node.x - 1, goal_node.y).manhattan_distance(hole)
        d3 = 5 * (goal_node.x - 1)
        return d1 + d2 + d3 + 1

    @aoc_samples((("part_1", TEST, 7),))
    def samples(self) -> None:
        pass


solution = Solution(2016, 22)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

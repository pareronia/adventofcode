#! /usr/bin/env python3
#
# Advent of Code 2025 Day 8
#

import sys
from math import prod

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.geometry3d import Position3D

TEST = """\
162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689
"""

Input = tuple[list[Position3D], list[tuple[int, int, int]]]
Output1 = int
Output2 = int


class DSU:
    def __init__(self, size: int) -> None:
        self.ids = list(range(size))
        self.sz = [1 for _ in range(size)]
        self.num_components = size

    def find(self, p: int) -> int:
        if self.ids[p] != p:
            self.ids[p] = self.find(self.ids[p])
        return self.ids[p]

    def unify(self, p: int, q: int) -> None:
        root_p, root_q = self.find(p), self.find(q)
        if root_p != root_q:
            if self.sz[root_p] < self.sz[root_q]:
                self.sz[root_q] += self.sz[root_p]
                self.ids[root_p] = root_q
                self.sz[root_p] = 0
            else:
                self.sz[root_p] += self.sz[root_q]
                self.ids[root_q] = root_p
                self.sz[root_q] = 0
            self.num_components -= 1


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        boxes = [
            Position3D.of(int(x), int(y), int(z))
            for x, y, z in (line.split(",") for line in input_data)
        ]
        pairs = sorted(
            (
                (i, j, boxes[i].squared_distance(boxes[j]))
                for i in range(len(boxes))
                for j in range(i + 1, len(boxes))
            ),
            key=lambda pair: pair[2],
        )
        return boxes, pairs

    def solve_1(self, inputs: Input, num_pairs: int = 1000) -> int:
        boxes, pairs = inputs
        dsu = DSU(len(boxes))
        for pair in pairs[:num_pairs]:
            dsu.unify(pair[0], pair[1])
        return prod(sorted(dsu.sz)[-3:])

    def part_1(self, pairs: Input) -> Output1:
        return self.solve_1(pairs)

    def part_2(self, inputs: Input) -> Output2:
        boxes, pairs = inputs
        dsu = DSU(len(boxes))
        for pair in pairs:
            dsu.unify(pair[0], pair[1])
            if dsu.num_components == 1 and dsu.sz[dsu.find(0)] == len(boxes):
                return boxes[pair[0]].x * boxes[pair[1]].x
        raise AssertionError

    def samples(self) -> None:
        inputs = self.parse_input(TEST.splitlines())
        assert self.solve_1(inputs, 10) == 40
        assert self.part_2(inputs) == 25272


solution = Solution(2025, 8)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

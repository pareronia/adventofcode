#! /usr/bin/env python3
#
# Advent of Code 2025 Day 8
#

import itertools
import sys
from collections import defaultdict
from collections import deque
from collections.abc import Callable
from collections.abc import Iterator
from functools import cmp_to_key
from math import prod

from aoc.common import InputData
from aoc.common import SolutionBase

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

Box = tuple[int, int, int]
Input = tuple[int, list[tuple[Box, Box]]]
Output1 = int
Output2 = int


def connected_components(
    nodes: set[Box], adjacent: Callable[[Box], Iterator[Box]]
) -> list[set[Box]]:
    components = list[set[Box]]()
    todo = set(nodes)
    while len(todo) != 0:
        node = todo.pop()
        component = {node}
        q = deque[Box]([node])
        while len(q) != 0:
            node = q.popleft()
            for n in adjacent(node):
                if n not in component:
                    q.append(n)
                component.add(n)
        components.append(component)
    return components


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        def cmp(p1: tuple[Box, Box], p2: tuple[Box, Box]) -> int:
            def distance(a: Box, b: Box) -> float:
                x1, y1, z1 = a
                x2, y2, z2 = b
                dx, dy, dz = x1 - x2, y1 - y2, z1 - z2
                return dx * dx + dy * dy + dz * dz

            dp1 = distance(p1[0], p1[1])
            dp2 = distance(p2[0], p2[1])
            if dp1 > dp2:
                return 1
            if dp1 < dp2:
                return -1
            return 0

        boxes = list[Box]()
        for line in input_data:
            x, y, z = map(int, line.split(","))
            boxes.append((x, y, z))
        return len(boxes), sorted(
            itertools.combinations(boxes, 2), key=cmp_to_key(cmp)
        )

    def get_components(
        self, edges: dict[Box, set[Box]]
    ) -> set[frozenset[Box]]:
        return {
            frozenset(c)
            for c in connected_components(
                set(edges.keys()), lambda n: (nxt for nxt in edges[n])
            )
        }

    def solve_1(self, inputs: Input, num_pairs: int) -> int:
        _, pairs = inputs
        edges = defaultdict[Box, set[Box]](set)
        for i in range(num_pairs):
            a, b = pairs[i]
            edges[a].add(b)
            edges[b].add(a)
        components = self.get_components(edges)
        best = sorted(components, key=len, reverse=True)
        return prod(len(c) for c in best[:3])

    def part_1(self, pairs: Input) -> Output1:
        return self.solve_1(pairs, 1000)

    def solve_2(self, inputs: Input, start: int) -> int:
        num_boxes, pairs = inputs
        edges = defaultdict[Box, set[Box]](set)
        edges = defaultdict[Box, set[Box]](set)
        for i in range(start):
            a, b = pairs[i]
            edges[a].add(b)
            edges[b].add(a)
        components = self.get_components(edges)
        while i < len(pairs) - 1:
            i += 1
            a, b = pairs[i]
            for c in components:
                if a in c and b in c:
                    break
            else:
                edges[a].add(b)
                edges[b].add(a)
                components = self.get_components(edges)
                if (
                    len(components) == 1
                    and len(next(_ for _ in components)) == num_boxes
                ):
                    return pairs[i][0][0] * pairs[i][1][0]
        raise AssertionError

    def part_2(self, pairs: Input) -> Output2:
        return self.solve_2(pairs, 1000)

    def samples(self) -> None:
        pairs = self.parse_input(TEST.splitlines())
        assert self.solve_1(pairs, 10) == 40
        assert self.solve_2(pairs, 10) == 25272


solution = Solution(2025, 8)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

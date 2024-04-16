#! /usr/bin/env python3
#
# Advent of Code 2023 Day 25
#

from __future__ import annotations

import random
import sys
from collections import defaultdict
from copy import deepcopy
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.graph import flood_fill

TEST = """\
jqt: rhn xhk nvd
rsh: frs pzl lsr
xhk: hfx
cmg: qnr nvd lhk bvb
rhn: xhk bvb hfx
bvb: xhk hfx
pzl: lsr hfx nvd
qnr: nvd
ntq: jqt hfx bvb xhk
nvd: lhk
lsr: lhk
rzs: qnr cmg lsr rsh
frs: qnr lhk lsr
"""


class Graph(NamedTuple):
    edges: dict[str, list[str]]

    @classmethod
    def from_input(cls, input: InputData) -> Graph:
        edges = defaultdict[str, list[str]](list)
        for line in input:
            key, values = line.split(": ")
            edges[key] += [_ for _ in values.split()]
            for v in values.split():
                edges[v].append(key)
        return Graph(edges)

    def combine_nodes(self, node_1: str, node_2: str, new_node: str) -> Graph:
        self.edges[new_node] = [
            d for d in self.edges[node_1] if d != node_2
        ] + [d for d in self.edges[node_2] if d != node_1]
        for node in {node_1, node_2}:
            for dst in self.edges[node]:
                self.edges[dst] = [
                    new_node if d == node else d for d in self.edges[dst]
                ]
            del self.edges[node]
        return Graph(self.edges)

    def get_connected(self, node: str) -> set[str]:
        return flood_fill(node, lambda n: (_ for _ in self.edges[n]))


Input = Graph
Output1 = int
Output2 = str


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Graph.from_input(input_data)

    def part_1(self, graph: Input) -> Output1:
        while True:
            g = deepcopy(graph)
            counts = {node: 1 for node in g.edges.keys()}
            while len(g.edges) > 2:
                a = random.sample(list(g.edges.keys()), 1)[0]
                b = random.sample(list(g.edges[a]), 1)[0]
                new_node = f"{a}-{b}"
                counts[new_node] = counts.get(a, 0) + counts.get(b, 0)
                del counts[a]
                del counts[b]
                g = g.combine_nodes(a, b, new_node)
            a, b = g.edges.keys()
            if len(g.edges[a]) == 3:
                return counts[a] * counts[b]

    def part_2(self, input: Input) -> Output2:
        return "🎄"

    @aoc_samples((("part_1", TEST, 54),))
    def samples(self) -> None:
        pass


solution = Solution(2023, 25)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

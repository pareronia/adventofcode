#! /usr/bin/env python3
#
# Advent of Code 2023 Day 25
#

from __future__ import annotations

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
    edges: dict[str, set[str]]

    @classmethod
    def from_input(cls, input: InputData) -> Graph:
        edges = defaultdict[str, set[str]](set)
        for line in input:
            key, values = line.split(": ")
            edges[key] |= {_ for _ in values.split()}
            for v in values.split():
                edges[v].add(key)
        return Graph(edges)

    def remove_edges(self, edges: list[tuple[str, str]]) -> Graph:
        new_edges = deepcopy(self.edges)
        for edge in edges:
            first, second = edge
            new_edges[first].remove(second)
            new_edges[second].remove(first)
        return Graph(new_edges)

    def get_connected(self, node: str) -> set[str]:
        return flood_fill(node, lambda n: (_ for _ in self.edges[n]))

    def visualize(self, file_name: str) -> None:
        with open(file_name, "w") as f:
            print("Graph {", file=f)
            for k, v in self.edges.items():
                for vv in v:
                    print(f"{k} -- {vv}", file=f)
            print("}", file=f)


Input = Graph
Output1 = int
Output2 = str


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Graph.from_input(input_data)

    def solve_1(
        self, graph: Input, disconnects: list[tuple[str, str]]
    ) -> Output1:
        g = graph.remove_edges(disconnects)
        first = g.get_connected(disconnects[0][0])
        second = g.get_connected(disconnects[0][1])
        return len(first) * len(second)

    def sample_1(self, graph: Input) -> Output1:
        return self.solve_1(
            graph, [("hfx", "pzl"), ("bvb", "cmg"), ("nvd", "jqt")]
        )

    def part_1(self, graph: Input) -> Output1:
        # visualize with graphviz/neato:
        # graph.visualize("/mnt/c/temp/graph.dot")
        # -> to cut: ssd--xqh, nrs--khn, qlc--mqb
        return self.solve_1(
            graph, [("ssd", "xqh"), ("nrs", "khn"), ("qlc", "mqb")]
        )

    def part_2(self, input: Input) -> Output2:
        return "🎄"

    @aoc_samples((("sample_1", TEST, 54),))
    def samples(self) -> None:
        pass


solution = Solution(2023, 25)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

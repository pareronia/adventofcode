#! /usr/bin/env python3
#
# Advent of Code 2025 Day 11
#

import itertools
import sys
from dataclasses import dataclass
from functools import cache
from math import prod
from pathlib import Path
from typing import Self

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST1 = """\
aaa: you hhh
you: bbb ccc
bbb: ddd eee
ccc: ddd eee fff
ddd: ggg
eee: out
fff: out
ggg: out
hhh: ccc fff iii
iii: out
"""
TEST2 = """\
svr: aaa bbb
aaa: fft
fft: ccc
bbb: tty
tty: ccc
ccc: ddd eee
ddd: hub
hub: fff
eee: dac
dac: fff
fff: ggg hhh
ggg: out
hhh: out
"""


@dataclass(frozen=True)
class Graph:
    edges: dict[str, set[str]]

    @classmethod
    def from_input(cls, inputs: InputData) -> Self:
        return cls(
            {
                from_[:-1]: set(to)
                for from_, *to in (line.split() for line in inputs)
            }
        )

    def count_all_paths_along(self, along: list[str]) -> int:
        def count_all_paths(start: str, end: str) -> int:
            @cache
            def dfs(node: str) -> int:
                if node == end:
                    return 1
                return sum(dfs(nxt) for nxt in self.edges.get(node, set()))

            return dfs(start)

        return prod(
            count_all_paths(n1, n2) for n1, n2 in itertools.pairwise(along)
        )

    def output_graph(self) -> None:
        with Path("~/tmp/2025_11.dot").open("w") as f:
            f.write("digraph G {\n")
            f.write('fft [style="filled" fillcolor=red]\n')
            f.write('dac [style="filled" fillcolor=red]\n')
            for k, vv in self.edges.items():
                for v in vv:  # noqa:FURB122
                    f.write(f"{k} -> {v}\n")
            f.write("}\n")


Input = Graph
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Graph.from_input(input_data)

    def part_1(self, graph: Graph) -> Output1:
        return graph.count_all_paths_along(["you", "out"])

    def part_2(self, graph: Graph) -> Output2:
        return graph.count_all_paths_along(
            ["svr", "dac", "fft", "out"]
        ) + graph.count_all_paths_along(["svr", "fft", "dac", "out"])

    @aoc_samples(
        (
            ("part_1", TEST1, 5),
            ("part_2", TEST2, 2),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2025, 11)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

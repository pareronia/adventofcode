#! /usr/bin/env python3
#
# Advent of Code 2025 Day 11
#

import sys
from collections import defaultdict
from collections import deque
from functools import cache
from pathlib import Path

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = InputData
Output1 = int
Output2 = int


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


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, inputs: Input) -> Output1:
        edges = defaultdict[str, set[str]](set)
        for line in inputs:
            from_, *to = line.split()
            edges[from_[:-1]] |= set(to)
        paths = set[tuple[str, ...]]()
        q = deque([["you"]])
        while q:
            path = q.pop()
            if path[-1] == "out":
                paths.add(tuple(path))
            for nxt in edges[path[-1]]:
                q.append([*path, nxt])
        return len(paths)

    def output_graph(self, edges: dict[str, set[str]]) -> None:
        with Path("~/tmp/2025_11.dot").open("w") as f:
            f.write("digraph G {\n")
            f.write('fft [style="filled" fillcolor=red]\n')
            f.write('dac [style="filled" fillcolor=red]\n')
            for k, vv in edges.items():
                for v in vv:  # noqa:FURB122
                    f.write(f"{k} -> {v}\n")
            f.write("}\n")

    def part_2(self, inputs: Input) -> Output2:
        edges = defaultdict[str, set[str]](set)
        for line in inputs:
            from_, *to = line.split()
            edges[from_[:-1]] |= set(to)

        @cache
        def dfs(state: tuple[str, bool, bool]) -> int:
            if state == ("out", True, True):
                return 1
            node, seen_fft, seen_dac = state
            if node == "fft":
                return sum(dfs((nxt, True, seen_dac)) for nxt in edges[node])
            if node == "dac":
                return sum(dfs((nxt, seen_fft, True)) for nxt in edges[node])
            return sum(dfs((nxt, seen_fft, seen_dac)) for nxt in edges[node])

        return dfs(("svr", False, False))

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

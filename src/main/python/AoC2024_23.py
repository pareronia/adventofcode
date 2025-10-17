#! /usr/bin/env python3
#
# Advent of Code 2024 Day 23
#

import sys
from collections import defaultdict

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = dict[str, set[str]]
Output1 = int
Output2 = str


TEST = """\
kh-tc
qp-kh
de-cg
ka-co
yn-aq
qp-ub
cg-tb
vc-aq
tb-ka
wh-tc
yn-cg
kh-ub
ta-co
de-co
tc-td
tb-wq
wh-td
ta-ka
td-qp
aq-cg
wq-ub
ub-vc
de-ta
wq-aq
wq-vc
wh-yn
ka-de
kh-ta
co-tc
wh-qp
tb-vc
td-yn
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        edges = defaultdict[str, set[str]](set)
        for line in input_data:
            node_1, node_2 = line.split("-")
            edges[node_1].add(node_2)
            edges[node_2].add(node_1)
        return edges

    def part_1(self, edges: Input) -> Output1:
        return len(
            {
                tuple(sorted((t, neighbour_1, neighbour_2)))
                for t in (comp for comp in edges if comp.startswith("t"))
                for neighbour_1 in edges[t]
                for neighbour_2 in edges[neighbour_1]
                if t in edges[neighbour_2]
            }
        )

    def part_2(self, edges: Input) -> Output2:
        cliques = [{comp} for comp in edges]
        for clique in cliques:
            for comp in edges:
                if clique & edges[comp] == clique:
                    clique.add(comp)
        return ",".join(sorted(max(cliques, key=len)))  # type:ignore[arg-type]

    @aoc_samples(
        (
            ("part_1", TEST, 7),
            ("part_2", TEST, "co,de,ka,ta"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 23)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2024 Day 23
#

import sys
from collections import defaultdict

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = InputData
Output1 = int
Output2 = int


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
        return input_data

    def part_1(self, input: Input) -> Output1:
        d = defaultdict[str, list[str]](list)
        for line in input:
            a, b = line.split("-")
            d[a].append(b)
            d[b].append(a)
        lst = set()
        ts = [c for c in d if c.startswith("t")]
        for t in ts:
            for n1 in d[t]:
                for n2 in d[n1]:
                    if t in d[n2]:
                        lst.add(tuple(_ for _ in sorted([t, n1, n2])))
        return len(lst)

    def part_2(self, input: Input) -> Output2:
        d = defaultdict[str, list[str]](list)
        for line in input:
            a, b = line.split("-")
            d[a].append(b)
            d[b].append(a)

        nws = [{c} for c in d]
        for nw in nws:
            for c in d:
                if all(n in d[c] for n in nw):
                    nw.add(c)
        return ",".join(sorted(max(nws, key=len)))

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

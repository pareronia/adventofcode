#! /usr/bin/env python3
#
# Advent of Code 2020 Day 7
#

import sys
from functools import cache
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

SHINY_GOLD = "shiny gold"
TEST1 = """\
light red bags contain 1 bright white bag, 2 muted yellow bags.
dark orange bags contain 3 bright white bags, 4 muted yellow bags.
bright white bags contain 1 shiny gold bag.
muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
dark olive bags contain 3 faded blue bags, 4 dotted black bags.
vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
faded blue bags contain no other bags.
dotted black bags contain no other bags.
"""
TEST2 = """\
shiny gold bags contain 2 dark red bags.
dark red bags contain 2 dark orange bags.
dark orange bags contain 2 dark yellow bags.
dark yellow bags contain 2 dark green bags.
dark green bags contain 2 dark blue bags.
dark blue bags contain 2 dark violet bags.
dark violet bags contain no other bags.
"""


class Edge(NamedTuple):
    src: str
    dst: str
    cnt: int


Input = set[Edge]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        def parse_line(line: str) -> tuple[str, set[tuple[int, str]]]:
            sp = line.split(" contain ")
            source = sp[0].split(" bags")[0]
            right = sp[1].split(", ")
            destinations = {
                (int(count), adj + " " + color)
                for count, adj, color, _ in (
                    r.split() for r in right if r != "no other bags."
                )
            }
            return source, destinations

        return {
            Edge(source, dst, cnt)
            for source, destinations in (
                parse_line(line) for line in input_data
            )
            for cnt, dst in destinations
        }

    def part_1(self, edges: Input) -> int:
        @cache
        def exists_path(src: str, dst: str) -> bool:
            return any(
                edge.dst == dst or exists_path(edge.dst, dst)
                for edge in edges
                if edge.src == src
            )

        return sum(
            src != SHINY_GOLD and exists_path(src, SHINY_GOLD)
            for src in {edge.src for edge in edges}
        )

    def part_2(self, edges: Input) -> int:
        @cache
        def count_contained(src: str) -> int:
            return sum(
                edge.cnt + edge.cnt * count_contained(edge.dst)
                for edge in edges
                if edge.src == src
            )

        return count_contained(SHINY_GOLD)

    @aoc_samples(
        (
            ("part_1", TEST1, 4),
            ("part_2", TEST1, 32),
            ("part_2", TEST2, 126),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2020, 7)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

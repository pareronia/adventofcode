#! /usr/bin/env python3
#
# Advent of Code 2020 Day 7
#
from typing import NamedTuple
from functools import reduce, cache
from aoc import my_aocd
from aoc.common import log

SHINY_GOLD = "shiny gold"


def _parse_line(line: str) -> tuple[str, set[tuple[int, str]]]:
    sp = line.split(" contain ")
    source = sp[0].split(" bags")[0]
    right = sp[1].split(", ")
    destinations = set[tuple[int, str]]()
    for right_part in right:
        if right_part != "no other bags.":
            contained = right_part.split()
            destinations.add((int(contained[0]),
                              contained[1] + " " + contained[2]))
    result = (source, destinations)
    log(f"{source} -> {destinations}")
    return result


class Edge(NamedTuple):
    src: str
    dst: str
    cnt: int


def _build_edges(inputs: tuple[str]) -> frozenset[Edge]:
    parsed_lines = [_parse_line(line) for line in inputs]
    log(parsed_lines)
    return frozenset({Edge(parsed_line[0], dst[1], dst[0])
                      for parsed_line in parsed_lines
                      for dst in parsed_line[1]})


def part_1(inputs: tuple[str]) -> int:
    @cache
    def exists_path(src: str, dst: str) -> bool:
        return reduce(
            lambda x, y: x or y,
            map(lambda edge: edge.dst == dst or exists_path(edge.dst, dst),
                [edge for edge in edges if edge.src == src]),
            False)

    edges = _build_edges(inputs)
    return len({edge.src
                for edge in edges
                if edge.src != SHINY_GOLD
                and exists_path(edge.src, SHINY_GOLD)})


def part_2(inputs: tuple[str]) -> int:
    @cache
    def count_contained(src: str) -> int:
        return reduce(
            lambda x, y: x + y,
            map(lambda edge: edge.cnt + edge.cnt * count_contained(edge.dst),
                [edge for edge in edges if edge.src == src]),
            0)

    edges = _build_edges(inputs)
    return count_contained(SHINY_GOLD)


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
""".splitlines()
TEST2 = """\
shiny gold bags contain 2 dark red bags.
dark red bags contain 2 dark orange bags.
dark orange bags contain 2 dark yellow bags.
dark yellow bags contain 2 dark green bags.
dark green bags contain 2 dark blue bags.
dark blue bags contain 2 dark violet bags.
dark violet bags contain no other bags.
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 7)

    assert part_1(TEST1) == 4
    assert part_2(TEST1) == 32
    assert part_2(TEST2) == 126

    inputs = my_aocd.get_input(2020, 7, 594)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

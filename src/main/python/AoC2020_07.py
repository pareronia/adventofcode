#! /usr/bin/env python3
#
# Advent of Code 2020 Day 7
#
from dataclasses import dataclass
from collections import defaultdict
from networkx import nx
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


@dataclass
class Edge:
    src: str
    dst: str
    cnt: int


def _build_edges(inputs: tuple[str]) -> list[Edge]:
    parsed_lines = [_parse_line(line) for line in inputs]
    log(parsed_lines)
    return [Edge(parsed_line[0], dst[1], dst[0])
            for parsed_line in parsed_lines
            for dst in parsed_line[1]]


def _build_digraph(edges: list[Edge]):
    dg = nx.DiGraph()
    dg.add_weighted_edges_from([(edge.src, edge.dst, edge.cnt)
                                for edge in edges])
    return dg


def part_1(inputs: tuple[str]) -> int:
    log(inputs)
    edges = _build_edges(inputs)
    log(edges)
    unique_sources = (edge.src for edge in edges)
    G = _build_digraph(edges)
    unique_containers = set[str]()
    for source in unique_sources:
        paths = nx.all_simple_paths(G, source=source, target=SHINY_GOLD)
        if next(paths, None):
            unique_containers.add(source)
    log(unique_containers)
    return len(unique_containers)


def _count_contained(containers: dict, start: str) -> int:
    return sum([c[1] + c[1]*_count_contained(containers, c[0])
                for c in containers[start]])


def part_2(inputs: tuple[str]) -> int:
    edges = _build_edges(inputs)
    containers = defaultdict(list)
    for edge in edges:
        containers[edge.src].append((edge.dst, edge.cnt))
    return _count_contained(containers, SHINY_GOLD)


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

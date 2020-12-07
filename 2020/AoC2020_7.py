#! /usr/bin/env python3
#
# Advent of Code 2020 Day 7
#
from dataclasses import dataclass
from networkx import nx
import my_aocd


def _parse_line(line: str) -> tuple[str, set[str]]:
    sp = line.split(" contain ")
    source = sp[0].split(" bags")[0]
    right = sp[1].split(", ")
    destinations = set[str]()
    for right_part in right:
        if right_part == "no other bags.":
            break
        else:
            contained = right_part.split()
            destinations.add(contained[1] + " " + contained[2])
    result = (source, destinations)
    # print(f"{source} -> {destinations}")
    return result


@dataclass
class Edge:
    src: str
    dst: str


def part_1(inputs: tuple[str]) -> int:
    print(inputs)
    parsed_lines = [_parse_line(line) for line in inputs]
    print(parsed_lines)
    unique_sources = (parsed_line[0] for parsed_line in parsed_lines)
    print(unique_sources)
    edges = [Edge(parsed_line[0], dst)
             for parsed_line in parsed_lines
             for dst in parsed_line[1]]
    print(edges)
    G = nx.DiGraph([(edge.src, edge.dst) for edge in edges])
    unique_containers = set[str]()
    for source in unique_sources:
        paths = nx.all_simple_paths(G, source=source, target="shiny gold")
        for path in paths:
            unique_containers.add(path[0])
    print(unique_containers)
    return len(unique_containers)


test = ("light red bags contain 1 bright white bag, 2 muted yellow bags.",
        "dark orange bags contain 3 bright white bags, 4 muted yellow bags.",
        "bright white bags contain 1 shiny gold bag.",
        "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.",
        "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.",
        "dark olive bags contain 3 faded blue bags, 4 dotted black bags.",
        "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.",
        "faded blue bags contain no other bags.",
        "dotted black bags contain no other bags.",
        )


def main() -> None:
    my_aocd.print_header(2020, 7)

    assert part_1(test) == 4

    inputs = my_aocd.get_input_as_tuple(2020, 7, 594)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")


if __name__ == '__main__':
    main()

#! /usr/bin/env python3
#
# Advent of Code 2020 Day 7
#
from dataclasses import dataclass
from networkx import nx
import my_aocd


def _parse_line(line: str) -> tuple[str, set[tuple[int, str]]]:
    sp = line.split(" contain ")
    source = sp[0].split(" bags")[0]
    right = sp[1].split(", ")
    destinations = set[tuple[int, str]]()
    for right_part in right:
        if right_part == "no other bags.":
            destinations.add((0, None))
        else:
            contained = right_part.split()
            destinations.add((int(contained[0]),
                              contained[1] + " " + contained[2]))
    result = (source, destinations)
    # print(f"{source} -> {destinations}")
    return result


@dataclass
class Edge:
    src: str
    dst: str
    cnt: int


def _build_edges(inputs: tuple[str]) -> list[Edge]:
    parsed_lines = [_parse_line(line) for line in inputs]
    # print(parsed_lines)
    return [Edge(parsed_line[0], dst[1], dst[0])
            for parsed_line in parsed_lines
            for dst in parsed_line[1]]


def _build_digraph(edges: list[Edge]):
    # dg = nx.DiGraph([(edge.src, edge.dst) for edge in edges])
    dg = nx.DiGraph()
    dg.add_weighted_edges_from([(edge.src, edge.dst, edge.cnt)
                                for edge in edges])
    return dg


def part_1(inputs: tuple[str]) -> int:
    # print(inputs)
    edges = _build_edges(inputs)
    # print(edges)
    unique_sources = (edge.src for edge in edges)
    G = _build_digraph(edges)
    unique_containers = set[str]()
    for source in unique_sources:
        paths = nx.all_simple_paths(G, source=source, target="shiny gold")
        for path in paths:
            # print(path)
            unique_containers.add(path[0])
    # print(unique_containers)
    return len(unique_containers)


def part_2(inputs: tuple[str]) -> int:
    edges = _build_edges(inputs)
    print(edges)
    unique_sinks = (edge.src for edge in edges if edge.dst is None)
    G = _build_digraph(edges)
    result = 0
    seen = set[tuple]()
    for sink in unique_sinks:
        paths = nx.all_simple_paths(G, source="shiny gold", target=sink)
        for path in paths:
            print(path)
            g_edges = []
            for i in range(len(path)-1):
                left = path[i]
                right = path[i+1]
                weight = G[left][right]['weight']
                g_edge = (left, right, weight)
                g_edges.append(g_edge)
                print(f"{left} -> {right} : {weight}")
            total = g_edges[-1][2]
            print(g_edges)
            _range = range(len(g_edges)-1, 0, -1)
            for i in _range:
                g_edge = g_edges[i]
                g_edge_prev = g_edges[i-1]
                total *= g_edge_prev[2]
                if (g_edge_prev[0], g_edge_prev[1]) not in seen:
                    total += g_edge_prev[2]
                seen.add((g_edge_prev[0], g_edge_prev[1]))
            result += total
            print(f"{result}")
    print([x for x in seen])
    print(f"Result: {result}")
    return result


test_1 = ("light red bags contain 1 bright white bag, 2 muted yellow bags.",
          "dark orange bags contain 3 bright white bags, 4 muted yellow bags.",
          "bright white bags contain 1 shiny gold bag.",
          "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.",
          "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.",
          "dark olive bags contain 3 faded blue bags, 4 dotted black bags.",
          "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.",
          "faded blue bags contain no other bags.",
          "dotted black bags contain no other bags.",
          )
test_2 = ("shiny gold bags contain 2 dark red bags.",
          "dark red bags contain 2 dark orange bags.",
          "dark orange bags contain 2 dark yellow bags.",
          "dark yellow bags contain 2 dark green bags.",
          "dark green bags contain 2 dark blue bags.",
          "dark blue bags contain 2 dark violet bags.",
          "dark violet bags contain no other bags.",
          )


def main() -> None:
    my_aocd.print_header(2020, 7)

    assert part_1(test_1) == 4
    assert part_2(test_1) == 32
    assert part_2(test_2) == 126

    inputs = my_aocd.get_input_as_tuple(2020, 7, 594)
    # result1 = part_1(inputs)
    # print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

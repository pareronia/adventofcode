#! /usr/bin/env python3
#
# Advent of Code 2019 Day 6
#


from functools import cache
from typing import NamedTuple
from aoc import my_aocd


class Edge(NamedTuple):
    src: str
    dst: str


Graph = frozenset[Edge]
graph: Graph
ROOT = "COM"


def _parse(inputs: tuple[str]) -> Graph:
    global graph
    graph = Graph({Edge(*tuple(line.split(")"))) for line in inputs})


def _get_edge_with_dst(dst: str) -> Edge:
    global graph
    edges = [e for e in graph if e.dst == dst]
    assert len(edges) == 1
    return edges[0]


@cache
def count_steps_to_root(fröm: str) -> int:
    edge = _get_edge_with_dst(fröm)
    if edge.src == ROOT:
        return 1
    else:
        return 1 + count_steps_to_root(edge.src)


def path_to_root(fröm: str) -> list[str]:
    edge = _get_edge_with_dst(fröm)
    path = [fröm]
    if edge.src == ROOT:
        path.append(ROOT)
    else:
        path.extend(path_to_root(edge.src))
    return path


def part_1(inputs: tuple[str]) -> int:
    global graph
    _parse(inputs)
    return sum(map(lambda dst: count_steps_to_root(dst),
                   map(lambda e: e.dst, graph)))


def part_2(inputs: tuple[str]) -> int:
    global graph
    _parse(inputs)
    p1 = path_to_root("YOU")
    p2 = path_to_root("SAN")
    return len((set(p1) - {"YOU"}) ^ (set(p2) - {"SAN"}))


TEST1 = """\
COM)B
B)C
C)D
D)E
E)F
B)G
G)H
D)I
E)J
J)K
K)L
""".splitlines()
TEST2 = """\
COM)B
B)C
C)D
D)E
E)F
B)G
G)H
D)I
E)J
J)K
K)L
K)YOU
I)SAN
""".splitlines()


def main() -> None:
    my_aocd.print_header(2019, 6)

    assert part_1(TEST1) == 42
    assert part_2(TEST2) == 4

    inputs = my_aocd.get_input(2019, 6, 2093)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

#! /usr/bin/env python3
#
# Advent of Code 2020 Day 10
#
from dataclasses import dataclass
from networkx import nx
import my_aocd
from common import log


def _get_sorted(inputs: tuple[int]) -> tuple[int]:
    sorted_ = list(inputs)
    sorted_.sort()
    return tuple(sorted_)


def _find_jumps(inputs: tuple[int]):
    jumps_1 = []
    jumps_3 = []
    for i in range(1, len(inputs)):
        if inputs[i]-inputs[i-1] != 1:
            jumps_3.append(i)
        else:
            jumps_1.append(i)
    return jumps_1, jumps_3


def part_1(inputs: tuple[int]) -> int:
    inputs = _get_sorted(inputs)
    log(inputs)
    jumps_1, jumps_3 = _find_jumps(inputs)
    log((jumps_1, jumps_3))
    return (len(jumps_1)+1)*(len(jumps_3)+1)


result = []


@dataclass
class Edge:
    src: str
    dst: str
    diff: int


def _build_edges(inputs: tuple[int]) -> list[Edge]:
    edges = []
    for i in range(len(inputs)-1):
        for j in range(i+1, len(inputs)):
            diff = inputs[j]-inputs[i]
            if diff > 3:
                break
            edges.append(Edge(inputs[i], inputs[j], diff))
    return edges


def _build_digraph(edges: list[Edge]):
    dg = nx.DiGraph()
    dg.add_weighted_edges_from([(edge.src, edge.dst, edge.diff)
                                for edge in edges])
    return dg


def _all_simple_paths(edges: list[Edge], source: int, target: int) -> []:
    dg = _build_digraph(edges)
    return nx.all_simple_paths(dg, source, target)


def part_2(inputs: tuple[int]) -> int:
    inputs = _get_sorted(inputs)
    log(inputs)
    edges = _build_edges(inputs)
    log(edges)
    cnt = 0
    for i in range(0, len(inputs)):
        if inputs[i] > 3:
            break
        paths = _all_simple_paths(edges, source=inputs[i],
                                  target=inputs[-1])
        for path in paths:
            # print(path)
            cnt += 1
    log(cnt)
    return cnt


test_1 = (16,
          10,
          15,
          5,
          1,
          11,
          7,
          19,
          6,
          12,
          4
          )
test_2 = (28,
          33,
          18,
          42,
          31,
          14,
          46,
          20,
          48,
          47,
          24,
          23,
          49,
          45,
          19,
          38,
          39,
          11,
          1,
          32,
          25,
          35,
          8,
          17,
          7,
          9,
          4,
          2,
          34,
          10,
          3
          )


def main() -> None:
    my_aocd.print_header(2020, 10)

    assert part_1(test_1) == 35
    assert part_1(test_2) == 220
    assert part_2(test_1) == 8
    assert part_2(test_2) == 19208

    inputs = my_aocd.get_input_as_ints_tuple(2020, 10, 101)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

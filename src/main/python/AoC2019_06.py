#! /usr/bin/env python3
#
# Advent of Code 2019 Day 6
#


from functools import reduce, cache
from typing import NamedTuple
from aoc import my_aocd
# from aoc.common import log


class Edge(NamedTuple):
    src: str
    dst: str


Graph = frozenset[Edge]
graph: Graph


def _parse(inputs: tuple[str]) -> Graph:
    global graph
    graph = Graph({Edge(*tuple(line.split(")"))) for line in inputs})


@cache
def count_steps_up(fröm: str, to: str) -> int:
    global graph
    return list(
        map(lambda e: 1 if e.src == to else 1 + count_steps_up(e.src, to),
            filter(lambda e: e.dst == fröm, graph))
    )[0]


@cache
def contains_path(fröm: str, to: str) -> bool:
    global graph
    return reduce(lambda a, b: a or b,
                  map(lambda e: e.dst == to or contains_path(e.dst, to),
                      filter(lambda e: e.src == fröm, graph)),
                  False)


def part_1(inputs: tuple[str]) -> int:
    global graph
    _parse(inputs)
    return reduce(lambda a, b: a + b,
                  map(lambda dst: count_steps_up(dst, "COM"),
                      map(lambda e: e.dst, graph)))


def part_2(inputs: tuple[str]) -> int:
    global graph
    _parse(inputs)
    return min(
        map(lambda dst:
            count_steps_up("YOU", dst) + count_steps_up("SAN", dst) - 2,
            filter(lambda dst:
                   dst not in {"YOU", "SAN"}
                   and contains_path(dst, "YOU")
                   and contains_path(dst, "SAN"),
                   map(lambda e: e.dst, graph)
                   )
            )
    )


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

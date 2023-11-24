#! /usr/bin/env python3
#
# Advent of Code 2022 Day 16
#


from __future__ import annotations

from collections import defaultdict
from typing import NamedTuple

from aoc.common import aoc_main
from aoc.graph import a_star


class Cave(NamedTuple):
    start: int
    valves: list[str]
    rates: list[int]
    tunnels: list[set[int]]

    @classmethod
    def from_input(cls, lines: tuple[str, ...]) -> Cave:
        valves = [""] * len(lines)
        map = dict[str, int]()
        rates = [0] * len(lines)
        edges = defaultdict[str, set[str]](set)
        tunnels = [set[int]()] * len(lines)

        for i, line in enumerate(lines):
            line = line.replace(",", "")
            line = line.replace(";", "")
            _, name, _, _, rate, _, _, _, _, *rest = line.split()
            if name == "AA":
                start = i
            valves[i] = name
            map[name] = i
            rates[i] = int(rate.split("=")[1])
            for valve in rest:
                edges[name].add(valve)
        for from_, to in edges.items():
            k = map[from_]
            v = {map[_] for _ in to}
            tunnels[k] = v
        return Cave(start, valves, rates, tunnels)

    def get_distances(self) -> list[list[int]]:
        size = len(self.valves)
        relevant_valves = [
            i for i in range(size) if self.rates[i] > 0 or i == self.start
        ]
        distances = [[0] * size for i in range(size)]
        for i in relevant_valves:
            _, distance, _ = a_star(
                i,
                lambda v: False,
                lambda v: (_ for _ in self.tunnels[v]),
                lambda v: 1,
            )
            for j in relevant_valves:
                distances[i][j] = distance[j]
        return distances


class DFS:
    def __init__(self, max_time: int, sample: bool) -> None:
        self.max_time = max_time
        self.sample = sample
        self.used = 0
        self.max_flow = 0
        self.best_per_used = defaultdict[int, int](int)

    def dfs(
        self, cave: Cave, distances: list[list[int]], start: int, time: int
    ) -> None:
        for i in range(len(cave.valves)):
            idx = 1 << i
            if cave.rates[i] == 0 or self.used & idx != 0:
                continue
            new_time = time + 1 + distances[start][i]
            if new_time >= self.max_time:
                continue
            flow = cave.rates[i] * (self.max_time - new_time)
            if (
                not self.sample
                and self.max_flow + flow < self.best_per_used[self.used + idx]
            ):
                continue
            self.max_flow += flow
            self.used += idx
            self.dfs(cave, distances, i, new_time)
            self.max_flow -= flow
            self.used -= idx
        best = self.best_per_used[self.used]
        self.best_per_used[self.used] = max(best, self.max_flow)


def part_1(inputs: tuple[str, ...]) -> int:
    cave = Cave.from_input(inputs)
    distances = cave.get_distances()
    dfs = DFS(30, False)
    dfs.dfs(cave, distances, cave.start, 0)
    return max(dfs.best_per_used.values())


def solve_2(inputs: tuple[str, ...], sample: bool) -> int:
    cave = Cave.from_input(inputs)
    distances = cave.get_distances()
    dfs = DFS(26, sample)
    dfs.dfs(cave, distances, cave.start, 0)
    return max(
        pressure1 + pressure2
        for used1, pressure1 in dfs.best_per_used.items()
        for used2, pressure2 in dfs.best_per_used.items()
        if used1 & used2 == 0
    )


def part_2(inputs: tuple[str, ...]) -> int:
    return solve_2(inputs, False)


TEST = """\
Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
Valve BB has flow rate=13; tunnels lead to valves CC, AA
Valve CC has flow rate=2; tunnels lead to valves DD, BB
Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
Valve EE has flow rate=3; tunnels lead to valves FF, DD
Valve FF has flow rate=0; tunnels lead to valves EE, GG
Valve GG has flow rate=0; tunnels lead to valves FF, HH
Valve HH has flow rate=22; tunnel leads to valve GG
Valve II has flow rate=0; tunnels lead to valves AA, JJ
Valve JJ has flow rate=21; tunnel leads to valve II
""".splitlines()


@aoc_main(2022, 16, part_1, part_2)
def main() -> None:
    assert part_1(TEST) == 1651  # type:ignore[arg-type]
    assert solve_2(TEST, True) == 1707  # type:ignore[arg-type]


if __name__ == "__main__":
    main()

#! /usr/bin/env python3

from collections import defaultdict
from collections.abc import Generator
from queue import PriorityQueue
from typing import Callable
from typing import TypeVar

T = TypeVar("T")


def a_star(
    start: T,
    is_end: Callable[[T], bool],
    adjacent: Callable[[T], Generator[T]],
    get_cost: Callable[[T], int],
) -> tuple[int, list[T]]:
    q = PriorityQueue()
    q.put((0, start))
    best = defaultdict(lambda: 1e9)
    best[start] = 0
    parent = {}
    while not q.empty():
        cost, node = q.get()
        if is_end(node):
            path = [node]
            curr = node
            while curr in parent:
                curr = parent[curr]
                path.append(curr)
            return cost, path
        c_total = best[node]
        for n in adjacent(node):
            new_risk = c_total + get_cost(n)
            if new_risk < best[n]:
                best[n] = new_risk
                parent[n] = node
                q.put((new_risk, n))
    raise RuntimeError("Unsolvable")
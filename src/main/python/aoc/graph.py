#! /usr/bin/env python3

from collections import defaultdict, deque
from collections.abc import Iterator
from queue import PriorityQueue
from typing import Callable
from typing import TypeVar

T = TypeVar("T")


def a_star(
    start: T,
    is_end: Callable[[T], bool],
    adjacent: Callable[[T], Iterator[T]],
    get_cost: Callable[[T], int],
) -> tuple[int, dict[T, int], list[T]]:
    q: PriorityQueue[tuple[int, T]] = PriorityQueue()
    q.put((0, start))
    best: defaultdict[T, int] = defaultdict(lambda: 1_000_000_000)
    best[start] = 0
    parent: dict[T, T] = {}
    path = []
    while not q.empty():
        cost, node = q.get()
        if is_end(node):
            path = [node]
            curr = node
            while curr in parent:
                curr = parent[curr]
                path.append(curr)
            break
        c_total = best[node]
        for n in adjacent(node):
            new_risk = c_total + get_cost(n)
            if new_risk < best[n]:
                best[n] = new_risk
                parent[n] = node
                q.put((new_risk, n))
    return cost, best, path


def bfs(
    start: T,
    is_end: Callable[[T], bool],
    adjacent: Callable[[T], Iterator[T]],
) -> int:
    q: deque[tuple[int, T]] = deque()
    q.append((0, start))
    seen: set[T] = set()
    seen.add(start)
    while not len(q) == 0:
        distance, node = q.popleft()
        if is_end(node):
            return distance
        for n in adjacent(node):
            if n in seen:
                continue
            seen.add(n)
            q.append((distance + 1, n))
    raise RuntimeError("unsolvable")


def flood_fill(
    start: T,
    adjacent: Callable[[T], Iterator[T]],
) -> set[T]:
    q: deque[T] = deque()
    q.append(start)
    seen: set[T] = set()
    seen.add(start)
    while not len(q) == 0:
        node = q.popleft()
        for n in adjacent(node):
            if n in seen:
                continue
            seen.add(n)
            q.append(n)
    return seen

#! /usr/bin/env python3

import sys
from collections import defaultdict
from collections import deque
from collections.abc import Iterator
from queue import PriorityQueue
from typing import Callable
from typing import NamedTuple
from typing import TypeVar

T = TypeVar("T")


def bfs(
    start: T,
    is_end: Callable[[T], bool],
    adjacent: Callable[[T], Iterator[T]],
) -> tuple[int, list[T]]:
    q: deque[tuple[int, T]] = deque()
    q.append((0, start))
    seen: set[T] = set()
    seen.add(start)
    parent: dict[T, T] = {}
    while not len(q) == 0:
        distance, node = q.popleft()
        if is_end(node):
            path = [node]
            curr = node
            while curr in parent:
                curr = parent[curr]
                path.append(curr)
            return distance, path
        for n in adjacent(node):
            if n in seen:
                continue
            seen.add(n)
            parent[n] = node
            q.append((distance + 1, n))
    raise RuntimeError("unsolvable")


def bfs_full(
    start: T,
    is_end: Callable[[T], bool],
    adjacent: Callable[[T], Iterator[T]],
) -> tuple[dict[T, int], dict[T, T]]:
    q: deque[tuple[int, T]] = deque()
    q.append((0, start))
    seen: set[T] = set()
    seen.add(start)
    parent: dict[T, T] = {}
    dists = defaultdict[T, int](int)
    while not len(q) == 0:
        distance, node = q.popleft()
        if is_end(node):
            dists[node] = distance
        for n in adjacent(node):
            if n in seen:
                continue
            seen.add(n)
            parent[n] = node
            q.append((distance + 1, n))
    return dists, parent


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


class AllResults[T](NamedTuple):
    start: T
    distances: dict[T, int]
    prececessors: dict[T, list[T]]

    def get_paths(self, t: T) -> list[list[T]]:
        if t == self.start:
            return [[self.start]]
        paths = list[list[T]]()
        for predecessor in self.prececessors.get(t, list()):
            for path in self.get_paths(predecessor):
                paths.append(path + [t])
        return paths

    def get_distance(self, t: T) -> int:
        return self.distances[t]


class Dijkstra:

    @classmethod
    def dijkstra(
        cls,
        start: T,
        is_end: Callable[[T], bool],
        adjacent: Callable[[T], Iterator[T]],
        get_cost: Callable[[T, T], int],
    ) -> tuple[int, dict[T, int], list[T]]:
        q: PriorityQueue[tuple[int, T]] = PriorityQueue()
        q.put((0, start))
        best: defaultdict[T, int] = defaultdict(lambda: sys.maxsize)
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
                new_risk = c_total + get_cost(node, n)
                if new_risk < best[n]:
                    best[n] = new_risk
                    parent[n] = node
                    q.put((new_risk, n))
        return cost, best, path

    @classmethod
    def distance(
        cls,
        start: T,
        is_end: Callable[[T], bool],
        adjacent: Callable[[T], Iterator[T]],
        get_cost: Callable[[T, T], int],
    ) -> int:
        q = PriorityQueue[tuple[int, T]]()
        q.put((0, start))
        distances = defaultdict[T, int](lambda: sys.maxsize)
        distances[start] = 0
        while not q.empty():
            distance, node = q.get()
            if is_end(node):
                return distance
            total = distances[node]
            if distance > total:
                continue
            for n in adjacent(node):
                new_distance = total + get_cost(node, n)
                if new_distance < distances[n]:
                    distances[n] = new_distance
                    q.put((new_distance, n))
        raise RuntimeError("unreachable")

    @classmethod
    def all(
        cls,
        start: T,
        is_end: Callable[[T], bool],
        adjacent: Callable[[T], Iterator[T]],
        get_cost: Callable[[T, T], int],
    ) -> AllResults[T]:
        q = PriorityQueue[tuple[int, T]]()
        q.put((0, start))
        distances = defaultdict[T, int](lambda: sys.maxsize)
        distances[start] = 0
        predecessors = dict[T, list[T]]()
        while not q.empty():
            distance, node = q.get()
            if is_end(node):
                break
            total = distances[node]
            if distance > total:
                continue
            for n in adjacent(node):
                new_distance = total + get_cost(node, n)
                dist_n = distances[n]
                if new_distance < dist_n:
                    distances[n] = new_distance
                    predecessors[n] = [node]
                    q.put((new_distance, n))
                elif new_distance == dist_n:
                    predecessors.setdefault(n, []).append(node)
        return AllResults(start, distances, predecessors)

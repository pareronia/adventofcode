#! /usr/bin/env python3
#
# Advent of Code 2021 Day 12
#

from __future__ import annotations
from typing import NamedTuple, Generator, Callable
from functools import lru_cache
from aoc import my_aocd
import aocd
from aoc.common import log


class Cave(NamedTuple):
    name: str

    def is_small(self) -> bool:
        return self.name.islower()

    def is_start(self):
        return self.name == "start"

    def is_end(self):
        return self.name == "end"


class Tunnel(NamedTuple):
    from_: Cave
    to: Cave


@lru_cache(maxsize=50)
def _do_get_tunnels_from(tunnels: tuple[Tunnel], cave: Cave) -> tuple[Tunnel]:
    return tuple([t for t in tunnels if t.from_ == cave])


class System(NamedTuple):
    tunnels: set[Tunnel]
    start: Cave
    end: Cave

    def get_tunnels_from(self, cave: Cave) -> Generator[Tunnel]:
        return (t for t
                in _do_get_tunnels_from(tuple(self.tunnels), cave))


class State(NamedTuple):
    small_caves_seen: set[Cave]
    small_caves_seen_twice: set[Cave]

    @classmethod
    def copy_of(cls, state: State) -> State:
        return State({_ for _ in state.small_caves_seen},
                     {_ for _ in state.small_caves_seen_twice})


def _parse(inputs: tuple[str]) -> System:
    tunnels = set[Tunnel]()
    for line in inputs:
        from_, to = [Cave(_) for _ in line.split('-')]
        tunnels.add(Tunnel(from_, to))
        if from_.is_start():
            start = from_
        elif to.is_end():
            end = to
        else:
            tunnels.add(Tunnel(to, from_))
    return System(tunnels, start, end)


def _dfs(system: System, start: Cave, end: Cave, state: State,
         proceed: Callable, on_path: Callable) -> None:
    if start == end:
        on_path()
        return
    for tunnel in system.get_tunnels_from(start):
        if proceed(tunnel, state):
            new_state = State.copy_of(state)
            if tunnel.to.is_small():
                if tunnel.to in new_state.small_caves_seen:
                    new_state.small_caves_seen_twice.add(tunnel.to)
                new_state.small_caves_seen.add(tunnel.to)
            _dfs(system, tunnel.to, end, new_state, proceed, on_path)


def _solve(system: System, proceed: Callable) -> int:
    cnt = 0

    def increment_cnt() -> None:
        nonlocal cnt
        cnt += 1

    state = State({system.start}, {})
    _dfs(system, system.start, system.end, state,
         proceed, increment_cnt)
    log(_do_get_tunnels_from.cache_info())
    return cnt


def part_1(inputs: tuple[str]) -> int:
    system = _parse(inputs)
    return _solve(system,
                  lambda tunnel, state:
                  not tunnel.to.is_small()
                  or tunnel.to not in state.small_caves_seen)


def part_2(inputs: tuple[str]) -> int:
    system = _parse(inputs)
    return _solve(system,
                  lambda tunnel, state:
                  not tunnel.to.is_small()
                  or tunnel.to not in state.small_caves_seen
                  or (not tunnel.to.is_start()
                      and not tunnel.to.is_end()
                      and not state.small_caves_seen_twice))


TEST1 = """\
start-A
start-b
A-c
A-b
b-d
A-end
b-end
""".splitlines()
TEST2 = """\
dc-end
HN-start
start-kj
dc-start
dc-HN
LN-dc
HN-end
kj-sa
kj-HN
kj-dc
""".splitlines()
TEST3 = """\
fs-end
he-DX
fs-he
start-DX
pj-DX
end-zg
zg-sl
zg-pj
pj-he
RW-he
fs-DX
pj-RW
zg-RW
start-pj
he-WI
zg-he
pj-fs
start-RW
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 12)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 10
    assert part_1(TEST2) == 19
    assert part_1(TEST3) == 226
    assert part_2(TEST1) == 36
    assert part_2(TEST2) == 103
    assert part_2(TEST3) == 3509

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 21)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()

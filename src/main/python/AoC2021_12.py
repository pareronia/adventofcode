#! /usr/bin/env python3
#
# Advent of Code 2021 Day 12
#

from __future__ import annotations
from typing import NamedTuple, Callable
from collections import defaultdict
from aoc import my_aocd
import aocd


class Cave(NamedTuple):
    name: str

    def is_small(self) -> bool:
        return self.name.islower()

    def is_start(self):
        return self.name == "start"

    def is_end(self):
        return self.name == "end"


class System(NamedTuple):
    tunnels: dict
    start: Cave
    end: Cave


class State(NamedTuple):
    small_caves_seen: set[Cave]
    small_caves_seen_twice: set[Cave]

    @classmethod
    def copy_of(cls, state: State) -> State:
        return State({_ for _ in state.small_caves_seen},
                     {_ for _ in state.small_caves_seen_twice})


def _parse(inputs: tuple[str]) -> System:
    tunnels = defaultdict(list)
    for line in inputs:
        from_, to = [Cave(_) for _ in line.split('-')]
        tunnels[from_].append(to)
        if from_.is_start():
            start = from_
        elif to.is_end():
            end = to
        tunnels[to].append(from_)
    return System(tunnels, start, end)


def _dfs(system: System, start: Cave, end: Cave, state: State,
         proceed: Callable, on_path: Callable) -> None:
    if start == end:
        on_path()
        return
    for to in system.tunnels[start]:
        if proceed(to, state):
            new_state = State.copy_of(state)
            if to.is_small():
                if to in new_state.small_caves_seen:
                    new_state.small_caves_seen_twice.add(to)
                new_state.small_caves_seen.add(to)
            _dfs(system, to, end, new_state, proceed, on_path)


def _solve(system: System, proceed: Callable) -> int:
    cnt = 0

    def increment_cnt() -> None:
        nonlocal cnt
        cnt += 1

    state = State({system.start}, {})
    _dfs(system, system.start, system.end, state,
         proceed, increment_cnt)
    return cnt


def part_1(inputs: tuple[str]) -> int:
    system = _parse(inputs)
    return _solve(system,
                  lambda to, state:
                  not to.is_small()
                  or to not in state.small_caves_seen)


def part_2(inputs: tuple[str]) -> int:
    system = _parse(inputs)
    return _solve(system,
                  lambda to, state:
                  not to.is_small()
                  or to not in state.small_caves_seen
                  or (not to.is_start()
                      and not to.is_end()
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

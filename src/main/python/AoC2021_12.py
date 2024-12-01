#! /usr/bin/env python3
#
# Advent of Code 2021 Day 12
#

from __future__ import annotations

import sys
from collections import defaultdict
from typing import Callable
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST1 = """\
start-A
start-b
A-c
A-b
b-d
A-end
b-end
"""
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
"""
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
"""


class Cave(NamedTuple):
    name: str

    def is_small(self) -> bool:
        return self.name.islower()

    def is_start(self) -> bool:
        return self.name == "start"

    def is_end(self) -> bool:
        return self.name == "end"


class System(NamedTuple):
    tunnels: dict[Cave, list[Cave]]
    start: Cave
    end: Cave


class State(NamedTuple):
    small_caves_seen: set[Cave]
    small_caves_seen_twice: set[Cave]

    @classmethod
    def copy_of(cls, state: State) -> State:
        return State(
            {_ for _ in state.small_caves_seen},
            {_ for _ in state.small_caves_seen_twice},
        )


Input = System
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, inputs: InputData) -> Input:
        tunnels = defaultdict(list)
        for line in inputs:
            from_, to = [Cave(_) for _ in line.split("-")]
            tunnels[from_].append(to)
            for _ in {from_, to}:
                if _.is_start():
                    start = _
                if _.is_end():
                    end = _
            tunnels[to].append(from_)
        return System(tunnels, start, end)

    def _dfs(
        self,
        system: System,
        start: Cave,
        end: Cave,
        state: State,
        proceed: Callable[[Cave, State], bool],
        on_path: Callable[[], None],
    ) -> None:
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
                self._dfs(system, to, end, new_state, proceed, on_path)

    def _solve(
        self, system: System, proceed: Callable[[Cave, State], bool]
    ) -> int:
        cnt = 0

        def increment_cnt() -> None:
            nonlocal cnt
            cnt += 1

        state = State({system.start}, set())
        self._dfs(
            system, system.start, system.end, state, proceed, increment_cnt
        )
        return cnt

    def part_1(self, system: Input) -> int:
        return self._solve(
            system,
            lambda to, state: not to.is_small()
            or to not in state.small_caves_seen,
        )

    def part_2(self, system: Input) -> int:
        return self._solve(
            system,
            lambda to, state: not to.is_small()
            or to not in state.small_caves_seen
            or (
                not to.is_start()
                and not to.is_end()
                and not state.small_caves_seen_twice
            ),
        )

    @aoc_samples(
        (
            ("part_1", TEST1, 10),
            ("part_1", TEST2, 19),
            ("part_1", TEST3, 226),
            ("part_2", TEST1, 36),
            ("part_2", TEST2, 103),
            ("part_2", TEST3, 3509),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2021, 12)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

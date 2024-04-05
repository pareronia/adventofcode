#! /usr/bin/env python3
#
# Advent of Code 2023 Day 20
#

from __future__ import annotations

import sys
from collections import defaultdict
from collections import deque
from enum import IntEnum
from functools import reduce
from math import lcm
from typing import NamedTuple
from typing import Protocol

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST1 = """\
broadcaster -> a, b, c
%a -> b
%b -> c
%c -> inv
&inv -> a
"""
TEST2 = """\
broadcaster -> a
%a -> inv, con
&inv -> b
%b -> con
&con -> output
"""


class ModuleType(IntEnum):
    FLIPFLOP = 1
    CONJUNCTION = 2
    BROADCAST = 3


class PulseType(IntEnum):
    LOW = 0
    HIGH = 1

    def flipped(self) -> PulseType:
        return PulseType.LOW if self == PulseType.HIGH else PulseType.HIGH


class Pulse(NamedTuple):
    src: str
    dst: str
    value: PulseType

    @classmethod
    def low(cls, src: str, dst: str) -> Pulse:
        return Pulse(src, dst, PulseType.LOW)

    @classmethod
    def high(cls, src: str, dst: str) -> Pulse:
        return Pulse(src, dst, PulseType.HIGH)

    @property
    def is_high(self) -> bool:
        return self.value == PulseType.HIGH

    @property
    def is_low(self) -> bool:
        return self.value == PulseType.LOW


class PulseListener(Protocol):
    def on_pulse(self, pulse: Pulse) -> None:
        pass


class Module:
    BROADCASTER = "broadcaster"
    BUTTON = "button"
    SELF = "*self*"

    def __init__(self, type: str, outputs: list[str]) -> None:
        self.type = type
        self.outputs = outputs
        self.state = dict[str, PulseType]()

    @property
    def is_conjunction(self) -> bool:
        return self.type == "&"

    @property
    def is_flip_flop(self) -> bool:
        return self.type == "%"

    @property
    def is_broadcaster(self) -> bool:
        return self.type == Module.BROADCASTER

    def process(self, pulse: Pulse) -> list[Pulse]:
        if self.is_broadcaster:
            return [
                Pulse(Module.BROADCASTER, o, pulse.value) for o in self.outputs
            ]
        elif self.is_flip_flop:
            if pulse.value == PulseType.LOW:
                new_state = self.state[Module.SELF].flipped()
                self.state[Module.SELF] = new_state
                return [
                    Pulse.low(pulse.dst, o)
                    if new_state == PulseType.LOW
                    else Pulse.high(pulse.dst, o)
                    for o in self.outputs
                ]
            else:
                return []
        else:
            self.state[pulse.src] = pulse.value
            all_high = all(v == PulseType.HIGH for v in self.state.values())
            return [
                Pulse.low(pulse.dst, o)
                if all_high
                else Pulse.high(pulse.dst, o)
                for o in self.outputs
            ]


class Modules:
    def __init__(self, modules: dict[str, Module]) -> None:
        self.modules = modules

    @classmethod
    def from_input(cls, input_data: InputData) -> Modules:
        modules = dict[str, Module]()
        for line in input_data:
            a, b = line.split(" -> ")
            outputs = [_ for _ in b.split(", ")]
            if a == Module.BROADCASTER:
                modules[Module.BROADCASTER] = Module(
                    Module.BROADCASTER, outputs
                )
            else:
                modules[a[1:]] = Module(a[0], outputs)
        for m in modules:
            module = modules[m]
            for o in module.outputs:
                m1 = modules.get(o, None)
                if m1 and m1.is_conjunction:
                    m1.state[m] = PulseType.LOW
            if module.is_flip_flop:
                module.state[Module.SELF] = PulseType.LOW
        return Modules(modules)

    def get_module_with_output_rx(self) -> str:
        return next(
            k
            for k, v in self.modules.items()
            if any(o == "rx" for o in v.outputs)
        )

    def push_button(self, listener: PulseListener) -> None:
        q = deque[Pulse]()
        q.append(Pulse.low(Module.BUTTON, Module.BROADCASTER))
        while len(q) > 0:
            pulse = q.popleft()
            listener.on_pulse(pulse)
            if target := self.modules.get(pulse.dst, None):
                for p in target.process(pulse):
                    q.append(p)


Input = Modules
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Modules.from_input(input_data)

    def part_1(self, modules: Input) -> Output1:
        class Listener(PulseListener):
            def __init__(self) -> None:
                self.lo = 0
                self.hi = 0

            def on_pulse(self, pulse: Pulse) -> None:
                if pulse.is_low:
                    self.lo += 1
                else:
                    self.hi += 1

        listener = Listener()
        for i in range(1000):
            modules.push_button(listener)
        return listener.lo * listener.hi

    def part_2(self, modules: Input) -> Output2:
        class Listener(PulseListener):
            def on_pulse(self, pulse: Pulse) -> None:
                if pulse.dst == to_rx and pulse.is_high:
                    memo[pulse.src].append(pushes)

        memo = defaultdict[str, list[int]](list)
        to_rx = modules.get_module_with_output_rx()
        pushes = 0
        listener = Listener()
        for i in range(10_000):
            pushes += 1
            modules.push_button(listener)
            if memo and all(len(v) > 1 for v in memo.values()):
                return reduce(lcm, (v[1] - v[0] for v in memo.values()))
        raise RuntimeError("unsolvable")

    @aoc_samples(
        (
            ("part_1", TEST1, 32000000),
            ("part_1", TEST2, 11687500),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 20)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

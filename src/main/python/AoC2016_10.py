#! /usr/bin/env python3
#
# Advent of Code 2016 Day 10
#

from __future__ import annotations

import sys
from collections import deque
from dataclasses import dataclass
from enum import Enum
from enum import auto
from enum import unique
from math import prod
from typing import Iterable
from typing import Self

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples


@dataclass(frozen=True)
class BotInput:
    value: int
    to_bot: int

    @classmethod
    def from_input(cls, string: str) -> Self:
        v, t = string[len("value ") :].split(" goes to bot ")
        return cls(int(v), int(t))


@unique
class Type(Enum):
    TO_BOT = auto()
    TO_OUT = auto()


@dataclass(frozen=True)
class BotActionType:
    type: Type
    target: int


@dataclass(frozen=True)
class BotAction:
    type: BotActionType
    value: int


@dataclass(frozen=True)
class BotResponse:
    bot: Bot
    lo: BotAction | None
    hi: BotAction | None


@dataclass(frozen=True)
class Comparison:
    lo_value: int
    hi_value: int


@dataclass(frozen=True)
class Bot:
    number: int
    lo_action: BotActionType
    hi_action: BotActionType
    values: list[int]
    comparisons: set[Comparison]

    @classmethod
    def from_input(cls, string: str) -> Self:
        bot, _, _, _, out1, t1, _, _, _, out2, t2 = string[
            len("bot ") :
        ].split()
        lo_action = BotActionType(
            Type.TO_BOT if out1 == "bot" else Type.TO_OUT, int(t1)
        )
        hi_action = BotActionType(
            Type.TO_BOT if out2 == "bot" else Type.TO_OUT, int(t2)
        )
        return cls(int(bot), lo_action, hi_action, [], set())

    def receive(self, value: int) -> BotResponse:
        values = self.values[:]
        values.append(value)
        comparisons = {_ for _ in self.comparisons}
        if len(values) == 2:
            lo_val, hi_val = sorted(values)
            comparisons.add(Comparison(lo_val, hi_val))
            new_bot = Bot(
                self.number,
                self.lo_action,
                self.hi_action,
                [],
                comparisons,
            )
            lo = BotAction(self.lo_action, lo_val)
            hi = BotAction(self.hi_action, hi_val)
            return BotResponse(new_bot, lo, hi)
        else:
            new_bot = Bot(
                self.number,
                self.lo_action,
                self.hi_action,
                values,
                comparisons,
            )
            return BotResponse(new_bot, None, None)


@dataclass(frozen=True)
class Factory:
    bots: dict[int, Bot]
    outputs: dict[int, int]

    @classmethod
    def from_inputs(cls, inputs: Iterable[str]) -> Self:
        bots = dict[int, Bot]()
        bot_inputs = list[BotInput]()
        for string in inputs:
            if string.startswith("value "):
                bot_inputs.append(BotInput.from_input(string))
            else:
                bot = Bot.from_input(string)
                bots[bot.number] = bot
        return cls(bots, cls.do_run(bots, bot_inputs))

    @classmethod
    def do_run(
        cls, bots: dict[int, Bot], bot_inputs: list[BotInput]
    ) -> dict[int, int]:
        outputs = dict[int, int]()
        q = deque(bot_inputs)
        while len(q) > 0:
            bi = q.popleft()
            output = bots[bi.to_bot].receive(bi.value)
            bots[output.bot.number] = output.bot
            for a in [output.lo, output.hi]:
                if a is None:
                    continue
                match a.type.type:
                    case Type.TO_BOT:
                        q.append(BotInput(a.value, a.type.target))
                    case Type.TO_OUT:
                        outputs[a.type.target] = a.value
                    case _:
                        pass
        return outputs


Input = Factory
Output1 = int
Output2 = int


TEST = """\
value 5 goes to bot 2
bot 2 gives low to bot 1 and high to bot 0
value 3 goes to bot 1
bot 1 gives low to output 1 and high to bot 0
bot 0 gives low to output 2 and high to output 0
value 2 goes to bot 2
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Factory.from_inputs(input_data)

    def solve_1(self, factory: Factory, first: int, second: int) -> int:
        return next(
            b.number
            for b in factory.bots.values()
            if any(
                c.lo_value == first and c.hi_value == second
                for c in b.comparisons
            )
        )

    def part_1(self, factory: Input) -> Output1:
        return self.solve_1(factory, 17, 61)

    def part_2(self, factory: Input) -> Output2:
        return prod(v for k, v in factory.outputs.items() if k in {0, 1, 2})

    @aoc_samples((("part_2", TEST, 30),))
    def samples(self) -> None:
        assert self.solve_1(self.parse_input(TEST.splitlines()), 2, 5) == 2


solution = Solution(2016, 10)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

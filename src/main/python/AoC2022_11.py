#! /usr/bin/env python3
#
# Advent of Code 2022 Day 11
#


import math
import sys
from collections import defaultdict
from collections.abc import Callable
from dataclasses import dataclass
from typing import Self

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
"""


@dataclass(frozen=True)
class Monkey:
    items: list[int]
    operation: Callable[[int], int]
    test: int
    throw: tuple[int, int]

    @classmethod
    def from_input(cls, block: list[str]) -> Self:
        items = list(
            map(int, block[1][len(" Starting items: ") :].split(", "))
        )
        operation = eval("lambda old: " + block[2].split("=")[1])  # noqa:S307
        test = int(block[3].split()[-1])
        true = int(block[4].split()[-1])
        false = int(block[5].split()[-1])
        return cls(items, operation, test, (false, true))


Input = InputData
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def parse(self, inputs: InputData) -> tuple[Monkey, ...]:
        return tuple(
            Monkey.from_input(block) for block in my_aocd.to_blocks(inputs)
        )

    def solve(
        self,
        monkeys: tuple[Monkey, ...],
        rounds: int,
        manage: Callable[[int], int],
    ) -> int:
        def _round(
            monkeys: tuple[Monkey, ...],
            counter: dict[int, int],
            manage: Callable[[int], int],
        ) -> None:
            for i, monkey in enumerate(monkeys):
                for item in monkey.items:
                    level = manage(monkey.operation(item))
                    monkeys[
                        monkey.throw[level % monkey.test == 0]
                    ].items.append(level)
                counter[i] += len(monkey.items)
                monkey.items.clear()

        counter = defaultdict[int, int](int)
        for _ in range(rounds):
            _round(monkeys, counter, manage)
        return math.prod(sorted(counter.values())[-2:])

    def part_1(self, inputs: Input) -> Output1:
        monkeys = self.parse(inputs)
        return self.solve(monkeys, 20, lambda x: x // 3)

    def part_2(self, inputs: Input) -> Output2:
        monkeys = self.parse(inputs)
        mod = math.prod(monkey.test for monkey in monkeys)
        return self.solve(monkeys, 10_000, lambda x: x % mod)

    @aoc_samples(
        (
            ("part_1", TEST, 10_605),
            ("part_2", TEST, 2_713_310_158),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 11)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

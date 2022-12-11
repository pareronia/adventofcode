#! /usr/bin/env python3
#
# Advent of Code 2022 Day 11
#


import math
import re
from collections import defaultdict
from typing import Callable
from typing import NamedTuple

import aocd
from aoc import my_aocd


class Monkey(NamedTuple):
    items: list[int]
    operation: Callable[[int], int]
    test: int
    throw: tuple[int, int]


def _parse(inputs: tuple[str]) -> list[Monkey]:
    def parse_monkey(block: list[str]) -> Monkey:
        items = [int(n) for n in re.findall(r"[0-9]+", block[1])]
        operation = eval("lambda old: " + block[2].split("=")[1])  # nosec
        test = int(block[3].split()[-1])
        true = int(block[4].split()[-1])
        false = int(block[5].split()[-1])
        return Monkey(items, operation, test, (false, true))

    return [parse_monkey(block) for block in my_aocd.to_blocks(inputs)]


def _round(
    monkeys: list[Monkey],
    counter: dict[int, int],
    manage: Callable[[int], int],
):
    for i, monkey in enumerate(monkeys):
        for item in monkey.items:
            level = manage(monkey.operation(item))
            monkeys[monkey.throw[level % monkey.test == 0]].items.append(level)
        counter[i] += len(monkey.items)
        monkey.items.clear()


def _solve(monkeys: list[Monkey], rounds: int, manage: str) -> int:
    counter = defaultdict[int, int](int)
    for _ in range(rounds):
        _round(monkeys, counter, manage)
    return math.prod(sorted(counter.values())[-2:])


def part_1(inputs: tuple[str]) -> int:
    monkeys = _parse(inputs)
    return _solve(monkeys, 20, lambda x: x // 3)


def part_2(inputs: tuple[str]) -> int:
    monkeys = _parse(inputs)
    mod = math.prod(monkey.test for monkey in monkeys)
    return _solve(monkeys, 10_000, lambda x: x % mod)


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
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 11)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 10_605
    assert part_2(TEST) == 2_713_310_158

    inputs = my_aocd.get_input_data(puzzle, 55)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

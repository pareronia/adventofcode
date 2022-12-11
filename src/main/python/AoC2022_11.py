#! /usr/bin/env python3
#
# Advent of Code 2022 Day 11
#


import math
import re
from collections import defaultdict
from typing import NamedTuple

import aocd
from aoc import my_aocd
from aoc.common import log

MOD = 1_000_000_007


class Monkey(NamedTuple):
    items: list[int]
    operation: str
    test: tuple[int, int, int]


def _parse(inputs: tuple[str]) -> dict[int, Monkey]:
    monkeys = dict()
    for i, block in enumerate(my_aocd.to_blocks(inputs)):
        items = [int(n) for n in re.findall(r"\d+", block[1])]
        operation = " ".join(block[2].split()[-2:])
        if operation.endswith("old"):
            operation = "** 2"
        test = int(block[3].split()[-1])
        true = int(block[4].split()[-1])
        false = int(block[5].split()[-1])
        monkeys[i] = Monkey(items, operation, (test, true, false))
    return monkeys


def _round(monkeys: dict, counter: dict, manage: str) -> dict:
    for i in monkeys:
        monkey = monkeys[i]
        while monkey.items:
            item = monkey.items.pop(0)
            counter[i] += 1
            level = eval(  # nosec
                "(" + str(item) + monkey.operation + ")" + manage
            )
            test, true, false = monkey.test
            if level % test == 0:
                monkeys[true].items.append(level)
            else:
                monkeys[false].items.append(level)
    return monkeys


def part_1(inputs: tuple[str]) -> int:
    monkeys = _parse(inputs)
    counter = defaultdict(int)
    log(monkeys)
    for _ in range(20):
        monkeys = _round(monkeys, counter, " // 3")
    log(monkeys)
    log(counter)
    top = [v for k, v in sorted(counter.items(), key=lambda item: item[1])]
    log(top[-2:])
    ans = top[-2] * top[-1]
    return ans


def part_2(inputs: tuple[str]) -> int:
    monkeys = _parse(inputs)
    counter = defaultdict(int)
    mod = math.prod(monkeys[i].test[0] for i in monkeys)
    for i in range(1, 10_001):
        monkeys = _round(monkeys, counter, " % " + str(mod))
        if i == 1 or i == 20 or i % 1_000 == 0:
            log(i)
            log(counter)
    top = [v for k, v in sorted(counter.items(), key=lambda item: item[1])]
    ans = top[-2] * top[-1]
    return ans


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

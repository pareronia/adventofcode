#! /usr/bin/env python3
#
# Advent of Code 2020 Day 16
#

from __future__ import annotations
from typing import NamedTuple
from collections import defaultdict
from collections.abc import Generator
from math import prod
from aoc import my_aocd
import aocd


class Rule(NamedTuple):
    field: str
    valid_ranges: frozenset[tuple[int, int]]

    def validate(self, value: int) -> bool:
        return any(rng[0] <= value <= rng[1] for rng in self.valid_ranges)


class Matches:
    matches: dict[Rule, set[int]]

    def __init__(self, matches: dict[Rule, set[int]]):
        self.matches = matches

    @classmethod
    def create(cls, tickets: tuple[tuple[int]], rules: tuple[Rule]) -> Matches:
        valid_tickets = tuple(t for t in tickets if not _is_invalid(t, rules))
        matches = defaultdict(set)
        for rule in rules:
            for idx in range(len(tickets[0])):
                if all(rule.validate(t[idx]) for t in valid_tickets):
                    matches[rule] |= {idx}
        return Matches(matches)

    def _remove(self, match: tuple[Rule, int]) -> None:
        rule, idx = match
        del self.matches[rule]
        for v in self.matches.values():
            v -= {idx}

    def get_matches(self) -> Generator[tuple[Rule, int]]:
        while len(self.matches) > 0:
            rule = [k for k, v in self.matches.items() if len(v) == 1][0]
            idx = [i for i in self.matches[rule]][0]
            self._remove((rule, idx))
            yield (rule, idx)


def _parse_rule(line: str) -> Rule:
    split1 = line.split(": ")
    ranges = {tuple(int(_) for _ in split.split("-"))
              for split in split1[1].split(" or ")}
    return Rule(split1[0], frozenset(ranges))


def _parse_ticket(line: str) -> tuple[int]:
    return tuple(int(_) for _ in line.split(","))


def _parse(inputs: tuple[str]):
    blocks = my_aocd.to_blocks(inputs)
    rules = tuple(_parse_rule(_) for _ in blocks[0])
    my_ticket = _parse_ticket(blocks[1][1])
    tickets = tuple(_parse_ticket(_) for _ in blocks[2][1:])
    return rules, my_ticket, tickets


def _does_not_match_any_rule(value: int, rules: tuple[Rule]) -> bool:
    return not any(r.validate(value) for r in rules)


def _get_invalid_values(ticket: tuple[int], rules: tuple[Rule]) -> tuple[int]:
    return tuple(v for v in ticket if _does_not_match_any_rule(v, rules))


def _is_invalid(ticket: tuple[int], rules: tuple[Rule]) -> bool:
    return any(_does_not_match_any_rule(v, rules) for v in ticket)


def part_1(inputs: tuple[str]) -> int:
    rules, _, tickets = _parse(inputs)
    return sum(v for t in tickets for v in _get_invalid_values(t, rules))


def part_2(inputs: tuple[str]) -> int:
    rules, my_ticket, tickets = _parse(inputs)
    matches = Matches.create(tickets, rules)
    return prod(my_ticket[idx]
                for rule, idx in matches.get_matches()
                if rule.field.startswith("departure "))


TEST1 = """\
class: 1-3 or 5-7
row: 6-11 or 33-44
seat: 13-40 or 45-50

your ticket:
7,1,14

nearby tickets:
7,3,47
40,4,50
55,2,20
38,6,12
""".splitlines()
TEST2 = """\
departure date: 0-1 or 4-19
departure time: 0-5 or 8-19
departure track: 0-13 or 16-19

your ticket:
11,12,13

nearby tickets:
3,9,18
15,1,5
5,14,9
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2020, 16)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 71
    assert part_2(TEST2) == 1716

    inputs = my_aocd.get_input_data(puzzle, 261)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()

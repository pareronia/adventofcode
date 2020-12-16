#! /usr/bin/env python3
#
# Advent of Code 2020 Day 16
#

from dataclasses import dataclass
import my_aocd
from common import log


@dataclass
class Rule:
    field: str
    valid_ranges: set[tuple[int, int]]

    def validate(self, value: int) -> bool:
        valid = False
        for range_ in self.valid_ranges:
            if range_[0] <= value <= range_[1]:
                valid = True
        return valid


def _append_empty_line(lst: tuple[str]) -> list[str]:
    new_lst = list(lst)
    new_lst.append("")
    return new_lst


def parse_rules(lines: list[str]) -> list[Rule]:
    rules = list()
    for line in lines:
        split1 = line.split(": ")
        split2 = split1[1].split(" or ")
        ranges = set()
        for split in split2:
            split3 = split.split("-")
            ranges.add((int(split3[0]), int(split3[1])))
        rule = Rule(split1[0], ranges)
        rules.append(rule)
    return rules


def parse_tickets(lines: list[str]) -> list[int]:
    return [[int(_) for _ in line.split(",")] for line in lines[1:]]


def parse(inputs: tuple[str]):
    _append_empty_line(inputs)
    blocks = list[list[str]]()
    idx = 0
    blocks.append([])
    for input_ in inputs:
        if len(input_) == 0:
            blocks.append([])
            idx += 1
        else:
            blocks[idx].append(input_)
    return parse_rules(blocks[0]), parse_tickets(blocks[2])


def _get_invalid_values(t: list[int], rules: list[Rule]) -> list[int]:
    all_valid_for_one_field = False
    invalid_t = []
    for _ in t:
        valid_for_one_field = False
        for r in rules:
            valid_for_one_field = valid_for_one_field or r.validate(_)
        if not valid_for_one_field:
            invalid_t.append(_)
        all_valid_for_one_field = all_valid_for_one_field \
            and valid_for_one_field
    if not all_valid_for_one_field:
        return invalid_t
    else:
        return []


def part_1(inputs: tuple[str]) -> int:
    rules, tickets = parse(inputs)
    log(rules)
    log(tickets)
    return sum([
        _ for _ in [
            __ for t in tickets for __ in _get_invalid_values(t, rules)
        ]
    ])


def part_2(inputs: tuple[str]) -> int:
    return 0


test = """\
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


def main() -> None:
    my_aocd.print_header(2020, 16)

    assert part_1(test) == 71
    assert part_2(test) == 0

    inputs = my_aocd.get_input_as_tuple(2020, 16, 261)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

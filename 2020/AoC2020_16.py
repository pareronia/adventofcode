#! /usr/bin/env python3
#
# Advent of Code 2020 Day 16
#

from dataclasses import dataclass
from collections import defaultdict
from functools import lru_cache
from math import prod
import my_aocd
from common import log


@dataclass(eq=True, frozen=True)
class Rule:
    field: str
    valid_ranges: frozenset[tuple[int, int]]

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
        rule = Rule(split1[0], frozenset(ranges))
        rules.append(rule)
    return rules


def parse_tickets(lines: list[str]) -> tuple[int]:
    return tuple([tuple([int(_) for _ in line.split(",")]
                        ) for line in lines[1:]]
                 )


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
    rules = parse_rules(blocks[0])
    my_ticket = parse_tickets(blocks[1])[0]
    tickets = parse_tickets(blocks[2])
    return rules, my_ticket, tickets


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
    rules, my_ticket, tickets = parse(inputs)
    log(rules)
    log(tickets)
    return sum([
        _ for _ in [
            __ for t in tickets for __ in _get_invalid_values(t, rules)
        ]
    ])


def _all_values_valid_for_rule(values: list[int], rule: Rule) -> bool:
    all_is_matched = True
    for ticket_i in values:
        all_is_matched = all_is_matched and rule.validate(ticket_i)
    return all_is_matched


@lru_cache()
def _match_ticket_field_and_rule(tickets, i: int, rule: Rule) -> bool:
    ticket_is = [t[i] for t in tickets]
    return _all_values_valid_for_rule(ticket_is, rule)


def _do_match(tickets, idxs: list[int], rules):
    matches = defaultdict(list)
    for i in idxs:
        for rule in rules:
            if _match_ticket_field_and_rule(tickets, i, rule):
                matches[i].append(rule)
    return matches


def _find_fields(target: int, fields, tickets, idxs: list[int], rules):
    if len(idxs) == 0 and len(rules) == 0:
        return
    log("Fields:")
    log(fields)
    matches = _do_match(tickets, idxs, rules)
    log("matches:")
    [log(i) for i in matches.items()]
    temp = [i for i in matches.items()]
    temp.sort(key=lambda m: len(m[1]))
    for t in temp:
        idx_t = t[0]
        matches_for_idx_t = t[1]
        for r_t in matches_for_idx_t:
            field = (idx_t, r_t)
            fields.append(field)
            new_idxs = [idx for idx in idxs if idx != idx_t]
            new_rules = [r for r in rules if r != r_t]
            _find_fields(target, fields, tickets, new_idxs, new_rules)
            if len(fields) == target:
                return
            if fields[-1] == (-1, -1):
                fields.pop()
            if fields[-1] == field:
                fields.pop()


def part_2(inputs: tuple[str]) -> int:
    rules, my_ticket, tickets = parse(inputs)
    log(rules)
    log(my_ticket)
    log("tickets: " + str(len(tickets)))
    assert sum([len(t) for t in tickets]) % len(rules) == 0
    valid_tickets = tuple([t for t in tickets
                           if len(_get_invalid_values(t, rules)) == 0])
    log("valid tickets: " + str(len(valid_tickets)))
    idxs = [i for i in range(len(valid_tickets[0]))]
    fields = list[tuple[int, str]]()
    _find_fields(len(rules), fields, valid_tickets, idxs, rules)
    log("Fields:")
    log(fields)
    departure_fields_values = []
    for f in fields:
        if f[1].field.startswith("departure"):
            departure_fields_values.append(my_ticket[f[0]])
    return prod(departure_fields_values)


test_1 = """\
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
test_2 = """\
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
    my_aocd.print_header(2020, 16)

    assert part_1(test_1) == 71
    assert part_2(test_2) == 1716

    inputs = my_aocd.get_input_as_tuple(2020, 16, 261)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

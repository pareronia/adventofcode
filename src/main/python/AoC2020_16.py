#! /usr/bin/env python3
#
# Advent of Code 2020 Day 16
#

from dataclasses import dataclass
from collections import defaultdict
from functools import lru_cache
from math import prod
from aoc import my_aocd
from aoc.common import log


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
    blocks = my_aocd.to_blocks(inputs)
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


@lru_cache(maxsize=400)
def _match_ticket_field_and_rule(tickets, i: int, rule: Rule) -> bool:
    ticket_is = [t[i] for t in tickets]
    return _all_values_valid_for_rule(ticket_is, rule)


def _do_match(tickets, idxs: list[int], rules) -> dict[int, list[Rule]]:
    matches = defaultdict(list)
    [matches[i].append(rule) for i in idxs for rule in rules
     if _match_ticket_field_and_rule(tickets, i, rule)]
    return matches


def _find_fields(fields, tickets, idxs: list[int], rules) -> None:
    if len(idxs) == 0 and len(rules) == 0:
        return
    log("Fields:")
    log(fields)
    matches = _do_match(tickets, idxs, rules)
    log("matches:")
    [log(i) for i in matches.items()]
    temp = [i for i in matches.items()]
    temp.sort(key=lambda m: len(m[1]))
    if not len(temp[0][1]) == 1:
        raise RuntimeError("Expected single match")
    idx_t = temp[0][0]
    r_t = temp[0][1][0]
    fields.append((idx_t, r_t))
    new_idxs = [idx for idx in idxs if idx != idx_t]
    new_rules = [r for r in rules if r != r_t]
    _find_fields(fields, tickets, new_idxs, new_rules)


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
    _find_fields(fields, valid_tickets, idxs, rules)
    log("Fields:")
    log(fields)
    log(_match_ticket_field_and_rule.cache_info())
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

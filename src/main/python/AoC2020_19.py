#! /usr/bin/env python3
#
# Advent of Code 2020 Day 19
#

from __future__ import annotations
from typing import NamedTuple
import re
from dataclasses import dataclass
from aoc import my_aocd
from aoc.common import log


@dataclass
class Rule:
    idx: int
    rules: list
    is_sink: bool

    def determine_sink(self):
        isnumeric = False
        for r in self.rules:
            for _ in r:
                isnumeric = isnumeric or _.isnumeric()
        self.is_sink = not isnumeric

    def find(self, value: str) -> list[tuple[int, int]]:
        found = list[tuple[int, int]]()
        for i, rule in enumerate(self.rules):
            for j, v in enumerate(rule):
                if v == value:
                    found.append((i, j))
        return found


def _parse(inputs: tuple[str]):
    blocks = my_aocd.to_blocks(inputs)
    rules = dict()
    for input_ in blocks[0]:
        splits = input_.split(": ")
        splits[1] = splits[1].replace("\"", "")
        rules[int(splits[0])] = splits[1]
    messages = blocks[1]
    return rules, messages


def _reduce_rules(rules: dict) -> None:
    sinks = [r[0] for r in rules.items()
             if not bool(re.search(r'\d', r[1]))]
    for r in rules.items():
        for sink in sinks:
            sink_key = str(sink)
            to_find = r'\b{s}\b'.format(s=sink_key)
            if bool(re.search(to_find, r[1])):
                sink_value = rules[sink]
                old_value = rules[r[0]]
                to_replace = r'\b{s}\b'.format(s=sink_key)
                rules[r[0]] = re.sub(to_replace,
                                     "(" + sink_value + ")",
                                     old_value)
    new_sinks = [r[0] for r in rules.items()
                 if not bool(re.search(r'\d', r[1]))]
    if new_sinks == sinks:
        return
    for sink in sinks:
        del rules[sink]
    log(rules)
    _reduce_rules(rules)


def _compact_rules(rules: dict) -> dict:
    for r in rules.items():
        regex = r"\((\w{1})\) \((\w{1})\)"
        subst = "(\\1*\\2)"
        result = re.sub(regex, subst, r[1], flags=re.MULTILINE)
        rules[r[0]] = result


def _translate_rules(rules: dict) -> dict:
    for r in rules.items():
        regex = r"\) \("
        subst = ")*("
        result = re.sub(regex, subst, r[1], flags=re.MULTILINE)
        rules[r[0]] = result
    for r in rules.items():
        regex = r" \| "
        subst = "+"
        result = re.sub(regex, subst, r[1], flags=re.MULTILINE)
        rules[r[0]] = result
    for r in rules.items():
        regex = r"\(a\)"
        subst = "a"
        result = re.sub(regex, subst, r[1], flags=re.MULTILINE)
        rules[r[0]] = result
    for r in rules.items():
        regex = r"\(b\)"
        subst = "b"
        result = re.sub(regex, subst, r[1], flags=re.MULTILINE)
        rules[r[0]] = result


class ResultAndPosition(NamedTuple):
    result: list
    position: int


def _build_rule(rule: str, pos: int = 0) -> ResultAndPosition:
    """ build chain: * = append, + = new item(=list)"""
    stack = ["", "*"]
    result = list()
    i = pos
    while i < len(rule):
        _ = rule[i]
        breakpoint()
        if _ in {"+", "*"}:
            stack.append([_])
        elif _ == ")":
            return ResultAndPosition(result, i)
        else:
            if _ == "(":
                sub = _build_rule(rule, i+1)
                operand_2 = sub.result
                i = sub.position
            elif _ not in {'a', 'b'}:
                raise ValueError("invalid input")
            else:
                operand_2 = _
            operator = stack.pop()[0]
            operand_1 = stack.pop()
            if operator == "*":
                operand_1.append(operand_2)
                result.append(operand_1)
            else:
                result.append([operand_1, operand_2])
            stack.append(result)
        i += 1
    return ResultAndPosition(result, i)


def _log_tree(tree):
    if type(tree) == list:
        for tree_item in tree:
            _log_tree(tree_item)
    else:
        for tree_item in tree:
            log(tree_item)


def _to_regex(rule: str) -> str:
    rule = rule.replace("*", "")
    return "^" + rule.replace(" ", "") + "$"


def _construct_rule(rules: dict) -> str:
    _reduce_rules(rules)
    # log(rules)
    _compact_rules(rules)
    # log(rules)
    # _translate_rules(rules)
    # log(rules)
    # _log_tree(_build_rule(rules[0])[0])
    return rules[0]


def part_1(inputs: tuple[str]) -> int:
    rules, messages = _parse(inputs)
    # log(rules)
    # log(messages)
    rule = _construct_rule(rules)
    assert len(rules) == 1
    assert rules.keys() == {0}
    regex = _to_regex(rule)
    log(regex)

    return sum([1 for message in messages if re.match(regex, message)])


def part_2(inputs: tuple[str]) -> int:
    rules, messages = _parse(inputs)
    # log(rules)
    # log(messages)
    # rules[8] = "42 | 42 8"
    # rules[11] = "42 31 | 42 11 31"
    rules[8] = "(42)+"
    # rules[11] = "(42)+ (31)+"
    rules[11] = "42 31 | 42 42 31 31 | 42 42 42 31 31 31"\
                " | 42 42 42 42 31 31 31 31"
    _reduce_rules(rules)
    log(rules)
    assert len(rules) == 1
    assert rules.keys() == {0}
    rule = rules[0]
    regex = "^" + rule.replace(" ", "") + "$"
    log(regex)

    result = sum([1 for message in messages
                  if re.match(regex, message)])
    log(result)
    return result


test_1 = """\
0: 4 1 5
1: 2 3 | 3 2
2: 4 4 | 5 5
3: 4 5 | 5 4
4: "a"
5: "b"

ababbb
bababa
abbbab
aaabbb
aaaabbb
""".splitlines()
test_2 = """\
42: 9 14 | 10 1
9: 14 27 | 1 26
10: 23 14 | 28 1
1: "a"
11: 42 31
5: 1 14 | 15 1
19: 14 1 | 14 14
12: 24 14 | 19 1
16: 15 1 | 14 14
31: 14 17 | 1 13
6: 14 14 | 1 14
2: 1 24 | 14 4
0: 8 11
13: 14 3 | 1 12
15: 1 | 14
17: 14 2 | 1 7
23: 25 1 | 22 14
28: 16 1
4: 1 1
20: 14 14 | 1 15
3: 5 14 | 16 1
27: 1 6 | 14 18
14: "b"
21: 14 1 | 1 14
25: 1 1 | 1 14
22: 14 14
8: 42
26: 14 22 | 1 20
18: 15 15
7: 14 5 | 1 21
24: 14 1

abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa
bbabbbbaabaabba
babbbbaabbbbbabbbbbbaabaaabaaa
aaabbbbbbaaaabaababaabababbabaaabbababababaaa
bbbbbbbaaaabbbbaaabbabaaa
bbbababbbbaaaaaaaabbababaaababaabab
ababaaaaaabaaab
ababaaaaabbbaba
baabbaaaabbaaaababbaababb
abbbbabbbbaaaababbbbbbaaaababb
aaaaabbaabaaaaababaa
aaaabbaaaabbaaa
aaaabbaabbaaaaaaabbbabbbaaabbaabaaa
babaaabbbaaabaababbaabababaaab
aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 19)

    assert part_1(test_1) == 2
    assert part_1(test_2) == 3
    assert part_2(test_2) == 12

    inputs = my_aocd.get_input_as_tuple(2020, 19, 534)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

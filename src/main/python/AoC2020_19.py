#! /usr/bin/env python3
#
# Advent of Code 2020 Day 19
#

from __future__ import annotations

import re
import sys
from copy import deepcopy

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

TEST1 = """\
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
"""
TEST2 = """\
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
"""


def _parse(inputs: tuple[str, ...]) -> tuple[dict[int, str], list[str]]:
    blocks = my_aocd.to_blocks(inputs)
    rules = dict()
    for input_ in blocks[0]:
        splits = input_.split(": ")
        splits[1] = splits[1].replace('"', "")
        rules[int(splits[0])] = splits[1]
    messages = blocks[1]
    return rules, messages


def _to_regex(rules: dict[int, str]) -> str:
    def reduce_rules() -> None:
        sinks = [r[0] for r in rules.items() if not re.search(r"\d", r[1])]
        for r in rules.items():
            for sink in sinks:
                sink_key = str(sink)
                to_find = r"\b{s}\b".format(s=sink_key)
                if bool(re.search(to_find, r[1])):
                    sink_value = rules[sink]
                    old_value = rules[r[0]]
                    to_replace = r"\b{s}\b".format(s=sink_key)
                    rules[r[0]] = re.sub(
                        to_replace, "(" + sink_value + ")", old_value
                    )
        new_sinks = [r[0] for r in rules.items() if not re.search(r"\d", r[1])]
        if new_sinks == sinks:
            assert len(rules) == 1
            assert rules.keys() == {0}
            return
        for sink in sinks:
            del rules[sink]
        log(rules)
        reduce_rules()

    def compact_rules() -> None:
        for r in rules.items():
            regex = r"\((\w{1})\) \((\w{1})\)"
            subst = "(\\1*\\2)"
            result = re.sub(regex, subst, r[1], flags=re.MULTILINE)
            rules[r[0]] = result

    reduce_rules()
    # log(rules)
    compact_rules()
    # log(rules)
    return "^" + rules[0].replace("*", "").replace(" ", "") + "$"


Input = tuple[dict[int, str], list[str]]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, inputs: InputData) -> Input:
        return _parse(tuple(inputs))

    def part_1(self, inputs: Input) -> int:
        rules, messages = deepcopy(inputs[0]), inputs[1]
        regex = _to_regex(rules)
        return sum(1 for message in messages if re.match(regex, message))

    def part_2(self, inputs: Input) -> int:
        rules, messages = deepcopy(inputs[0]), inputs[1]
        # rules[8] = "42 | 42 8"
        rules[8] = "(42)+"
        # rules[11] = "42 31 | 42 11 31"
        rules[11] = "|".join("42 " * i + "31 " * i for i in range(1, 6))
        regex = _to_regex(rules)
        return sum(1 for message in messages if re.match(regex, message))

    @aoc_samples(
        (
            ("part_1", TEST1, 2),
            ("part_1", TEST2, 3),
            ("part_2", TEST2, 12),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2020, 19)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

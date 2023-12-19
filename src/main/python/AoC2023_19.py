#! /usr/bin/env python3
#
# Advent of Code 2023 Day 19
#

from __future__ import annotations

import sys
from collections import defaultdict
from math import prod
from typing import NamedTuple

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

TEST = """\
px{a<2006:qkq,m>2090:A,rfg}
pv{a>1716:R,A}
lnx{m>1548:A,A}
rfg{s<537:gd,x>2440:R,A}
qs{s>3448:A,lnx}
qkq{x<1416:A,crn}
crn{x>2662:A,R}
in{s<1351:px,qqz}
qqz{s>2770:qs,m<1801:hdj,R}
gd{a>3333:R,R}
hdj{m>838:A,pv}

{x=787,m=2655,a=1222,s=2876}
{x=1679,m=44,a=2067,s=496}
{x=2036,m=264,a=79,s=2244}
{x=2461,m=1339,a=466,s=291}
{x=2127,m=1623,a=2188,s=1013}
"""


Range = tuple[int, int]


class Part(NamedTuple):
    x: int
    m: int
    a: int
    s: int

    def score(self) -> int:
        return sum([self.x, self.m, self.a, self.s])


class PartRange(NamedTuple):
    x: Range
    m: Range
    a: Range
    s: Range

    def copy_with(self, prop: str, value: Range) -> PartRange:
        return PartRange(
            value if prop == "x" else self.x,
            value if prop == "m" else self.m,
            value if prop == "a" else self.a,
            value if prop == "s" else self.s,
        )

    def score(self) -> int:
        return prod(r[1] - r[0] + 1 for r in [self.x, self.m, self.a, self.s])


class Rule(NamedTuple):
    operand1: str
    operation: str
    operand2: int
    result: str

    def eval(self, part: Part) -> str | None:
        if (
            self.operation == "<"
            and int.__lt__(getattr(part, self.operand1), self.operand2)
        ) or (
            self.operation == ">"
            and int.__gt__(getattr(part, self.operand1), self.operand2)
        ):
            return self.result
        else:
            return None

    def eval_range(self, r: PartRange) -> list[tuple[PartRange, str | None]]:
        if self.operand2 == 0:
            # TODO: janky!!
            nr = r.copy_with(self.operand1, getattr(r, self.operand1))
            return [(nr, self.result)]
        lo, hi = getattr(r, self.operand1)
        if self.operation == "<":
            match = (lo, self.operand2 - 1)
            nomatch = (self.operand2, hi)
        else:
            match = (self.operand2 + 1, hi)
            nomatch = (lo, self.operand2)
        ans = list[tuple[PartRange, str | None]]()
        if match[0] <= match[1]:
            nr = r.copy_with(self.operand1, match)
            ans.append((nr, self.result))
        if nomatch[0] <= nomatch[1]:
            nr = r.copy_with(self.operand1, nomatch)
            ans.append((nr, None))
        return ans


class Workflow(NamedTuple):
    name: str
    rules: list[Rule]

    def eval(self, part: Part) -> str:
        for rule in self.rules:
            res = rule.eval(part)
            # log(
            #     f"eval [{self.name}: "
            #     f"{rule.operand1}{rule.operation}{rule.operand2}] on {part}"
            #     f"-> {res}"
            # )
            if res is not None:
                return res
        assert False

    def eval_range(self, range: PartRange) -> list[tuple[PartRange, str]]:
        ans = list[tuple[PartRange, str]]()
        ranges = [range]
        for rule in self.rules:
            new_ranges = list[PartRange]()
            for r in ranges:
                ress = rule.eval_range(r)
                for res in ress:
                    if res[1] is not None:
                        ans.append((res[0], res[1]))
                    else:
                        new_ranges.append(res[0])
                log(
                    f"eval [{self.name}: "
                    f"{rule.operand1}{rule.operation}{rule.operand2}]"
                    f" on {r} -> {ress}"
                )
            ranges = new_ranges
        return ans


class System(NamedTuple):
    workflows: dict[str, Workflow]
    parts: list[Part]


Input = System
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        ww, pp = my_aocd.to_blocks(input_data)
        workflows = dict[str, Workflow]()
        for w in ww:
            i = w.index("{")
            name = w[:i]
            rules = list[Rule]()
            rr = w[:-1][i + 1 :].split(",")  # noqa[E203]
            for r in rr:
                if ":" in r:
                    op, res = r.split(":")
                    operand1 = op[0]
                    operation = op[1]
                    operand2 = int(op[2:])
                else:
                    operand1 = "x"
                    operation = ">"
                    operand2 = 0
                    res = r
                rules.append(Rule(operand1, operation, operand2, res))
            workflows[name] = Workflow(name, rules)
        parts = list[Part]()
        for p in pp:
            x, m, a, s = p[1:-1].split(",")
            parts.append(
                Part(
                    int(x.split("=")[1]),
                    int(m.split("=")[1]),
                    int(a.split("=")[1]),
                    int(s.split("=")[1]),
                )
            )
        return System(workflows, parts)

    def part_1(self, system: Input) -> Output1:
        ans = 0
        for part in system.parts:
            w = system.workflows["in"]
            while True:
                res = w.eval(part)
                if res == "A":
                    ans += part.score()
                    break
                elif res == "R":
                    break
                else:
                    w = system.workflows[res]
                    continue
        return ans

    def part_2(self, system: Input) -> Output2:
        ans = 0
        prs = [(PartRange((1, 4000), (1, 4000), (1, 4000), (1, 4000)), "in")]
        d = defaultdict[str, int](int)
        d["in"] = 4000**4
        log(d)
        while prs:
            new_prs = list[tuple[PartRange, str]]()
            for pr in prs:
                for res in system.workflows[pr[1]].eval_range(pr[0]):
                    d[res[1]] += res[0].score()
                    if res[1] == "R":
                        continue
                    elif res[1] == "A":
                        ans += res[0].score()
                        continue
                    else:
                        new_prs.append((res[0], res[1]))
            log(d)
            prs = new_prs
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 19114),
            ("part_2", TEST, 167409079868000),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 19)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

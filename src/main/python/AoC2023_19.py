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
from aoc.common import clog

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
ACCEPTED = "A"
REJECTED = "R"
CONTINUE = "=continue="
CATCHALL = "catch-all"
IN = "in"


class Part(NamedTuple):
    x: int
    m: int
    a: int
    s: int

    @classmethod
    def from_str(cls, string: str) -> Part:
        x, m, a, s = [int(sp.split("=")[1]) for sp in string[1:-1].split(",")]
        return Part(x, m, a, s)

    def score(self) -> int:
        return sum([self.x, self.m, self.a, self.s])


class PartRange(NamedTuple):
    x: Range
    m: Range
    a: Range
    s: Range

    @classmethod
    def from_part(cls, part: Part) -> PartRange:
        return PartRange(
            (part.x, part.x),
            (part.m, part.m),
            (part.a, part.a),
            (part.s, part.s),
        )

    def copy_with(self, prop: str, value: Range) -> PartRange:
        return PartRange(
            value if prop == "x" else self.x,
            value if prop == "m" else self.m,
            value if prop == "a" else self.a,
            value if prop == "s" else self.s,
        )

    def copy(self) -> PartRange:
        return PartRange(self.x, self.m, self.a, self.s)

    def matches(self, part: Part) -> bool:
        return (
            self.x[0] <= part.x <= self.x[1]
            and self.m[0] <= part.m <= self.m[1]
            and self.a[0] <= part.a <= self.a[1]
            and self.s[0] <= part.s <= self.s[1]
        )

    def score(self) -> int:
        return prod(r[1] - r[0] + 1 for r in [self.x, self.m, self.a, self.s])


class Rule(NamedTuple):
    operand1: str | None
    operation: str
    operand2: int | None
    result: str

    @classmethod
    def from_str(cls, string: str) -> Rule:
        if ":" in string:
            op, res = string.split(":")
            return Rule(op[0], op[1], int(op[2:]), res)
        else:
            return Rule(None, CATCHALL, None, string)

    def eval(self, range: PartRange) -> list[tuple[PartRange, str]]:
        if self.operation == CATCHALL:
            return [(range.copy(), self.result)]
        else:
            assert self.operand1 is not None and self.operand2 is not None
            lo, hi = getattr(range, self.operand1)
            if self.operation == "<":
                match = (lo, self.operand2 - 1)
                nomatch = (self.operand2, hi)
            else:
                match = (self.operand2 + 1, hi)
                nomatch = (lo, self.operand2)
            ans = list[tuple[PartRange, str]]()
            if match[0] <= match[1]:
                nr = range.copy_with(self.operand1, match)
                ans.append((nr, self.result))
            if nomatch[0] <= nomatch[1]:
                nr = range.copy_with(self.operand1, nomatch)
                ans.append((nr, CONTINUE))
            return ans


class Workflow(NamedTuple):
    name: str
    rules: list[Rule]

    @classmethod
    def from_str(cls, string: str) -> Workflow:
        i = string.index("{")
        name = string[:i]
        rules = [
            Rule.from_str(r)
            for r in string[:-1][i + 1 :].split(",")  # noqa[E203]
        ]
        return Workflow(name, rules)

    def eval(self, range: PartRange) -> list[tuple[PartRange, str]]:
        ans = list[tuple[PartRange, str]]()
        ranges = [range]
        for rule in self.rules:
            new_ranges = list[PartRange]()
            for r in ranges:
                ress = rule.eval(r)
                for res in ress:
                    if res[1] is not CONTINUE:
                        ans.append((res[0], res[1]))
                    else:
                        new_ranges.append(res[0])
                clog(
                    lambda: f"eval [{self.name}: "
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
        return System(
            {wf.name: wf for wf in [Workflow.from_str(w) for w in ww]},
            [Part.from_str(p) for p in pp],
        )

    def solve(
        self, workflows: dict[str, Workflow], prs: list[tuple[PartRange, str]]
    ) -> dict[str, list[PartRange]]:
        solution = defaultdict[str, list[PartRange]](list)
        while prs:
            new_prs = list[tuple[PartRange, str]]()
            for pr in prs:
                for res in workflows[pr[1]].eval(pr[0]):
                    if res[1] == REJECTED or res[1] == ACCEPTED:
                        solution[res[1]].append(res[0])
                    else:
                        new_prs.append((res[0], res[1]))
            prs = new_prs
        return solution

    def part_1(self, system: Input) -> Output1:
        solution = self.solve(
            system.workflows,
            [(PartRange.from_part(part), IN) for part in system.parts],
        )
        return sum(
            part.score()
            for part in system.parts
            if [acc for acc in solution[ACCEPTED] if acc.matches(part)]
        )

    def part_2(self, system: Input) -> Output2:
        solution = self.solve(
            system.workflows,
            [(PartRange((1, 4000), (1, 4000), (1, 4000), (1, 4000)), IN)],
        )
        rej, acc = [
            sum(
                sum(range.score() for range in ranges)
                for k, ranges in solution.items()
                if k == kk
            )
            for kk in (REJECTED, ACCEPTED)
        ]
        assert acc + rej == 4000**4
        return acc

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

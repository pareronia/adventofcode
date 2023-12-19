#! /usr/bin/env python3
#
# Advent of Code 2023 Day 19
#

import sys
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


class Part(NamedTuple):
    x: int
    m: int
    a: int
    s: int

    def score(self) -> int:
        return sum([self.x, self.m, self.a, self.s])


class Rule(NamedTuple):
    operation: str
    result: str

    def eval(self, part: Part) -> str | None:
        if self.operation.startswith("x"):
            x = part.x  # noqa[F841]
        elif self.operation.startswith("m"):
            m = part.m  # noqa[F841]
        elif self.operation.startswith("a"):
            a = part.a  # noqa[F841]
        elif self.operation.startswith("s"):
            s = part.s  # noqa[F841]
        if eval(self.operation):  # nosec
            return self.result
        else:
            return None


class Workflow(NamedTuple):
    name: str
    rules: list[Rule]

    def eval(self, part: Part) -> str:
        for rule in self.rules:
            res = rule.eval(part)
            log(
                f"eval workflow {self.name}, {rule.operation} on {part=}"
                f"-> {res}"
            )
            if res is not None:
                return res
        assert False


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
                else:
                    op = "True"
                    res = r
                rules.append(Rule(op, res))
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
        log(system)
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

    def part_2(self, input: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 19114),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 19)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

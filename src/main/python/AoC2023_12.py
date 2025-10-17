#! /usr/bin/env python3
#
# Advent of Code 2023 Day 12
#

import sys
from collections.abc import Callable
from functools import cache
from itertools import product

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = InputData
Output1 = int
Output2 = int


TEST = """\
???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1
"""


def count(s: str, counts: tuple[int, ...]) -> int:
    _count.cache_clear()
    return _count(s, counts, 0)


@cache
def _count(s: str, counts: tuple[int, ...], idx: int) -> int:
    # base case: end of s
    if s == "":
        if len(counts) == 0 and idx == 0:
            # no more groups required and not in group -> OK
            return 1
        # still group(s) required or still in group -> NOK
        return 0
    # otherwise:
    ans = 0
    # '?' can be '.' or '#'
    nxts = {".", "#"} if s[0] == "?" else {s[0]}
    for nxt in nxts:
        if nxt == "#":
            # if '#': move to next char in current group
            ans += _count(s[1:], counts, idx + 1)
        elif nxt == ".":
            # if '.' (between groups)
            if idx > 0:
                # was in group before
                if len(counts) > 0 and idx == counts[0]:
                    # finished group matches required -> find next required
                    ans += _count(s[1:], counts[1:], 0)
            else:
                # was not in group: keep looking for next group
                ans += _count(s[1:], counts, 0)
        else:
            # should not happen
            raise AssertionError
    return ans


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def brute_force_count(self, s: str, counts: tuple[int, ...]) -> int:
        ans = 0
        total = sum(counts)
        s_count = s.count("#")
        for p in product("#.", repeat=s.count("?")):
            if s_count + p.count("#") != total:
                continue
            pi = iter(p)
            test = "".join(
                s[i] if s[i] != "?" else next(pi) for i in range(len(s))
            )
            if tuple(len(_) for _ in test.split(".") if len(_) != 0) == counts:
                ans += 1
        return ans

    def solve(
        self, records: Input, f: Callable[[str], tuple[str, tuple[int, ...]]]
    ) -> int:
        return sum(count(*f(line)) for line in records)

    def part_1(self, records: Input) -> Output1:
        def parse(line: str) -> tuple[str, tuple[int, ...]]:
            s, w = line.split()
            counts = tuple(int(_) for _ in w.split(","))
            return s + ".", counts

        return self.solve(records, parse)

    def part_2(self, records: Input) -> Output2:
        def parse(line: str) -> tuple[str, tuple[int, ...]]:
            s, w = line.split()
            counts = tuple(int(_) for _ in w.split(","))
            return "?".join([s] * 5) + ".", counts * 5

        return self.solve(records, parse)

    @aoc_samples(
        (
            ("part_1", TEST, 21),
            ("part_2", TEST, 525152),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 12)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

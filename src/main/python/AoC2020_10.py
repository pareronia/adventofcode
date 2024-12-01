#! /usr/bin/env python3
#
# Advent of Code 2020 Day 10
#

import sys
from collections import Counter
from collections import defaultdict

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST1 = """\
16
10
15
5
1
11
7
19
6
12
4
"""
TEST2 = """\
28
33
18
42
31
14
46
20
48
47
24
23
49
45
19
38
39
11
1
32
25
35
8
17
7
9
4
2
34
10
3
"""

Input = list[int]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        numbers = [0] + sorted(int(_) for _ in input_data)
        numbers.append(numbers[-1] + 3)
        return numbers

    def part_1(self, numbers: Input) -> int:
        cnt = Counter(
            numbers[i] - numbers[i - 1] for i in range(1, len(numbers))
        )
        return cnt[1] * cnt[3]

    def part_2(self, numbers: Input) -> int:
        seen = defaultdict(lambda: 0)
        seen[0] = 1
        for i, n in enumerate(numbers[1:]):
            for j in (
                numbers[k] for k in range(i, -1, -1) if n - numbers[k] <= 3
            ):
                seen[n] += seen[j]
        return seen[numbers[-1]]

    @aoc_samples(
        (
            ("part_1", TEST1, 35),
            ("part_1", TEST2, 220),
            ("part_2", TEST1, 8),
            ("part_2", TEST2, 19208),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2020, 10)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

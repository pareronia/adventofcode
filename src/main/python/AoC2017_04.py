#! /usr/bin/env python3
#
# Advent of Code 2017 Day 4
#

import sys
from collections import Counter
from typing import Callable

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[str]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)

    def solve(
        self, inputs: list[str], strategy: Callable[[list[str]], bool]
    ) -> int:
        return sum(strategy(line.split()) for line in inputs)

    def part_1(self, inputs: list[str]) -> int:
        def has_no_duplicate_words(words: list[str]) -> bool:
            return Counter(words).most_common(1)[0][1] == 1

        return self.solve(inputs, has_no_duplicate_words)

    def part_2(self, inputs: list[str]) -> int:
        def has_no_anagrams(words: list[str]) -> bool:
            return len(words) == len(
                {tuple(sorted(Counter(word).items())) for word in words}
            )

        return self.solve(inputs, has_no_anagrams)

    @aoc_samples(
        (
            ("part_1", "aa bb cc dd ee", 1),
            ("part_1", "aa bb cc dd aa", 0),
            ("part_1", "aa bb cc dd aaa", 1),
            ("part_2", "abcde fghij", 1),
            ("part_2", "abcde xyz ecdab", 0),
            ("part_2", "a ab abc abd abf abj", 1),
            ("part_2", "iiii oiii ooii oooi oooo", 1),
            ("part_2", "oiii ioii iioi iiio", 0),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2017, 4)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2015 Day 11
#

import re
import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

ALPH = "abcdefghijklmnopqrstuvwxyz"
NEXT = "bcdefghjjkmmnppqrstuvwxyza"
CONFUSING_LETTERS = {"i", "o", "l"}
RE = re.compile(r"([a-z])\1")

Input = str
Output1 = str
Output2 = str


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return next(iter(input_data))

    def is_ok(self, password: str) -> bool:
        if len(set(RE.findall(password))) < 2:
            return False
        trio = False
        for i in range(len(password)):
            if password[i] in CONFUSING_LETTERS:
                return False
            trio = trio or (
                i < len(password) - 3
                and ord(password[i]) == ord(password[i + 1]) - 1
                and ord(password[i + 1]) == ord(password[i + 2]) - 1
            )
        return trio

    def get_next(self, password: str) -> str:
        def increment(password: str, i: int) -> str:
            password = (
                password[:i]
                + NEXT[ALPH.index(password[i])]
                + password[i + 1 :]
            )
            if password[i] == "a":
                password = increment(password, i - 1)
            return password

        while True:
            password = increment(password, len(password) - 1)
            if self.is_ok(password):
                break
        return password

    def part_1(self, password: Input) -> Output1:
        return self.get_next(password)

    def part_2(self, password: Input) -> Output2:
        return self.get_next(self.get_next(password))

    @aoc_samples(
        (
            ("part_1", "abcdefgh", "abcdffaa"),
            ("part_1", "ghijklmn", "ghjaabcc"),
        )
    )
    def samples(self) -> None:
        assert not self.is_ok("abci")
        assert not self.is_ok("hijklmmn")
        assert not self.is_ok("abbceffg")
        assert not self.is_ok("abbcegjk")


solution = Solution(2015, 11)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2017 Day 16
#
import sys
from dataclasses import dataclass
from string import ascii_lowercase
from typing import Protocol
from typing import Self

from aoc.common import InputData
from aoc.common import SolutionBase

TEST = "s1,x3/4,pe/b"

PROGRAMS = ascii_lowercase[:16]


class Move(Protocol):
    def execute(self, d: list[str]) -> list[str]:
        pass


@dataclass(frozen=True)
class Spin(Move):
    amount: int

    @classmethod
    def from_input(cls, s: str) -> Self:
        return cls(int(s))

    def execute(self, d: list[str]) -> list[str]:
        n = len(d) - self.amount
        return d[n:] + d[:n]


@dataclass(frozen=True)
class Exchange(Move):
    pos_1: int
    pos_2: int

    @classmethod
    def from_input(cls, s: str) -> Self:
        a, b = s.split("/")
        return cls(int(a), int(b))

    def execute(self, d: list[str]) -> list[str]:
        tmp = d[self.pos_1]
        d[self.pos_1] = d[self.pos_2]
        d[self.pos_2] = tmp
        return d


@dataclass(frozen=True)
class Partner(Move):
    program_1: str
    program_2: str

    @classmethod
    def from_input(cls, s: str) -> Self:
        a, b = s.split("/")
        return cls(a, b)

    def execute(self, d: list[str]) -> list[str]:
        pos_1, pos_2 = -1, -1
        for i in range(len(d)):
            if d[i] == self.program_1:
                pos_1 = i
            if d[i] == self.program_2:
                pos_2 = i
            if pos_1 >= 0 and pos_2 >= 0:
                break
        tmp = d[pos_1]
        d[pos_1] = d[pos_2]
        d[pos_2] = tmp
        return d


@dataclass(frozen=True)
class Dance:
    moves: list[Move]

    @classmethod
    def parse_move(cls, s: str) -> Move:
        match s[0]:
            case "s":
                return Spin.from_input(s[1:])
            case "x":
                return Exchange.from_input(s[1:])
            case _:
                return Partner.from_input(s[1:])

    @classmethod
    def from_input(cls, inputs: InputData) -> Self:
        return cls([cls.parse_move(s) for s in next(iter(inputs)).split(",")])

    def execute(self, string: str, reps: int) -> str:
        d = list(string)
        for _ in range(reps):
            for move in self.moves:
                d = move.execute(d)
        return "".join(d)


Input = Dance
Output1 = str
Output2 = str


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Dance.from_input(input_data)

    def part_1(self, dance: Dance) -> str:
        return dance.execute(PROGRAMS, reps=1)

    def part_2(self, dance: Dance) -> str:
        cnt = 0
        ans = PROGRAMS
        while True:
            ans = dance.execute(ans, reps=1)
            cnt += 1
            if ans == PROGRAMS:
                break
        return dance.execute(PROGRAMS, reps=1_000_000_000 % cnt)

    def samples(self) -> None:
        assert Spin.from_input("3").execute(list("abcde")) == list("cdeab")
        assert Exchange.from_input("3/4").execute(list("abcde")) == list(
            "abced"
        )
        assert Partner.from_input("a/c").execute(list("abcde")) == list(
            "cbade"
        )
        assert Dance.from_input([TEST]).execute("abcde", 1) == "baedc"
        assert Dance.from_input([TEST]).execute("abcde", 2) == "ceadb"


solution = Solution(2017, 16)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

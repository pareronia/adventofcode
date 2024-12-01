#! /usr/bin/env python3
#
# Advent of Code 2021 Day 8
#

from __future__ import annotations

import sys
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | \
fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | \
fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | \
cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | \
efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | \
gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | \
gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | \
cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | \
ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | \
gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | \
fgae cfgab fg bagce
"""


class SignalAndOutput(NamedTuple):
    signals: list[str]
    outputs: list[str]

    @classmethod
    def from_str(cls, s: str) -> SignalAndOutput:
        signals = ["".join(sorted(_)) for _ in s.split(" | ")[0].split()]
        outputs = ["".join(sorted(_)) for _ in s.split(" | ")[1].split()]
        return SignalAndOutput(signals, outputs)


Input = list[SignalAndOutput]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [SignalAndOutput.from_str(s) for s in input_data]

    def part_1(self, input: Input) -> int:
        return sum(
            len(word) in {2, 3, 4, 7}
            for line in (line for _, line in input)
            for word in line
        )

    def part_2(self, input: Input) -> int:
        def solve(input: SignalAndOutput) -> int:
            def find(segments: int, expected: int) -> set[str]:
                ans = {_ for _ in input.signals if len(_) == segments}
                assert len(ans) == expected
                return ans

            def shares_all_segments(container: str, contained: str) -> bool:
                cd = set(contained)
                return set(container) & cd == cd

            digits = dict[str, str]()
            # 1
            twos = find(2, 1)
            one = list(twos)[0]
            digits[one] = "1"
            # 7
            threes = find(3, 1)
            digits[list(threes)[0]] = "7"
            # 4
            fours = find(4, 1)
            four = list(fours)[0]
            digits[four] = "4"
            # 8
            sevens = find(7, 1)
            digits[list(sevens)[0]] = "8"
            # 9
            sixes = find(6, 3)
            possible_nines = {
                six for six in sixes if shares_all_segments(six, four)
            }
            assert len(possible_nines) == 1
            nine = list(possible_nines)[0]
            digits[nine] = "9"
            # 0
            possible_zeroes = {
                six
                for six in sixes
                if six != nine and shares_all_segments(six, one)
            }
            assert len(possible_zeroes) == 1
            zero = list(possible_zeroes)[0]
            digits[zero] = "0"
            # 6
            possible_sixes = {
                six for six in sixes if six != nine and six != zero
            }
            assert len(possible_sixes) == 1
            digits[list(possible_sixes)[0]] = "6"
            # 3
            fives = find(5, 3)
            possible_threes = {
                five for five in fives if shares_all_segments(five, one)
            }
            assert len(possible_threes) == 1
            three = list(possible_threes)[0]
            digits[three] = "3"
            # 5
            possible_fives = {
                five
                for five in fives
                if five != three and shares_all_segments(nine, five)
            }
            assert len(possible_fives) == 1
            five = list(possible_fives)[0]
            digits[five] = "5"
            # 2
            possible_twos = {f for f in fives if f != five and f != three}
            assert len(possible_twos) == 1
            digits[list(possible_twos)[0]] = "2"

            return int("".join(digits[o] for o in input.outputs))

        return sum(solve(_) for _ in input)

    @aoc_samples(
        (
            ("part_1", TEST, 26),
            ("part_2", TEST, 61229),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2021, 8)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

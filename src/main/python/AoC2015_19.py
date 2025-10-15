#! /usr/bin/env python3
#
# Advent of Code 2015 Day 19
#

import sys
from collections import defaultdict

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

TEST1 = """\
H => HO
H => OH
O => HH

HOH
"""
TEST2 = """\
H => HO
H => OH
O => HH

HOHOHO
"""
TEST3 = """\
H => HO
H => OH
Oo => HH

HOHOoHO
"""
TEST4 = """\
e => H
e => O
H => HO
H => OH
O => HH

HOH
"""
TEST5 = """\
e => H
e => O
H => HO
H => OH
O => HH

HOHOHO
"""

Replacements = dict[str, list[str]]
Input = tuple[Replacements, str]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        blocks = my_aocd.to_blocks(input_data)
        replacements = defaultdict[str, list[str]](list[str])
        for line in blocks[0]:
            split = line.split(" => ")
            replacements[split[0]].append(split[1])
        assert len(blocks[1]) == 1
        return replacements, blocks[1][0]

    def run_replacement(
        self, replacements: Replacements, molecule: str
    ) -> set[str]:
        molecules = set[str]()
        key = ""
        for i, c in enumerate(molecule):
            if len(key) == 2:
                key = ""
                continue
            if c in replacements:
                key = c
            elif molecule[i : i + 2] in replacements:
                key = molecule[i : i + 2]
            else:
                continue
            for r in replacements[key]:
                molecules.add(molecule[:i] + r + molecule[i + len(key) :])
        return molecules

    def part_1(self, inputs: Input) -> Output1:
        replacements, molecule = inputs
        return len(self.run_replacement(replacements, molecule))

    def part_2_bis(self, inputs: Input) -> Output2:
        def fabricate(
            target: str,
            molecule: str,
            replacements: Replacements,
            cnt: int,
        ) -> int:
            new_molecules = self.run_replacement(replacements, molecule)
            if target in new_molecules:
                return cnt + 1
            for m in new_molecules:
                if len(m) > len(target):
                    return 0
                result = fabricate(target, m, replacements, cnt + 1)
                if result > 0:
                    return result
            return 0

        replacements, molecule = inputs
        return fabricate(molecule, "e", replacements, 0)

    def part_2(self, inputs: Input) -> Output2:
        replacements, molecule = inputs
        cnt = 0
        new_molecule = molecule
        while new_molecule != "e":
            for new in replacements:
                for old in replacements[new]:
                    if old in new_molecule:
                        new_molecule = new_molecule.replace(old, new, 1)
                        cnt += 1
                        log(cnt)
                        log(f"{old}->{new}")
                        log(new_molecule)
        return cnt

    @aoc_samples(
        (
            ("part_1", TEST1, 4),
            ("part_1", TEST2, 7),
            ("part_1", TEST3, 6),
            ("part_2_bis", TEST4, 3),
            ("part_2_bis", TEST5, 6),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2015, 19)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

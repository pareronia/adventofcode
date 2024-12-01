#! /usr/bin/env python3
#
# Advent of Code 2023 Day 1
#

from __future__ import annotations

import math
import sys
from collections import defaultdict
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST1 = """\
10 ORE => 10 A
1 ORE => 1 B
7 A, 1 B => 1 C
7 A, 1 C => 1 D
7 A, 1 D => 1 E
7 A, 1 E => 1 FUEL
"""
TEST2 = """\
9 ORE => 2 A
8 ORE => 3 B
7 ORE => 5 C
3 A, 4 B => 1 AB
5 B, 7 C => 1 BC
4 C, 1 A => 1 CA
2 AB, 3 BC, 4 CA => 1 FUEL
"""
TEST3 = """\
157 ORE => 5 NZVS
165 ORE => 6 DCFZ
44 XJWVT, 5 KHKGT, 1 QDVJ, 29 NZVS, 9 GPVTF, 48 HKGWZ => 1 FUEL
12 HKGWZ, 1 GPVTF, 8 PSHF => 9 QDVJ
179 ORE => 7 PSHF
177 ORE => 5 HKGWZ
7 DCFZ, 7 PSHF => 2 XJWVT
165 ORE => 2 GPVTF
3 DCFZ, 7 NZVS, 5 HKGWZ, 10 PSHF => 8 KHKGT
"""
TEST4 = """\
2 VPVL, 7 FWMGM, 2 CXFTF, 11 MNCFX => 1 STKFG
17 NVRVD, 3 JNWZP => 8 VPVL
53 STKFG, 6 MNCFX, 46 VJHF, 81 HVMC, 68 CXFTF, 25 GNMV => 1 FUEL
22 VJHF, 37 MNCFX => 5 FWMGM
139 ORE => 4 NVRVD
144 ORE => 7 JNWZP
5 MNCFX, 7 RFSQX, 2 FWMGM, 2 VPVL, 19 CXFTF => 3 HVMC
5 VJHF, 7 MNCFX, 9 VPVL, 37 CXFTF => 6 GNMV
145 ORE => 6 MNCFX
1 NVRVD => 8 CXFTF
1 VJHF, 6 MNCFX => 4 RFSQX
176 ORE => 6 VJHF
"""
TEST5 = """\
171 ORE => 8 CNZTR
7 ZLQW, 3 BMBT, 9 XCVML, 26 XMNCP, 1 WPTQ, 2 MZWV, 1 RJRHP => 4 PLWSL
114 ORE => 4 BHXH
14 VRPVC => 6 BMBT
6 BHXH, 18 KTJDG, 12 WPTQ, 7 PLWSL, 31 FHTLT, 37 ZDVW => 1 FUEL
6 WPTQ, 2 BMBT, 8 ZLQW, 18 KTJDG, 1 XMNCP, 6 MZWV, 1 RJRHP => 6 FHTLT
15 XDBXC, 2 LTCX, 1 VRPVC => 6 ZLQW
13 WPTQ, 10 LTCX, 3 RJRHP, 14 XMNCP, 2 MZWV, 1 ZLQW => 1 ZDVW
5 BMBT => 4 WPTQ
189 ORE => 9 KTJDG
1 MZWV, 17 XDBXC, 3 XCVML => 2 XMNCP
12 VRPVC, 27 CNZTR => 2 XDBXC
15 KTJDG, 12 BHXH => 5 XCVML
3 BHXH, 2 VRPVC => 7 MZWV
121 ORE => 7 VRPVC
7 XCVML => 6 RJRHP
5 BHXH, 4 VRPVC => 5 LTCX
"""

FUEL = "FUEL"
ORE = "ORE"
ONE_TRILLION = 1_000_000_000_000


class Material(NamedTuple):
    name: str
    amount: int

    @classmethod
    def from_string(cls, string: str) -> Material:
        amount, name = string.split()
        return Material(name, int(amount))


class Reaction(NamedTuple):
    material: Material
    reactants: set[Material]

    @classmethod
    def from_string(cls, string: str) -> Reaction:
        left, right = string.split(" => ")
        reactants = {Material.from_string(s) for s in left.split(", ")}
        return Reaction(Material.from_string(right), reactants)


Input = dict[str, Reaction]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return {
            r.material.name: r for r in map(Reaction.from_string, input_data)
        }

    def ore_needed_for(
        self,
        material: str,
        amount: int,
        reactions: Input,
        inventory: dict[str, int] = defaultdict[str, int](int),
    ) -> int:
        if material == ORE:
            return amount
        available = inventory[material]
        if amount <= available:
            inventory[material] = available - amount
            return 0
        else:
            inventory[material] = 0

        reaction = reactions[material]
        produced = reaction.material.amount
        needed = amount - available
        runs = math.ceil(float(needed) / produced)
        if needed < produced * runs:
            inventory[material] += produced * runs - needed
        return sum(
            self.ore_needed_for(r.name, r.amount * runs, reactions, inventory)
            for r in reaction.reactants
        )

    def part_1(self, input: Input) -> Output1:
        return self.ore_needed_for(FUEL, 1, input, defaultdict[str, int](int))

    def part_2(self, input: Input) -> Output2:
        part_1 = self.ore_needed_for(FUEL, 1, input)
        lo = ONE_TRILLION // part_1 // 10
        hi = ONE_TRILLION // part_1 * 10
        while lo <= hi:
            mid = (lo + hi) // 2
            ans = self.ore_needed_for(FUEL, mid, input)
            if ans == ONE_TRILLION:
                return mid
            elif ans < ONE_TRILLION:
                lo = mid + 1
            else:
                hi = mid - 1
        return lo - 1

    @aoc_samples(
        (
            ("part_1", TEST1, 31),
            ("part_1", TEST2, 165),
            ("part_1", TEST3, 13312),
            ("part_1", TEST4, 180697),
            ("part_1", TEST5, 2210736),
            ("part_2", TEST3, 82892753),
            ("part_2", TEST4, 5586022),
            ("part_2", TEST5, 460664),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2019, 14)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

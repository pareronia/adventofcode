#! /usr/bin/env python3
#
# Advent of Code 2016 Day 4
#

from __future__ import annotations

import re
import sys
from collections import Counter
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

REGEXP = r"([-a-z]+)-([0-9]+)\[([a-z]{5})\]$"
MATCH = r"[a-z]{9}-[a-z]{6}-[a-z]{7}$"

TEST = """\
aaaaa-bbb-z-y-x-123[abxyz]
a-b-c-d-e-f-g-h-987[abcde]
not-a-real-room-404[oarel]
totally-real-room-200[decoy]
"""


class Room(NamedTuple):
    name: str
    sector_id: int
    checksum: str

    @classmethod
    def from_input(cls, line: str) -> Room:
        m = re.search(REGEXP, line)
        assert m is not None
        return Room(m.groups()[0], int(m.groups()[1]), m.groups()[2])

    def is_real(self) -> bool:
        most_common = sorted(
            Counter(self.name.replace("-", "")).items(),
            key=lambda item: item[1] * -100 + ord(item[0]),
        )
        return "".join(x for x, _ in most_common[:5]) == self.checksum

    def decrypt(self) -> str:
        shift = self.sector_id % 26
        return "".join(
            chr((ord(c) + shift - 97) % 26 + 97) if c != "-" else " "
            for c in self.name
        )


Input = list[Room]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [Room.from_input(line) for line in input_data]

    def part_1(self, rooms: Input) -> int:
        return sum(room.sector_id for room in rooms if room.is_real())

    def part_2(self, rooms: Input) -> int:
        return next(
            room.sector_id
            for room in rooms
            if re.match(MATCH, room.name) is not None
            and room.decrypt() == "northpole object storage"
        )

    @aoc_samples((("part_1", TEST, 1514),))
    def samples(self) -> None:
        pass


solution = Solution(2016, 4)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

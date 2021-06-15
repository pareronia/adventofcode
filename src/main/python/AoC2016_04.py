#! /usr/bin/env python3
#
# Advent of Code 2016 Day 4
#

from __future__ import annotations
from typing import NamedTuple
import re
from aoc import my_aocd


REGEXP = r'([-a-z]+)-([0-9]+)\[([a-z]{5})\]$'
MATCH = r'[a-z]{9}-[a-z]{6}-[a-z]{7}$'


class Room(NamedTuple):
    name: str
    sector_id: int
    checksum: str

    @classmethod
    def of(cls, name: str, sector_id: str, checksum: str) -> Room:
        return Room(name, int(sector_id), checksum)

    def is_real(self) -> bool:
        hist = {c: self.name.count(c)
                for c in set(self.name.replace('-', ''))}
        items = list(hist.items())
        items.sort(key=lambda x: x[1]*-100 + ord(x[0]))
        return "".join([x for x, y in items][:5]) == self.checksum

    def decrypt(self) -> str:
        shift = self.sector_id % 26
        return "".join([chr((ord(c) + shift - 97) % 26 + 97)
                        if c != '-' else ' '
                        for c in self.name])


def parse(inputs: tuple[str]) -> tuple[Room]:
    return (Room.of(*re.search(REGEXP, input_).groups())
            for input_ in inputs)


def part_1(inputs: tuple[str]) -> int:
    return sum(room.sector_id
               for room in
               parse(inputs) if room.is_real())


def part_2(inputs: tuple[str]) -> int:
    matches = [room.sector_id for room in parse(inputs)
               if re.match(MATCH, room.name) is not None
               and room.decrypt() == 'northpole object storage']
    assert len(matches) == 1
    return matches[0]


TEST = '''\
aaaaa-bbb-z-y-x-123[abxyz]
a-b-c-d-e-f-g-h-987[abcde]
not-a-real-room-404[oarel]
totally-real-room-200[decoy]
'''.splitlines()


def main() -> None:
    my_aocd.print_header(2016, 4)

    assert part_1(TEST) == 1514

    inputs = my_aocd.get_input(2016, 4, 935)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

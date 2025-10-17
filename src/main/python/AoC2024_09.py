#! /usr/bin/env python3
#
# Advent of Code 2024 Day 9
#

import heapq
import sys
from enum import Enum
from enum import auto
from enum import unique

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[int]
Output1 = int
Output2 = int
File = tuple[int, int, int]

TRIANGLE = [0, 0, 1, 3, 6, 10, 15, 21, 28, 36]

TEST = """\
2333133121414131402
"""


@unique
class Mode(Enum):
    MODE_1 = auto()
    MODE_2 = auto()

    def create_files(self, fid: int, pos: int, sz: int) -> list[File]:
        match self:
            case Mode.MODE_1:
                return [(fid, pos + i, 1) for i in range(sz)]
            case Mode.MODE_2:
                return [(fid, pos, sz)]

    def checksum(self, f: File) -> int:
        fid, pos, sz = f
        match self:
            case Mode.MODE_1:
                return fid * pos
            case Mode.MODE_2:
                return fid * (pos * sz + TRIANGLE[sz])


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(map(int, next(iter(input_data))))

    def solve(self, disk: Input, mode: Mode) -> int:
        files = list[File]()
        free_by_sz: list[list[int]] = [[] for _ in range(10)]
        is_free, fid, pos = False, 0, 0
        for n in disk:
            if is_free:
                heapq.heappush(free_by_sz[n], pos)
            else:
                files.extend(mode.create_files(fid, pos, n))
                fid += 1
            pos += n
            is_free = not is_free
        ans = 0
        for fid, pos, sz in reversed(files):
            earliest = min(
                (
                    (i, free)
                    for i, free in enumerate(free_by_sz[sz:], sz)
                    if len(free) > 0
                ),
                key=lambda e: e[1][0],
                default=None,
            )
            new_pos = pos
            if earliest is not None:
                free_sz, free = earliest
                free_pos = free[0]
                if free_pos < pos:
                    heapq.heappop(free_by_sz[free_sz])
                    new_pos = free_pos
                    if sz < free_sz:
                        heapq.heappush(free_by_sz[free_sz - sz], new_pos + sz)
            ans += mode.checksum((fid, new_pos, sz))
        return ans

    def part_1(self, disk: Input) -> Output1:
        return self.solve(disk, Mode.MODE_1)

    def part_2(self, disk: Input) -> Output2:
        return self.solve(disk, Mode.MODE_2)

    @aoc_samples(
        (
            ("part_1", TEST, 1928),
            ("part_2", TEST, 2858),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 9)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

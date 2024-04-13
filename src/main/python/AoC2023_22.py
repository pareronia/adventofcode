#! /usr/bin/env python3
#
# Advent of Code 2023 Day 22
#

from __future__ import annotations

import itertools
import sys
from typing import Callable
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry3d import Cuboid

TEST = """\
1,0,1~1,2,1
0,0,2~2,0,2
0,2,3~2,2,3
0,0,4~0,2,4
2,0,5~2,2,5
0,1,6~2,1,6
1,1,8~1,1,9
"""


class Stack(NamedTuple):
    bricks: set[Cuboid]
    supportees: dict[Cuboid, set[Cuboid]]
    supporters: dict[Cuboid, set[Cuboid]]
    bricks_by_z1: dict[int, list[Cuboid]]
    bricks_by_z2: dict[int, list[Cuboid]]

    @classmethod
    def from_input(cls, input: InputData) -> Stack:
        def overlap_xy(lhs: Cuboid, rhs: Cuboid) -> bool:
            return lhs.overlap_x(rhs) and lhs.overlap_y(rhs)

        def group_bricks_by(
            f: Callable[[Cuboid], int]
        ) -> dict[int, list[Cuboid]]:
            return {
                k: list(v)
                for k, v in itertools.groupby(sorted(bricks, key=f), f)
            }

        def group_by_touching(
            f: Callable[[Cuboid], list[Cuboid]]
        ) -> dict[Cuboid, set[Cuboid]]:
            return {
                brick: set(
                    filter(
                        lambda b: overlap_xy(b, brick),
                        f(brick),
                    )
                )
                for brick in bricks
            }

        def stack(
            bricks: set[Cuboid],
            bricks_by_z1: dict[int, list[Cuboid]],
            bricks_by_z2: dict[int, list[Cuboid]],
        ) -> None:
            def is_not_supported(brick: Cuboid) -> bool:
                return not any(
                    overlap_xy(brick, other)
                    for other in bricks_by_z2.get(brick.z1 - 1, [])
                )

            def move_down(brick: Cuboid) -> None:
                def move_to_z(brick: Cuboid, dz: int) -> Cuboid:
                    return Cuboid(
                        brick.x1,
                        brick.x2,
                        brick.y1,
                        brick.y2,
                        brick.z1 + dz,
                        brick.z2 + dz,
                    )

                moved_brick = move_to_z(brick, -1)
                bricks_by_z1[brick.z1].remove(brick)
                bricks_by_z2[brick.z2].remove(brick)
                bricks_by_z1.setdefault(moved_brick.z1, []).append(moved_brick)
                bricks_by_z2.setdefault(moved_brick.z2, []).append(moved_brick)

            moved = True
            while moved:
                moved = False
                for z in filter(lambda z: z > 1, sorted(bricks_by_z1.keys())):
                    for brick in filter(is_not_supported, bricks_by_z1[z][:]):
                        move_down(brick)
                        moved = True
            bricks.clear()
            for v in bricks_by_z2.values():
                bricks |= set(v)

        bricks = set[Cuboid]()
        for line in input:
            splits = line.split("~")
            x1, y1, z1 = map(int, splits[0].split(","))
            x2, y2, z2 = map(int, splits[1].split(","))
            bricks.add(Cuboid.of(x1, x2, y1, y2, z1, z2))
        bricks_by_z1 = group_bricks_by(lambda b: b.z1)
        bricks_by_z2 = group_bricks_by(lambda b: b.z2)
        stack(bricks, bricks_by_z1, bricks_by_z2)
        supportees = group_by_touching(
            lambda brick: bricks_by_z1.get(brick.z2 + 1, [])
        )
        supporters = group_by_touching(
            lambda brick: bricks_by_z2.get(brick.z1 - 1, [])
        )

        return Stack(
            bricks, supportees, supporters, bricks_by_z1, bricks_by_z2
        )

    def get_deletable(self) -> set[Cuboid]:
        def is_not_single_supporter(brick: Cuboid) -> bool:
            return not any(
                {brick} == self.supporters[b] for b in self.supportees[brick]
            )

        return set(filter(is_not_single_supporter, self.bricks))

    def get_not_deletable(self) -> set[Cuboid]:
        return self.bricks - self.get_deletable()

    def delete(self, brick: Cuboid) -> set[Cuboid]:
        q = [brick]
        falling = set[Cuboid]([brick])
        while len(q) > 0:
            b = q.pop(0)
            for s in self.supportees[b]:
                if all(sp in falling for sp in self.supporters[s]):
                    if s not in falling:
                        q.append(s)
                    falling.add(s)
        falling.remove(brick)
        return falling


Input = Stack
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Stack.from_input(input_data)

    def part_1(self, stack: Input) -> Output1:
        return len(stack.get_deletable())

    def part_2(self, stack: Input) -> Output2:
        return sum(
            map(lambda b: len(stack.delete(b)), stack.get_not_deletable())
        )

    @aoc_samples(
        (
            ("part_1", TEST, 5),
            ("part_2", TEST, 7),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 22)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

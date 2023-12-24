#! /usr/bin/env python3
#
# Advent of Code 2023 Day 24
#

import sys
import itertools

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
from aoc.geometry3d import Position3D
from aoc.geometry3d import Vector3D

Input = InputData
Output1 = int
Output2 = int


TEST = """\
19, 13, 30 @ -2,  1, -2
18, 19, 22 @ -1, -1, -2
20, 25, 34 @ -2, -2, -4
12, 31, 28 @ -1, -2, -1
20, 19, 15 @  1, -5, -3
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, input: Input) -> Output1:
        def intersection(
            p1: Position3D, v1: Vector3D, p2: Position3D, v2: Vector3D
        ) -> tuple[float, float] | None:
            x1, y1, _ = p1
            x2, y2 = x1 + 100 * v1.x, y1 + 100 * v1.y
            x3, y3, _ = p2
            x4, y4 = x3 + 100 * v2.x, y3 + 100 * v2.y
            d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)
            if d == 0:
                return None
            x = (
                (x1 * y2 - y1 * x2) * (x3 - x4)
                - (x1 - x2) * (x3 * y4 - y3 * x4)
            ) / d
            y = (
                (x1 * y2 - y1 * x2) * (y3 - y4)
                - (y1 - y2) * (x3 * y4 - y3 * x4)
            ) / d
            return x, y

        MIN = 200000000000000
        MAX = 400000000000000
        ans = 0
        hs = []
        for line in input:
            p, v = line.split(" @ ")
            pos = Position3D.of(*map(int, p.split(", ")))
            vel = Vector3D.of(*map(int, v.split(", ")))
            hs.append((pos, vel))
        log(hs)
        for hs_a, hs_b in itertools.combinations(hs, 2):
            p_a, v_a = hs_a
            p_b, v_b = hs_b
            ix = intersection(p_a, v_a, p_b, v_b)
            if ix is None:
                log("None")
                continue
            ix_x, ix_y = ix
            log((ix_x, ix_y))
            t_ix_a = (ix_x - p_a.x) / v_a.x
            t_ix_b = (ix_x - p_b.x) / v_b.x
            log(f"{t_ix_a=}, {t_ix_b=}")
            if t_ix_a < 0 or t_ix_b < 0:
                continue
            if MIN <= ix_x <= MAX and MIN <= ix_y <= MAX:
                ans += 1
        return ans

    def part_2(self, input: Input) -> Output2:
        hs = []
        for line in input:
            pp, vv = line.split(" @ ")
            pos = Position3D.of(*map(int, pp.split(", ")))
            vel = Vector3D.of(*map(int, vv.split(", ")))
            hs.append((pos, vel))
        # (DistanceDifference % (RockVelocity-HailVelocity) = 0
        v_x, v_y, v_z = set[int](), set[int](), set[int]()
        for hs_a, hs_b in itertools.combinations(hs, 2):
            p_a, v_a = hs_a
            p_b, v_b = hs_b
            if v_a.x == v_b.x and v_a.x != 0:
                dp_x = p_a.x - p_b.x
                tmp = set[int]()
                for v in range(-1000, 1000):
                    if v != v_a.x and dp_x % (v - v_a.x) == 0:
                        tmp.add(v)
                v_x = v_x | tmp if len(v_x) == 0 else v_x & tmp
            if v_a.y == v_b.y and v_a.y != 0:
                dp_y = p_a.y - p_b.y
                tmp = set[int]()
                for v in range(-1000, 1000):
                    if v != v_a.y and dp_y % (v - v_a.y) == 0:
                        tmp.add(v)
                v_y = v_y | tmp if len(v_y) == 0 else v_y & tmp
            if v_a.z == v_b.z and v_a.z != 0:
                dp_z = p_a.z - p_b.z
                tmp = set[int]()
                for v in range(-1000, 1000):
                    if v != v_a.z and dp_z % (v - v_a.z) == 0:
                        tmp.add(v)
                v_z = v_z | tmp if len(v_z) == 0 else v_z & tmp
        if not len(v_x) == len(v_y) == len(v_z) == 1:
            raise RuntimeError()
        v_r = Vector3D(v_x.pop(), v_y.pop(), v_z.pop())
        p_a, v_a = hs[0]
        p_b, v_b = hs[1]
        m_a = (v_a.y - v_r.y) / (v_a.x - v_r.x)
        m_b = (v_b.y - v_r.y) / (v_b.x - v_r.x)
        c_a = p_a.y - (m_a * p_a.x)
        c_b = p_b.y - (m_b * p_b.x)
        x = int((c_b - c_a) / (m_a - m_b))
        y = int(m_a * x + c_a)
        t = (x - p_a.x) // (v_a.x - v_r.x)
        z = p_a.z + (v_a.z - v_r.z) * t
        return x + y + z

    @aoc_samples(
        (
            # ("part_1", TEST, 2),
            # ("part_2", TEST, 47),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 24)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

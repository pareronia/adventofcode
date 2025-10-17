#! /usr/bin/env python3
#
# Advent of Code 2023 Day 24
#

import itertools
import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
from aoc.geometry import Line
from aoc.geometry import Position
from aoc.geometry import Vector
from aoc.geometry3d import Position3D
from aoc.geometry3d import Vector3D

Input = list[tuple[Position3D, Vector3D]]
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
    def solve_1(self, hailstones: Input, xy_min: int, xy_max: int) -> int:
        def paths_will_intersect_inside_area(
            hs_a: tuple[Position3D, Vector3D],
            hs_b: tuple[Position3D, Vector3D],
            xy_min: int,
            xy_max: int,
        ) -> bool:
            p_a, v_a = hs_a
            p_b, v_b = hs_b
            line_a = Line.of(
                Position.of(p_a.x, p_a.y), Vector.of(v_a.x, v_a.y)
            )
            line_b = Line.of(
                Position.of(p_b.x, p_b.y), Vector.of(v_b.x, v_b.y)
            )
            ix = line_a.intersection(line_b)
            if ix is None:
                return False
            ix_x, ix_y = ix
            t_ix_a = (ix_x - p_a.x) / v_a.x
            t_ix_b = (ix_x - p_b.x) / v_b.x
            return (
                t_ix_a >= 0
                and t_ix_b >= 0
                and xy_min <= ix_x <= xy_max
                and xy_min <= ix_y <= xy_max
            )

        return sum(
            paths_will_intersect_inside_area(hs_a, hs_b, xy_min, xy_max)
            for hs_a, hs_b in itertools.combinations(hailstones, 2)
        )

    def solve_2(self, hailstones: Input) -> int:
        # (DistanceDifference % (RockVelocity-HailVelocity) = 0
        def find_rock_velocity(hailstones: Input) -> Vector3D:
            v_x, v_y, v_z = set[int](), set[int](), set[int]()
            for hs_a, hs_b in itertools.combinations(hailstones, 2):
                p_a, v_a = hs_a
                p_b, v_b = hs_b
                if v_a.x == v_b.x and v_a.x != 0:
                    dp_x = p_a.x - p_b.x
                    tmp = {
                        v
                        for v in range(-1000, 1000)
                        if v not in (0, v_a.x) and dp_x % (v - v_a.x) == 0
                    }
                    v_x = v_x | tmp if len(v_x) == 0 else v_x & tmp
                if v_a.y == v_b.y and v_a.y != 0:
                    dp_y = p_a.y - p_b.y
                    tmp = {
                        v
                        for v in range(-1000, 1000)
                        if v not in (0, v_a.y) and dp_y % (v - v_a.y) == 0
                    }
                    v_y = v_y | tmp if len(v_y) == 0 else v_y & tmp
                if v_a.z == v_b.z and v_a.z != 0:
                    dp_z = p_a.z - p_b.z
                    tmp = {
                        v
                        for v in range(-1000, 1000)
                        if v not in (0, v_a.z) and dp_z % (v - v_a.z) == 0
                    }
                    v_z = v_z | tmp if len(v_z) == 0 else v_z & tmp
            log((v_x, v_y, v_z))
            if not len(v_x) > 0 and len(v_y) > 0 and len(v_z) > 0:
                raise RuntimeError
            return Vector3D(v_x.pop(), v_y.pop(), v_z.pop())

        """
        px1 + vx1*t = px2 + vx2*t
        px1 - px2 = vx2*t - vx1*t
        (px1-px2)/(vx2-vx1) = t

        y = mx + b
        m = vy/vx
        b = py - m*px
        m1x + b1 = m2x + b2
        m1x - m2x = b2 - b1
        x = (b2-b1)/(m1-m2)
        """

        def find_rock_start_position(
            hailstones: Input, v_r: Vector3D
        ) -> Position3D:
            p_a, v_a = hailstones[0]
            p_b, v_b = hailstones[1]
            m_a = (v_a.y - v_r.y) / (v_a.x - v_r.x)
            m_b = (v_b.y - v_r.y) / (v_b.x - v_r.x)
            c_a = p_a.y - (m_a * p_a.x)
            c_b = p_b.y - (m_b * p_b.x)
            x = int((c_b - c_a) / (m_a - m_b))
            y = int(m_a * x + c_a)
            t = (x - p_a.x) // (v_a.x - v_r.x)
            z = p_a.z + (v_a.z - v_r.z) * t
            return Position3D(x, y, z)

        v_r = find_rock_velocity(hailstones)
        p_r = find_rock_start_position(hailstones, v_r)
        return p_r.x + p_r.y + p_r.z

    def parse_input(self, input_data: InputData) -> Input:
        hs = []
        for line in input_data:
            pp, vv = line.split(" @ ")
            pos = Position3D.of(*map(int, pp.split(", ")))
            vel = Vector3D.of(*map(int, vv.split(", ")))
            hs.append((pos, vel))
        return hs

    def sample_1(self, hailstones: Input) -> int:
        return self.solve_1(hailstones, 7, 27)

    def part_1(self, hailstones: Input) -> Output1:
        return self.solve_1(hailstones, 200000000000000, 400000000000000)

    def part_2(self, hailstones: Input) -> Output2:
        return self.solve_2(hailstones)

    @aoc_samples(
        (
            ("sample_1", TEST, 2),
            ("part_2", TEST, 47),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 24)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2017 Day 20
#
from __future__ import annotations
import re
from collections import defaultdict
from typing import NamedTuple
from aoc import my_aocd
from aoc.geometry3d import Position3D, Vector3D
import aocd


TICKS = 400


class Particle(NamedTuple):
    number: int
    position: Position3D
    velocity: Position3D
    acceleration: Position3D

    @classmethod
    def create(cls, number: int, nums: list[int]) -> Particle:
        assert len(nums) == 9
        return Particle(
            number,
            Position3D(nums[0], nums[1], nums[2]),
            Position3D(nums[3], nums[4], nums[5]),
            Position3D(nums[6], nums[7], nums[8]),
        )

    def next(self) -> Particle:
        v2 = self.velocity.translate(Vector3D.to_from(self.acceleration))
        p2 = self.position.translate(Vector3D.to_from(v2))
        return Particle(self.number, p2, v2, self.acceleration)

    def distance(self) -> int:
        return self.position.manhattan_distance_to_origin()


def _parse(inputs: tuple[str]) -> list[Particle]:
    return [
        Particle.create(i, list(map(int, re.findall(r"[-0-9]+", line))))
        for i, line in enumerate(inputs)
    ]


def part_1(inputs: tuple[str]) -> int:
    particles = _parse(inputs)
    for _ in range(TICKS):
        particles = [p.next() for p in particles]
    particles.sort(key=lambda p: p.distance())
    return particles[0].number


def part_2(inputs: tuple[str]) -> int:
    particles = _parse(inputs)
    for _ in range(TICKS):
        m = defaultdict(list)
        [m[pn.position].append(pn) for pn in [p.next() for p in particles]]
        particles = [p for mv in m.values() for p in mv if len(mv) == 1]
    return len(m)


TEST1 = """\
p=<3,0,0>, v=<2,0,0>, a=<-1,0,0>
p=<4,0,0>, v=<0,0,0>, a=<-2,0,0>
""".splitlines()
TEST2 = """\
p=<-6,0,0>, v=< 3,0,0>, a=<0,0,0>
p=<-4,0,0>, v=< 2,0,0>, a=<0,0,0>
p=<-2,0,0>, v=< 1,0,0>, a=<0,0,0>
p=< 3,0,0>, v=<-1,0,0>, a=<0,0,0>
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2017, 20)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 0
    assert part_2(TEST2) == 1

    inputs = my_aocd.get_input(2017, 20, 1_000)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

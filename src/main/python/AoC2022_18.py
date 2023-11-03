#! /usr/bin/env python3
#
# Advent of Code 2022 Day 18
#


from collections import defaultdict, deque
from itertools import product

import aocd
from aoc import my_aocd

Cube = tuple[int, int, int]
DIRS = [
    (-1, 0, 0),
    (1, 0, 0),
    (0, -1, 0),
    (0, 1, 0),
    (0, 0, -1),
    (0, 0, 1),
]


def _parse(inputs: tuple[str, ...]) -> list[Cube]:
    ans = []
    for line in inputs:
        x, y, z = map(int, line.split(","))
        ans.append((x, y, z))
    return ans


def _surface_area(cubes: list[Cube]) -> int:
    def _count_gaps(nums: list[int]) -> int:
        return sum(
            1 for i in range(1, len(nums)) if nums[i] != nums[i - 1] + 1
        )

    dx, dy, dz = defaultdict(list), defaultdict(list), defaultdict(list)
    for x, y, z in cubes:
        dx[(y, z)].append(x)
        dy[(x, z)].append(y)
        dz[(x, y)].append(z)
    return sum(
        sum(2 * _count_gaps(sorted(v)) + 2 for v in values)
        for values in (dx.values(), dy.values(), dz.values())
    )


def part_1(inputs: tuple[str, ...]) -> int:
    cubes = _parse(inputs)
    return _surface_area(cubes)


def part_2(inputs: tuple[str, ...]) -> int:
    cubes = _parse(inputs)
    min_x = min_y = min_z = 1_000_000_000
    max_x = max_y = max_z = -1_000_000_000
    for x, y, z in cubes:
        min_x, min_y, min_z = min(x, min_x), min(y, min_y), min(z, min_z)
        max_x, max_y, max_z = max(x, max_x), max(y, max_y), max(z, max_z)
    min_x, min_y, min_z = (m - 1 for m in (min_x, min_y, min_z))
    max_x, max_y, max_z = (m + 1 for m in (max_x, max_y, max_z))

    air = set[Cube]()
    q = deque[Cube]()
    q.append((min_x, min_y, min_z))
    while q:
        x, y, z = q.popleft()
        if (x, y, z) in cubes:
            continue
        air.add((x, y, z))
        for dx, dy, dz in DIRS:
            xx, yy, zz = x + dx, y + dy, z + dz
            if (
                min_x <= xx <= max_x
                and min_y <= yy <= max_y
                and min_z <= zz <= max_z
                and (xx, yy, zz) not in air
            ):
                q.append((xx, yy, zz))
                air.add((xx, yy, zz))
    trapped = [
        (x, y, z)
        for x, y, z in product(
            range(min_x + 1, max_x),
            range(min_y + 1, max_y),
            range(min_z + 1, max_z),
        )
        if not (x, y, z) in air and not (x, y, z) in cubes
    ]
    return _surface_area(cubes) - _surface_area(trapped)


TEST1 = tuple(
    """\
1,1,1
2,1,1
""".splitlines()
)
TEST2 = tuple(
    """\
2,2,2
1,2,2
3,2,2
2,1,2
2,3,2
2,2,1
2,2,3
2,2,4
2,2,6
1,2,5
3,2,5
2,1,5
2,3,5
""".splitlines()
)


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 18)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 10
    assert part_1(TEST2) == 64
    assert part_2(TEST2) == 58

    inputs = my_aocd.get_input_data(puzzle, 2136)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

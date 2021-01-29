#! /usr/bin/env python3
#
# Advent of Code 2020 Day 17
#
# TODO: make one solver for GoL for multiple dimensions

from __future__ import annotations
from collections import defaultdict
from functools import lru_cache
from aoc import my_aocd
from aoc.common import log


ON = "#"
OFF = "."


def _create_cell_1(r: int, c: int) -> tuple[int, ...]:
    return (0, r, c)


def _create_cell_2(r: int, c: int) -> tuple[int, ...]:
    return (0, 0, r, c)


def _parse(inputs: tuple[str], cell_factory) -> set[tuple[int, ...]]:
    return set(cell_factory(r, c)
               for r, row in enumerate(inputs)
               for c, state in enumerate(row)
               if state == ON
               )


@lru_cache(maxsize=1500)
def _get_neighbours_1(z: int, y: int, x: int) -> tuple[int]:
    return tuple((layer, row, col)
                 for layer in range(z-1, z+2)
                 for row in range(y-1, y+2)
                 for col in range(x-1, x+2)
                 if not (layer == z and row == y and col == x)
                 )


@lru_cache(maxsize=25000)
def _get_neighbours_2(w: int, z: int, y: int, x: int) -> tuple[int]:
    return tuple((wtf, layer, row, col)
                 for wtf in range(w-1, w+2)
                 for layer in range(z-1, z+2)
                 for row in range(y-1, y+2)
                 for col in range(x-1, x+2)
                 if not (wtf == w and layer == z and row == y and col == x)
                 )


def _next_generation(space: set[tuple[int, ...]],
                     neighbour_factory) -> set[tuple[int, ...]]:
    to_on = set()
    to_off = set()
    for cell in space:
        neighbours = neighbour_factory(*cell)
        neighbours_on = sum(1 for _ in neighbours if _ in space)
        if neighbours_on in {2, 3}:
            to_on.add(cell)
        else:
            to_off.add(cell)
        for n in set(neighbours) - space:
            n_neighbours = neighbour_factory(*n)
            n_neighbours_on = sum(1 for _ in n_neighbours if _ in space)
            if n_neighbours_on == 3:
                to_on.add(n)
    space |= to_on
    space ^= to_off
    return space


def _output_1(space: set):
    if not __debug__:
        return
    zz = defaultdict(set)
    [zz[cell[0]].add((cell[1], cell[2])) for cell in space]
    result = list[str]()
    for z in sorted(zz.keys()):
        min_c = min(c for r, c in zz[z])
        max_c = max(c for r, c in zz[z])
        min_r = min(r for r, c in zz[z])
        max_r = max(r for r, c in zz[z])
        result.append(f"z={z}")
        for r in range(min_r, max_r+1):
            line = ""
            for c in range(min_c, max_c+1):
                line += (ON if (r, c) in zz[z] else OFF)
            result.append(line)
        result.append(" ")
    log(result)


def _output_2(space: set):
    if not __debug__:
        return
    w = defaultdict(set)
    [w[cell[0]].add((cell[1], cell[2], cell[3])) for cell in space]
    wz = defaultdict(set)
    for k, v in w.items():
        z = defaultdict(set)
        [z[cell[0]].add((cell[1], cell[2])) for cell in v]
        wz[k] = z
    result = list[str]()
    for w in sorted(wz.keys()):
        min_z = min(z for z in wz[w].keys() if z >= 0)
        min_c = min(c for r, c in wz[w][min_z])
        max_c = max(c for r, c in wz[w][min_z])
        min_r = min(r for r, c in wz[w][min_z])
        max_r = max(r for r, c in wz[w][min_z])
        for z in sorted(wz[w].keys()):
            result.append(f"z={z}, w={w}")
            for r in range(min_r, max_r+1):
                line = ""
                for c in range(min_c, max_c+1):
                    line += (ON if (r, c) in wz[w][z] else OFF)
                result.append(line)
            result.append(" ")
    log(result)


def _run_generations(space: set[tuple[int, ...]],
                     generations: int,
                     neighbour_factory,
                     output) -> set[tuple[int, ...]]:
    for i in range(generations):
        log(f"GENERATION #{i+1}")
        output(space := _next_generation(space, neighbour_factory))
    log(neighbour_factory.cache_info())
    return space


def part_1(inputs: tuple[str]) -> int:
    _output_1(space := _parse(inputs, _create_cell_1))
    return len(_run_generations(space, 6, _get_neighbours_1, _output_1))


def part_2(inputs: tuple[str]) -> int:
    _output_2(space := _parse(inputs, _create_cell_2))
    return len(_run_generations(space, 6, _get_neighbours_2, _output_2))


TEST = """\
.#.
..#
###
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 17)

    assert part_1(TEST) == 112
    assert part_2(TEST) == 848

    inputs = my_aocd.get_input(2020, 17, 8)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

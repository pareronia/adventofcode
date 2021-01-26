#! /usr/bin/env python3
#
# Advent of Code 2020 Day 17
#
# TODO: limit boundaries when possible; points way out in the void aren't
#       going to change...
# TODO: make one solver for GoL for multiple dimensions

from __future__ import annotations
from functools import lru_cache
from math import prod
from dataclasses import dataclass
from aoc import my_aocd
from aoc.common import log


ON = "#"
OFF = "."


class Cube:
    layer: int
    row: int
    col: int
    state: str

    def __init__(self, layer: int, row: int, col: int, state: str):
        self.layer = layer
        self.row = row
        self.col = col
        self.state = state

    def output(self) -> str:
        return self.state

    def is_active(self) -> bool:
        return self.state == ON

    def set_active(self) -> None:
        self.state = ON

    def set_inactive(self) -> None:
        self.state = OFF


class Tesseract(Cube):
    wtf: int

    def __init__(self, wtf: int, layer: int, row: int, col: int, state: str):
        super().__init__(layer, row, col, state)
        self.wtf = wtf

    def __repr__(self):
        return type(self).__name__ \
                + "<" \
                + ', '.join(['{k}={v}'.format(k=k, v=self.__dict__.get(k))
                             for k in ['wtf', 'layer', 'row',
                                       'col', 'state']
                             ]) \
                + ">"


class Space:
    cubes: [[[Cube]]]

    def __init__(self):
        self.cubes = [[[]]]

    @classmethod
    def build(cls, dim_z: int, dim_y: int, dim_x: int) -> Space:
        layers = []
        for z in range(dim_z):
            rows = []
            for y in range(dim_y):
                cols = []
                for x in range(dim_x):
                    cols.append(Cube(z, y, x, OFF))
                rows.append(cols)
            layers.append(rows)
        space = Space()
        space.set_cubes(layers)
        return space

    def set_cubes(self, cubes: [[[Cube]]]) -> None:
        self.cubes = cubes

    def output(self) -> str:
        result = list[str]()
        offset = self.get_dimension()[0] // 2
        for z, layer in enumerate(self.cubes):
            result.append(f"z={z-offset}")
            for y, row in enumerate(layer):
                line = ""
                for cube in row:
                    line += cube.output()
                result.append(line)
            result.append(" ")
        return result

    def get_dimension(self):
        return (len(self.cubes), len(self.cubes[0]), len(self.cubes[0][0]))

    def get_count(self) -> int:
        return prod(self.get_dimension())

    def get_cubes(self):
        for layer in self.cubes:
            for row in layer:
                for cube in row:
                    yield cube

    def count_active(self):
        return sum([1 for c in self.get_cubes() if c.is_active()])

    def get_cube(self, layer: int, row: int, col: int) -> Cube:
        return self.cubes[layer][row][col]

    @classmethod
    @lru_cache(maxsize=5200)
    def get_neighbours(cls, z: int, y: int, x: int,
                       dimensions: tuple[int]) -> tuple[int]:
        return tuple((layer, row, col)
                     for layer in range(z-1, z+2)
                     for row in range(y-1, y+2)
                     for col in range(x-1, x+2)
                     if not (layer == z and row == y and col == x)
                     and 0 <= layer < dimensions[0]
                     and 0 <= row < dimensions[1]
                     and 0 <= col < dimensions[2]
                     )

    def count_active_neighbours(self, cube: Cube) -> int:
        dimensions = self.get_dimension()
        return sum([
            1
            for c in Space.get_neighbours(cube.layer, cube.row, cube.col,
                                          dimensions)
            if self.get_cube(*c).is_active()
        ])

    def run_cycle(self) -> None:
        new_space = Space.build(*self.get_dimension())
        for new_cube in new_space.get_cubes():
            old_cube = self.get_cube(new_cube.layer, new_cube.row,
                                     new_cube.col)
            active_neighbours = self.count_active_neighbours(old_cube)
            if old_cube.is_active():
                if active_neighbours == 2 or active_neighbours == 3:
                    new_cube.set_active()
                else:
                    new_cube.set_inactive()
            else:
                if active_neighbours == 3:
                    new_cube.set_active()
                else:
                    new_cube.set_inactive()
        self.set_cubes(new_space.cubes)


@dataclass
class HyperSpace:
    t7ts: [[[[Tesseract]]]]

    def __init__(self):
        self.t7ts = [[[[]]]]

    @classmethod
    def build(cls, dim_w: int, dim_z: int, dim_y: int, dim_x: int) -> Space:
        t7ts = []
        for w in range(dim_w):
            layers = []
            for z in range(dim_z):
                rows = []
                for y in range(dim_y):
                    cols = []
                    for x in range(dim_x):
                        cols.append(Tesseract(w, z, y, x, OFF))
                    rows.append(cols)
                layers.append(rows)
            t7ts.append(layers)
        space = HyperSpace()
        space.set_t7ts(t7ts)
        return space

    def set_t7ts(self, t7ts: [[[[Tesseract]]]]) -> None:
        self.t7ts = t7ts

    def get_t7t(self, wtf: int, layer: int, row: int, col: int) -> Tesseract:
        return self.t7ts[wtf][layer][row][col]

    def get_t7ts(self):
        for wtf in self.t7ts:
            for layer in wtf:
                for row in layer:
                    for t7t in row:
                        yield t7t

    def get_dimension(self):
        return (len(self.t7ts), len(self.t7ts[0]),
                len(self.t7ts[0][0]), len(self.t7ts[0][0][0]))

    def output(self) -> str:
        result = list[str]()
        offset = self.get_dimension()[0] // 2
        for w, wtf in enumerate(self.t7ts):
            for z, layer in enumerate(wtf):
                result.append(f"z={z-offset}, w={w-offset}")
                for y, row in enumerate(layer):
                    line = ""
                    for t7t in row:
                        line += t7t.output()
                    result.append(line)
                result.append(" ")
        return result

    def get_count(self) -> int:
        return prod(self.get_dimension())

    def count_active(self):
        return sum([1 for t in self.get_t7ts() if t.is_active()])

    def run_cycle(self) -> None:
        new_space = HyperSpace.build(*self.get_dimension())
        for new_t7t in new_space.get_t7ts():
            old_t7t = self.get_t7t(new_t7t.wtf, new_t7t.layer,
                                   new_t7t.row, new_t7t.col)
            active_neighbours = self.count_active_neighbours(old_t7t)
            if old_t7t.is_active():
                if active_neighbours == 2 or active_neighbours == 3:
                    new_t7t.set_active()
                else:
                    new_t7t.set_inactive()
            else:
                if active_neighbours == 3:
                    new_t7t.set_active()
                else:
                    new_t7t.set_inactive()
        self.set_t7ts(new_space.t7ts)

    @classmethod
    @lru_cache(maxsize=67600)
    def get_neighbours(cls, w: int, z: int, y: int, x: int,
                       dimensions: tuple[int]) -> tuple[int]:
        return tuple((wtf, layer, row, col)
                     for wtf in range(w-1, w+2)
                     for layer in range(z-1, z+2)
                     for row in range(y-1, y+2)
                     for col in range(x-1, x+2)
                     if not (wtf == w and layer == z and row == y and col == x)
                     and 0 <= wtf < dimensions[0]
                     and 0 <= layer < dimensions[1]
                     and 0 <= row < dimensions[2]
                     and 0 <= col < dimensions[3]
                     )

    def count_active_neighbours(self, t7t: Tesseract) -> int:
        dimensions = self.get_dimension()
        return sum([
            1
            for t in HyperSpace.get_neighbours(t7t.wtf,
                                               t7t.layer, t7t.row, t7t.col,
                                               dimensions)
            if self.get_t7t(*t).is_active()
        ])


def _parse_1(inputs: tuple[str], cycles: int) -> Space:
    dim = len(inputs[0]) + 2*cycles
    dim_z = 1 + 2*cycles
    center_z = dim_z // 2
    center = dim // 2 - len(inputs[0]) // 2
    space = Space.build(dim_z, dim, dim)
    for r, input_ in enumerate(inputs):
        for c, state in enumerate(input_):
            if state == ON:
                space.get_cube(center_z, center+r, center+c).set_active()
            elif state == OFF:
                space.get_cube(center_z, center+r, center+c).set_inactive()
            else:
                raise ValueError("Invalid input")
    return space


def _parse_2(inputs: tuple[str], cycles: int) -> HyperSpace:
    dim = len(inputs[0]) + 2*cycles
    dim_wz = 1 + 2*cycles
    center_wz = dim_wz // 2
    center = dim // 2 - len(inputs[0]) // 2
    space = HyperSpace.build(dim_wz, dim_wz, dim, dim)
    for r, input_ in enumerate(inputs):
        for c, state in enumerate(input_):
            if state == ON:
                space.get_t7t(center_wz, center_wz,
                              center+r, center+c).set_active()
            elif state == OFF:
                space.get_t7t(center_wz, center_wz,
                              center+r, center+c).set_inactive()
            else:
                raise ValueError("Invalid input")
    return space


def part_1(inputs: tuple[str]) -> int:
    space = _parse_1(inputs, 6)
    log(space.output())
    log(space.get_count())
    log(space.count_active())
    for i in range(1, 7):
        log(f"GENERATION #{i}")
        space.run_cycle()
        log(space.output())
        log(space.get_count())
        log(space.count_active())
    log(Space.get_neighbours.cache_info())
    return space.count_active()


def part_2(inputs: tuple[str]) -> int:
    space = _parse_2(inputs, 6)
    log(space.get_count())
    log(space.count_active())
    for i in range(1, 7):
        log(f"GENERATION #{i}")
        space.run_cycle()
        log(space.output())
        log(space.get_count())
        log(space.count_active())
    log(HyperSpace.get_neighbours.cache_info())
    return space.count_active()


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

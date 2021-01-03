#! /usr/bin/env python3
#
# Advent of Code 2020 Day 20
#
"""
References:
    * The Dihedral Group of the Square: https://solitaryroad.com/c308.html
"""

from __future__ import annotations
from dataclasses import dataclass
from typing import Iterator
import math
from aoc import my_aocd
from aoc.common import log


@dataclass(frozen=True)
class Tile:
    idx: int
    grid: list[str]

    def get_top_edge(self) -> str:
        return self.grid[0]

    def get_top_edge_reverse(self) -> str:
        return self.get_top_edge()[::-1]

    def get_bottom_edge(self) -> str:
        return self.grid[-1]

    def get_bottom_edge_reverse(self) -> str:
        return self.get_bottom_edge()[::-1]

    def _get_col(self, i: int) -> str:
        return ''.join([line[i] for line in self.grid])

    def get_left_edge(self) -> str:
        return self._get_col(0)

    def get_left_edge_reverse(self) -> str:
        return self.get_left_edge()[::-1]

    def get_right_edge(self) -> str:
        return self._get_col(-1)

    def get_right_edge_reverse(self) -> str:
        return self.get_right_edge()[::-1]

    def get_all_edges(self) -> list[list[str]]:
        edges = []
        edges.append(self.get_top_edge())
        edges.append(self.get_right_edge())
        edges.append(self.get_bottom_edge())
        edges.append(self.get_left_edge())
        return edges

    def get_all_edges_reverse(self) -> list[list[str]]:
        edges = []
        edges.append(self.get_top_edge_reverse())
        edges.append(self.get_right_edge_reverse())
        edges.append(self.get_bottom_edge_reverse())
        edges.append(self.get_left_edge_reverse())
        return edges

    def count_common_edges(self, tile: Tile) -> int:
        self_edges = self.get_all_edges()
        self_edges_reverse = self.get_all_edges_reverse()
        other_edges = tile.get_all_edges()
        other_edges_reverse = tile.get_all_edges_reverse()
        common = sum([1 for edge in self_edges if edge in other_edges])
        common += sum([1 for edge in self_edges_reverse
                       if edge in other_edges_reverse])
        common += sum([1 for edge in self_edges
                       if edge in other_edges_reverse])
        common += sum([1 for edge in self_edges_reverse
                       if edge in other_edges])
        return common

    @classmethod
    def flip_horizontally(cls, tile: Tile) -> Tile:
        flipped_grid = [line[::-1] for line in tile.grid]
        return Tile(tile.idx, flipped_grid)

    @classmethod
    def flip_vertically(cls, tile: Tile) -> Tile:
        flipped_grid = [tile.grid[i]
                        for i in range(len(tile.grid)-1, -1, -1)]
        return Tile(tile.idx, flipped_grid)

    @classmethod
    def rotate_90(cls, tile: Tile) -> Tile:
        rotated_grid = [tile._get_col(i)[::-1]
                        for i in range(len(tile.grid[0]))]
        return Tile(tile.idx, rotated_grid)

    @classmethod
    def rotate_180(cls, tile: Tile) -> Tile:
        rotated_grid = [tile.grid[i][::-1]
                        for i in range(len(tile.grid)-1, -1, -1)]
        return Tile(tile.idx, rotated_grid)

    @classmethod
    def rotate_270(cls, tile: Tile) -> Tile:
        rotated_grid = [tile._get_col(i)
                        for i in range(len(tile.grid[0])-1, -1, -1)]
        return Tile(tile.idx, rotated_grid)


@dataclass(frozen=True)
class TileSet():
    tiles: list[Tile]

    def get_tiles(self) -> Iterator[Tile]:
        for tile in self.tiles:
            yield tile


def _parse(inputs: tuple[str]) -> list[Tile]:
    blocks = my_aocd.to_blocks(inputs)
    tiles = list[Tile]()
    for block in blocks:
        grid = list[str]()
        for line in block:
            if line.startswith("Tile"):
                idx = int(line[len("Tile")+1:-1])
            else:
                grid.append(line)
        tiles.append(Tile(idx, grid))
    return tiles


def part_1(inputs: tuple[str]) -> int:
    tiles = TileSet(_parse(inputs))
    commons = {}
    for tile1 in tiles.get_tiles():
        commons[tile1.idx] = {}
        for tile2 in tiles.get_tiles():
            if tile1.idx == tile2.idx:
                continue
            common = tile1.count_common_edges(tile2)
            if common > 0:
                commons[tile1.idx][tile2.idx] = common
    log(commons)
    corners = [c[0] for c in commons.items() if len(c[1]) == 2]
    return math.prod([c for c in corners])


def part_2(inputs: tuple[str]) -> int:
    return 0


test = """\
Tile 2311:
..##.#..#.
##..#.....
#...##..#.
####.#...#
##.##.###.
##...#.###
.#.#.#..##
..#....#..
###...#.#.
..###..###

Tile 1951:
#.##...##.
#.####...#
.....#..##
#...######
.##.#....#
.###.#####
###.##.##.
.###....#.
..#.#..#.#
#...##.#..

Tile 1171:
####...##.
#..##.#..#
##.#..#.#.
.###.####.
..###.####
.##....##.
.#...####.
#.##.####.
####..#...
.....##...

Tile 1427:
###.##.#..
.#..#.##..
.#.##.#..#
#.#.#.##.#
....#...##
...##..##.
...#.#####
.#.####.#.
..#..###.#
..##.#..#.

Tile 1489:
##.#.#....
..##...#..
.##..##...
..#...#...
#####...#.
#..#.#.#.#
...#.#.#..
##.#...##.
..##.##.##
###.##.#..

Tile 2473:
#....####.
#..#.##...
#.##..#...
######.#.#
.#...#.#.#
.#########
.###.#..#.
########.#
##...##.#.
..###.#.#.

Tile 2971:
..#.#....#
#...###...
#.#.###...
##.##..#..
.#####..##
.#..####.#
#..#.#..#.
..####.###
..#.#.###.
...#.#.#.#

Tile 2729:
...#.#.#.#
####.#....
..#.#.....
....#..#.#
.##..##.#.
.#.####...
####.#.#..
##.####...
##..#.##..
#.##...##.

Tile 3079:
#.#.#####.
.#..######
..#.......
######....
####.#..#.
.#...#.##.
#.#####.##
..#.###...
..#.......
..#.###...
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 20)

    assert part_1(test) == 1951 * 3079 * 2971 * 1171
    assert part_2(test) == 0

    inputs = my_aocd.get_input_as_tuple(2020, 20, 1727)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

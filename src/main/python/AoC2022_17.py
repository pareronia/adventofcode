#! /usr/bin/env python3
#
# Advent of Code 2022 Day 17
#

from __future__ import annotations

import itertools
import sys
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.geometry import Position
from aoc.geometry import Vector

Input = list[Direction]
Output1 = int
Output2 = int


TEST = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
SHAPES = [
    #  🔲🔲🔲🔲
    {Position(0, 0), Position(1, 0), Position(2, 0), Position(3, 0)},
    #    🔲
    #  🔲🔲🔲
    #    🔲
    {
        Position(1, 2),
        Position(0, 1),
        Position(1, 1),
        Position(2, 1),
        Position(1, 0),
    },
    #    🔲
    #    🔲
    #  🔲🔲
    {
        Position(2, 2),
        Position(2, 1),
        Position(0, 0),
        Position(1, 0),
        Position(2, 0),
    },
    #  🔲
    #  🔲
    #  🔲
    #  🔲
    {Position(0, 0), Position(0, 1), Position(0, 2), Position(0, 3)},
    #  🔲🔲
    #  🔲🔲
    {Position(0, 0), Position(0, 1), Position(1, 0), Position(1, 1)},
]
WIDTH = 7
FLOOR = {Position(x, -1) for x in range(WIDTH)}
KEEP_ROWS = 55
LOOP_TRESHOLD = 3_000
OFFSET_X = 2
OFFSET_Y = 3


class Rock(NamedTuple):
    idx: int
    shape: set[Position]

    def move(self, vector: Vector) -> Rock:
        new_shape = {p.translate(vector) for p in self.shape}
        return Rock(self.idx, new_shape)

    def inside_x(self, start_inclusive: int, end_exclusive: int) -> bool:
        return all(start_inclusive <= p.x < end_exclusive for p in self.shape)


class State(NamedTuple):
    shape: int
    tops: tuple[int, ...]
    jet: Direction


class Cycle(NamedTuple):
    cycle: int
    top: int


class Stack:
    def __init__(self, positions: set[Position]) -> None:
        self.positions = set(positions)
        self.tops = Stack.get_tops(positions)
        self.top = 0

    @classmethod
    def get_tops(cls, positions: set[Position]) -> dict[int, int]:
        return {
            k: max(p.y for p in v)
            for k, v in itertools.groupby(
                sorted(positions, key=lambda p: p.x), lambda p: p.x
            )
        }

    def get_tops_normalized(self) -> tuple[int, ...]:
        return tuple(self.top - self.tops[i] for i in range(WIDTH))

    def overlapped_by(self, rock: Rock) -> bool:
        return any(p in self.positions for p in rock.shape)

    def add(self, rock: Rock) -> None:
        for p in rock.shape:
            self.tops[p.x] = max(self.tops[p.x], p.y)
            self.positions.add(p)
            self.top = max(self.top, p.y + 1)
        self.positions = self.get_top_rows(KEEP_ROWS)

    def get_top_rows(self, n: int) -> set[Position]:
        return set(filter(lambda p: p.y > self.top - n, self.positions))


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [Direction.from_str(s) for s in next(iter(input_data))]

    def solve(self, jets: Input, requested_drops: int) -> int:
        stack = Stack(FLOOR)
        states = dict[State, list[Cycle]]()
        jet_supplier = itertools.cycle(jets)
        shape_supplier = enumerate(itertools.cycle(SHAPES))

        def drop(drop_idx: int) -> State:
            shape_idx, shape = next(shape_supplier)
            rock = Rock(shape_idx % len(SHAPES), shape).move(
                Vector(OFFSET_X, stack.top + OFFSET_Y)
            )
            cnt = 0
            while True:
                assert cnt < 10_000, "infinite loop"
                jet = next(jet_supplier)
                state = State(rock.idx, stack.get_tops_normalized(), jet)
                if cnt == 1:
                    states.setdefault(state, []).append(
                        Cycle(drop_idx, stack.top)
                    )
                cnt += 1
                moved = rock.move(jet.vector)
                if moved.inside_x(0, WIDTH) and not stack.overlapped_by(moved):
                    rock = moved
                moved = rock.move(Direction.DOWN.vector)
                if stack.overlapped_by(moved):
                    break
                rock = moved
            stack.add(rock)
            return state

        drops = 0
        while True:
            state = drop(drops)
            drops += 1
            if drops == requested_drops:
                return stack.top
            if drops >= LOOP_TRESHOLD and len(states.get(state, [])) > 1:
                cycles = states[state]
                loop_size = cycles[1].cycle - cycles[0].cycle
                diff = cycles[1].top - cycles[0].top
                loops = (requested_drops - drops) // loop_size
                left = requested_drops - (drops + loops * loop_size)
                for _ in range(left):
                    drop(drops)
                    drops += 1
                return stack.top + loops * diff

    def part_1(self, jets: Input) -> Output1:
        return self.solve(jets, 2022)

    def part_2(self, jets: Input) -> Output2:
        return self.solve(jets, 1_000_000_000_000)

    @aoc_samples(
        (
            ("part_1", TEST, 3_068),
            ("part_2", TEST, 1_514_285_714_288),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 17)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

#! /usr/bin/env python3
#
# Advent of Code 2021 Day 23
#

from __future__ import annotations

from copy import deepcopy
import heapq
import sys
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

A, B, C, D, EMPTY, WALL = "A", "B", "C", "D", ".", "#"
ROOMS = {"room_a": 2, "room_b": 4, "room_c": 6, "room_d": 8}
ENERGY = {A: 1, B: 10, C: 100, D: 1_000}
HALLWAY_SIZE = 11
EMPTY_HALL = [EMPTY] * 11


class Room(NamedTuple):
    destination_for: str
    capacity: int
    amphipods: list[str]

    def __hash__(self) -> int:
        return hash(
            (self.destination_for, self.capacity, tuple(self.amphipods))
        )

    def completeness(self) -> int:
        return sum(_ == self.destination_for for _ in self.amphipods)

    def empty_count(self) -> int:
        return sum(_ == EMPTY for _ in self.amphipods)

    def is_complete(self) -> bool:
        return self.capacity == self.completeness()

    def available_for_move(self) -> int | None:
        if self.completeness() + self.empty_count() == self.capacity:
            return None
        for i, _ in enumerate(reversed(self.amphipods)):
            if _ != EMPTY:
                return self.capacity - 1 - i
        raise RuntimeError

    def vacancy_for(self, amphipod: str) -> int | None:
        assert amphipod != EMPTY
        if amphipod != self.destination_for:
            return None
        if self.is_complete():
            return None
        if self.empty_count() == self.capacity:
            return 0
        for i in range(self.capacity):
            if self.amphipods[i] == EMPTY:
                return i
            elif self.amphipods[i] != amphipod:
                return None
        raise RuntimeError


class Diagram(NamedTuple):
    hallway: Room
    room_a: Room
    room_b: Room
    room_c: Room
    room_d: Room

    @classmethod
    def from_strings(cls, strings: list[str]) -> Diagram:
        level = 0
        hallway = [_ for _ in strings[1].strip(WALL)]
        amphipods_a, amphipods_b, amphipods_c, amphipods_d = [], [], [], []
        for line in reversed(strings[2:-1]):
            a, b, c, d = (_ for _ in line.strip() if _ != WALL)
            amphipods_a.append(a)
            amphipods_b.append(b)
            amphipods_c.append(c)
            amphipods_d.append(d)
            level += 1
        return Diagram(
            Room(EMPTY, HALLWAY_SIZE, hallway),
            Room(A, level, amphipods_a),
            Room(B, level, amphipods_b),
            Room(C, level, amphipods_c),
            Room(D, level, amphipods_d),
        )

    def assert_valid(self) -> None:
        assert len(self.hallway.amphipods) == self.hallway.capacity
        assert len(self.room_a.amphipods) == self.room_a.capacity
        assert len(self.room_b.amphipods) == self.room_b.capacity
        assert len(self.room_c.amphipods) == self.room_c.capacity
        assert len(self.room_d.amphipods) == self.room_d.capacity
        assert all(self.hallway.amphipods[i] == EMPTY for i in ROOMS.values())
        if __debug__:
            for r in ROOMS:
                room = getattr(self, r)
                for i, amphipod in enumerate(room.amphipods):
                    if amphipod == EMPTY and i < room.capacity - 1:
                        assert room.amphipods[i + 1] == EMPTY

    def is_complete(self) -> bool:
        return (
            self.hallway.amphipods == EMPTY_HALL
            and self.room_a.is_complete()
            and self.room_b.is_complete()
            and self.room_c.is_complete()
            and self.room_d.is_complete()
        )

    def empty_in_hallway_in_range(self, range_: list[int]) -> set[int]:
        free = set[int]()
        for i in range_:
            if i in ROOMS.values():
                continue
            if self.hallway.amphipods[i] == EMPTY:
                free.add(i)
            else:
                break
        return free

    def empty_in_hallway_left_from(self, room: str) -> set[int]:
        return self.empty_in_hallway_in_range(
            list(range(ROOMS[room] - 1, -1, -1))
        )

    def empty_in_hallway_right_from(self, room: str) -> set[int]:
        return self.empty_in_hallway_in_range(
            list(range(ROOMS[room] + 1, self.hallway.capacity))
        )

    def moves_to_hallway(self) -> set[tuple[str, int, int]]:
        moves = set[tuple[str, int, int]]()
        for room_name in ROOMS.keys():
            room = getattr(self, room_name)
            from_ = room.available_for_move()
            if from_ is None:
                continue
            for to in self.empty_in_hallway_left_from(room_name):
                moves.add((room_name, from_, to))
            for to in self.empty_in_hallway_right_from(room_name):
                moves.add((room_name, from_, to))
        return moves

    def all_empty(self, pos: int, room_name: str) -> bool:
        assert room_name in ROOMS
        assert self.hallway.amphipods[pos] != EMPTY
        room_pos = ROOMS[room_name]
        assert room_pos != pos
        if room_pos < pos:
            for i in range(room_pos + 1, pos):
                if self.hallway.amphipods[i] != EMPTY:
                    return False
        elif pos < room_pos:
            for i in range(pos + 1, room_pos):
                if self.hallway.amphipods[i] != EMPTY:
                    return False
        return True

    def moves_from_hallway(self) -> set[tuple[str, int, int]]:
        moves = set[tuple[str, int, int]]()
        for from_, amphipod in enumerate(self.hallway.amphipods):
            if amphipod == EMPTY:
                continue
            rooms = [
                r
                for r in ROOMS.keys()
                if getattr(self, r).destination_for == amphipod
            ]
            assert len(rooms) == 1
            room_name = rooms[0]
            if not self.all_empty(from_, room_name):
                continue
            to = getattr(self, room_name).vacancy_for(amphipod)
            if to is None:
                continue
            moves.add((room_name, from_, to))
        return moves

    def energy_for_move_to_hallway(self, move: tuple[str, int, int]) -> int:
        room_name, from_, to = move
        room = getattr(self, room_name)
        hor = abs(ROOMS[room_name] - to)
        ver = room.capacity - from_
        return int((hor + ver) * ENERGY[room.amphipods[from_]])

    def energy_for_move_from_hallway(self, move: tuple[str, int, int]) -> int:
        room_name, from_, to = move
        room = getattr(self, room_name)
        hor = abs(ROOMS[room_name] - from_)
        ver = room.capacity - to
        return int((hor + ver) * ENERGY[self.hallway.amphipods[from_]])

    def do_move_from_hallway(self, move: tuple[str, int, int]) -> Diagram:
        room_name, from_, to = move
        copy = deepcopy(self)
        room = getattr(copy, room_name)
        assert copy.hallway.amphipods[from_] == room.destination_for
        temp = room.amphipods[to]
        assert temp == EMPTY
        room.amphipods[to] = copy.hallway.amphipods[from_]
        copy.hallway.amphipods[from_] = temp
        self.assert_valid()
        copy.assert_valid()
        return copy

    def do_move_to_hallway(self, move: tuple[str, int, int]) -> Diagram:
        room_name, from_, to = move
        copy = deepcopy(self)
        room = getattr(copy, room_name)
        temp = copy.hallway.amphipods[to]
        assert temp == EMPTY
        copy.hallway.amphipods[to] = room.amphipods[from_]
        room.amphipods[from_] = temp
        self.assert_valid()
        copy.assert_valid()
        return copy


TEST = """\
#############
#...........#
###B#C#B#D###
  #A#D#C#A#
  #########
"""

Input = list[str]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)

    def solve(self, start: Diagram) -> int:
        visited = set[Diagram]()
        to_visit = list[tuple[int, Diagram]]([(0, start)])
        while to_visit:
            energy, diagram = heapq.heappop(to_visit)
            if diagram.is_complete():
                return energy
            if diagram in visited:
                continue
            visited.add(diagram)

            for move in diagram.moves_from_hallway():
                new_diagram = diagram.do_move_from_hallway(move)
                new_energy = energy + diagram.energy_for_move_from_hallway(
                    move
                )
                heapq.heappush(to_visit, (new_energy, new_diagram))
            for move in diagram.moves_to_hallway():
                new_diagram = diagram.do_move_to_hallway(move)
                new_energy = energy + diagram.energy_for_move_to_hallway(move)
                heapq.heappush(to_visit, (new_energy, new_diagram))
        raise RuntimeError("unsolvable")

    def part_1(self, input: Input) -> Output1:
        diagram = Diagram.from_strings(input)
        log(diagram)
        diagram.assert_valid()
        return self.solve(diagram)

    def part_2(self, input: Input) -> Output2:
        input_2 = [
            input[0],
            input[1],
            input[2],
            "  #D#C#B#A#",
            "  #D#B#A#C#",
            input[3],
            input[4],
        ]
        diagram = Diagram.from_strings(input_2)
        log(diagram)
        diagram.assert_valid()
        return self.solve(diagram)

    @aoc_samples(
        (
            ("part_1", TEST, 12_521),
            ("part_2", TEST, 44_169),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2021, 23)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

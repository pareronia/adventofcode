#! /usr/bin/env python3
#
# Advent of Code 2019 Day 17
#

from __future__ import annotations

import sys
from typing import Callable
from typing import NamedTuple

from aoc.collections import index_of_sublist
from aoc.collections import indexes_of_sublist
from aoc.collections import subtract_all
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import log
from aoc.geometry import Direction
from aoc.geometry import Turn
from aoc.grid import Cell
from aoc.intcode import IntCode

NEWLINE = "\n"
SCAFFOLD = "#"
FUNCTIONS = ["A", "B", "C"]
Input = list[int]
Output1 = int
Output2 = int

TEST = """\
#######...#####
#.....#...#...#
#.....#...#...#
......#...#...#
......#...###.#
......#.....#.#
^########...#.#
......#.#...#.#
......#########
........#...#..
....#########..
....#...#......
....#...#......
....#...#......
....#####......
"""


class IntCodeComputer(NamedTuple):
    program: list[int]

    def run_camera(self) -> list[int]:
        intcode = IntCode(self.program)
        out = list[int]()
        intcode.run([], out)
        return out

    def run_robot(self, commands: list[str]) -> list[int]:
        intcode = IntCode([2] + self.program[1:])
        input = [ord(ch) for command in commands for ch in command + NEWLINE]
        output = list[int]()
        intcode.run(input, output)
        return output


class CameraFeed(NamedTuple):
    scaffolds: set[Cell]
    robot_cell: Cell
    robot_dir: Direction

    @classmethod
    def ascii_to_strings(cls, ascii: list[int]) -> list[str]:
        strings: list[str] = []
        sb: list[str] = []
        while not len(ascii) == 0:
            o = chr(ascii.pop(0))
            if o == NEWLINE and len(sb) > 0:
                strings.append("".join(sb))
                sb = []
            else:
                sb.append(o)
        return strings

    @classmethod
    def from_ascii(cls, ascii: list[int]) -> CameraFeed:
        strings = CameraFeed.ascii_to_strings(ascii)
        return CameraFeed.from_strings(strings)

    @classmethod
    def from_strings(cls, strings: list[str]) -> CameraFeed:
        arrows = Direction.capital_arrows()
        scaffolds = set[Cell]()
        log((len(strings), len(strings[0])))
        for r in range(len(strings)):
            for c in range(len(strings[0])):
                val = strings[r][c]
                if val in arrows:
                    robot_cell = Cell(r, c)
                    robot_dir = Direction.from_str(val)
                elif val == SCAFFOLD:
                    scaffolds.add(Cell(r, c))
        return CameraFeed(scaffolds, robot_cell, robot_dir)


class Command(NamedTuple):
    letter: str
    cnt: int

    def __str__(self) -> str:
        return self.letter if self.cnt == 1 else f"{self.letter}({self.cnt})"


class PathFinder:
    def __init__(self, feed: CameraFeed) -> None:
        self.feed = feed

    class State(NamedTuple):
        prev: Cell
        curr: Cell

    class DFS:
        def __init__(
            self,
            scaffolds: set[Cell],
            path: list[Cell],
            start: Cell,
            end: Cell,
        ) -> None:
            self.scaffolds = scaffolds
            self.path = path
            self.seen = set[PathFinder.State]()
            self.is_end: Callable[[PathFinder.State], bool] = (
                lambda state: state.curr == end
            )
            self.state = PathFinder.State(start, start)
            self.path.append(start)
            self.seen.add(self.state)

        def dfs(self) -> bool:
            if self.is_end(self.state):
                return True
            sc = {
                n
                for n in self.state.curr.get_capital_neighbours()
                if n in self.scaffolds
            }
            if len(sc) == 4:
                adjacent = {
                    s
                    for s in sc
                    if s.row == self.state.prev.row
                    or s.col == self.state.prev.col
                }
            else:
                adjacent = {_ for _ in sc}
            for cell in adjacent:
                new_state = PathFinder.State(self.state.curr, cell)
                if new_state in self.seen:
                    continue
                old_state = self.state
                self.state = new_state
                self.path.append(cell)
                self.seen.add(self.state)
                if self.dfs():
                    return True
                else:
                    self.path.pop()
                    self.seen.remove(self.state)
                    self.state = old_state
            return False

    def find_path(self) -> list[Cell]:
        start = self.feed.robot_cell
        end = next(
            cell
            for cell in self.feed.scaffolds
            if sum(
                n in self.feed.scaffolds or n == self.feed.robot_cell
                for n in cell.get_capital_neighbours()
            )
            == 1
        )
        path = list[Cell]()
        dfs = PathFinder.DFS(self.feed.scaffolds, path, start, end)
        dfs.dfs()
        return path

    def to_commands(self, moves: list[Direction]) -> list[Command]:
        commands = list[list[Command]]()
        curr = list[Command]()
        for i in range(len(moves) - 1):
            if moves[i] == moves[i + 1]:
                curr.append(Command(curr[-1].letter, 1))
            else:
                turn = Turn.from_directions(moves[i], moves[i + 1])
                if len(curr) > 0:
                    commands.append(curr[:])
                    curr = []
                curr.append(Command(turn.letter, 1))
        commands.append(curr[:])
        return [Command(lst[0].letter, len(lst)) for lst in commands]

    def create_ascii_input(self, max_size: int, min_repeats: int) -> list[str]:
        path = self.find_path()
        # log(path)
        moves = [self.feed.robot_dir] + [
            path[i].to(path[i + 1]) for i in range(len(path) - 1)
        ]
        # log(moves)
        commands = self.to_commands(moves)
        log(", ".join(str(c) for c in commands))
        lst = commands[:]
        d = dict[str, list[Command]]()
        for x in FUNCTIONS:
            for i in range(max_size, 1, -1):
                if i > len(lst):
                    continue
                idxs = indexes_of_sublist(lst, lst[:i])
                if len(idxs) < min_repeats:
                    continue
                else:
                    d[x] = lst[0:i][:]
                    lst = subtract_all(lst, lst[:i])
                    break
        # log(d)
        main = list[str]()
        lst = commands[:]
        while len(lst) > 0:
            for k, v in d.items():
                if index_of_sublist(lst, v) == 0:
                    main.append(k)
                    lst = lst[len(v) :]  # noqa E203
                    break
        log(main)
        ascii_input = (
            [",".join(main)]
            + [
                ",".join(f"{c.letter},{c.cnt}" for c in d[x])
                for x in FUNCTIONS
            ]
            + ["n"]
        )
        log(ascii_input)
        return ascii_input


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [int(_) for _ in list(input_data)[0].split(",")]

    def part_1(self, program: Input) -> Output1:
        feed = CameraFeed.from_ascii(IntCodeComputer(program).run_camera())
        return sum(
            cell.row * cell.col
            for cell in feed.scaffolds
            if all(n in feed.scaffolds for n in cell.get_capital_neighbours())
        )

    def part_2(self, program: Input) -> Output2:
        computer = IntCodeComputer(program)
        path_finder = PathFinder(CameraFeed.from_ascii(computer.run_camera()))
        ascii_input = path_finder.create_ascii_input(max_size=5, min_repeats=3)
        return computer.run_robot(ascii_input)[-1]

    def samples(self) -> None:
        path_finder = PathFinder(CameraFeed.from_strings(TEST.splitlines()))
        assert path_finder.create_ascii_input(max_size=3, min_repeats=2,) == [
            "A,B,C,B,A,C",
            "R,8,R,8",
            "R,4,R,4,R,8",
            "L,6,L,2",
            "n",
        ]


solution = Solution(2019, 17)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

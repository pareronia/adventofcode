#! /usr/bin/env python3
#
# Advent of Code 2015 Day 7
#

import sys
from collections import deque

from aoc.common import InputData
from aoc.common import SolutionBase

Gate = tuple[str, str, str | None, str]
Wires = dict[str, int]
Input = tuple[dict[str, int], list[Gate]]
Output1 = int
Output2 = int


TEST = """\
123 -> x
456 -> y
x AND y -> d
x OR y -> e
x LSHIFT 2 -> f
y RSHIFT 2 -> g
NOT x -> h
NOT y -> i
i -> j
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        wires, gates = Wires(), list[Gate]()
        for line in input_data:
            first, second = line.split(" -> ")
            splits = first.split()
            if len(splits) == 1:
                if first.isnumeric():
                    wires[second] = int(first)
                    continue
                else:
                    in1, op, in2 = splits[0], "SET", None
            elif len(splits) == 2:
                in1, op, in2 = splits[1], "NOT", None
            else:
                in1, op, in2 = splits[0], splits[1], splits[2]
            gates.append((in1, op, in2, second))
        return wires, gates

    def solve(self, wires: Wires, gates: list[Gate], wire: str) -> int:
        def exec_op(in1: str, op: str, in2: str | None, out: str) -> None:
            match op:
                case "SET":
                    wires[out] = wires[in1]
                case "AND":
                    assert in2 is not None
                    if in1.isnumeric():
                        wires[out] = int(in1) & wires[in2]
                    else:
                        wires[out] = wires[in1] & wires[in2]
                case "LSHIFT":
                    assert in2 is not None
                    wires[out] = wires[in1] << int(in2)
                case "NOT":
                    wires[out] = (1 << 16) + ~wires[in1]
                case "OR":
                    assert in2 is not None
                    wires[out] = wires[in1] | wires[in2]
                case "RSHIFT":
                    assert in2 is not None
                    wires[out] = wires[in1] >> int(in2)
                case _:
                    raise ValueError

        q = deque(gates)
        while q:
            in1, op, in2, out = q.popleft()
            if (in1.isnumeric() or in1 in wires) and (
                in2 is None or in2.isnumeric() or in2 in wires
            ):
                exec_op(in1, op, in2, out)
            else:
                q.append((in1, op, in2, out))
        return wires[wire]

    def part_1(self, input: Input) -> Output1:
        wires, gates = input
        return self.solve(wires.copy(), gates, "a")

    def part_2(self, input: Input) -> Output2:
        wires, gates = input
        wires_2 = wires.copy()
        wires_2["b"] = self.solve(wires.copy(), gates, "a")
        return self.solve(wires_2, gates, "a")

    def samples(self) -> None:
        wires, gates = self.parse_input(TEST.splitlines())
        assert self.solve(wires, gates, "x") == 123
        assert self.solve(wires, gates, "y") == 456
        assert self.solve(wires, gates, "d") == 72
        assert self.solve(wires, gates, "e") == 507
        assert self.solve(wires, gates, "f") == 492
        assert self.solve(wires, gates, "g") == 114
        assert self.solve(wires, gates, "h") == 65412
        assert self.solve(wires, gates, "i") == 65079
        assert self.solve(wires, gates, "j") == 65079


solution = Solution(2015, 7)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

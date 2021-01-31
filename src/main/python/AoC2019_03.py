#! /usr/bin/env python3
#
# Advent of Code 2019 Day 3
#

from typing import Iterator
from aoc import my_aocd

Coord = tuple[int, int]
Wire = list[Coord]


def _parse(inputs: tuple[str]) -> (Iterator[str], Iterator[str]):
    return ((_ for _ in inputs[i].split(",")) for i in range(2))


def _to_leg(start: Coord, instruction: str) -> Iterator[Coord]:
    direction = instruction[0]
    amount = int(instruction[1:])
    if direction not in {"R", "U", "L", "D"}:
        raise ValueError("Invalid input")
    for i in range(1, amount + 1):
        if direction == "R":
            yield (start[0] + i, start[1])
        elif direction == "U":
            yield (start[0], start[1] + i)
        elif direction == "L":
            yield (start[0] - i, start[1])
        else:
            yield (start[0], start[1] - i)


def _to_wire(start: Coord, instructions: Iterator[str]) -> Wire:
    wire = Wire()
    for instruction in instructions:
        for coord in _to_leg(start, instruction):
            wire.append(coord)
            start = coord
    return wire


def _get_wires(wire_instructions) -> (Wire, Wire):
    return (_to_wire((0, 0), instructions)
            for instructions in wire_instructions)


def part_1(inputs: tuple[str]) -> int:
    wire1, wire2 = _get_wires(_parse(inputs))
    return min(abs(x) + abs(y) for x, y in set(wire1) & set(wire2))


def part_2(inputs: tuple[str]) -> int:
    wire1, wire2 = _get_wires(_parse(inputs))
    return min(wire1.index(c) + 1 + wire2.index(c) + 1
               for c in set(wire1) & set(wire2))


TEST1 = """\
R8,U5,L5,D3
U7,R6,D4,L4
""".splitlines()
TEST2 = """\
R75,D30,R83,U83,L12,D49,R71,U7,L72
U62,R66,U55,R34,D71,R55,D58,R83
""".splitlines()
TEST3 = """\
R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
U98,R91,D20,R16,D67,R40,U7,R15,U6,R7
""".splitlines()


def main() -> None:
    my_aocd.print_header(2019, 3)

    assert part_1(TEST1) == 6
    assert part_1(TEST2) == 159
    assert part_1(TEST3) == 135
    assert part_2(TEST1) == 30
    assert part_2(TEST2) == 610
    assert part_2(TEST3) == 410

    inputs = my_aocd.get_input(2019, 3, 2)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

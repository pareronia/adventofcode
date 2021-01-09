#! /usr/bin/env python3
#
# Advent of Code 2015 Day 19
#

from collections import defaultdict
from aoc import my_aocd


def _parse(inputs: tuple[str]) -> tuple[dict, str]:
    blocks = my_aocd.to_blocks(inputs)
    replacements = defaultdict(list[str])
    for line in blocks[0]:
        split = line.split(" => ")
        replacements[split[0]].append(split[1])
    assert len(blocks[1]) == 1
    return replacements, blocks[1][0]


def _run_replacement(replacements: dict, molecule: str) -> set[str]:
    molecules = set[str]()
    key = ""
    for i, c in enumerate(molecule):
        if len(key) == 2:
            key = ""
            continue
        if c in replacements:
            key = c
        elif molecule[i:i+2] in replacements:
            key = molecule[i:i+2]
        else:
            continue
        for r in replacements[key]:
            molecules.add(molecule[:i] + r + molecule[i+len(key):])
    return molecules


def part_1(inputs: tuple[str]) -> int:
    replacements, molecule = _parse(inputs)
    return len(_run_replacement(replacements, molecule))


def _find(replacements: dict, molecule: str, target: str, cnt: int) -> int:
    new_molecules = _run_replacement(replacements, molecule)
    if target in new_molecules:
        return cnt + 1
    else:
        for m in new_molecules:
            if len(m) > len(target):
                return 0
            result = _find(replacements, m, target, cnt + 1)
            if result > 0:
                return result
        return 0


def part_2(inputs: tuple[str]) -> int:
    replacements, molecule = _parse(inputs)
    return _find(replacements, "e", molecule, 0)


TEST1 = """\
H => HO
H => OH
O => HH

HOH
""".splitlines()
TEST2 = """\
H => HO
H => OH
O => HH

HOHOHO
""".splitlines()
TEST3 = """\
H => HO
H => OH
Oo => HH

HOHOoHO
""".splitlines()
TEST4 = """\
e => H
e => O
H => HO
H => OH
O => HH

HOH
""".splitlines()
TEST5 = """\
e => H
e => O
H => HO
H => OH
O => HH

HOHOHO
""".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 19)

    assert part_1(TEST1) == 4
    assert part_1(TEST2) == 7
    assert part_1(TEST3) == 6
    assert part_2(TEST4) == 3
    assert part_2(TEST5) == 6

    inputs = my_aocd.get_input(2015, 19, 45)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

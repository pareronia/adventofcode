#! /usr/bin/env python3
#
# Advent of Code 2015 Day 19
#

import aocd
from collections import defaultdict

from aoc import my_aocd
from aoc.common import log


def _parse(inputs: tuple[str]) -> tuple[dict[str, list[str]], str]:
    blocks = my_aocd.to_blocks(inputs)
    replacements = defaultdict[str, list[str]](list[str])
    for line in blocks[0]:
        split = line.split(" => ")
        replacements[split[0]].append(split[1])
    assert len(blocks[1]) == 1
    return replacements, blocks[1][0]


def _run_replacement(
    replacements: dict[str, list[str]], molecule: str
) -> set[str]:
    molecules = set[str]()
    key = ""
    for i, c in enumerate(molecule):
        if len(key) == 2:
            key = ""
            continue
        if c in replacements:
            key = c
        elif molecule[i : i + 2] in replacements:  # noqa E203
            key = molecule[i : i + 2]  # noqa E203
        else:
            continue
        for r in replacements[key]:
            molecules.add(molecule[:i] + r + molecule[i + len(key) :])  # noqa E203
    return molecules


def part_1(inputs: tuple[str]) -> int:
    replacements, molecule = _parse(inputs)
    return len(_run_replacement(replacements, molecule))


def _fabricate(
    target: str, molecule: str, replacements: dict[str, list[str]], cnt: int
) -> int:
    new_molecules = _run_replacement(replacements, molecule)
    if target in new_molecules:
        return cnt + 1
    else:
        for m in new_molecules:
            if len(m) > len(target):
                return 0
            result = _fabricate(target, m, replacements, cnt + 1)
            if result > 0:
                return result
        return 0


def part_2_bis(inputs: tuple[str]) -> int:
    replacements, molecule = _parse(inputs)
    return _fabricate(molecule, "e", replacements, 0)


def part_2(inputs: tuple[str]) -> int:
    replacements, molecule = _parse(inputs)
    cnt = 0
    new_molecule = molecule
    while new_molecule != "e":
        for new in replacements.keys():
            for old in replacements[new]:
                if old in new_molecule:
                    new_molecule = new_molecule.replace(old, new, 1)
                    cnt += 1
                    log(cnt)
                    log(f"{old}->{new}")
                    log(new_molecule)
    return cnt


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
    puzzle = aocd.models.Puzzle(2015, 19)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 4  # type:ignore[arg-type]
    assert part_1(TEST2) == 7  # type:ignore[arg-type]
    assert part_1(TEST3) == 6  # type:ignore[arg-type]
    assert part_2_bis(TEST4) == 3  # type:ignore[arg-type]
    assert part_2_bis(TEST5) == 6  # type:ignore[arg-type]

    inputs = my_aocd.get_input_data(puzzle, 45)
    result1 = part_1(inputs)  # type:ignore[arg-type]
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)  # type:ignore[arg-type]
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

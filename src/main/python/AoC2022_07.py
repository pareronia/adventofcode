#! /usr/bin/env python3
#
# Advent of Code 2022 Day 7
#


from collections import defaultdict

import aocd

from aoc import my_aocd


def get_sizes(inputs: tuple[str]) -> dict[str, int]:
    path = list[str]()
    sizes = defaultdict[str, int](int)
    for line in inputs[1:]:
        if line.startswith("$ cd "):
            name = line[5:]
            if name == "..":
                path = path[:-1]
            else:
                path.append(name)
        elif not line.startswith("$"):
            first, _ = line.split()
            if not first == "dir":
                size = int(first)
                for i in range(len(path) + 1):
                    pp = "/".join(_ for _ in path[:i])
                    sizes["/" + pp] += size
    return sizes


def part_1(inputs: tuple[str]) -> int:
    return sum(v for v in get_sizes(inputs).values() if v <= 100_000)


def part_2(inputs: tuple[str]) -> int:
    sizes = get_sizes(inputs)
    wanted = 30_000_000 - (70_000_000 - sizes["/"])
    return next(v for v in sorted(sizes.values()) if v >= wanted)


TEST = """\
$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 7)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 95_437
    assert part_2(TEST) == 24_933_642

    inputs = my_aocd.get_input_data(puzzle, 1027)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()

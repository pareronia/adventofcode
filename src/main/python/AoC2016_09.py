#! /usr/bin/env python3
#
# Advent of Code 2016 Day 9
#

from aoc import my_aocd


def _parse(inputs: tuple[str]) -> str:
    assert len(inputs) == 1
    return inputs[0]


def _decompressed_length(input_: str, recursive: bool) -> int:
    if '(' not in input_:
        return len(input_)

    cnt = 0
    in_marker = False
    marker = ""
    i = 0
    while i < len(input_):
        c = input_[i]
        if c == '(':
            in_marker = True
        elif c == ')':
            splits = marker.split('x')
            skip = int(splits[0])
            repeat = int(splits[1])
            if recursive:
                skipped = input_[i+1:i+1+skip]
                cnt += repeat * _decompressed_length(skipped, True)
            else:
                cnt += repeat * skip
            i += skip
            marker = ""
            in_marker = False
        else:
            if in_marker:
                marker += c
            else:
                cnt += 1
        i += 1
    return cnt


def part_1(inputs: tuple[str]) -> int:
    return _decompressed_length(_parse(inputs), False)


def part_2(inputs: tuple[str]) -> int:
    return _decompressed_length(_parse(inputs), True)


TEST1 = "ADVENT".splitlines()
TEST2 = "A(1x5)BC".splitlines()
TEST3 = "(3x3)XYZ".splitlines()
TEST4 = "A(2x2)BCD(2x2)EFG".splitlines()
TEST5 = "(6x1)(1x3)A".splitlines()
TEST6 = "X(8x2)(3x3)ABCY".splitlines()
TEST7 = "(27x12)(20x12)(13x14)(7x10)(1x12)A".splitlines()
TEST8 = "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN".splitlines()


def main() -> None:
    my_aocd.print_header(2016, 9)

    assert part_1(TEST1) == 6
    assert part_1(TEST2) == 7
    assert part_1(TEST3) == 9
    assert part_1(TEST4) == 11
    assert part_1(TEST5) == 6
    assert part_1(TEST6) == 18
    assert part_2(TEST3) == 9
    assert part_2(TEST6) == 20
    assert part_2(TEST7) == 241920
    assert part_2(TEST8) == 445

    inputs = my_aocd.get_input(2016, 9, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

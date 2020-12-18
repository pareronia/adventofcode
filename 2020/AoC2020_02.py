#! /usr/bin/env python3
#
# Advent of Code 2020 Day 2
#
import re
from operator import xor
import my_aocd


def _parse(input_: str) -> (int, int, str, str, int):
    m = re.search(r'^(\d{1,2})-(\d{1,2}) ([a-z]{1}): ([a-z]+)$', input_)
    first = int(m.group(1))
    second = int(m.group(2))
    wanted = m.group(3)
    passw = m.group(4)
    m1 = re.findall(r'(' + wanted + '{1})', passw)
    return (first, second, wanted, passw, len(m1))


def _output(input_: str, parsed: str, check: bool) -> None:
    print(f"{input_} -> {parsed} : {check}")


def part_1(inputs: tuple[str], verbose: bool = False) -> int:
    def _check_valid_1(input_: str, verbose: bool) -> bool:
        parsed = _parse(input_)
        check = parsed[4] in range(parsed[0], parsed[1] + 1)
        if (verbose):
            _output(input_, parsed, check)
        return check
    return len([i for i in inputs if _check_valid_1(i, verbose)])


def part_2(inputs: tuple[str], verbose: bool = False) -> int:
    def _check_valid_2(input_: str, verbose: bool) -> bool:
        parsed = _parse(input_)
        first = parsed[0]
        second = parsed[1]
        wanted = parsed[2]
        passw = parsed[3]
        first_matched = passw[first - 1] == wanted
        second_matched = passw[second - 1] == wanted
        check = xor(first_matched, second_matched)
        if (verbose):
            _output(input_, parsed, check)
        return check
    return len([i for i in inputs if _check_valid_2(i, verbose)])


test = {"1-3 a: abcde",
        "2-9 c: ccccccccc",
        "1-3 b: cdefg",
        }


def main() -> None:
    my_aocd.print_header(2020, 2)

    assert part_1(test) == 2
    assert part_2(test) == 1

    inputs = my_aocd.get_input_as_tuple(2020, 2, 1000)
    result1 = part_1(inputs)
    result2 = part_2(inputs)
    print("Part 1: " + str(result1))
    print("Part 2: " + str(result2))


if __name__ == '__main__':
    main()
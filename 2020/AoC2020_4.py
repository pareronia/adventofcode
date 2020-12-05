#! /usr/bin/env python3
#
# Advent of Code 2020 Day 4
#
import aocd


def _get_input() -> list[str]:
    inputs = aocd.get_data(year=2020, day=4).splitlines()
    assert len(inputs) == 1009
    return inputs


def _normalize(inputs: list[str]) -> list[str]:
    lines = []
    line = ""
    for input_ in inputs:
        if len(input_) > 0:
            line += input_
            continue
        lines.append(line)
        line = ""
    lines.append(line)
    return lines


def _check_valid_1(line: str, verbose: bool) -> bool:
    colons = line.count(":")
    has_cid = "cid:" in line
    valid = colons == 8 or (colons == 7 and not has_cid)
    if verbose:
        if valid:
            print(f"colons: {colons}, has_cid: {has_cid} -> valid ({line})")
        else:
            print(f"colons: {colons}, has_cid: {has_cid} -> INVALID ({line})")
    return valid


def part_1(inputs: list[str], verbose: bool = False) -> int:
    return len([input_ for input_ in _normalize(inputs)
                if _check_valid_1(input_, verbose)])


test = ["ecl:gry pid:860033327 eyr:2020 hcl:#fffffd",
        "byr:1937 iyr:2017 cid:147 hgt:183cm",
        "",
        "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884",
        "hcl:#cfa07d byr:1929",
        "",
        "hcl:#ae17e1 iyr:2013",
        "eyr:2024",
        "ecl:brn pid:760753108 byr:1931",
        "hgt:179cm",
        "",
        "hcl:#cfa07d eyr:2025 pid:166559648",
        "iyr:2011 ecl:brn hgt:59in",
        ]


def main() -> None:
    print("====================================================")
    print("AoC 2020 Day 4 - https://adventofcode.com/2020/day/4")
    print("====================================================")
    print("")

    assert part_1(test) == 2

    inputs = _get_input()
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")


if __name__ == '__main__':
    main()

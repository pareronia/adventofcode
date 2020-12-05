#! /usr/bin/env python3
#
# Advent of Code 2020 Day 4
#


def part_1(inputs: list[str]) -> int:
    invalid = 0
    colons = 0
    has_cid = False
    for input_ in inputs:
        if len(input_) == 0:
            if colons <= 6:
                invalid += 1
            if colons == 7 and not has_cid:
                invalid += 1
            colons = 0
            has_cid = False
            continue
        colons += input_.count(":")
        has_cid = "cid:" in input_
    return invalid


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


if __name__ == '__main__':
    main()

#! /usr/bin/env python3
#
# Advent of Code 2020 Day 4
#
import re
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
            line += input_ + " "
            continue
        lines.append(line)
        line = ""
    lines.append(line[:-1])
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


class Passport:
    byr: str  # (Birth Year)
    iyr: str  # (Issue Year)
    eyr: str  # (Expiration Year)
    hgt: str  # (Height)
    hcl: str  # (Hair Color)
    ecl: str  # (Eye Color)
    pid: str  # (Passport ID)

    def __str__(self):
        return type(self).__module__.split(".")[-1] \
                + "." + type(self).__name__ \
                + "<" \
                + ', '.join(['{k}={v}'.format(k=k, v=self.__dict__.get(k))
                             for k in self.__dict__]) \
                + ">"

    def _byr_valid(self) -> bool:
        return 1920 <= int(self.byr) <= 2002

    def _iyr_valid(self) -> bool:
        return 2010 <= int(self.iyr) <= 2020

    def _eyr_valid(self) -> bool:
        return 2020 <= int(self.eyr) <= 2030

    def _hgt_valid(self) -> bool:
        if self.hgt.endswith("in"):
            return 59 <= int(self.hgt[:-2]) <= 76
        elif self.hgt.endswith("cm"):
            return 150 <= int(self.hgt[:-2]) <= 193
        else:
            return False

    def _hcl_valid(self) -> bool:
        return re.search(r'^#[0-9a-f]{6}$', self.hcl) is not None

    def _ecl_valid(self) -> bool:
        return self.ecl in ("amb", "blu", "brn", "gry", "grn", "hzl", "oth")

    def _pid_valid(self) -> bool:
        return re.search(r'^[0-9]{9}$', self.pid) is not None

    def valid(self) -> bool:
        return self._byr_valid() and self._iyr_valid() \
            and self._eyr_valid() and self._hgt_valid() \
            and self._hcl_valid() and self._ecl_valid() \
            and self._pid_valid()


def _get_value(line: str, field: str) -> str:
    m = re.search(r'' + field + r':([A-Za-z0-9#]+)', line)
    return m.group(1) if m is not None else None


def _check_valid_2(line: str, verbose: bool) -> bool:
    if not _check_valid_1(line, verbose):
        return False

    passport = Passport()
    passport.byr = _get_value(line, "byr")
    passport.iyr = _get_value(line, "iyr")
    passport.eyr = _get_value(line, "eyr")
    passport.hgt = _get_value(line, "hgt")
    passport.hcl = _get_value(line, "hcl")
    passport.ecl = _get_value(line, "ecl")
    passport.pid = _get_value(line, "pid")
    return passport.valid()


def part_1(inputs: list[str], verbose: bool = False) -> int:
    return len([input_ for input_ in _normalize(inputs)
                if _check_valid_1(input_, verbose)])


def part_2(inputs: list[str], verbose: bool = False) -> int:
    return len([input_ for input_ in _normalize(inputs)
                if _check_valid_2(input_, verbose)])


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
        "",
        "eyr:1972 cid:100",
        "hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926",
        "",
        "iyr:2019",
        "hcl:#602927 eyr:1967 hgt:170cm",
        "ecl:grn pid:012533040 byr:1946",
        "",
        "hcl:dab227 iyr:2012",
        "ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277",
        "",
        "hgt:59cm ecl:zzz",
        "eyr:2038 hcl:74454a iyr:2023",
        "pid:3556412378 byr:2007",
        "",
        "pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980",
        "hcl:#623a2f",
        "",
        "eyr:2029 ecl:blu cid:129 byr:1989",
        "iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm",
        "",
        "hcl:#888785",
        "hgt:164cm byr:2001 iyr:2015 cid:88",
        "pid:545766238 ecl:hzl",
        "eyr:2022",
        "",
        "iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021",
        "pid:093154719",
        ]


def main() -> None:
    print("====================================================")
    print("AoC 2020 Day 4 - https://adventofcode.com/2020/day/4")
    print("====================================================")
    print("")

    assert part_1(test) == 10
    assert part_2(test) == 6

    inputs = _get_input()
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

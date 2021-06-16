#! /usr/bin/env python3
#
# Advent of Code 2015 Day 7
#

import re
from aoc import my_aocd

ABBA = r'([a-z])(?!\1)([a-z])\2\1'
HYPERNET = r'\[([a-z]*)\]'
ABA = r'([a-z])(?!\1)[a-z]\1'


def _is_tls(ip: str) -> bool:
    for b in re.findall(HYPERNET, ip):
        if re.search(ABBA, b) is not None:
            return False
        ip = ip.replace('[' + b + ']', '/')
    return re.search(ABBA, ip) is not None


def _aba2bab(aba: str) -> str:
    return aba[1] + aba[0] + aba[1]


def _is_sls(ip: str) -> bool:
    bs = re.findall(HYPERNET, ip)
    for b in bs:
        ip = ip.replace('[' + b + ']', '/')
    abas = [m.group()
            for i in range(len(ip) - 3)
            for m in re.finditer(ABA, ip[i:])]
    for aba in abas:
        bab = _aba2bab(aba)
        for b in bs:
            if bab in b:
                return True
    return False


def part_1(inputs: tuple[str]) -> int:
    return sum(1 for input_ in inputs if _is_tls(input_))


def part_2(inputs: tuple[str]) -> int:
    return sum(1 for input_ in inputs if _is_sls(input_))


TEST1 = '''\
abba[mnop]qrst
abcd[bddb]xyyx
aaaa[qwer]tyui
ioxxoj[asdfgh]zxcvbn
abcox[ooooo]xocba
'''.splitlines()
TEST2 = '''\
aba[bab]xyz
xyx[xyx]xyx
aaa[kek]eke
zazbz[bzb]cdb
'''.splitlines()


def main() -> None:
    my_aocd.print_header(2016, 7)

    assert part_1(TEST1) == 2
    assert part_2(TEST2) == 3

    inputs = my_aocd.get_input(2016, 7, 2000)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

#! /usr/bin/env python3
#
# Advent of Code 2016 Day 21
#

from aoc import my_aocd
from aoc.common import log


def _get_digits(s: str, expected: int) -> tuple[int]:
    digits = tuple(map(lambda x: int(x), filter(lambda x: x.isdigit(), s)))
    if len(digits) != expected:
        raise ValueError(f"Expected {expected}, got {len(digits)}")
    return digits


def _swap_positions(s: str, first: int, second: int) -> str:
    ch = list(s)
    ch[first], ch[second] = ch[second], ch[first]
    return "".join(ch)


def _swap_letters(s: str, first: str, second: str):
    i1 = s.index(first)
    i2 = s.index(second)
    return _swap_positions(s, i1, i2)


def _rotate_right(s: str, amount: int) -> str:
    amount = amount % len(s)
    return s[-amount:] + s[:-amount]


def _rotate_by_letter(s: str, c: str) -> str:
    index = s.index(c)
    amount = index + 1 + (1 if index >= 4 else 0)
    return _rotate_right(s, amount)


def _solve(operations: tuple[str], start: str, encrypt: bool) -> str:
    ch = start
    for i in range(len(operations)):
        idx = i if encrypt else len(operations) - 1 - i
        op = operations[idx]
        log(op)
        if op.startswith("swap position "):
            params = _get_digits(op, 2)
            first = params[0] if encrypt else params[1]
            second = params[1] if encrypt else params[0]
            ch = _swap_positions(ch, first, second)
        elif op.startswith("swap letter "):
            s = op[len("swap letter "):].split(" with letter ")
            assert len(s[0]) == 1 and len(s[1]) == 1
            first = s[0] if encrypt else s[1]
            second = s[1] if encrypt else s[0]
            ch = _swap_letters(ch, first, second)
        elif op.startswith("reverse positions "):
            params = _get_digits(op, 2)
            ch = ch[:params[0]] \
                + ch[params[0]:params[1]+1][::-1] \
                + ch[params[1]+1:]
        elif op.startswith("rotate ") and not op.startswith("rotate based "):
            param = _get_digits(op, 1)[0]
            if "left" in op:
                amount = len(ch) - (param if encrypt else len(ch) - param)
            else:
                amount = param if encrypt else len(ch) - param
            ch = _rotate_right(ch, amount)
        elif op.startswith("move "):
            params = _get_digits(op, 2)
            from_ = params[0 if encrypt else 1]
            to = params[1 if encrypt else 0]
            if from_ == to:
                raise ValueError("Expected to and from to be different")
            if from_ < to:
                ch = ch[0:from_] + ch[from_+1:to+1] + ch[from_] + ch[to+1:]
            else:
                ch = ch[0:to] + ch[from_] + ch[to:from_] + ch[from_+1:]
        elif op.startswith("rotate based "):
            letter = op[-1:]
            if encrypt:
                ch = _rotate_by_letter(ch, letter)
            else:
                ch1 = ch
                ch2 = ""
                while ch2 != ch:
                    ch1 = _rotate_right(ch1, len(ch1) - 1)
                    ch2 = _rotate_by_letter(ch1, letter)
                ch = ch1
        else:
            raise ValueError("Invalid input")
        log(ch)
    return ch


def part_1(inputs: tuple[str]) -> str:
    return _solve(inputs, "abcdefgh", True)


def part_2(inputs: tuple[str]) -> str:
    return _solve(inputs, "fbgdceah", False)


TEST = '''\
swap position 4 with position 0
swap letter d with letter b
reverse positions 0 through 4
rotate left 1 step
move position 1 to position 4
move position 3 to position 0
rotate based on position of letter b
rotate based on position of letter d
'''.splitlines()


def main() -> None:
    my_aocd.print_header(2016, 21)

    assert _solve(TEST, "abcde", True) == "decab"
    assert _solve(TEST, "decab", False) == "abcde"

    inputs = my_aocd.get_input(2016, 21, 100)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

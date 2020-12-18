#! /usr/bin/env python3
#
# Advent of Code 2020 Day 18
#

from typing import NamedTuple
import my_aocd
from common import log


def _parse(inputs: tuple[str]) -> list[str]:
    return [[_ for _ in input_ if _ != " "] for input_ in inputs]


class ResultAndPosition(NamedTuple):
    result: int
    position: int


def evaluate(expression: list[str], pos: int = 0) -> ResultAndPosition:
    stack = [0, "+"]
    result = 0
    i = pos
    while i < len(expression):
        _ = expression[i]
        if _ in {"+", "*"}:
            stack.append(_)
        elif _ == ")":
            return ResultAndPosition(result, i)
        else:
            if _ == "(":
                sub = evaluate(expression, i+1)
                operand_2 = sub.result
                i = sub.position
            elif not _.isnumeric():
                raise ValueError("invalid input")
            else:
                operand_2 = int(_)
            operator = stack.pop()
            operand_1 = stack.pop()
            if operator == "+":
                result = operand_1 + operand_2
            else:
                result = operand_1 * operand_2
            stack.append(result)
        i += 1
    return ResultAndPosition(result, i)


def part_1(inputs: tuple[str]) -> int:
    expressions = _parse(inputs)
    [log(str(expression)) for expression in expressions]
    return sum([evaluate(exp).result for exp in expressions])


def part_2(inputs: tuple[str]) -> int:
    new_inputs = []
    for input_ in inputs:
        x = input_.replace("(", "((")
        x = x.replace(")", "))")
        x = "(" + x.replace("*", ") * (") + ")"
        log(f"{input_} -> {x}")
        new_inputs.append(x)
    expressions = _parse(new_inputs)
    return sum([evaluate(exp).result for exp in expressions])


test = """\
1 + 2 * 3 + 4 * 5 + 6
1 + (2 * 3) + (4 * (5 + 6))
2 * 3 + (4 * 5)
5 + (8 * 3 + 9 + 3 * 4 * 3)
5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))
((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 18)

    assert part_1(test) == 71 + 51 + 26 + 437 + 12240 + 13632
    assert part_2(test) == 231 + 51 + 46 + 1445 + 669060 + 23340

    inputs = my_aocd.get_input_as_tuple(2020, 18, 383)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
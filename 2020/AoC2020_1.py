#! /usr/bin/env python3
#
# Advent of Code 2020 Day 1
#
import aocd
import time


def _get_input() -> tuple:
    inputs = tuple(aocd.get_data(year=2020, day=1).splitlines())
    assert len(inputs) == 200
    return inputs


def _print_elapsed(prefix: str, start: float, stop: float) -> None:
    print(f"{prefix} Elapsed time: {stop-start:0.5f} seconds")


def _print_part_1(prefix: str, first: int, second: int, result: int) -> None:
    print(f"{prefix} Twosome: {first} * {second} = {result}")


def _print_part_2(prefix: str, first: int, second: int, third: int,
                  result: int) -> None:
    print(f"{prefix} Threesome: {first} * {second} * {third} = {result}")


def _part_1_primeagen(inputs: tuple) -> int:
    others = set()
    start = time.perf_counter()
    end = len(inputs) - 1
    for i in range(0, end):
        i1 = int(inputs[i])
        others.add(i1)
        check = 2020 - i1
        if check in others:
            result = i1*check
            stop = time.perf_counter()
            _print_part_1("[part_1_primeagen]", i1, check, result)
            _print_elapsed("[part_1_primeagen]", start, stop)
            return result


def _part_2_primeagen(inputs: tuple) -> int:
    others = set()
    start = time.perf_counter()
    end = len(inputs) - 1
    for i in range(0, end):
        i1 = int(inputs[i])
        others.add(i1)
        for j in range(i + 1, end):
            i2 = int(inputs[j])
            check = 2020 - i1 - i2
            if check in others:
                result = i1*i2*check
                stop = time.perf_counter()
                _print_part_2("[part_2_primeagen]", i1, i2, check, result)
                _print_elapsed("[part_2_primeagen]", start, stop)
                return result


def _squared(inputs: tuple, target: int) -> tuple:
    for i in range(0, len(inputs) - 1):
        i1 = int(inputs[i])
        for j in inputs[i:]:
            i2 = int(j)
            if i1 + i2 == target:
                return (i1, i2)


def _part_1_squared(inputs: tuple) -> int:
    start = time.perf_counter()
    twosome = _squared(inputs, 2020)
    result = twosome[0]*twosome[1]
    stop = time.perf_counter()
    _print_part_1("[part_1_squared]", twosome[0], twosome[1], result)
    _print_elapsed("[part_1_squared]", start, stop)
    return result


def _part_2_cubed(inputs: tuple) -> int:
    start = time.perf_counter()
    for i in range(0, len(inputs) - 1):
        i1 = int(inputs[i])
        twosome = _squared(inputs[i:], 2020-i1)
        if twosome is not None:
            threesome = (twosome[0], twosome[1], i1)
            break
    result = threesome[0]*threesome[1]*threesome[2]
    stop = time.perf_counter()
    _print_part_2("[part_2_cubed]", threesome[0], threesome[1], threesome[2],
                  result)
    _print_elapsed("[part_2_cubed]", start, stop)
    return result


test = ("1721",
        "979",
        "366",
        "299",
        "675",
        "1456",
        )


def main() -> None:
    print("====================================================")
    print("AoC 2020 Day 1 - https://adventofcode.com/2020/day/1")
    print("====================================================")
    print("")

    assert _part_1_squared(test) == 514579
    assert _part_2_cubed(test) == 241861950
    assert _part_1_primeagen(test) == 514579
    assert _part_2_primeagen(test) == 241861950

    inputs = _get_input()
    print(inputs)
    _part_1_squared(inputs)
    _part_1_primeagen(inputs)
    _part_2_cubed(inputs)
    _part_2_primeagen(inputs)


if __name__ == '__main__':
    main()

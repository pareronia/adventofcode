#! /usr/bin/env python3
#
# Advent of Code 2020 Day 1
#
import time
import my_aocd


def _print_elapsed(prefix: str, start: float, stop: float) -> None:
    print(f"{prefix} Elapsed time: {stop-start:0.5f} seconds")


def _print_part_1(prefix: str, first: int, second: int, result: int) -> None:
    print(f"{prefix} Twosome: {first} * {second} = {result}")


def _print_part_2(prefix: str, first: int, second: int, third: int,
                  result: int) -> None:
    print(f"{prefix} Threesome: {first} * {second} * {third} = {result}")


def part_1_primeagen(inputs: tuple[int]) -> int:
    others = set[int]()
    start = time.perf_counter()
    for n1 in inputs:
        others.add(n1)
        check = 2020 - n1
        if check in others:
            result = n1*check
            stop = time.perf_counter()
            _print_part_1("[part_1_primeagen]", n1, check, result)
            _print_elapsed("[part_1_primeagen]", start, stop)
            return result


def part_2_primeagen(inputs: tuple[int]) -> int:
    others = set[int]()
    start = time.perf_counter()
    for i in range(len(inputs)):
        n1 = inputs[i]
        others.add(n1)
        for n2 in inputs[i:]:
            check = 2020 - n1 - n2
            if check in others:
                result = n1*n2*check
                stop = time.perf_counter()
                _print_part_2("[part_2_primeagen]", n1, n2, check, result)
                _print_elapsed("[part_2_primeagen]", start, stop)
                return result


def _squared(inputs: tuple[int], target: int) -> tuple[int]:
    for i in range(len(inputs)):
        n1 = inputs[i]
        for n2 in inputs[i:]:
            if n1 + n2 == target:
                return (n1, n2)


def part_1_squared(inputs: tuple[int]) -> int:
    start = time.perf_counter()
    twosome = _squared(inputs, 2020)
    result = twosome[0]*twosome[1]
    stop = time.perf_counter()
    _print_part_1("[part_1_squared]", twosome[0], twosome[1], result)
    _print_elapsed("[part_1_squared]", start, stop)
    return result


def part_2_cubed(inputs: tuple[int]) -> int:
    start = time.perf_counter()
    for i in range(len(inputs)):
        n1 = inputs[i]
        twosome = _squared(inputs[i:], 2020-n1)
        if twosome is not None:
            threesome = (twosome[0], twosome[1], n1)
            break
    result = threesome[0]*threesome[1]*threesome[2]
    stop = time.perf_counter()
    _print_part_2("[part_2_cubed]", threesome[0], threesome[1], threesome[2],
                  result)
    _print_elapsed("[part_2_cubed]", start, stop)
    return result


test = (1721,
        979,
        366,
        299,
        675,
        1456,
        )


def main() -> None:
    my_aocd.print_header(2020, 1)

    assert part_1_squared(test) == 514579
    assert part_2_cubed(test) == 241861950
    assert part_1_primeagen(test) == 514579
    assert part_2_primeagen(test) == 241861950

    inputs = my_aocd.get_input_as_ints_tuple(2020, 1, 200)
    part_1_squared(inputs)
    part_1_primeagen(inputs)
    part_2_cubed(inputs)
    part_2_primeagen(inputs)


if __name__ == '__main__':
    main()

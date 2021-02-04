#! /usr/bin/env python3
#
# Advent of Code 2020 Day 1
#
import time
from aoc import my_aocd
from aoc.common import log


def _parse(inputs: tuple[str]) -> tuple[int]:
    return tuple(int(_) for _ in inputs)


def _print_elapsed(prefix: str, start: float, stop: float) -> None:
    log(f"{prefix} Elapsed time: {stop-start:0.5f} seconds")


def _print_part_1(prefix: str, first: int, second: int, result: int) -> None:
    log(f"{prefix} Twosome: {first} * {second} = {result}")


def _print_part_2(prefix: str, first: int, second: int, third: int,
                  result: int) -> None:
    log(f"{prefix} Threesome: {first} * {second} * {third} = {result}")


def _squared(numbers: tuple[int], target: int) -> tuple[int]:
    for i, n1 in enumerate(numbers):
        for n2 in numbers[i:]:
            if n1 + n2 == target:
                return n1, n2


def _seen_2(numbers: tuple[int], target: int) -> tuple[int]:
    seen = set[int]()
    for i, n1 in enumerate(numbers):
        seen.add(n1)
        n2 = target - n1
        if n2 in seen:
            return n1, n2


def _do_part_1(numbers: tuple[int], f, prefix: str) -> int:
    start = time.perf_counter()
    n1, n2 = f(numbers, 2020)
    result = n1 * n2
    stop = time.perf_counter()
    _print_part_1(prefix, n1, n2, result)
    _print_elapsed(prefix, start, stop)
    return result


def part_1_squared(inputs: tuple[str]) -> int:
    return _do_part_1(_parse(inputs), _squared, "[part_1_squared]")


def part_1_seen(inputs: tuple[str]) -> int:
    return _do_part_1(_parse(inputs), _seen_2, "[part_1_seen]")


def _cubed(numbers: tuple[int], target: int) -> tuple[int]:
    for i, n1 in enumerate(numbers):
        if (twosome := _squared(numbers[i:], target - n1)) is not None:
            return n1, twosome[0], twosome[1]


def _seen_3(numbers: tuple[int], target: int) -> tuple[int]:
    seen = set[int]()
    for i, n1 in enumerate(numbers):
        seen.add(n1)
        for n2 in numbers[i:]:
            n3 = 2020 - n1 - n2
            if n3 in seen:
                return n1, n2, n3


def _do_part_2(numbers: tuple[int], f, prefix: str) -> int:
    start = time.perf_counter()
    n1, n2, n3 = f(numbers, 2020)
    result = n1 * n2 * n3
    stop = time.perf_counter()
    _print_part_2("[part_2_cubed]", n1, n2, n3, result)
    _print_elapsed("[part_2_cubed]", start, stop)
    return result


def part_2_cubed(inputs: tuple[str]) -> int:
    return _do_part_2(_parse(inputs), _cubed, "[part_2_cubed]")


def part_2_seen(inputs: tuple[str]) -> int:
    return _do_part_2(_parse(inputs), _seen_3, "[part_2_seen]")


def part_1(inputs: tuple[str]) -> int:
    return part_1_seen(inputs)


def part_2(inputs: tuple[str]) -> int:
    return part_2_seen(inputs)


TEST = """\
1721
979
366
299
675
1456
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 1)

    assert part_1_squared(TEST) == 514579
    assert part_2_cubed(TEST) == 241861950
    assert part_1_seen(TEST) == 514579
    assert part_2_seen(TEST) == 241861950

    inputs = my_aocd.get_input(2020, 1, 200)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

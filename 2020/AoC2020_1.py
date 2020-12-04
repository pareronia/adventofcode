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


def _primeagen(inputs: tuple) -> int:
    others = set()
    start = time.perf_counter()
    for _input in inputs:
        i = int(_input)
        others.add(i)
        check = 2020 - i
        if check in others:
            result = i*check
            print(f"[primeagen] Twosome: {i} * {check} = {result}")
            stop = time.perf_counter()
            _print_elapsed("[primeagen]", start, stop)
            return result


def _squared(inputs: tuple) -> int:
    start = time.perf_counter()
    twosome = None
    end = len(inputs) - 1
    for i in range(0, end):
        i1 = int(inputs[i])
        for j in range(i + 1, end):
            i2 = int(inputs[j])
            if i1 + i2 == 2020:
                twosome = (i1, i2)
                break
    result = twosome[0]*twosome[1]
    stop = time.perf_counter()
    print(f"[squared] Twosome: {twosome[0]} * {twosome[1]}"
          f" = {result}")
    _print_elapsed("[squared]", start, stop)
    return result


def _cubed(inputs: tuple) -> int:
    start = time.perf_counter()
    end = len(inputs) - 1
    for i in range(0, end):
        i1 = int(inputs[i])
        for j in range(i + 1, end):
            i2 = int(inputs[j])
            for k in range(j + 1, end):
                i3 = int(inputs[k])
                if i1 + i2 + i3 == 2020:
                    threesome = (i1, i2, i3)
                    break
    result = threesome[0]*threesome[1]*threesome[2]
    stop = time.perf_counter()
    print(f"[cubed] Threesome: {threesome[0]} * {threesome[1]}"
          f" * {threesome[2]}"
          f" = {result}")
    _print_elapsed("[cubed]", start, stop)
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

    assert _squared(test) == 514579
    assert _cubed(test) == 241861950
    assert _primeagen(test) == 514579

    inputs = _get_input()
    print(inputs)
    _squared(inputs)
    _primeagen(inputs)
    _cubed(inputs)


if __name__ == '__main__':
    main()

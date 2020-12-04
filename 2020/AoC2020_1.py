#! /usr/bin/env python3
#
# Advent of Code 2020 Day 1
#
import aocd
import time


def _get_input() -> tuple:
    return tuple(aocd.get_data(year=2020, day=1).splitlines())


def _print_elapsed(prefix: str, start: float, stop: float) -> None:
    print(f"{prefix} Elapsed time: {stop-start:0.5f} seconds")


def _primeagen(inputs: tuple) -> None:
    others = set()
    start = time.perf_counter()
    for _input in inputs:
        i = int(_input)
        others.add(i)
        check = 2020 - i
        if check in others:
            print(f"[primeagen] Twosome: {i} * {check} = {i*check}")
            stop = time.perf_counter()
            _print_elapsed("[primeagen]", start, stop)
            return


def _squared(inputs: tuple) -> None:
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
    stop = time.perf_counter()
    print(f"[squared] Twosome: {twosome[0]} * {twosome[1]}"
          f" = {twosome[0]*twosome[1]}")
    _print_elapsed("[squared]", start, stop)


def _cubed(inputs: tuple) -> None:
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
    stop = time.perf_counter()
    print(f"[cubed] Threesome: {threesome[0]} * {threesome[1]}"
          f" * {threesome[2]}"
          f" = {threesome[0]*threesome[1]*threesome[2]}")
    _print_elapsed("[cubed]", start, stop)


def main() -> None:
    inputs = _get_input()
    print(inputs)
    _squared(inputs)
    _primeagen(inputs)
    _cubed(inputs)


if __name__ == '__main__':
    main()

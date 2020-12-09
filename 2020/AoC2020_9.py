#! /usr/bin/env python3
#
# Advent of Code 2020 Day 9
#
import my_aocd


def has_sum(numbers: list[int], target: int) -> bool:
    result = False
    for n1 in numbers:
        n2 = target - n1
        if n2 in numbers:
            result = True
    return result


def part_1(inputs: tuple[int], window_size: int = 25) -> int:
    _range = range(window_size, len(inputs))
    for i in _range:
        n = inputs[i]
        search_window = inputs[i-window_size:i]
        if not has_sum(search_window, n):
            return n
    raise ValueError("Unsolvable")


def part_2(inputs: tuple[int]) -> int:
    return 0


test = (35,
        20,
        15,
        25,
        47,
        40,
        62,
        55,
        65,
        95,
        102,
        117,
        150,
        182,
        127,
        219,
        299,
        277,
        309,
        576
        )


def main() -> None:
    my_aocd.print_header(2020, 9)

    assert part_1(test, 5) == 127
    # assert part_2(test) == 0

    inputs = my_aocd.get_input_as_ints_tuple(2020, 9, 1000)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

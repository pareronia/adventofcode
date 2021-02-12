#! /usr/bin/env python3
#
# Advent of Code 2019 Day 8
#

from advent_of_code_ocr import convert_6
from aoc import my_aocd

WIDTH = 25
HEIGHT = 6


def _parse(inputs: tuple[str], width: int, height: int) -> tuple[str]:
    assert len(inputs) == 1
    layer_size = width * height
    assert len(inputs[0]) % layer_size == 0
    return (inputs[0][i:i+layer_size]
            for i in range(0, len(inputs[0]), layer_size))


def part_1(inputs: tuple[str]) -> int:
    layers = _parse(inputs, WIDTH, HEIGHT)
    c = [(lyr.count("0"), lyr.count("1"), lyr.count("2")) for lyr in layers]
    min0 = min(zeroes for zeroes, _, _ in c)
    return [ones * twos for zeroes, ones, twos in c if zeroes == min0][0]


def _get_image(inputs: tuple[str], width: int, height: int) -> str:
    layers = list(_parse(inputs, width, height))
    image = ""
    for i in range(0, len(layers[0])):
        for lyr in layers:
            if lyr[i] == "2":
                continue
            else:
                break
        image += lyr[i]
    return image


def part_2(inputs: tuple[str]) -> str:
    image = _get_image(inputs, WIDTH, HEIGHT)
    to_ocr = "\n".join([image[i:i+WIDTH]
                        for i in range(0, WIDTH * HEIGHT, WIDTH)])
    return convert_6(to_ocr, fill_pixel="1", empty_pixel="0")


TEST = '''0222112222120000'''.splitlines()


def main() -> None:
    my_aocd.print_header(2019, 8)

    assert _get_image(TEST, 2, 2) == "0110"

    inputs = my_aocd.get_input(2019, 8, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()

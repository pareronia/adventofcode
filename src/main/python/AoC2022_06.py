#! /usr/bin/env python3
#
# Advent of Code 2022 Day 6
#


from aoc.common import aoc_main


def solve(buffer: str, size: int) -> int:
    for i in range(size, len(buffer)):
        test = buffer[i - size : i]  # noqa
        if len(set(test)) == size:
            return i
    raise RuntimeError


def part_1(inputs: tuple[str, ...]) -> int:
    return solve(inputs[0], 4)


def part_2(inputs: tuple[str, ...]) -> int:
    return solve(inputs[0], 14)


TEST1 = "mjqjpqmgbljsphdztnvjfqwrcgsmlb".splitlines()
TEST2 = "bvwbjplbgvbhsrlpgdmjqwftvncz".splitlines()
TEST3 = "nppdvjthqldpwncqszvftbrmjlhg".splitlines()
TEST4 = "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg".splitlines()
TEST5 = "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw".splitlines()


@aoc_main(2022, 6, part_1, part_2)
def main() -> None:
    assert part_1(TEST1) == 7  # type:ignore[arg-type]
    assert part_1(TEST2) == 5  # type:ignore[arg-type]
    assert part_1(TEST3) == 6  # type:ignore[arg-type]
    assert part_1(TEST4) == 10  # type:ignore[arg-type]
    assert part_1(TEST5) == 11  # type:ignore[arg-type]
    assert part_2(TEST1) == 19  # type:ignore[arg-type]
    assert part_2(TEST2) == 23  # type:ignore[arg-type]
    assert part_2(TEST3) == 23  # type:ignore[arg-type]
    assert part_2(TEST4) == 29  # type:ignore[arg-type]
    assert part_2(TEST5) == 26  # type:ignore[arg-type]


if __name__ == "__main__":
    main()

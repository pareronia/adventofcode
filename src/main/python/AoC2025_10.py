#! /usr/bin/env python3
#
# Advent of Code 2025 Day 10
#

import itertools
import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

Input = InputData
Output1 = int
Output2 = int


TEST = """\
[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, inputs: Input) -> Output1:
        machines = list[tuple[int, list[int]]]()
        for line in inputs:
            ll, *presses, _ = line.split()
            ll = ll[1:-1]
            ll = ll.translate(str.maketrans(".#", "01"))
            light = int(ll, base=2)
            log((ll, light))
            buttons = list[int]()
            for press in presses:
                press = press[1:-1]  # noqa:PLW2901
                pp = press.split(",")
                m = sum(2 ** (len(ll) - 1 - int(p)) for p in pp)
                log((f"-> {m:b}", m))
                buttons.append(m)
            machines.append((light, buttons))
        ans = 0
        for machine in machines:
            log(f"{machine=}")
            for n in range(1, len(machine[1]) + 1):
                log(f"{n=}")
                for c in itertools.combinations(machine[1], n):
                    v = 0
                    for cc in c:
                        v = v ^ cc
                    log(f"{c} -> {v}")
                    if v == machine[0]:
                        log("yes")
                        ans += n
                        break
                else:
                    continue
                break
        return ans

    def part_2(self, inputs: Input) -> Output2:
        machines = list[tuple[list[int], list[list[int]]]]()
        for line in inputs:
            _, *presses, joltages = line.split()
            jj = list(map(int, joltages[1:-1].split(",")))
            pp = list[list[int]]()
            for press in presses:
                press = press[1:-1]  # noqa:PLW2901
                pp.append(list(map(int, press.split(","))))
            machines.append((jj, pp))
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 7),
            ("part_2", TEST, 33),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2025, 10)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

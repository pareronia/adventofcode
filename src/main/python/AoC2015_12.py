#! /usr/bin/env python3
#
# Advent of Code 2015 Day 12
#

import json
import sys
from typing import Any

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST1 = "[1,2,3]"
TEST2 = '{"a":2,"b":4}'
TEST3 = "[[[3]]]"
TEST4 = '{"a":{"b":4},"c":-1}'
TEST5 = '{"a":[-1,1]}'
TEST6 = '[-1,{"a":1}]'
TEST7 = "[]"
TEST8 = "{}"
TEST9 = '[1,{"c":"red","b":2},3]'
TEST10 = '{"d":"red","e":[1,2,3,4],"f":5}'
TEST11 = '[1,"red",5]'

Input = str
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return next(iter(input_data))

    def add_all_numbers(self, string: str) -> int:
        numbers = "".join(
            c for c in string if c in {",", "-"} or c.isnumeric()
        )
        return sum(int(n) for n in numbers.split(",") if len(n) > 0)

    def part_1(self, json_str: Input) -> Output1:
        return self.add_all_numbers(json_str)

    def part_2(self, json_str: Input) -> Output2:
        def object_hook(obj: dict[Any, Any]) -> Any:  # noqa: ANN401
            if "red" in obj.values():
                return None
            return obj

        string = json.dumps(json.loads(json_str, object_hook=object_hook))
        return self.add_all_numbers(string)

    @aoc_samples(
        (
            ("part_1", TEST1, 6),
            ("part_1", TEST2, 6),
            ("part_1", TEST3, 3),
            ("part_1", TEST4, 3),
            ("part_1", TEST5, 0),
            ("part_1", TEST6, 0),
            ("part_1", TEST7, 0),
            ("part_2", TEST8, 0),
            ("part_2", TEST1, 6),
            ("part_2", TEST9, 4),
            ("part_2", TEST10, 0),
            ("part_2", TEST11, 6),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2015, 12)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

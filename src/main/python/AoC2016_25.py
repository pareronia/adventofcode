#! /usr/bin/env python3
#
# Advent of Code 2015 Day 25
#

import sys

from aoc.assembunny import Assembunny
from aoc.assembunny import AssembunnyInstruction
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.vm import Program
from aoc.vm import VirtualMachine

Input = list[AssembunnyInstruction]
Output1 = int
Output2 = str


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, inputs: InputData) -> Input:
        return Assembunny.parse(tuple(inputs))

    def solve_vm(self, instructions: Input) -> int:
        vm_instructions = Assembunny.translate(instructions)

        def run_program(init_a: int) -> list[str]:
            output = list()
            program = Program(
                vm_instructions,
                inf_loop_treshold=6_000,
                output_consumer=lambda s: output.append(s),
            )
            program.set_register_value("a", init_a)
            try:
                VirtualMachine().run_program(program)
            finally:
                return output

        n = 150
        while True:
            output = run_program(n)
            if all(i % 2 == int(v) for i, v in enumerate(output)):
                return n
            n += 1

    def part_1(self, instructions: Input) -> int:
        return self.solve_vm(instructions)

    def part_2(self, instructions: Input) -> str:
        return "🎄"

    def samples(self) -> None:
        pass


solution = Solution(2016, 25)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

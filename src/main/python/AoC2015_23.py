#! /usr/bin/env python3
#
# Advent of Code 2015 Day 23
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.vm import Instruction
from aoc.vm import Program
from aoc.vm import VirtualMachine

TEST = """\
inc b
jio a, +2
tpl b
inc b
"""


Input = list[Instruction]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        def create_instruction(
            operation: str, operands: tuple[str, ...]
        ) -> Instruction:
            match operation:
                case "hlf":
                    return Instruction.DIV(operands[0], "2")
                case "tpl":
                    return Instruction.MUL(operands[0], "3")
                case "inc":
                    return Instruction.ADD(operands[0], 1)
                case "jmp":
                    return Instruction.JMP(int(operands[0]))
                case "jie":
                    return Instruction.JIE(operands[0], int(operands[1]))
                case "jio":
                    return Instruction.JI1(operands[0], int(operands[1]))
                case _:
                    msg = "Unexpected instruction"
                    raise NotImplementedError(msg)

        return [
            create_instruction(operation, operands)
            for operation, operands in (
                (line[:3], tuple(_ for _ in line[4:].split(", ")))
                for line in input_data
            )
        ]

    def solve(self, instructions: list[Instruction]) -> int:
        program = Program(instructions)
        VirtualMachine().run_program(program)
        return int(program.registers["b"])

    def part_1(self, instructions: Input) -> Output1:
        return self.solve(instructions)

    def part_2(self, instructions: Input) -> Output2:
        instructions.insert(0, Instruction.SET("a", "1"))
        return self.solve(instructions)

    @aoc_samples(
        (
            ("part_1", TEST, 4),
            ("part_2", TEST, 2),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2015, 23)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

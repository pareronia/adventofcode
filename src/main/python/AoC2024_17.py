#! /usr/bin/env python3
#
# Advent of Code 2024 Day 17
#

import sys
from collections import deque

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
from aoc.vm import Instruction
from aoc.vm import Program
from aoc.vm import VirtualMachine

Input = tuple[int, int, int, list[int]]
Output1 = str
Output2 = int


TEST1 = """\
Register A: 729
Register B: 0
Register C: 0

Program: 0,1,5,4,3,0
"""
TEST2 = """\
Register A: 2024
Register B: 0
Register C: 0

Program: 0,3,5,4,3,0
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        lines = list(input_data)
        a, b, c = map(int, (lines[i][12:] for i in range(3)))
        ops = list(map(int, lines[4][9:].split(",")))
        return a, b, c, ops

    def create_instructions(self, ops: list[int]) -> list[Instruction]:
        def combo(operand: int) -> str:
            match operand:
                case 0 | 1 | 2 | 3:
                    return str(operand)
                case 4 | 5 | 6:
                    return ["*A", "*B", "*C"][operand - 4]
                case _:
                    raise ValueError

        ins = []
        ip_map = dict[int, int]()
        ip = 0
        for i in range(0, len(ops), 2):
            ip_map[i] = ip
            opcode, operand = ops[i], ops[i + 1]
            match opcode:
                case 0:
                    ins.append(Instruction.RSH("A", combo(operand)))
                    ip += 1
                case 1:
                    ins.append(Instruction.XOR("B", str(operand)))
                    ip += 1
                case 2:
                    ins.append(Instruction.SET("B", str(combo(operand))))
                    ins.append(Instruction.AND("B", "7"))
                    ip += 2
                case 3:
                    ins.append(
                        Instruction.JN0("*A", "!" + str(ip_map[operand]))
                    )
                    ip += 1
                case 4:
                    ins.append(Instruction.XOR("B", "*C"))
                    ip += 1
                case 5:
                    ins.append(Instruction.SET("X", str(combo(operand))))
                    ins.append(Instruction.AND("X", "7"))
                    ins.append(Instruction.OUT("*X"))
                    ip += 3
                case 6:
                    ins.append(Instruction.SET("C", "*B"))
                    ins.append(Instruction.RSH("C", combo(operand)))
                    ip += 2
                case 7:
                    ins.append(Instruction.SET("C", "*A"))
                    ins.append(Instruction.RSH("C", combo(operand)))
                    ip += 2
                case _:
                    raise ValueError
        return ins

    def run_program(
        self, ins: list[Instruction], a: int, b: int, c: int
    ) -> list[str]:
        output = []
        program = Program(ins, output_consumer=lambda s: output.append(s))
        program.registers["A"] = int(a)
        program.registers["B"] = int(b)
        program.registers["C"] = int(c)
        VirtualMachine().run_program(program)
        return output

    def part_1(self, input: Input) -> Output1:
        a, b, c, ops = input
        ins = self.create_instructions(ops)
        return ",".join(self.run_program(ins, a, b, c))

    def part_2(self, input: Input) -> Output2:
        _, b, c, ops = input
        ins = self.create_instructions(ops)
        wanted = list(str(_) for _ in ops)
        log(f"{wanted=}")
        seen = set([0])
        q = deque([0])
        while q:
            cand_a = q.popleft() * 8
            for i in range(8):
                na = cand_a + i
                res = self.run_program(ins, na, b, c)
                if res == wanted:
                    return na
                if res == wanted[-len(res) :] and na not in seen:  # noqa E203
                    seen.add(na)
                    log(na)
                    q.append(na)
        raise RuntimeError("unsolvable")

    @aoc_samples(
        (
            ("part_1", TEST1, "4,6,3,5,6,3,5,2,1,0"),
            ("part_2", TEST2, 117440),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 17)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()

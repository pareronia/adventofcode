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

Input = InputData
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
        return input_data

    def run_program(self, lines: list[str]) -> list[str]:
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
        ins.append(Instruction.SET("A", lines[0][12:]))
        ins.append(Instruction.SET("B", lines[1][12:]))
        ins.append(Instruction.SET("C", lines[2][12:]))
        ip = 3
        ops = list(map(int, lines[4][9:].split(",")))
        for i in range(0, len(ops), 2):
            ip_map[i] = ip
            opcode, operand = ops[i], ops[i + 1]
            match opcode:
                case 0:
                    ins.append(Instruction.SET("X", "2"))
                    ins.append(Instruction.SET("Y", combo(operand)))
                    ins.append(Instruction.ADD("Y", -1))
                    ins.append(Instruction.LSH("X", "*Y"))
                    ins.append(Instruction.DIV("A", "*X"))
                    ip += 5
                case 1:
                    ins.append(Instruction.XOR("B", str(operand)))
                    ip += 1
                case 2:
                    ins.append(Instruction.SET("X", str(combo(operand))))
                    ins.append(Instruction.MOD("X", "8"))
                    ins.append(Instruction.SET("B", "*X"))
                    ip += 3
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
                    ins.append(Instruction.MOD("X", "8"))
                    ins.append(Instruction.OUT("*X"))
                    ip += 3
                case 6:
                    ins.append(Instruction.SET("X", "2"))
                    ins.append(Instruction.SET("Y", combo(operand)))
                    ins.append(Instruction.ADD("Y", -1))
                    ins.append(Instruction.LSH("X", "*Y"))
                    ins.append(Instruction.SET("B", "*A"))
                    ins.append(Instruction.DIV("B", "*X"))
                    ip += 6
                case 7:
                    ins.append(Instruction.SET("X", "2"))
                    ins.append(Instruction.SET("Y", combo(operand)))
                    ins.append(Instruction.ADD("Y", -1))
                    ins.append(Instruction.LSH("X", "*Y"))
                    ins.append(Instruction.SET("C", "*A"))
                    ins.append(Instruction.DIV("C", "*X"))
                    ip += 6
        output = []
        program = Program(ins, output_consumer=lambda s: output.append(s))
        vm = VirtualMachine()
        vm.run_program(program)
        return output

    def part_1(self, input: Input) -> Output1:
        lines = list(input)
        output = self.run_program(lines)
        return ",".join(map(str, output))

    def part_2(self, input: Input) -> Output2:
        lines = list(input)

        def run_with(a: str) -> list[str]:
            lines[0] = "Register A: " + a
            return self.run_program(lines)

        wanted = lines[4][9:].replace(",", "")
        log(f"{wanted=}")
        seen = set(["0"])
        q = deque(["0"])
        while q:
            a = q.popleft()
            if "".join(str(_) for _ in run_with(a)) == wanted:
                return int(a)
            na = int(a) * 8
            for i in range(8):
                test = str(na + i)
                res = "".join(str(_) for _ in run_with(test))
                size = len(res)
                if res == wanted[-size:]:
                    if test not in seen:
                        seen.add(test)
                        log(test)
                        q.append(test)
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

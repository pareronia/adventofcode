from __future__ import annotations
from typing import NamedTuple
from aoc.vm import Instruction


class AssembunnyInstruction(NamedTuple):
    operation: str
    operands: tuple[str]

    @classmethod
    def of(cls, in_: tuple[str]) -> AssembunnyInstruction:
        return AssembunnyInstruction(in_[0], (in_[1:]))

    def check_valid_operation(self) -> None:
        if self.operation not in ("cpy", "inc", "dec", "jnz", "tgl", "out"):
            raise ValueError("Invalid instruction operation")
        for operand in self.operands:
            if not (operand.strip('-').isnumeric() or operand in "abcd"):
                raise ValueError("Invalid instruction operand")


class Assembunny:

    @classmethod
    def parse(cls, inputs: tuple[str]) -> list[AssembunnyInstruction]:
        return [AssembunnyInstruction.of(input_.split()) for input_ in inputs]

    @classmethod
    def translate(cls, lines: list[AssembunnyInstruction]) \
            -> list[Instruction]:
        instructions = list[Instruction]()
        for line in lines:
            line.check_valid_operation()
            if line.operation == "cpy":
                value, register = line.operands
                if value.strip('-').isnumeric():
                    instructions.append(Instruction.SET(register, value))
                elif register in ("abcd"):
                    instructions.append(Instruction.SET(register, "*" + value))
                else:
                    raise ValueError("Invalid operands for cpy")
            elif line.operation == "inc":
                register = line.operands[0]
                instructions.append(Instruction.ADD(register, 1))
            elif line.operation == "dec":
                register = line.operands[0]
                instructions.append(Instruction.ADD(register, -1))
            elif line.operation == "jnz":
                test, value = line.operands
                if test in ("abcd"):
                    test = "*" + test
                elif not test.strip('-').isnumeric():
                    raise ValueError("Invalid operands for jnz")
                if value in ("abcd"):
                    value = "*" + value
                elif not value.strip('-').isnumeric():
                    raise ValueError("Invalid operands for jnz")
                instructions.append(Instruction.JN0(test, value))
            elif line.operation == "tgl":
                register, = line.operands
                instructions.append(Instruction.TGL(register))
            elif line.operation == "out":
                value, = line.operands
                if value in ("abcd"):
                    value = "*" + value
                elif not value.strip('-').isnumeric():
                    raise ValueError("Invalid operands for out")
                instructions.append(Instruction.OUT(value))
        return instructions

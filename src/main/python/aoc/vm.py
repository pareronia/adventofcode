from __future__ import annotations
from collections.abc import Callable
from .common import log


class Instruction:
    _opcode: str
    _operands: tuple[str]

    def __init__(self, opcode: str, operands) -> None:
        self._opcode = opcode
        self._operands = operands

    def __repr__(self):
        return f"Instruction[opcode: {self._opcode}, "\
               f"operands: {self._operands}]"

    @classmethod
    def NOP(cls) -> Instruction:
        return Instruction("NOP", None)

    @classmethod
    def JMP(cls, value: int) -> Instruction:
        return Instruction("JMP", (value, ))

    @classmethod
    def MEM(cls, address: int, value: int) -> Instruction:
        return Instruction("MEM", (address, value))

    @classmethod
    def ACC(cls, value: int) -> Instruction:
        return Instruction("ACC", (value, ))

    @property
    def opcode(self) -> str:
        return self._opcode

    @property
    def operands(self) -> tuple[str]:
        return self._operands


class Program:
    _instructions: list[Instruction]
    _memory: dict[int, object]
    _accumulator: int
    _instruction_pointer: int
    _error_on_inf_loop: bool
    _error_on_jump_beyond_zero: bool

    def __init__(self,
                 instructions: list[Instruction],
                 error_on_inf_loop: bool = False,
                 error_on_jump_beyond_zero: bool = True
                 ) -> None:
        self._instructions = instructions
        self._error_on_inf_loop = error_on_inf_loop
        self._error_on_jump_beyond_zero = error_on_jump_beyond_zero
        self._memory = dict[int, object]()
        self._accumulator = 0
        self._instruction_pointer = 0

    @property
    def instructions(self) -> list[Instruction]:
        return self._instructions

    @property
    def error_on_inf_loop(self) -> bool:
        return self._error_on_inf_loop

    @property
    def error_on_jump_beyond_zero(self) -> bool:
        return self._error_on_jump_beyond_zero

    @property
    def instruction_pointer(self) -> int:
        return self._instruction_pointer

    @property
    def memory(self) -> dict[int, object]:
        return self._memory

    @property
    def accumulator(self) -> int:
        return self._accumulator

    def null_operation(self) -> None:
        pass

    def add_to_accumulator(self, value: int) -> int:
        self._accumulator += value
        return self._accumulator

    def move_instruction_pointer(self, value: int) -> int:
        self._instruction_pointer += value
        return self._instruction_pointer

    def set_memory_value(self, address: int, value: object) -> None:
        self._memory[address] = value


class VirtualMachine:
    _instruction_set: dict[str, Callable[[Program, Instruction], None]]

    def __init__(self):
        self._instruction_set = {
            "NOP": self._nop,
            "JMP": self._jmp,
            "ACC": self._acc,
            "MEM": self._mem,
        }

    def _nop(self, program: Program, instruction: Instruction):
        program.null_operation()
        program.move_instruction_pointer(1)

    def _jmp(self, program: Program, instruction: Instruction):
        (count, *_) = instruction.operands
        program.move_instruction_pointer(count)

    def _acc(self, program: Program, instruction: Instruction):
        (value, *_) = instruction.operands
        program.add_to_accumulator(value)
        program.move_instruction_pointer(1)

    def _mem(self, program: Program, instruction: Instruction):
        (address, value) = instruction.operands
        program.set_memory_value(address, value)
        program.move_instruction_pointer(1)

    def run_program(self, program: Program):
        seen = set[int]()
        while 0 <= program.instruction_pointer < len(program.instructions) \
                and program.instruction_pointer not in seen:
            instruction = program.instructions[program.instruction_pointer]
            seen.add(program.instruction_pointer)
            if instruction.opcode not in self._instruction_set:
                raise ValueError("Unsupported instruction: "
                                 + instruction.opcode)
            self._instruction_set[instruction.opcode](program, instruction)
            if program.instruction_pointer < 0 \
               and program.error_on_jump_beyond_zero:
                raise ValueError("Invalid instruction argument")
            if program.instruction_pointer in seen \
                    and program.error_on_inf_loop:
                raise RuntimeError("Infinite loop!")
        log("Normal exit")

from __future__ import annotations
from collections import defaultdict
from collections.abc import Callable
from aoc.common import log


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
    def JIE(cls, register: str, value: int) -> Instruction:
        return Instruction("JIE", (register, value))

    @classmethod
    def JI1(cls, register: str, value: int) -> Instruction:
        return Instruction("JI1", (register, value))

    @classmethod
    def JN0(cls, register: str, value: int) -> Instruction:
        return Instruction("JN0", (register, value))

    @classmethod
    def SET(cls, register: str, value: int) -> Instruction:
        return Instruction("SET", (register, value))

    @classmethod
    def CPY(cls, from_register: str, to_register: str) -> Instruction:
        return Instruction("CPY", (from_register, to_register))

    @classmethod
    def ADD(cls, register: str, value: int) -> Instruction:
        return Instruction("ADD", (register, value))

    @classmethod
    def MUL(cls, register: str, value: int) -> Instruction:
        return Instruction("MUL", (register, value))

    @classmethod
    def DIV(cls, register: str, value: int) -> Instruction:
        return Instruction("DIV", (register, value))

    @classmethod
    def MEM(cls, address: int, value: object) -> Instruction:
        return Instruction("MEM", (address, value))

    @property
    def opcode(self) -> str:
        return self._opcode

    @property
    def operands(self) -> tuple[str]:
        return self._operands


class Program:
    _instructions: list[Instruction]
    _memory: dict[int, object]
    _registers: dict[str, object]
    _instruction_pointer: int
    _inf_loop_treshold: int
    _error_on_jump_beyond_zero: bool
    _cycles: int

    def __init__(self,
                 instructions: list[Instruction],
                 inf_loop_treshold: int = None,
                 error_on_jump_beyond_zero: bool = True
                 ) -> None:
        self._instructions = instructions
        self._inf_loop_treshold = inf_loop_treshold
        self._error_on_jump_beyond_zero = error_on_jump_beyond_zero
        self._memory = dict[int, object]()
        self._registers = dict[str, object]()
        self._instruction_pointer = 0
        self._cycles = 0

    @property
    def instructions(self) -> list[Instruction]:
        return self._instructions

    @property
    def inf_loop_treshold(self) -> int:
        return self._inf_loop_treshold

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
    def registers(self) -> dict[str, object]:
        return self._registers

    @property
    def cycles(self) -> int:
        return self._cycles

    def null_operation(self) -> None:
        pass

    def set_register_value(self, register: str, value: object) -> None:
        self._registers[register] = value

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
            "JIE": self._jie,
            "JI1": self._ji1,
            "JN0": self._jn0,
            "SET": self._set,
            "CPY": self._cpy,
            "ADD": self._add,
            "MUL": self._mul,
            "DIV": self._div,
            "MEM": self._mem,
        }

    def _nop(self, program: Program, instruction: Instruction):
        program.null_operation()
        program.move_instruction_pointer(1)

    def _jmp(self, program: Program, instruction: Instruction):
        log(instruction.opcode + str(instruction.operands))
        (count, *_) = instruction.operands
        program.move_instruction_pointer(count)
        log(program.registers)

    def _jie(self, program: Program, instruction: Instruction):
        log(instruction.opcode + str(instruction.operands))
        (register, count) = instruction.operands
        if register not in program.registers \
                or program.registers[register] % 2 == 0:
            program.move_instruction_pointer(count)
        else:
            program.move_instruction_pointer(1)
        log(program.registers)

    def _ji1(self, program: Program, instruction: Instruction):
        log(instruction.opcode + str(instruction.operands))
        (register, count) = instruction.operands
        if register in program.registers \
                and program.registers[register] == 1:
            program.move_instruction_pointer(count)
        else:
            program.move_instruction_pointer(1)
        log(program.registers)

    def _jn0(self, program: Program, instruction: Instruction):
        log(instruction.opcode + str(instruction.operands))
        (register, count) = instruction.operands
        if register in program.registers \
                and program.registers[register] != 0:
            program.move_instruction_pointer(count)
        else:
            program.move_instruction_pointer(1)
        log(program.registers)

    def _set(self, program: Program, instruction: Instruction):
        (register, value) = instruction.operands
        program.set_register_value(register, value)
        program.move_instruction_pointer(1)

    def _cpy(self, program: Program, instruction: Instruction):
        (from_register, to_register) = instruction.operands
        if from_register in program.registers:
            program.set_register_value(
                to_register,
                program.registers[from_register])
        program.move_instruction_pointer(1)

    def _add(self, program: Program, instruction: Instruction):
        log(instruction.opcode + str(instruction.operands))
        (register, value) = instruction.operands
        new_value = value \
            if register not in program.registers \
            else program.registers[register] + value
        program.set_register_value(register, new_value)
        program.move_instruction_pointer(1)
        log(program.registers)

    def _div(self, program: Program, instruction: Instruction):
        log(instruction.opcode + str(instruction.operands))
        (register, value) = instruction.operands
        new_value = 0 \
            if register not in program.registers \
            else program.registers[register] // value
        program.set_register_value(register, new_value)
        program.move_instruction_pointer(1)
        log(program.registers)

    def _mul(self, program: Program, instruction: Instruction):
        log(instruction.opcode + str(instruction.operands))
        (register, value) = instruction.operands
        new_value = value \
            if register not in program.registers \
            else program.registers[register] * value
        program.set_register_value(register, new_value)
        program.move_instruction_pointer(1)
        log(program.registers)

    def _mem(self, program: Program, instruction: Instruction):
        (address, value) = instruction.operands
        program.set_memory_value(address, value)
        program.move_instruction_pointer(1)

    def run_program(self, program: Program):
        seen = defaultdict(int)
        while 0 <= program.instruction_pointer < len(program.instructions):
            instruction = program.instructions[program.instruction_pointer]
            if program.inf_loop_treshold is not None:
                seen[program.instruction_pointer] += 1
            if instruction.opcode not in self._instruction_set:
                raise ValueError("Unsupported instruction: "
                                 + instruction.opcode)
            self._instruction_set[instruction.opcode](program, instruction)
            program._cycles += 1
            if program.instruction_pointer < 0 \
               and program.error_on_jump_beyond_zero:
                raise ValueError("Invalid instruction argument")
            if program.inf_loop_treshold is not None \
               and program.instruction_pointer in seen:
                instruction_count = seen[program.instruction_pointer]
                if instruction_count >= program.inf_loop_treshold:
                    raise RuntimeError("Infinite loop!")
        log("Normal exit")

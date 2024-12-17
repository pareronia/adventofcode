from __future__ import annotations
from collections import defaultdict
from collections.abc import Callable
from typing import Any
from aoc.common import log as do_log


class Instruction:
    _opcode: str
    _operands: tuple[Any, ...] | None

    def __init__(self, opcode: str, operands: tuple[Any, ...] | None) -> None:
        self._opcode = opcode
        self._operands = operands

    def __repr__(self) -> str:
        return (
            f"Instruction[opcode: {self._opcode}, "
            f"operands: {self._operands}]"
        )

    @classmethod
    def NOP(cls) -> Instruction:
        return Instruction("NOP", None)

    @classmethod
    def JMP(cls, value: int) -> Instruction:
        return Instruction("JMP", (value,))

    @classmethod
    def JIE(cls, register: str, value: int) -> Instruction:
        return Instruction("JIE", (register, value))

    @classmethod
    def JI1(cls, register: str, value: int) -> Instruction:
        return Instruction("JI1", (register, value))

    @classmethod
    def JN0(cls, register: str, value: str) -> Instruction:
        return Instruction("JN0", (register, value))

    @classmethod
    def SET(cls, register: str, value: str) -> Instruction:
        return Instruction("SET", (register, value))

    @classmethod
    def TGL(cls, register: str) -> Instruction:
        return Instruction("TGL", (register,))

    @classmethod
    def ADD(cls, register: str, value: int) -> Instruction:
        return Instruction("ADD", (register, value))

    @classmethod
    def SUB(cls, register: str, value: str) -> Instruction:
        return Instruction("SUB", (register, value))

    @classmethod
    def MUL(cls, register: str, value: str) -> Instruction:
        return Instruction("MUL", (register, value))

    @classmethod
    def DIV(cls, register: str, value: str) -> Instruction:
        return Instruction("DIV", (register, value))

    @classmethod
    def MEM(cls, address: int, value: object) -> Instruction:
        return Instruction("MEM", (address, value))

    @classmethod
    def OUT(cls, operand: str) -> Instruction:
        return Instruction("OUT", (operand,))

    @classmethod
    def RSH(cls, operand: str, value: str) -> Instruction:
        return Instruction("RSH", (operand, value))

    @classmethod
    def AND(cls, register: str, value: str) -> Instruction:
        return Instruction("AND", (register, value))

    @classmethod
    def XOR(cls, operand: str, value: str) -> Instruction:
        return Instruction("XOR", (operand, value))

    @property
    def opcode(self) -> str:
        return self._opcode

    @property
    def operands(self) -> tuple[Any, ...] | None:
        return self._operands

    @property
    def is_MUL(self) -> bool:
        return self._opcode == "MUL"


class Program:
    _instructions: list[Instruction]
    _memory: dict[int, object]
    _registers: dict[str, int | str]
    _instruction_pointer: int
    _inf_loop_treshold: int | None
    _output_consumer: Callable[[str], None] | None
    _cycles: int
    _debug: bool

    def __init__(
        self,
        instructions: list[Instruction],
        inf_loop_treshold: int | None = None,
        output_consumer: Callable[[str], None] | None = None,
        debug: bool = False,
    ) -> None:
        self._instructions = instructions
        self._inf_loop_treshold = inf_loop_treshold
        self._memory = dict[int, object]()
        self._registers = dict[str, int | str]()
        self._instruction_pointer = 0
        self._output_consumer = output_consumer
        self._cycles = 0
        self._debug = debug

    @property
    def instructions(self) -> list[Instruction]:
        return self._instructions

    @property
    def inf_loop_treshold(self) -> int | None:
        return self._inf_loop_treshold

    @property
    def instruction_pointer(self) -> int:
        return self._instruction_pointer

    @property
    def memory(self) -> dict[int, object]:
        return self._memory

    @property
    def registers(self) -> dict[str, int | str]:
        return self._registers

    @property
    def cycles(self) -> int:
        return self._cycles

    @property
    def output_consumer(self) -> Callable[[str], None] | None:
        return self._output_consumer

    def log(self, s: object) -> None:
        if self._debug:
            do_log(s)

    def null_operation(self) -> None:
        pass

    def set_register_value(self, register: str, value: int | str) -> None:
        self._registers[register] = value

    def move_instruction_pointer(self, value: int) -> int:
        self._instruction_pointer += value
        return self._instruction_pointer

    def set_instruction_pointer(self, value: int) -> int:
        self._instruction_pointer = value
        return self._instruction_pointer

    def set_memory_value(self, address: int, value: object) -> None:
        self._memory[address] = value

    def replace_instruction(self, idx: int, new_ins: Instruction) -> None:
        self.log(self.instructions)
        self.log(f"replacing {self.instructions[idx]} with {new_ins}")
        self._instructions[idx] = new_ins
        self.log(self.instructions)


class VirtualMachine:
    _instruction_set: dict[str, Callable[[Program, Instruction, Any], Any]]
    _debug: bool

    def __init__(self, debug: bool = False) -> None:
        self._instruction_set = {
            "NOP": self._nop,
            "JMP": self._jmp,
            "JIE": self._jie,
            "JI1": self._ji1,
            "JN0": self._jn0,
            "SET": self._set,
            "TGL": self._tgl,
            "ADD": self._add,
            "SUB": self._sub,
            "MUL": self._mul,
            "DIV": self._div,
            "MEM": self._mem,
            "OUT": self._out,
            "RSH": self._rsh,
            "AND": self._and,
            "XOR": self._xor,
        }
        self._debug = debug

    def log(self, s: object) -> None:
        if self._debug:
            do_log(s)

    def _nop(
        self, program: Program, instruction: Instruction, ip: int
    ) -> None:
        program.null_operation()
        program.move_instruction_pointer(1)

    def _jmp(
        self, program: Program, instruction: Instruction, ip: int
    ) -> None:
        if instruction.operands is None:
            raise RuntimeError
        self.log(instruction.opcode + str(instruction.operands))
        (count, *_) = instruction.operands
        program.move_instruction_pointer(count)
        self.log(program.registers)

    def _jie(
        self, program: Program, instruction: Instruction, ip: int
    ) -> None:
        if instruction.operands is None:
            raise RuntimeError
        self.log(instruction.opcode + str(instruction.operands))
        (register, count) = instruction.operands
        if (
            register not in program.registers
            or program.registers[register] % 2 == 0
        ):
            program.move_instruction_pointer(count)
        else:
            program.move_instruction_pointer(1)
        self.log(program.registers)

    def _ji1(
        self, program: Program, instruction: Instruction, ip: int
    ) -> None:
        if instruction.operands is None:
            raise RuntimeError
        self.log(instruction.opcode + str(instruction.operands))
        (register, count) = instruction.operands
        if register in program.registers and program.registers[register] == 1:
            program.move_instruction_pointer(count)
        else:
            program.move_instruction_pointer(1)
        self.log(program.registers)

    def _jn0(
        self, program: Program, instruction: Instruction, ip: int
    ) -> None:
        if instruction.operands is None:
            raise RuntimeError
        self.log(instruction.opcode + str(instruction.operands))
        (test, count) = instruction.operands
        if test.startswith("*"):
            if test[1:] in program.registers:
                test = program.registers[test[1:]]
            else:
                test = 0
        else:
            test = int(test)
        if count.startswith("!"):
            if test != 0:
                program.set_instruction_pointer(int(count[1:]))
            else:
                program.move_instruction_pointer(1)
        else:
            if count.startswith("*"):
                if count[1:] in program.registers:
                    count = program.registers[count[1:]]
                else:
                    count = 0
            else:
                count = int(count)
            if test != 0:
                program.move_instruction_pointer(int(count))
            else:
                program.move_instruction_pointer(1)
        self.log(program.registers)

    def _set(
        self, program: Program, instruction: Instruction, ip: int
    ) -> None:
        if instruction.operands is None:
            raise RuntimeError
        self.log(instruction.opcode + str(instruction.operands))
        (register, value) = instruction.operands
        if str(value).startswith("*"):
            value = value[1:]
            if value in program.registers:
                program.set_register_value(
                    register, int(program.registers[value])
                )
        else:
            program.set_register_value(register, int(value))
        program.move_instruction_pointer(1)
        self.log(program.registers)

    def _tgl(
        self, program: Program, instruction: Instruction, ip: int
    ) -> None:
        if instruction.operands is None:
            raise RuntimeError
        self.log(instruction.opcode + str(instruction.operands))
        (register,) = instruction.operands
        if register in program.registers:
            idx = ip + int(program.registers[register])
            if 0 <= idx < len(program.instructions):
                new_ins = self._toggle_instruction(program.instructions[idx])
                program.replace_instruction(idx, new_ins)
        program.move_instruction_pointer(1)

    def _toggle_instruction(self, instruction: Instruction) -> Instruction:
        if instruction.operands is None:
            raise RuntimeError
        if instruction.opcode == "ADD" and instruction.operands[1] == 1:
            return Instruction.ADD(instruction.operands[0], -1)
        elif (
            instruction.opcode == "ADD"
            and instruction.operands[1] == -1
            or instruction.opcode == "TGL"
        ):
            return Instruction.ADD(instruction.operands[0], 1)
        elif instruction.opcode == "JN0":
            op2 = str(instruction.operands[1])
            return Instruction.SET(
                op2[1:] if op2.startswith("*") else op2,
                str(instruction.operands[0]),
            )
        elif instruction.opcode == "SET":
            op1 = str(instruction.operands[0])
            return Instruction.JN0(
                str(instruction.operands[1]),
                op1 if op1.isnumeric() else "*" + op1,
            )
        else:
            raise RuntimeError(
                "Cannot toggle instruction: " + str(instruction)
            )

    def _add(
        self, program: Program, instruction: Instruction, ip: int
    ) -> None:
        if instruction.operands is None:
            raise RuntimeError
        self.log(instruction.opcode + str(instruction.operands))
        (register, value) = instruction.operands
        new_value = (
            value
            if register not in program.registers
            else program.registers[register] + value
        )
        program.set_register_value(register, new_value)
        program.move_instruction_pointer(1)
        self.log(program.registers)

    def _sub(
        self, program: Program, instruction: Instruction, ip: str
    ) -> None:
        if instruction.operands is None:
            raise RuntimeError
        self.log(instruction.opcode + str(instruction.operands))
        (register, value) = instruction.operands
        value = self._value(program, value)
        new_value = (
            value
            if register not in program.registers
            else program.registers[register] - value
        )
        program.set_register_value(register, new_value)
        program.move_instruction_pointer(1)
        self.log(program.registers)

    def _div(
        self, program: Program, instruction: Instruction, ip: int
    ) -> None:
        if instruction.operands is None:
            raise RuntimeError
        self.log(instruction.opcode + str(instruction.operands))
        (register, value) = instruction.operands
        value = self._value(program, value)
        new_value = (
            0
            if register not in program.registers
            else program.registers[register] // value
        )
        program.set_register_value(register, new_value)
        program.move_instruction_pointer(1)
        self.log(program.registers)

    def _mul(
        self, program: Program, instruction: Instruction, ip: str
    ) -> None:
        if instruction.operands is None:
            raise RuntimeError
        self.log(instruction.opcode + str(instruction.operands))
        (register, value) = instruction.operands
        value = self._value(program, value)
        new_value = (
            value
            if register not in program.registers
            else program.registers[register] * value
        )
        program.set_register_value(register, new_value)
        program.move_instruction_pointer(1)
        self.log(program.registers)

    def _rsh(
        self, program: Program, instruction: Instruction, ip: str
    ) -> None:
        if instruction.operands is None:
            raise RuntimeError
        self.log(instruction.opcode + str(instruction.operands))
        (register, value) = instruction.operands
        value = self._value(program, value)
        new_value = (
            value
            if register not in program.registers
            else program.registers[register] >> value
        )
        program.set_register_value(register, new_value)
        program.move_instruction_pointer(1)
        self.log(program.registers)

    def _and(
        self, program: Program, instruction: Instruction, ip: str
    ) -> None:
        if instruction.operands is None:
            raise RuntimeError
        self.log(instruction.opcode + str(instruction.operands))
        (register, value) = instruction.operands
        value = self._value(program, value)
        new_value = (
            value
            if register not in program.registers
            else program.registers[register] & value
        )
        program.set_register_value(register, new_value)
        program.move_instruction_pointer(1)
        self.log(program.registers)

    def _xor(
        self, program: Program, instruction: Instruction, ip: str
    ) -> None:
        if instruction.operands is None:
            raise RuntimeError
        self.log(instruction.opcode + str(instruction.operands))
        (register, value) = instruction.operands
        value = self._value(program, value)
        new_value = (
            value
            if register not in program.registers
            else program.registers[register] ^ value
        )
        program.set_register_value(register, new_value)
        program.move_instruction_pointer(1)
        self.log(program.registers)

    def _mem(
        self, program: Program, instruction: Instruction, ip: int
    ) -> None:
        if instruction.operands is None:
            raise RuntimeError
        (address, value) = instruction.operands
        program.set_memory_value(address, value)
        program.move_instruction_pointer(1)

    def _out(
        self, program: Program, instruction: Instruction, ip: int
    ) -> None:
        if instruction.operands is None:
            raise RuntimeError
        self.log(instruction.opcode + str(instruction.operands))
        (operand,) = instruction.operands
        if operand.startswith("*"):
            operand = operand[1:]
            if operand in program.registers:
                operand = program.registers[operand]
        else:
            operand = int(operand)
        if operand is not None and program.output_consumer is not None:
            program.output_consumer(str(operand))
        program.move_instruction_pointer(1)

    def _value(self, program: Program, op: str) -> int | str:
        if op.startswith("*"):
            return program.registers[op[1:]]
        else:
            return int(op)

    def run_program(self, program: Program) -> None:
        seen: defaultdict[int, int] = defaultdict(int)
        while 0 <= program.instruction_pointer < len(program.instructions):
            # instruction = program.instructions[program.instruction_pointer]
            if program.inf_loop_treshold is not None:
                seen[program.instruction_pointer] += 1

            #                      + instruction.opcode)
            # self._instruction_set[instruction.opcode](
            #     program,
            #     instruction,
            #     program.instruction_pointer)
            # program._cycles += 1
            self.step(program)
            if (
                program.inf_loop_treshold is not None
                and program.instruction_pointer in seen
            ):
                instruction_count = seen[program.instruction_pointer]
                if instruction_count >= program.inf_loop_treshold:
                    raise RuntimeError("Infinite loop!")
        self.log("Normal exit")

    def step(self, program: Program) -> None:
        instruction = program.instructions[program.instruction_pointer]
        if instruction.opcode not in self._instruction_set:
            raise ValueError("Unsupported instruction: " + instruction.opcode)
        self._instruction_set[instruction.opcode](
            program, instruction, program.instruction_pointer
        )
        program._cycles += 1

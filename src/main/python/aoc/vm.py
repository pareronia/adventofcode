from __future__ import annotations
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
    _memory: dict[int, int]
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

    def set_memory_value(self, address: int, value: int) -> None:
        self._memory[address] = value


class VirtualMachine:

    def run_program(self, program: Program) -> None:
        seen = set[int]()
        while program.instruction_pointer < len(program.instructions) \
                and not program.instruction_pointer < 0 \
                and program.instruction_pointer not in seen:
            instruction = program.instructions[program.instruction_pointer]
            seen.add(program.instruction_pointer)
            if instruction.opcode == "NOP":
                program.null_operation()
                program.move_instruction_pointer(1)
            elif instruction.opcode == "JMP":
                program.move_instruction_pointer(instruction.operands[0])
                if program.instruction_pointer < 0 \
                        and program.error_on_jump_beyond_zero:
                    raise ValueError("Invalid instruction argument")
            elif instruction.opcode == "ACC":
                program.add_to_accumulator(instruction.operands[0])
                program.move_instruction_pointer(1)
            elif instruction.opcode == "MEM":
                program.set_memory_value(instruction.operands[0],
                                         instruction.operands[1])
                program.move_instruction_pointer(1)
            else:
                raise ValueError("Unsupported instruction: "
                                 + instruction.opcode)
            if program.instruction_pointer in seen \
                    and program.error_on_inf_loop:
                raise RuntimeError("Infinite loop!")
        log("Normal exit")
        return


def main() -> None:
    vm = VirtualMachine()
    prog = Program([
        Instruction.NOP(),
        Instruction.NOP(),
        Instruction.JMP(2),
        Instruction.NOP(),
        Instruction.NOP(),
        Instruction.MEM(1, 100),
        Instruction.ACC(6),
        Instruction.MEM(3, 300),
        Instruction.ACC(7),
    ])
    vm.run_program(prog)
    print(prog.__dict__)
    prog = Program([
        Instruction.NOP(),
        Instruction.JMP(-1),
    ], error_on_inf_loop=True)
    vm.run_program(prog)
    print(prog.__dict__)


if __name__ == '__main__':
    main()

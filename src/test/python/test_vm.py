#! /usr/bin/env python3
#

import unittest
from aoc.vm import VirtualMachine, Program, Instruction


class TestVM(unittest.TestCase):

    def test(self) -> None:
        output = []
        vm = VirtualMachine()
        prog = Program([
            Instruction.NOP(),
            Instruction.JMP(2),
            Instruction.NOP(),
            Instruction.NOP(),
            Instruction.SET("F", "64"),
            Instruction.RSH("F", "2"),
            Instruction.ADD("F", -2),
            Instruction.AND("F", "7"),
            Instruction.MEM(1, 100),
            Instruction.ADD("A", 6),
            Instruction.MEM(3, 300),
            Instruction.JIE("A", 1),
            Instruction.JI1("A", 20),
            Instruction.ADD("A", 7),
            Instruction.OUT("*A"),
            Instruction.SET("B", "1"),
            Instruction.JI1("B", 2),
            Instruction.JIE("B", 20),
            Instruction.NOP(),
            Instruction.DIV("A", "2"),
            Instruction.OUT("7"),
            Instruction.MUL("A", "3"),
            Instruction.SET("C", "*B"),
            Instruction.SUB("B", "1"),
            Instruction.JN0("*C", "-3"),
            Instruction.SET("E", "3"),
            Instruction.JN0("*D", "1"),
            Instruction.JN0("*B", "*E"),
            Instruction.SET("A", "100"),
            Instruction.SET("B", "200"),
        ], output_consumer=lambda s: output.append(s))

        vm.run_program(prog)

        self.assertEqual(len(prog.memory), 2)
        self.assertEqual(prog.memory[1], 100)
        self.assertEqual(prog.memory[3], 300)
        self.assertEqual(prog.registers["A"], 54)
        self.assertEqual(prog.registers["B"], -1)
        self.assertEqual(prog.registers["C"], 0)
        self.assertTrue("D" not in prog.registers)
        self.assertEqual(prog.registers["E"], 3)
        self.assertEqual(prog.registers["F"], 6)
        self.assertEqual(prog.instruction_pointer, 30)
        self.assertEqual(output, ["13", "7"])

    def test_error_on_infinite_loop(self) -> None:
        vm = VirtualMachine()
        prog = Program([
            Instruction.NOP(),
            Instruction.JMP(-1),
        ], inf_loop_treshold=2)

        self.assertRaises(RuntimeError,
                          vm.run_program,
                          prog)
        self.assertEqual(prog.instruction_pointer, 0)

    def test_invalidInstruction(self) -> None:
        vm = VirtualMachine()
        prog = Program([
            Instruction("XXX", ()),
        ])

        self.assertRaises(ValueError,
                          vm.run_program,
                          prog)

    def test_toggle_instruction(self):
        vm = VirtualMachine()

        ins = vm._toggle_instruction(Instruction.SET("a", "1"))

        self.assertEqual(ins.opcode, "JN0")
        self.assertEqual(ins.operands, ("1", "*a"))


if __name__ == '__main__':
    unittest.main()

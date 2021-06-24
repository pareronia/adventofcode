#! /usr/bin/env python3
#

import unittest
from aoc.vm import VirtualMachine, Program, Instruction


class TestVM(unittest.TestCase):

    def test(self):
        vm = VirtualMachine()
        prog = Program([
            Instruction.NOP(),
            Instruction.JMP(2),
            Instruction.NOP(),
            Instruction.NOP(),
            Instruction.MEM(1, 100),
            Instruction.ADD("A", 6),
            Instruction.MEM(3, 300),
            Instruction.JIE("A", 1),
            Instruction.JI1("A", 20),
            Instruction.ADD("A", 7),
            Instruction.SET("B", 1),
            Instruction.JI1("B", 2),
            Instruction.JIE("B", 20),
            Instruction.NOP(),
            Instruction.DIV("A", 2),
            Instruction.MUL("A", 3),
            Instruction.CPY("A", "C"),
            Instruction.JN0("B", 3),
            Instruction.SET("A", 100),
            Instruction.SET("B", 200),
        ])

        vm.run_program(prog)

        self.assertEqual(len(prog.memory), 2)
        self.assertEqual(prog.memory[1], 100)
        self.assertEqual(prog.memory[3], 300)
        self.assertEqual(prog.registers["A"], 18)
        self.assertEqual(prog.registers["B"], 1)
        self.assertEqual(prog.registers["C"], 18)
        self.assertEqual(prog.instruction_pointer, 20)

    def test_error_on_infinite_loop(self):
        vm = VirtualMachine()
        prog = Program([
            Instruction.NOP(),
            Instruction.JMP(-1),
        ], inf_loop_treshold=2)

        self.assertRaises(RuntimeError,
                          vm.run_program,
                          prog)
        self.assertEqual(prog.instruction_pointer, 0)

    def test_invalidInstruction(self):
        vm = VirtualMachine()
        prog = Program([
            Instruction("XXX", ()),
        ])

        self.assertRaises(ValueError,
                          vm.run_program,
                          prog)


if __name__ == '__main__':
    unittest.main()

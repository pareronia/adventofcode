#! /usr/bin/env python3
#

import unittest
from aoc.vm import VirtualMachine, Program, Instruction


class TestVM(unittest.TestCase):

    def test(self):
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

        self.assertEqual(len(prog.memory), 2)
        self.assertEqual(prog.memory[1], 100)
        self.assertEqual(prog.memory[3], 300)
        self.assertEqual(prog.accumulator, 13)
        self.assertEqual(prog.instruction_pointer, 9)

    def test_error_on_infinite_loop(self):
        vm = VirtualMachine()
        prog = Program([
            Instruction.NOP(),
            Instruction.JMP(-1),
        ], error_on_inf_loop=True)

        try:
            vm.run_program(prog)
        except RuntimeError:
            pass

        self.assertEqual(prog.instruction_pointer, 0)


if __name__ == '__main__':
    unittest.main()

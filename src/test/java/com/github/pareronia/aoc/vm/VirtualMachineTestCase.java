package com.github.pareronia.aoc.vm;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VirtualMachineTestCase {
    
    private VirtualMachine vm;
    
    @BeforeEach
    public void setUp() {
        vm = new VirtualMachine();
    }

    @Test
    public void test() {
        final List<Long> output = new ArrayList<>();
        final Program program = new Program(asList(
            Instruction.NOP(),
            Instruction.JMP(2),
            Instruction.NOP(),
            Instruction.NOP(),
            Instruction.ADD("A", 6L),
            Instruction.OUT("*A"),
            Instruction.SET("B", "1"),
            Instruction.OUT("7"),
            Instruction.MUL("A", "3"),
            Instruction.SET("C", "*B"),
            Instruction.SUB("B", "1"),
            Instruction.JN0("*C", "-3"),
            Instruction.SET("E", "2"),
            Instruction.JN0("*D", "1"),
            Instruction.JN0("*B", "*E"),
            Instruction.SET("A", "99"),
            Instruction.ADD("B", "3"),
            Instruction.SET("E", "2"),
            Instruction.JG0("*B", "*E"),
            Instruction.NOP(),
            Instruction.SET("E", "7"),
            Instruction.ADD("E", "1"),
            Instruction.RSH("E", "2"),
            Instruction.SET("F", "12"),
            Instruction.XOR("F", "5"),
            Instruction.SET("G", "12"),
            Instruction.AND("G", "5")
        ),
        output::add);
        
        vm.runProgram(program);
        
        assertThat(program.getMemory().size()).isEqualTo(0);
        assertThat(program.getRegisters().get("A")).isEqualTo(54);
        assertThat(program.getRegisters().get("B")).isEqualTo(2L);
        assertThat(program.getRegisters().get("C")).isEqualTo(0L);
        assertThat(program.getRegisters().get("D")).isNull();
        assertThat(program.getRegisters().get("E")).isEqualTo(2L);
        assertThat(program.getRegisters().get("F")).isEqualTo(9L);
        assertThat(program.getRegisters().get("G")).isEqualTo(4L);
        assertThat(program.getInstructionPointer()).isEqualTo(27);
        assertThat(output).containsExactly(6L, 7L);
    }
}

package com.github.pareronia.aoc.vm;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class VirtualMachineTestCase {
    
    private VirtualMachine vm;
    
    @Before
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
            Instruction.ADD("B", "-1"),
            Instruction.JN0("*C", "-3"),
            Instruction.SET("E", "2"),
            Instruction.JN0("*D", "1"),
            Instruction.JN0("*B", "*E"),
            Instruction.SET("A", "99")
        ),
        output::add);
        
        vm.runProgram(program);
        
        assertThat(program.getMemory().size(), is(0));
        assertThat(program.getRegisters().get("A"), is(54L));
        assertThat(program.getRegisters().get("B"), is(-1L));
        assertThat(program.getRegisters().get("C"), is(0L));
        assertThat(program.getRegisters().get("D"), is(nullValue()));
        assertThat(program.getRegisters().get("E"), is(2L));
        assertThat(program.getInstructionPointer(), is(16));
        assertThat(output, is(List.of(6L, 7L)));
    }
}

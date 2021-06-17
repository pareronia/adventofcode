package com.github.pareronia.aoc.vm;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
        final Program program = new Program(asList(
            Instruction.NOP(),
            Instruction.ADD("A", 6L),
            Instruction.SET("B", 1L),
            Instruction.MUL("A", 3L)
        ));
        
        vm.runProgram(program);
        
        assertThat(program.getMemory().size(), is(0));
        assertThat(program.getRegisters().get("A"), is(18L));
        assertThat(program.getRegisters().get("B"), is(1L));
        assertThat(program.getInstructionPointer(), is(4));
    }
}

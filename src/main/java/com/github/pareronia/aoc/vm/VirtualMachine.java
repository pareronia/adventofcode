package com.github.pareronia.aoc.vm;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class VirtualMachine {

    private final Map<Opcode, Execution> instructionSet;
    
    public VirtualMachine() {
        this.instructionSet = new HashMap<>();
        this.instructionSet.put(Opcode.NOP, this::nop);
        this.instructionSet.put(Opcode.SET, this::set);
        this.instructionSet.put(Opcode.ADD, this::add);
        this.instructionSet.put(Opcode.MUL, this::mul);
    }
    
    private void nop(Program program, Instruction instruction) {
        program.nullOperation();
        program.moveIntructionPointer(1);
    }
    
    private void set(Program program, Instruction instruction) {
        final String register = (String) instruction.getOperands().get(0);
        final Long value = (Long) instruction.getOperands().get(1);
        program.setRegisterValue(register, value);
        program.moveIntructionPointer(1);
    }

    private void add(Program program, Instruction instruction) {
        final String register = (String) instruction.getOperands().get(0);
        final Long value = (Long) instruction.getOperands().get(1);
        final Long newValue = Optional.ofNullable(program.getRegisters().get(register))
                                .map(v -> v + value)
                                .orElse(value);
        program.getRegisters().put(register, newValue);
        program.moveIntructionPointer(1);
    }

    private void mul(Program program, Instruction instruction) {
        final String register = (String) instruction.getOperands().get(0);
        final Long value = (Long) instruction.getOperands().get(1);
        final Long newValue = Optional.ofNullable(program.getRegisters().get(register))
                .map(v -> v * value)
                .orElse(value);
        program.getRegisters().put(register, newValue);
        program.moveIntructionPointer(1);
    }
    
    public void runProgram(Program program) {
        while (0 <= program.getInstructionPointer()
                && program.getInstructionPointer() < program.getInstructions().size()) {
            final Instruction instruction = program.getInstructions()
                    .get(program.getInstructionPointer());
            this.instructionSet.get(instruction.getOpcode())
                    .execute(program, instruction);
        }
    }
    
    @FunctionalInterface
    interface Execution {
        void execute(Program program, Instruction instruction);
    }
}
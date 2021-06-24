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
        this.instructionSet.put(Opcode.CPY, this::cpy);
        this.instructionSet.put(Opcode.ADD, this::add);
        this.instructionSet.put(Opcode.MUL, this::mul);
        this.instructionSet.put(Opcode.JMP, this::jmp);
        this.instructionSet.put(Opcode.JN0, this::jn0);
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
    
    private void cpy(Program program, Instruction instruction) {
        final String fromRegister = (String) instruction.getOperands().get(0);
        final String toRegister = (String) instruction.getOperands().get(1);
        Optional.ofNullable(program.getRegisters().get(fromRegister))
                .map(v -> program.getRegisters().put(toRegister, v));
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
    
    private void jmp(Program program, Instruction instruction) {
        final Integer count = (Integer) instruction.getOperands().get(0);
        program.moveIntructionPointer(count);
    }
    
    private void jn0(Program program, Instruction instruction) {
        final String register = (String) instruction.getOperands().get(0);
        final Integer count = (Integer) instruction.getOperands().get(1);
        Optional.ofNullable(program.getRegisters().get(register))
                .filter(v -> !v.equals(0L))
                .ifPresentOrElse(
                        v -> program.moveIntructionPointer(count),
                        () -> program.moveIntructionPointer(1));
        
    }
    
    public void runProgram(Program program) {
        final Map<Integer, Integer> seen = new HashMap<>();
        while (0 <= program.getInstructionPointer()
                && program.getInstructionPointer() < program.getInstructions().size()) {
            final Instruction instruction = program.getInstructions()
                    .get(program.getInstructionPointer());
            if (program.getInfiniteLoopTreshold() != null) {
                seen.merge(program.getInstructionPointer(), 1, (a, b) -> a + b);
            }
            this.instructionSet.get(instruction.getOpcode())
                    .execute(program, instruction);
            program.incrementCycles();
            if (program.getInfiniteLoopTreshold() != null
                    && seen.containsKey(program.getInstructionPointer())) {
                final Integer instructionCount = seen.get(program.getInstructionPointer());
                if (instructionCount >= program.getInfiniteLoopTreshold()) {
                    throw new InfiniteLoopException();
                }
            }
        }
    }
    
    @FunctionalInterface
    interface Execution {
        void execute(Program program, Instruction instruction);
    }
    
    public static final class InfiniteLoopException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
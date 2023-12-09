package com.github.pareronia.aoc.vm;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Program {
    private final List<Instruction> instructions;
    private final Map<Integer, Object> memory = new HashMap<>();
    private final Map<String, Long> registers = new HashMap<>();
    private final Integer infiniteLoopTreshold;
    private final Consumer<Long> outputConsumer;
    private Supplier<Long> inputSupplier = null;
    private Integer instructionPointer = 0;
    private long cycles = 0L;

    public Program(
            final List<Instruction> instructions,
            final Integer infiniteLoopTreshold,
            final Consumer<Long> outputConsumer
    ) {
        this.instructions = instructions;
        this.infiniteLoopTreshold = infiniteLoopTreshold;
        this.outputConsumer = outputConsumer;
    }

    public Map<Integer, Object> getMemory() {
        return memory;
    }
    
    public Map<String, Long> getRegisters() {
        return registers;
    }

    protected Integer getInfiniteLoopTreshold() {
        return infiniteLoopTreshold;
    }

    protected Consumer<Long> getOutputConsumer() {
        return outputConsumer;
    }

    protected Supplier<Long> getInputSupplier() {
        return inputSupplier;
    }

    public void setInputSupplier(final Supplier<Long> inputSupplier) {
        this.inputSupplier = inputSupplier;
    }

    public Integer getInstructionPointer() {
        return instructionPointer;
    }

    public long getCycles() {
        return cycles;
    }

    public void reset() {
        this.instructionPointer = 0;
        this.cycles = 0;
        this.getRegisters().clear();
    }

    public Program(final List<Instruction> instructions) {
        this(instructions, null, null);
    }
    
    public Program(final List<Instruction> instructions,
                   final Integer infiniteLoopTreshold) {
        this(instructions, infiniteLoopTreshold, null);
    }
    
    public Program(final List<Instruction> instructions,
                   final Consumer<Long> outputConsumer) {
        this(instructions, null, outputConsumer);
    }

    public List<Instruction> getInstructions() {
        return Collections.unmodifiableList(this.instructions);
    }
    
    public void replaceInstruction(final int index, final Instruction instruction) {
        this.instructions.set(index, instruction);
    }
    
    public void nullOperation() {
        // nop
    }
    
    public void setRegisterValue(final String register, final Long value) {
        this.registers.put(register, value);
    }
    
    public void setMemoryValue(final Integer address, final Object value) {
        this.memory.put(address, value);
    }
    
    public Integer moveIntructionPointer(final Integer amount) {
        this.instructionPointer += amount;
        return this.instructionPointer;
    }
    
    public void incrementCycles() {
        this.cycles++;
    }
}

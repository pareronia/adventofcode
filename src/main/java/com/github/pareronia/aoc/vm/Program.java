package com.github.pareronia.aoc.vm;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Program {
    private final List<Instruction> instructions;
    @Getter
    private final Map<Integer, Object> memory = new HashMap<>();
    @Getter
    private final Map<String, Long> registers = new HashMap<>();
    @Getter(value = AccessLevel.PACKAGE)
    private final Integer infiniteLoopTreshold;
    @Getter(value = AccessLevel.PACKAGE)
    private final Consumer<Long> outputConsumer;
    @Setter
    @Getter(value = AccessLevel.PACKAGE)
    private Supplier<Long> inputSupplier = null;
    @Getter
    private Integer instructionPointer = 0;
    @Getter
    private long cycles = 0L;
    
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

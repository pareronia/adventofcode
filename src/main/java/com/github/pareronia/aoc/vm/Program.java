package com.github.pareronia.aoc.vm;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Program {
    private final List<Instruction> instructions;
    @Getter
    private final Map<Integer, Object> memory = new HashMap<>();
    @Getter
    private final Map<String, Long> registers = new HashMap<>();
    @Getter(value = AccessLevel.PACKAGE)
    private final Integer infiniteLoopTreshold;
    @Getter
    private Integer instructionPointer = 0;
    @Getter
    private long cycles = 0L;

    public Program(List<Instruction> instructions) {
        this(instructions, null);
    }

    public List<Instruction> getInstructions() {
        return Collections.unmodifiableList(this.instructions);
    }
    
    public void nullOperation() {
        return;
    }
    
    public void setRegisterValue(String register, Long value) {
        this.registers.put(register, value);
    }
    
    public void setMemoryValue(Integer address, Object value) {
        this.memory.put(address, value);
    }
    
    public Integer moveIntructionPointer(Integer amount) {
        this.instructionPointer += amount;
        return this.instructionPointer;
    }
    
    public void incrementCycles() {
        this.cycles++;
    }
}

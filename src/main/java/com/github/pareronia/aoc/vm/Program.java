package com.github.pareronia.aoc.vm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Program {
    private final List<Instruction> instructions;
    private final Map<Integer, Object> memory = new HashMap<>();
    private final Map<String, Long> registers = new HashMap<>();
    private Integer instructionPointer = 0;
    private long cycles = 0L;

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

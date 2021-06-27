package com.github.pareronia.aoc.vm;

import static java.util.Arrays.asList;

import java.util.Collections;
import java.util.List;

import lombok.Value;

@Value
public class Instruction {
    private final Opcode opcode;
    private final List<Object> operands;

    public static Instruction NOP() {
        return new Instruction(Opcode.NOP, Collections.emptyList());
    }

    public static Instruction SET(final String register, final String value) {
        return new Instruction(Opcode.SET, asList(register, value));
    }

    public static Instruction ADD(final String register, final Long value) {
        return new Instruction(Opcode.ADD, asList(register, value));
    }

    public static Instruction MUL(final String register, final Long value) {
        return new Instruction(Opcode.MUL, asList(register, value));
    }

    public static Instruction JMP(final Integer count) {
        return new Instruction(Opcode.JMP, List.of(count));
    }

    public static Instruction JN0(final String register, final String count) {
        return new Instruction(Opcode.JN0, asList(register, count));
    }
    
    public static Instruction TGL(final String register) {
        return new Instruction(Opcode.TGL, List.of(register));
    }
    
    public static Instruction OUT(final String operand) {
        return new Instruction(Opcode.OUT, List.of(operand));
    }
}

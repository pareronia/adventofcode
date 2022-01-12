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
        return new Instruction(Opcode.ADD, asList(register, value.toString()));
    }

    public static Instruction ADD(final String register, final String value) {
        return new Instruction(Opcode.ADD, asList(register, value));
    }

    public static Instruction MUL(final String register, final Long value) {
        return new Instruction(Opcode.MUL, asList(register, value.toString()));
    }

    public static Instruction MUL(final String register, final String value) {
        return new Instruction(Opcode.MUL, asList(register, value));
    }

    public static Instruction DIV(final String register, final String value) {
        return new Instruction(Opcode.DIV, asList(register, value));
    }

    public static Instruction MOD(final String register, final String value) {
        return new Instruction(Opcode.MOD, asList(register, value));
    }

    public static Instruction EQL(final String register, final String value) {
        return new Instruction(Opcode.EQL, asList(register, value));
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

    public static Instruction INP(final String operand) {
        return new Instruction(Opcode.INP, List.of(operand));
    }
}

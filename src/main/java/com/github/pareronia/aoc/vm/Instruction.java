package com.github.pareronia.aoc.vm;

import static java.util.Arrays.asList;

import java.util.Collections;
import java.util.List;

public record Instruction(Opcode opcode, List<Object> operands) {

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

    public static Instruction SUB(final String register, final String value) {
        return new Instruction(Opcode.SUB, asList(register, value));
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
    
    public static Instruction JG0(final String register, final String count) {
        return new Instruction(Opcode.JG0, asList(register, count));
    }
    
    public static Instruction TGL(final String register) {
        return new Instruction(Opcode.TGL, List.of(register));
    }
    
    public static Instruction OUT(final String operand) {
        return new Instruction(Opcode.OUT, List.of(operand));
    }

    public static Instruction ON0(final String register, final String operand) {
        return new Instruction(Opcode.ON0, List.of(register, operand));
    }

    public static Instruction INP(final String operand) {
        return new Instruction(Opcode.INP, List.of(operand));
    }
    
    public static Instruction RSH(final String register, final String count) {
        return new Instruction(Opcode.RSH, asList(register, count));
    }
    
    public static Instruction XOR(final String register, final String value) {
        return new Instruction(Opcode.XOR, asList(register, value));
    }
    
    public static Instruction AND(final String register, final String value) {
        return new Instruction(Opcode.AND, asList(register, value));
    }
    
    public boolean isMUL() {
        return this.opcode == Opcode.MUL;
    }
}

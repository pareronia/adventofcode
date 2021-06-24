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

    public static Instruction SET(String register, Long value) {
        return new Instruction(Opcode.SET, asList(register, value));
    }

    public static Instruction CPY(String fromRegister, String toRegister) {
        return new Instruction(Opcode.CPY, asList(fromRegister, toRegister));
    }

    public static Instruction ADD(String register, Long value) {
        return new Instruction(Opcode.ADD, asList(register, value));
    }

    public static Instruction MUL(String register, Long value) {
        return new Instruction(Opcode.MUL, asList(register, value));
    }

    public static Instruction JMP(Integer count) {
        return new Instruction(Opcode.JMP, List.of(count));
    }

    public static Instruction JN0(String register, Integer count) {
        return new Instruction(Opcode.JN0, asList(register, count));
    }
}

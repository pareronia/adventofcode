package com.github.pareronia.aoc.assembunny;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.subarray;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.github.pareronia.aoc.vm.Instruction;

import lombok.Value;

/**
 * 'Assembunny' util class.
 *
 * @see <a href="https://adventofcode.com/2016/day/12">https://adventofcode.com/2016/day/12</a>
 */
public class Assembunny {

    private static final Set<String> REGISTERS = Set.of("a", "b", "c", "d");
    
    public static List<AssembunnyInstruction> parse(final List<String> inputs) {
        return inputs.stream()
                .map(input -> input.split(" "))
                .map(s -> new AssembunnyInstruction(s[0], asList(subarray(s, 1, s.length))))
                .collect(toList());
    }
    
    public static List<Instruction> translate(final List<AssembunnyInstruction> lines) {
        final List<Instruction> instructions = new ArrayList<>();
        for (final AssembunnyInstruction line : lines) {
            if (line.operator.equals("cpy")) {
                final String value = line.operands.get(0);
                final String register = line.operands.get(1);
                if (REGISTERS.contains(value)) {
                    instructions.add(Instruction.SET(register, "*" + value));
                } else if (isNumeric(value)) {
                    instructions.add(Instruction.SET(register, value));
                } else {
                    throw new IllegalArgumentException("Invalid operands for jnz");
                }
            } else if (line.operator.equals("inc")) {
                final String register = line.operands.get(0);
                instructions.add(Instruction.ADD(register, 1L));
            } else if (line.operator.equals("dec")) {
                final String register = line.operands.get(0);
                instructions.add(Instruction.ADD(register, -1L));
            } else if (line.operator.equals("jnz")) {
                final String test_ = line.operands.get(0);
                final String test;
                if (REGISTERS.contains(test_)) {
                    test = "*" + test_;
                } else if (isNumeric(test_)) {
                    test = test_;
                } else {
                    throw new IllegalArgumentException("Invalid operands for jnz");
                }
                final String value;
                final String value_ = line.operands.get(1);
                if (REGISTERS.contains(value_)) {
                    value = "*" + value_;
                } else if (isNumeric(value_)) {
                    value = value_;
                } else {
                    throw new IllegalArgumentException("Invalid operands for jnz");
                }
                instructions.add(Instruction.JN0(test, value));
            } else if (line.operator.equals("tgl")) {
                final String register = line.operands.get(0);
                instructions.add(Instruction.TGL(register));
            } else {
                throw new IllegalArgumentException("Invalid input: " + line);
            }
        }
        return instructions;
    }

    private static boolean isNumeric(final String s) {
        return StringUtils.isNumeric(s.replace("-", ""));
    }

    @Value
    public static final class AssembunnyInstruction {
        private final String operator;
        private final List<String> operands;
    }
}

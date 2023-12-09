package com.github.pareronia.aoc.assembunny;

import static java.util.Arrays.asList;
import static java.util.Arrays.copyOfRange;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.vm.Instruction;

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
                .map(s -> new AssembunnyInstruction(s[0], asList(copyOfRange(s, 1, s.length))))
                .collect(toList());
    }
    
    public static List<Instruction> translate(final List<AssembunnyInstruction> lines) {
        final List<Instruction> instructions = new ArrayList<>();
        for (final AssembunnyInstruction line : lines) {
            if (line.operator.equals("cpy")) {
                final String value = getOperand(line, 0);
                final String register = line.operands.get(1);
                instructions.add(Instruction.SET(register, value));
            } else if (line.operator.equals("inc")) {
                final String register = line.operands.get(0);
                instructions.add(Instruction.ADD(register, 1L));
            } else if (line.operator.equals("dec")) {
                final String register = line.operands.get(0);
                instructions.add(Instruction.ADD(register, -1L));
            } else if (line.operator.equals("jnz")) {
                final String test = getOperand(line, 0);
                final String value = getOperand(line, 1);
                instructions.add(Instruction.JN0(test, value));
            } else if (line.operator.equals("tgl")) {
                final String register = line.operands.get(0);
                instructions.add(Instruction.TGL(register));
            } else if (line.operator.equals("out")) {
                final String operand = getOperand(line, 0);
                instructions.add(Instruction.OUT(operand));
            } else {
                throw new IllegalArgumentException("Invalid input: " + line);
            }
        }
        return instructions;
    }

    private static String getOperand(final AssembunnyInstruction line, final int idx) {
        final String operand_ = line.operands.get(idx);
        final String operand;
        if (REGISTERS.contains(operand_)) {
            operand = "*" + operand_;
        } else if (isNumeric(operand_)) {
            operand = operand_;
        } else {
            throw new IllegalArgumentException("Invalid operands for " + line.operator);
        }
        return operand;
    }

    private static boolean isNumeric(final String s) {
        return StringUtils.isNumeric(s.replace("-", ""));
    }

    public static final class AssembunnyInstruction {
        private final String operator;
        private final List<String> operands;
        
        public AssembunnyInstruction(final String operator, final List<String> operands) {
            this.operator = operator;
            this.operands = operands;
        }

        public String getOperator() {
            return operator;
        }

        public List<String> getOperands() {
            return operands;
        }
    }
}

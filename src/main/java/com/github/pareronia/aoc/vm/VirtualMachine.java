package com.github.pareronia.aoc.vm;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import com.github.pareronia.aoc.StringUtils;

public class VirtualMachine {

    private final Map<Opcode, Execution> instructionSet;
    
    public VirtualMachine() {
        this.instructionSet = new HashMap<>();
        this.instructionSet.put(Opcode.NOP, this::nop);
        this.instructionSet.put(Opcode.SET, this::set);
        this.instructionSet.put(Opcode.ADD, this::add);
        this.instructionSet.put(Opcode.SUB, this::sub);
        this.instructionSet.put(Opcode.MUL, this::mul);
        this.instructionSet.put(Opcode.DIV, this::div);
        this.instructionSet.put(Opcode.MOD, this::mod);
        this.instructionSet.put(Opcode.EQL, this::eql);
        this.instructionSet.put(Opcode.JMP, this::jmp);
        this.instructionSet.put(Opcode.JN0, this::jn0);
        this.instructionSet.put(Opcode.JG0, this::jg0);
        this.instructionSet.put(Opcode.TGL, this::tgl);
        this.instructionSet.put(Opcode.OUT, this::out);
        this.instructionSet.put(Opcode.ON0, this::on0);
        this.instructionSet.put(Opcode.INP, this::inp);
    }
    
    private void nop(final Program program, final Instruction instruction, final Integer ip) {
        program.nullOperation();
        program.moveIntructionPointer(1);
    }
    
    private void set(final Program program, final Instruction instruction, final Integer ip) {
        final String register = (String) instruction.getOperands().get(0);
        final String op2 = (String) instruction.getOperands().get(1);
        final Optional<Long> value = getValue(program, op2);
        value.ifPresent(v -> program.setRegisterValue(register, v));
        program.moveIntructionPointer(1);
    }

    private void add(final Program program, final Instruction instruction, final Integer ip) {
        final String register = (String) instruction.getOperands().get(0);
        final String op2 = (String) instruction.getOperands().get(1);
        final Optional<Long> value = getValue(program, op2);
        value.ifPresent(v -> {
            final Long newValue = Optional.ofNullable(program.getRegisters().get(register))
                    .map(r -> r + v)
                    .orElse(v); // FIXME
            program.getRegisters().put(register, newValue);
        });
        program.moveIntructionPointer(1);
    }
    
    private void sub(final Program program, final Instruction instruction, final Integer ip) {
        final String register = (String) instruction.getOperands().get(0);
        final String op2 = (String) instruction.getOperands().get(1);
        final Optional<Long> value = getValue(program, op2);
        value.ifPresent(v -> {
            final Long newValue = Optional.ofNullable(program.getRegisters().get(register))
                    .map(r -> r - v)
                    .orElse(v); // FIXME
            program.getRegisters().put(register, newValue);
        });
        program.moveIntructionPointer(1);
    }
    
    private void mul(final Program program, final Instruction instruction, final Integer ip) {
        final String register = (String) instruction.getOperands().get(0);
        final String op2 = (String) instruction.getOperands().get(1);
        final Optional<Long> value = getValue(program, op2);
        value.ifPresent(v -> {
            final Long newValue = Optional.ofNullable(program.getRegisters().get(register))
                    .map(r -> r * v)
                    .orElse(v); // FIXME
            program.getRegisters().put(register, newValue);
        });
        program.moveIntructionPointer(1);
    }

    private void div(final Program program, final Instruction instruction, final Integer ip) {
        final String register = (String) instruction.getOperands().get(0);
        final String op2 = (String) instruction.getOperands().get(1);
        final Optional<Long> value = getValue(program, op2);
        value.ifPresent(v -> {
            assert v != 0;
            final Long newValue = Optional.ofNullable(program.getRegisters().get(register))
                    .map(r -> Math.floorDiv(r, v))
                    .orElse(v); // FIXME
            program.getRegisters().put(register, newValue);
        });
        program.moveIntructionPointer(1);
    }

    private void mod(final Program program, final Instruction instruction, final Integer ip) {
        final String register = (String) instruction.getOperands().get(0);
        final String op2 = (String) instruction.getOperands().get(1);
        final Optional<Long> value = getValue(program, op2);
        value.ifPresent(v -> {
            assert v > 0;
            final Long newValue = Optional.ofNullable(program.getRegisters().get(register))
                    .map(r -> r % v)
                    .orElse(v); // FIXME
            program.getRegisters().put(register, newValue);
        });
        program.moveIntructionPointer(1);
    }

    private void eql(final Program program, final Instruction instruction, final Integer ip) {
        final String register = (String) instruction.getOperands().get(0);
        final String op2 = (String) instruction.getOperands().get(1);
        final Optional<Long> value = getValue(program, op2);
        value.ifPresent(v -> {
            final Long newValue = Optional.ofNullable(program.getRegisters().get(register))
                    .map(r -> r == v ? 1L : 0L)
                    .orElse(0L); // FIXME
            program.getRegisters().put(register, newValue);
        });
        program.moveIntructionPointer(1);
    }
    
    private void jmp(final Program program, final Instruction instruction, final Integer ip) {
        final Integer count = (Integer) instruction.getOperands().get(0);
        program.moveIntructionPointer(count);
    }
    
    private void jn0(final Program program, final Instruction instruction, final Integer ip) {
        jump(program, instruction, ip, v -> !v.equals(0L));
    }
    
    private void jg0(final Program program, final Instruction instruction, final Integer ip) {
        jump(program, instruction, ip, v -> v > 0);
    }
    
    private void jump(final Program program, final Instruction instruction,
            final Integer ip, final Predicate<Long> condition
    ) {
        final String op1 = (String) instruction.getOperands().get(0);
        final Optional<Long> test = getValue(program, op1);
        final String op2 = (String) instruction.getOperands().get(1);
        final Long count;
        if (op2.startsWith("*")) {
            count = program.getRegisters().get(op2.substring(1));
        } else {
            count = Long.valueOf(op2);
        }
        test.filter(condition)
                .ifPresentOrElse(
                        v -> program.moveIntructionPointer(count.intValue()),
                        () -> program.moveIntructionPointer(1));
    }
    
    private void tgl(final Program program, final Instruction instruction, final Integer ip) {
        final String register = (String) instruction.getOperands().get(0);
        Optional.ofNullable(program.getRegisters().get(register))
                .map(v -> v.intValue())
                .filter(v -> ip + v < program.getInstructions().size()
                                && ip + v >= 0)
                .ifPresent(v -> {
                    final int index = ip + v;
                    final Instruction newInstruction
                        = toggleInstruction(program.getInstructions().get(index));
                    program.replaceInstruction(index, newInstruction);
                });
        program.moveIntructionPointer(1);
    }
    
    private Instruction toggleInstruction(final Instruction instruction) {
        if (instruction.getOpcode() == Opcode.ADD
                && instruction.getOperands().get(1).equals(1L)) {
            final String register = (String) instruction.getOperands().get(0);
            return Instruction.ADD(register, -1L);
        } else if (instruction.getOpcode() == Opcode.ADD
                && instruction.getOperands().get(1).equals(-1L)
                || instruction.getOpcode() == Opcode.TGL) {
            final String register = (String) instruction.getOperands().get(0);
            return Instruction.ADD(register, 1L);
        } else if (instruction.getOpcode() == Opcode.JN0) {
            final String op1 = (String) instruction.getOperands().get(0);
            final String op2 = (String) instruction.getOperands().get(1);
            return Instruction.SET(
                    op2.startsWith("*") ? op2.substring(1) : op2,
                    op1);
        } else if (instruction.getOpcode() == Opcode.SET) {
            final String op1 = (String) instruction.getOperands().get(0);
            final String op2 = (String) instruction.getOperands().get(1);
            return Instruction.JN0(
                    op2,
                    StringUtils.isNumeric(op1) ? op1 : "*" + op1);
        }
        throw new IllegalStateException("Cannot toggle instruction " + instruction);
    }
    
    private void out(final Program program, final Instruction instruction, final Integer ip) {
        final String op1 = (String) instruction.getOperands().get(0);
        final Optional<Long> value = getValue(program, op1);
        if (program.getOutputConsumer() != null) {
            value.ifPresent(v -> program.getOutputConsumer().accept(v));
        }
        program.moveIntructionPointer(1);
    }
    
    private void on0(final Program program, final Instruction instruction, final Integer ip) {
        final String op1 = (String) instruction.getOperands().get(0);
        final Optional<Long> test = getValue(program, op1);
        final Optional<Long> value;
        final String op2 = (String) instruction.getOperands().get(1);
        if (op2.startsWith("*")) {
            value = Optional.ofNullable(program.getRegisters().get(op2.substring(1)));
        } else {
            value = Optional.of(Long.valueOf(op2));
        }
        test.filter(t -> t != 0).ifPresent(x -> {
            if (program.getOutputConsumer() != null) {
                value.ifPresent(program.getOutputConsumer()::accept);
            }
        });
        program.moveIntructionPointer(1);
    }
    
    private void inp(final Program program, final Instruction instruction, final Integer ip ) {
        final String op1 = (String) instruction.getOperands().get(0);
        assert program.getInputSupplier() != null;
        final Long value = program.getInputSupplier().get();
        program.getRegisters().put(op1, value);
        program.moveIntructionPointer(1);
    }
    
    public void runProgram(final Program program) {
        final Map<Integer, Integer> seen = new HashMap<>();
        while (0 <= program.getInstructionPointer()
                && program.getInstructionPointer() < program.getInstructions().size()) {
            if (program.getInfiniteLoopTreshold() != null) {
                seen.merge(program.getInstructionPointer(), 1, (a, b) -> a + b);
            }
            step(program);
            if (program.getInfiniteLoopTreshold() != null
                    && seen.containsKey(program.getInstructionPointer())) {
                final Integer instructionCount = seen.get(program.getInstructionPointer());
                if (instructionCount >= program.getInfiniteLoopTreshold()) {
                    throw new InfiniteLoopException();
                }
            }
        }
    }

    public void step(final Program program) {
        final Integer instructionPointer = program.getInstructionPointer();
        final Instruction instruction
                = program.getInstructions().get(instructionPointer);
        this.instructionSet.get(instruction.getOpcode())
                .execute(program, instruction, instructionPointer);
        program.incrementCycles();
    }

    private Optional<Long> getValue(final Program program, final String op) {
        if (op.startsWith("*")) {
            return Optional.ofNullable(program.getRegisters().get(op.substring(1)));
        } else {
            return Optional.of(Long.valueOf(op));
        }
    }
    
    @FunctionalInterface
    interface Execution {
        void execute(Program program, Instruction instruction, Integer ip);
    }
    
    public static final class InfiniteLoopException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
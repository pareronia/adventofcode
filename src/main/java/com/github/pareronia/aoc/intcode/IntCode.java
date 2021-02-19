package com.github.pareronia.aoc.intcode;

import java.util.List;

public class IntCode {
    
    private static final int ADD = 1;
    private static final int MUL = 2;
    private static final int EXIT = 99;

    public Integer run(List<Integer> program) {
        for (int i = 0; i < program.size(); i = i + 4) {
            final Integer op = program.get(i);
            switch (op) {
            case ADD:
                add(program, i);
                break;
            case MUL:
                mul(program, i);
                break;
            case EXIT:
                return program.get(0);
            default:
                throw new IllegalStateException("Invalid op");
            }
        }
        throw new IllegalStateException("Intcode program did not exit normally");
    }

    private void add(List<Integer> program, int i) {
        final Integer summand1 = program.get(program.get(i + 1));
        final Integer summand2 = program.get(program.get(i + 2));
        final Integer destination = program.get(i + 3);
        program.set(destination, summand1 + summand2);
    }

    private void mul(List<Integer> program, int i) {
        final Integer factor1 = program.get(program.get(i + 1));
        final Integer factor2 = program.get(program.get(i + 2));
        final Integer destination = program.get(i + 3);
        program.set(destination, factor1 * factor2);
    }
}

package com.github.pareronia.aoc.intcode;

import java.util.List;

public class IntCode {
    
    private static final int ADD = 1;
    private static final int MUL = 2;
    private static final int INPUT = 3;
    private static final int OUTPUT = 4;
    private static final int JIT = 5;
    private static final int JIF = 6;
    private static final int LT = 7;
    private static final int EQ = 8;
    private static final int EXIT = 99;

	private final boolean debug;
    
    private Integer output;

    public IntCode(boolean debug) {
        this.debug = debug;
    }

    public void run(List<Integer> program) {
       run(program, null);
    }
    
    public void run(List<Integer> program, Integer input) {
        log(program.size());
        for (int i = 0; i < program.size(); ) {
            final Integer op = program.get(i);
            final Integer opcode = op % 100;
            final int mode1 = (op / 100) % 10;
            final int mode2 = (op / 1000) % 10;
            switch (opcode) {
            case ADD:
                final Integer summand1 = getParam1(program, mode1, i);
                final Integer summand2 = getParam2(program, mode2, i);
                final Integer destinationADD = getParam3(program, 1, i);
                program.set(destinationADD, summand1 + summand2);
                i += 4;
                break;
            case MUL:
                final Integer factor1 = getParam1(program, mode1, i);
                final Integer factor2 = getParam2(program, mode2, i);
                final Integer destinationMUL = getParam3(program, 1, i);
                program.set(destinationMUL, factor1 * factor2);
                i += 4;
                break;
            case INPUT:
                program.set(getParam1(program, 1, i), input);
                i += 2;
                break;
            case OUTPUT:
                this.output = getParam1(program, mode1, i);
                log(i + ": " + this.output + "(" + mode1 + ")");
                i += 2;
                break;
            case JIT:
                if (getParam1(program, mode1, i) != 0) {
                    i = getParam2(program, mode2, i);
                } else {
                    i += 3;
                }
                break;
            case JIF:
                if (getParam1(program, mode1, i) == 0) {
                    i = getParam2(program, mode2, i);
                } else {
                    i += 3;
                }
                break;
            case LT:
                final Integer destinationLT = getParam3(program, 1, i);
                if (getParam1(program, mode1, i) < getParam2(program, mode2, i)) {
                    program.set(destinationLT, 1);
                } else {
                    program.set(destinationLT, 0);
                }
                i += 4;
                break;
            case EQ:
                final Integer destinationEQ = getParam3(program, 1, i);
                if (getParam1(program, mode1, i) == getParam2(program, mode2, i)) {
                    program.set(destinationEQ, 1);
                } else {
                    program.set(destinationEQ, 0);
                }
                i += 4;
                break;
            case EXIT:
                log(i + ": EXIT");
                return;
            default:
                throw new IllegalStateException("Invalid opcode: "+ opcode);
            }
        }
        throw new IllegalStateException("Intcode program did not exit normally");
    }

    public Integer getOutput() {
        return output;
    }

    private Integer getParam1(List<Integer> program, int mode, int i) {
        return getParam(program, mode, i, 1);
    }

    private Integer getParam2(List<Integer> program, int mode, int i) {
        return getParam(program, mode, i, 2);
    }
    
    private Integer getParam3(List<Integer> program, int mode, int i) {
        return getParam(program, mode, i, 3);
    }
    
    private Integer getParam(List<Integer> program, int mode, int i, int j) {
        if (mode == 0) {
            return program.get(program.get(i + j));
        } else if (mode == 1) {
            return program.get(i + j);
        } else {
            throw new IllegalStateException("Invalid mode: " + mode);
        }
    }

	private void log(Object obj) {
		if (!debug) {
			return;
		}
		System.out.println(obj);
	}

}
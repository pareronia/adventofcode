package com.github.pareronia.aoc.intcode;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.AssertUtils;
import com.github.pareronia.aoc.solution.Logger;
import com.github.pareronia.aoc.solution.LoggerEnabled;

public class IntCode implements LoggerEnabled {
    
    private static final int ADD = 1;
    private static final int MUL = 2;
    private static final int INPUT = 3;
    private static final int OUTPUT = 4;
    private static final int JIT = 5;
    private static final int JIF = 6;
    private static final int LT = 7;
    private static final int EQ = 8;
    private static final int BASE = 9;
    private static final int EXIT = 99;

    private static final int POSITION = 0;
    private static final int IMMEDIATE = 1;
    private static final int RELATIVE = 2;

	private final Logger logger;
    
    private int ip;
    private int base;
    private final List<Long> program;
    private boolean runTillInputRequired;
    private boolean runTillHasOutput;
    private boolean halted;

    public IntCode(final List<Long> instructions, final boolean debug) {
        this.program = new ArrayList<>(instructions);
        this.logger = new Logger(debug);
    }

    public void run(final List<Long> program) {
        run(new ArrayDeque<>(), new ArrayDeque<>());
    }
    
    public void run(
            final long input,
            final Deque<Long> output
    ) {
        run(new ArrayDeque<>(List.of(input)), output);
    }
    
    public void run(
            final Deque<Long> input,
            final Deque<Long> output
    ) {
        this.ip = 0;
        this.base = 0;
        doRun(input, output);
    }
    
    public void runTillInputRequired(
            final Deque<Long> input,
            final Deque<Long> output
    ) {
        this.runTillInputRequired = true;
        doRun(input, output);
    }
    
    public void runTillHasOutput(
            final Deque<Long> input,
            final Deque<Long> output
    ) {
        this.runTillHasOutput = true;
        doRun(input, output);
    }
    
    private void doRun(
            final Deque<Long> input,
            final Deque<Long> output
    ) {
        while (true) {
            final Long op = program.get(ip);
            final int opcode = (int) (op % 100);
            final int[] modes = {
                -1,
                (int) ((op / 100) % 10),
                (int) ((op / 1000) % 10),
                (int) ((op / 10000) % 10),
            };
            final int[] addr = getAddr(modes);
            switch (opcode) {
            case ADD -> {
                set(addr[3], get(addr[1]) + get(addr[2]));
                ip += 4;
            }
            case MUL -> {
                set(addr[3], get(addr[1]) * get(addr[2]));
                ip += 4;
            }
            case INPUT -> {
                if (this.runTillInputRequired && input.isEmpty()) {
                    this.runTillInputRequired = false;
                    return;
                }
                set(addr[1], input.pop());
                ip += 2;
            }
            case OUTPUT -> {
                output.add(get(addr[1]));
                ip += 2;
                if (this.runTillHasOutput) {
                    this.runTillHasOutput = false;
                    return;
                }
            }
            case JIT -> ip = get(addr[1]) != 0 ? getInt(addr[2]) : ip + 3;
            case JIF -> ip = get(addr[1]) == 0 ? getInt(addr[2]) : ip + 3;
            case LT -> {
                set(addr[3], get(addr[1]) < get(addr[2]) ? 1 : 0);
                ip += 4;
            }
            case EQ -> {
                set(addr[3], get(addr[1]) == get(addr[2]) ? 1 : 0);
                ip += 4;
            }
            case BASE -> {
                base += get(addr[1]);
                ip += 2;
            }
            case EXIT -> {
                log(String.format("%d: EXIT", ip));
                this.halted = true;
                return;
            }
            default -> throw new IllegalStateException(
                                "Invalid opcode: '%d'".formatted(opcode));
            }
        }
    }
    
    private long get(final int addr) {
        AssertUtils.assertTrue(addr >= 0, () -> "index out of range");
        if (addr >= this.program.size()) {
            return 0;
        }
        return this.program.get(addr);
    }

    private int getInt(final int addr) {
        return Math.toIntExact(this.get(addr));
    }
    
    private void set(final int addr, final long value) {
        AssertUtils.assertTrue(addr >= 0, () -> "index out of range");
        if (addr >= this.program.size()) {
        IntStream.range(0, addr - this.program.size() + 1)
                .mapToLong(i -> 0L)
                .forEach(this.program::add);
        }
        this.program.set(addr, value);
    }
    
    private int[] getAddr(final int[] modes) {
        final int[] addr = new int[4];
        try {
            for (int i = 1; i <= 3; i++) {
                addr[i] = switch(modes[i]) {
                    case POSITION -> Math.toIntExact(
                                            this.program.get(this.ip + i));
                    case IMMEDIATE -> this.ip + i;
                    case RELATIVE -> Math.toIntExact(
                                this.program.get(this.ip + i) + this.base);
                    default -> throw new IllegalArgumentException(
                                "Invalid mode '%d'".formatted(modes[i]));
                };
            }
        } catch (final IndexOutOfBoundsException | ArithmeticException e) {
        }
        return addr;
    }

	public boolean isHalted() {
        return halted;
    }
	
	public List<Long> getProgram() {
	    return Collections.unmodifiableList(this.program);
	}

	@Override
    public Logger getLogger() {
        return this.logger;
    }

    public static List<Long> parse(final String input) {
        return Stream.of(input.split(","))
                .map(Long::valueOf)
                .collect(toUnmodifiableList());
	}
}
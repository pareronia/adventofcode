package com.github.pareronia.aoc.intcode;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.AssertUtils;

public class IntCode {
    
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

	private final boolean debug;
    
    private int ip;
    private int base;
    private List<Long> program;
    private boolean runTillInputRequired;
    private boolean runTillHasOutput;
    private boolean halted;

    public IntCode(final boolean debug) {
        this.debug = debug;
    }

    public List<Long> run(final List<Long> program) {
       return run(program, new ArrayDeque<>(), new ArrayDeque<>());
    }
    
    public List<Long> run(
            final List<Long> program,
            final long input,
            final Deque<Long> output
    ) {
        return run(program, new ArrayDeque<>(List.of(input)), output);
    }
    
    public List<Long> run(
            final List<Long> instructions,
            final Deque<Long> input,
            final Deque<Long> output
    ) {
        this.ip = 0;
        this.base = 0;
        this.program = new ArrayList<>(instructions);
        return doRun(input, output);
    }
    
    public void runTillInputRequired(
            final List<Long> instructions,
            final Deque<Long> input,
            final Deque<Long> output
    ) {
        this.runTillInputRequired = true;
        run(instructions, input, output);
    }
    
    public void runTillHasOutput(
            final List<Long> instructions,
            final Deque<Long> input,
            final Deque<Long> output
    ) {
        this.runTillHasOutput = true;
        run(instructions, input, output);
    }
    
    public void continueTillInputRequired(
            final Deque<Long> input,
            final Deque<Long> output
    ) {
        this.runTillInputRequired = true;
        doRun(input, output);
    }
    
    public void continueTillHasOutput(
            final Deque<Long> input,
            final Deque<Long> output
    ) {
        this.runTillHasOutput = true;
        doRun(input, output);
    }
    
    private List<Long> doRun(
            final Deque<Long> input,
            final Deque<Long> output
    ) {
        while (true) {
            final Long op = program.get(ip);
            final int opcode = (int) (op % 100);
            final int[] modes = new int[] {
                -1,
                (int) ((op / 100) % 10),
                (int) ((op / 1000) % 10),
                (int) ((op / 10000) % 10),
            };
            final int[] addr = getAddr(modes);
            switch (opcode) {
            case ADD:
                set(addr[3], get(addr[1]) + get(addr[2]));
                ip += 4;
                break;
            case MUL:
                set(addr[3], get(addr[1]) * get(addr[2]));
                ip += 4;
                break;
            case INPUT:
                if (this.runTillInputRequired && input.isEmpty()) {
                    this.runTillInputRequired = false;
                    return this.program;
                }
                set(addr[1], input.pop());
                ip += 2;
                break;
            case OUTPUT:
                output.add(get(addr[1]));
                ip += 2;
                if (this.runTillHasOutput) {
                    this.runTillHasOutput = false;
                    return this.program;
                }
                break;
            case JIT:
                ip = get(addr[1]) != 0 ? getInt(addr[2]) : ip + 3;
                break;
            case JIF:
                ip = get(addr[1]) == 0 ? getInt(addr[2]) : ip + 3;
                break;
            case LT:
                set(addr[3], get(addr[1]) < get(addr[2]) ? 1 : 0);
                ip += 4;
                break;
            case EQ:
                set(addr[3], get(addr[1]) == get(addr[2]) ? 1 : 0);
                ip += 4;
                break;
            case BASE:
                base += get(addr[1]);
                ip += 2;
                break;
            case EXIT:
                log(String.format("%d: EXIT", ip));
                this.halted = true;
                return program;
            default:
                throw new IllegalStateException(
                        String.format("Invalid opcode: '%d'", opcode));
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
                if (modes[i] == POSITION) {
                    addr[i] = Math.toIntExact(this.program.get(this.ip + i));
                } else if (modes[i] == IMMEDIATE) {
                    addr[i] = this.ip + i;
                } else if (modes[i] == RELATIVE) {
                    addr[i] = Math.toIntExact(this.program.get(this.ip + i) + this.base);
                } else {
                    throw new IllegalArgumentException(
                            String.format("Invalid mode '%d'", modes[i]));
                }
            }
        } catch (final IndexOutOfBoundsException | ArithmeticException e) {
        }
        return addr;
    }

	public boolean isHalted() {
        return halted;
    }

    private void log(final Object obj) {
		if (!debug) {
			return;
		}
		System.out.println(obj);
	}
	
	public static List<Long> parse(final String input) {
        return Stream.of(input.split(","))
                .map(Long::valueOf)
                .collect(toUnmodifiableList());
	}
}
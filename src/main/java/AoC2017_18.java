import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;

import com.github.pareronia.aoc.MutableBoolean;
import com.github.pareronia.aoc.MutableInt;
import com.github.pareronia.aoc.vm.Instruction;
import com.github.pareronia.aoc.vm.Program;
import com.github.pareronia.aoc.vm.VirtualMachine;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2017_18 extends AoCBase {
    
    private final List<String> input;
    
    private AoC2017_18(final List<String> inputs, final boolean debug) {
        super(debug);
        this.input = inputs;
    }

    public static AoC2017_18 create(final List<String> input) {
        return new AoC2017_18(input, false);
    }

    public static AoC2017_18 createDebug(final List<String> input) {
        return new AoC2017_18(input, true);
    }
    
    private List<Instruction> buildInstructions(
            final List<String> inputs,
            final Function<String, Instruction> sndBuilder,
            final Function<String, Instruction> rcvBuilder
    ) {
        final Function<String, String> getValue
                = cs -> cs.matches("[+-]?[0-9]*") ? cs : "*" + cs;
        final List<Instruction> instructions = new ArrayList<>();
        for (final String s : inputs) {
            final String[] splits = s.split(" ");
            switch (splits[0]) {
            case "snd":
                instructions.add(sndBuilder.apply(getValue.apply(splits[1])));
                break;
            case "set":
                instructions.add(Instruction.SET(splits[1], getValue.apply(splits[2])));
                break;
            case "add":
                instructions.add(Instruction.ADD(splits[1], getValue.apply(splits[2])));
                break;
            case "mul":
                instructions.add(Instruction.MUL(splits[1], getValue.apply(splits[2])));
                break;
            case "mod":
                instructions.add(Instruction.MOD(splits[1], getValue.apply(splits[2])));
                break;
            case "rcv":
                instructions.add(rcvBuilder.apply(splits[1]));
                break;
            case "jgz":
                instructions.add(Instruction.JG0(getValue.apply(splits[1]), getValue.apply(splits[2])));
                break;
            default:
                throw new IllegalStateException();
            }
        }
        return instructions;
    }
    
    private void initializeRegisters(final Program program) {
        for (final char ch : "abfip".toCharArray()) {
            program.setRegisterValue(String.valueOf(ch), 0L);
        }
    }
    
    @Override
    public Integer solvePart1() {
        final String frequency = "frequency";
        final List<Instruction> instructions = buildInstructions(
                this.input,
                op -> Instruction.SET(frequency, op),
                op -> Instruction.ON0("*" + op, "*" + frequency));
        final Deque<Long> q = new ArrayDeque<>();
        final Program program = new Program(instructions, q::addFirst);
        initializeRegisters(program);

        final VirtualMachine vm = new VirtualMachine();
        while (q.isEmpty()) {
            vm.step(program);
        }
        return q.getFirst().intValue();
    }
    
    @Override
    public Integer solvePart2() {
        final long EMPTY = -1L;
        final List<Instruction> instructions = buildInstructions(
                this.input,
                op -> Instruction.OUT(op),
                op -> Instruction.INP(op));
        final Deque<Long> q0 = new ArrayDeque<>();
        final Deque<Long> q1 = new ArrayDeque<>();
        final MutableBoolean waiting0 = new MutableBoolean(false);
        final MutableBoolean waiting1 = new MutableBoolean(false);
        final MutableInt cnt1 = new MutableInt(0);
        final Program program0 = new Program(instructions, t -> {
            log("-> " + t);
            q1.addLast(t);
            waiting1.setFalse();
        });
        program0.setInputSupplier(() -> {
            if (q0.isEmpty()) {
                waiting0.setTrue();
                program0.moveIntructionPointer(-1);
                return EMPTY;
            }
            log("<- "+ q0.peekFirst());
            return q0.pollFirst();
        });
        initializeRegisters(program0);
        program0.setRegisterValue("p", 0L);
        final Program program1 = new Program(instructions, t -> {
            log("-> " + t);
            q0.addLast(t);
            waiting0.setFalse();
            cnt1.increment();
        });
        program1.setInputSupplier(() -> {
            if (q1.isEmpty()) {
                waiting1.setTrue();
                program1.moveIntructionPointer(-1);
                return EMPTY;
            }
            log("<- "+ q1.peekFirst());
            return q1.pollFirst();
        });
        initializeRegisters(program1);
        program1.setRegisterValue("p", 1L);

        final VirtualMachine vm = new VirtualMachine();
        int cycle = 0;
        while (true) {
            if (waiting0.isTrue() && waiting1.isTrue()) {
                log("DEADLOCK!");
                break;
            }
            log("cycle: " + cycle);
            log("Stepping p0");
            vm.step(program0);
            log(() -> program0.getInstructionPointer() + ": " + new TreeMap<>(program0.getRegisters()));
            log(() -> "q0: (" + q0.size() +") " + q0.peekFirst());
            log(() -> "q1: (" + q1.size() +") " + q1.peekFirst());
            log("Stepping p1");
            vm.step(program1);
            log(() -> program1.getInstructionPointer() + ": " + new TreeMap<>(program1.getRegisters()));
            log(() -> "q0: (" + q0.size() +") " + q0.peekFirst());
            log(() -> "q1: (" + q1.size() +") " + q1.peekFirst());
            cycle++;
        }
        log("cycles: " + cycle);
        log(cnt1.getValue());
        return cnt1.getValue();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_18.createDebug(TEST1).solvePart1().equals(4);
        assert AoC2017_18.createDebug(TEST2).solvePart2().equals(3);

        final Puzzle puzzle = Aocd.puzzle(2017, 18);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2017_18.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2017_18.create(puzzle.getInputData()).solvePart2())
        );
    }
    
    private static final List<String> TEST1 = splitLines(
            "set a 1\r\n" +
            "add a 2\r\n" +
            "mul a a\r\n" +
            "mod a 5\r\n" +
            "snd a\r\n" +
            "set a 0\r\n" +
            "rcv a\r\n" +
            "jgz a -1\r\n" +
            "set a 1\r\n" +
            "jgz a -2"
    );
    private static final List<String> TEST2 = splitLines(
            "snd 1\r\n" +
            "snd 2\r\n" +
            "snd p\r\n" +
            "rcv a\r\n" +
            "rcv b\r\n" +
            "rcv c\r\n" +
            "rcv d"
    );
}

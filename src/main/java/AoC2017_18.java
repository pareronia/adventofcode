import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.github.pareronia.aoc.vm.Instruction;
import com.github.pareronia.aoc.vm.Program;
import com.github.pareronia.aoc.vm.VirtualMachine;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2017_18 extends AoCBase {
    
    private static final String FREQUENCY = "frequency";

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
    
    private String getValue(final String cs) {
        return cs.matches("[+-]?[0-9]*") ? cs : "*" + cs;
    }
    
    @Override
    public Integer solvePart1() {
        final List<Instruction> instructions = new ArrayList<>();
        for (final String s : this.input) {
            final String[] splits = s.split(" ");
            switch (splits[0]) {
            case "snd":
                instructions.add(Instruction.SET(FREQUENCY, "*" + splits[1]));
                break;
            case "set":
                instructions.add(Instruction.SET(splits[1], getValue(splits[2])));
                break;
            case "add":
                instructions.add(Instruction.ADD(splits[1], getValue(splits[2])));
                break;
            case "mul":
                instructions.add(Instruction.MUL(splits[1], getValue(splits[2])));
                break;
            case "mod":
                instructions.add(Instruction.MOD(splits[1], getValue(splits[2])));
                break;
            case "rcv":
                instructions.add(Instruction.ON0("*" + splits[1], "*" + FREQUENCY));
                break;
            case "jgz":
                instructions.add(Instruction.JG0("*" + splits[1], getValue(splits[2])));
                break;
            default:
                throw new IllegalStateException();
            }
        }
        final Deque<Long> q = new ArrayDeque<>();
        final Program program = new Program(instructions, q::addFirst);
        for (final char ch : "abcdefghijklmnopqrstuvwxyz".toCharArray()) {
            program.setRegisterValue(String.valueOf(ch), 0L);
        }
        final VirtualMachine vm = new VirtualMachine();
        while (q.isEmpty()) {
            vm.step(program);
        }
        return q.getFirst().intValue();
    }
    
    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_18.createDebug(TEST).solvePart1().equals(4);

        final Puzzle puzzle = Aocd.puzzle(2017, 18);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2017_18.createDebug(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2017_18.create(puzzle.getInputData()).solvePart2())
        );
    }
    
    private static final List<String> TEST = splitLines(
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
}

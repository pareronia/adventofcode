import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.IterTools.permutations;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2019_07 extends AoCBase {
    
    private final List<Long> program;
    
    private AoC2019_07(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 1;
        this.program = IntCode.parse(input.get(0));
    }

    public static AoC2019_07 create(final List<String> input) {
        return new AoC2019_07(input, false);
    }

    public static AoC2019_07 createDebug(final List<String> input) {
        return new AoC2019_07(input, true);
    }
    
    private long run(final long phaseSetting, final long in) {
        final IntCode intCode = new IntCode(false);
        final Deque<Long> output = new ArrayDeque<>();
        final ArrayDeque<Long> input = new ArrayDeque<>(List.of(phaseSetting, in));
        intCode.run(this.program, input, output);
        return output.getLast();
    }
    
    private Long solve(final List<Integer> phaseSettings) {
        final long out1 = run(phaseSettings.get(0), 0);
        final long out2 = run(phaseSettings.get(1), out1);
        final long out3 = run(phaseSettings.get(2), out2);
        final long out4 = run(phaseSettings.get(3), out3);
        return run(phaseSettings.get(4), out4);
    }

    @Override
    public Long solvePart1() {
        return permutations(range(5))
                .mapToLong(this::solve)
                .max().getAsLong();
    }
    
    @Override
    public Long solvePart2() {
        return 0L;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2019_07.createDebug(TEST1).solve(List.of(4, 3, 2, 1, 0)) == 43210;
        assert AoC2019_07.createDebug(TEST2).solve(List.of(0, 1, 2, 3, 4)) == 54321;
        assert AoC2019_07.createDebug(TEST3).solve(List.of(1, 0, 4, 3, 2)) == 65210;
        
        final Puzzle puzzle = Aocd.puzzle(2019, 7);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2019_07.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2019_07.create(inputData)::solvePart2)
        );
    }
    
    private static final List<String> TEST1 = splitLines(
        "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0"
    );
    private static final List<String> TEST2 = splitLines(
        "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0"
    );
    private static final List<String> TEST3 = splitLines(
        "3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33," +
        "1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0"
    );
}
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2019_09 extends AoCBase {
    
    private final List<Long> program;
    
    private AoC2019_09(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 1;
        this.program = IntCode.parse(input.get(0));
    }

    public static AoC2019_09 create(final List<String> input) {
        return new AoC2019_09(input, false);
    }

    public static AoC2019_09 createDebug(final List<String> input) {
        return new AoC2019_09(input, true);
    }
    
    private Long solve(final long input) {
        final IntCode intCode = new IntCode(this.program, this.debug);
        final Deque<Long> output = new ArrayDeque<>();
        intCode.run(input, output);
        return output.getLast();
    }

    @Override
    public Long solvePart1() {
        return solve(1);
    }
    
    @Override
    public Long solvePart2() {
        return solve(2);
    }

    public static void main(final String[] args) throws Exception {
        final Puzzle puzzle = Aocd.puzzle(2019, 9);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2019_09.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2019_09.create(inputData)::solvePart2)
        );
    }
}
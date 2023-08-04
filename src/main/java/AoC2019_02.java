import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.IterTools.product;

import java.util.ArrayList;
import java.util.List;

import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aocd.Puzzle;

public class AoC2019_02 extends AoCBase {
    
    private final List<Long> program;
    
    private AoC2019_02(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 1;
        this.program = IntCode.parse(input.get(0));
    }

    public static AoC2019_02 create(final List<String> input) {
        return new AoC2019_02(input, false);
    }

    public static AoC2019_02 createDebug(final List<String> input) {
        return new AoC2019_02(input, true);
    }
    
    private Long runProgram(final long noun, final long verb) {
        final List<Long> theProgram = new ArrayList<>(this.program);
        theProgram.set(1, noun);
        theProgram.set(2, verb);
        final IntCode intCode = new IntCode(theProgram, this.debug);
        intCode.run(theProgram);
        return intCode.getProgram().get(0);
    }
    
    @Override
    public Long solvePart1() {
        return runProgram(12, 2);
    }

    @Override
    public Integer solvePart2() {
        return product(range(100), range(100)).stream()
            .filter(p -> runProgram(p.get(0), p.get(1)) == 19_690_720)
            .map(p -> 100 * p.get(0) + p.get(1))
            .findFirst().orElseThrow();
    }

    public static void main(final String[] args) throws Exception {
        final Puzzle puzzle = Puzzle.create(2019, 2);
        final List<String> input = puzzle.getInputData();
        puzzle.check(
           () -> lap("Part 1", AoC2019_02.create(input)::solvePart1),
           () -> lap("Part 2", AoC2019_02.create(input)::solvePart2)
	    );
    }
}
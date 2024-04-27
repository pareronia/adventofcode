import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.IterTools.product;

import java.util.ArrayList;
import java.util.List;

import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2019_02 extends SolutionBase<List<Long>, Long, Integer> {
    
    private AoC2019_02(final boolean debug) {
        super(debug);
    }

    public static AoC2019_02 create() {
        return new AoC2019_02(false);
    }

    public static AoC2019_02 createDebug() {
        return new AoC2019_02(true);
    }
    
    private Long runProgram(
            final List<Long> program,
            final long noun,
            final long verb
    ) {
        final List<Long> theProgram = new ArrayList<>(program);
        theProgram.set(1, noun);
        theProgram.set(2, verb);
        final IntCode intCode = new IntCode(theProgram, this.debug);
        intCode.run(theProgram);
        return intCode.getProgram().get(0);
    }

    @Override
    protected List<Long> parseInput(final List<String> inputs) {
        return IntCode.parse(inputs.get(0));
    }

    @Override
    public Long solvePart1(final List<Long> program) {
        return runProgram(program, 12, 2);
    }

    @Override
    public Integer solvePart2(final List<Long> program) {
        return product(range(100), range(100)).stream()
            .filter(p -> runProgram(program, p.get(0), p.get(1)) == 19_690_720)
            .map(p -> 100 * p.get(0) + p.get(1))
            .findFirst().orElseThrow();
    }

    public static void main(final String[] args) throws Exception {
        AoC2019_02.create().run();
    }
}
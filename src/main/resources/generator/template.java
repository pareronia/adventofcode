import java.util.List;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC${year}_${day2}
        extends SolutionBase<List<String>, Integer, Integer> {
    
    private AoC${year}_${day2}(final boolean debug) {
        super(debug);
    }
    
    public static AoC${year}_${day2} create() {
        return new AoC${year}_${day2}(false);
    }
    
    public static AoC${year}_${day2} createDebug() {
        return new AoC${year}_${day2}(true);
    }
    
    @Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }

    @Override
    public Integer solvePart1(final List<String> input) {
        return 0;
    }
    
    @Override
    public Integer solvePart2(final List<String> input) {
        return 0;
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "TODO"),
        @Sample(method = "part2", input = TEST, expected = "TODO"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC${year}_${day2}.create().run();
    }

    private static final String TEST = "TODO";
}

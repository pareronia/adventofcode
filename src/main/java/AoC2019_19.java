import static java.util.stream.Stream.iterate;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2019_19 extends SolutionBase<List<Long>, Integer, Integer> {
    
    private AoC2019_19(final boolean debug) {
        super(debug);
    }

    public static AoC2019_19 create() {
        return new AoC2019_19(false);
    }

    public static AoC2019_19 createDebug() {
        return new AoC2019_19(true);
    }
 
    @Override
    protected List<Long> parseInput(final List<String> inputs) {
        return IntCode.parse(inputs.get(0));
    }

    @Override
    public Integer solvePart1(final List<Long> program) {
        return (int) iterate(0, r -> r < 50, r -> r + 1)
            .flatMap(r ->
                iterate(0, c -> c < 50, c -> c + 1).map(c -> Cell.at(r, c))
            )
            .filter(cell -> inBeam(program, cell))
            .count();
    }

    @Override
    public Integer solvePart2(final List<Long> program) {
        for (int r = 600; r < 1_100; r++) {
            for (int c = 900; c < 1_100; c++) {
                if (this.inBeam(program, Cell.at(r + 99,  c - 99))
                        && this.inBeam(program, Cell.at(r,  c))) {
                    return r * 10_000 + (c - 99);
                }
            }
        }
        throw new IllegalStateException("Unsolvable");
    }

    public static void main(final String[] args) throws Exception {
        AoC2019_19.create().run();
    }
    
    private boolean inBeam(final List<Long> program, final Cell cell) {
        final Deque<Long> input = new ArrayDeque<>();
        input.add((long) cell.getRow());
        input.add((long) cell.getCol());
        final Deque<Long> output = new ArrayDeque<>();
        new IntCode(program, false).runTillHasOutput(input, output);
        return output.getFirst() == 1;
    }
}
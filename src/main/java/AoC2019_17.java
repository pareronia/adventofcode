import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2019_17 extends AoCBase {
    
    private static final char SCAFFOLD = '#';
    private static final char NEWLINE = '\n';
    
    private final List<Long> program;
    
    private AoC2019_17(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 1;
        this.program = IntCode.parse(input.get(0));
    }

    public static AoC2019_17 create(final List<String> input) {
        return new AoC2019_17(input, false);
    }

    public static AoC2019_17 createDebug(final List<String> input) {
        return new AoC2019_17(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        final IntCode intCode = new IntCode(this.program, this.debug);
        final Deque<Long> input = new ArrayDeque<>();
        final Deque<Long> output = new ArrayDeque<>();
        final List<String> strings = new ArrayList<>();
        final StringBuilder sb = new StringBuilder();
        while (true) {
            intCode.runTillHasOutput(input, output);
            if (intCode.isHalted()) {
                break;
            }
            final char out = (char) (long) output.pop();
            if (out == NEWLINE && sb.length() > 0) {
                strings.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(out);
            }
        }
        strings.forEach(this::log);
        final Grid grid = Grid.from(strings);
        return grid.getAllEqualTo(SCAFFOLD)
            .filter(cell -> grid.getCapitalNeighbours(cell)
                        .allMatch(n -> grid.getValueAt(n) == SCAFFOLD))
            .mapToInt(cell -> cell.getRow() * cell.getCol())
            .sum();
    }
    
    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        final Puzzle puzzle = Aocd.puzzle(2019, 17);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2019_17.createDebug(inputData)::solvePart1),
            () -> lap("Part 2", AoC2019_17.create(inputData)::solvePart2)
        );
    }
}
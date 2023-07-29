import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aoc.navigation.NavigationWithHeading;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2019_11 extends AoCBase {
    
    private final List<Long> program;
    
    private AoC2019_11(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 1;
        this.program = IntCode.parse(input.get(0));
    }

    public static AoC2019_11 create(final List<String> input) {
        return new AoC2019_11(input, false);
    }

    public static AoC2019_11 createDebug(final List<String> input) {
        return new AoC2019_11(input, true);
    }
    
    @Override
    public Long solvePart1() {
        final Set<Position> white = new HashSet<>();
        final NavigationWithHeading nav
            = new NavigationWithHeading(Position.of(0, 0), Headings.NORTH.get());
        final IntCode intCode = new IntCode(this.debug);
        final Deque<Long> input = new ArrayDeque<>(List.of(0L));
        final Deque<Long> output = new ArrayDeque<>();
        intCode.runTillInputRequired(this.program, input, output);
        while (!intCode.isHalted()) {
            if (output.pop() == 1L) {
                white.add(nav.getPosition());
            } else {
                white.remove(nav.getPosition());
            }
            if (output.pop() == 0) {
                nav.left(90);
            } else {
                nav.right(90);
            }
            nav.forward(1);
            input.add(white.contains(nav.getPosition()) ? 1L : 0L);
            intCode.continueTillInputRequired(input, output);
        }
        return nav.getVisitedPositions(false).stream().distinct().count();
    }
    
    @Override
    public Long solvePart2() {
        return 0L;
    }

    public static void main(final String[] args) throws Exception {
        final Puzzle puzzle = Aocd.puzzle(2019, 11);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2019_11.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2019_11.create(inputData)::solvePart2)
        );
    }
}
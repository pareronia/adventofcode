import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.NavigationWithHeading;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2016_01 extends AoCBase {

    private final List<String> inputs;

    private AoC2016_01(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.inputs = Stream.of(inputs.get(0).split(", ")).collect(toList());
    }

    public static final AoC2016_01 create(final List<String> input) {
        return new AoC2016_01(input, false);
    }

    public static final AoC2016_01 createDebug(final List<String> input) {
        return new AoC2016_01(input, true);
    }
    
    private NavigationWithHeading navigate() {
        final NavigationWithHeading nav
            = new NavigationWithHeading(Position.of(0,  0), Heading.NORTH);
        for (final String step : inputs) {
            nav.turn(Turn.fromChar(step.charAt(0)));
            for (int i = 0; i < Integer.valueOf(step.substring(1)); i++) {
                nav.forward(1);
            }
        }
        return nav;
    }

    @Override
    public Integer solvePart1() {
        final Position destination = navigate().getPosition();
        return Math.abs(destination.getX()) + Math.abs(destination.getY());
    }

    @Override
    public Integer solvePart2() {
        final List<Position> visited = navigate().getVisitedPositions(true);
        final Set<Position> seen = new HashSet<>();
        for (final Position position : visited) {
            if (seen.contains(position)) {
                return Math.abs(position.getX()) + Math.abs(position.getY());
            } else {
                seen.add(position);
            }
        }
        throw new IllegalStateException("Unsolvable");
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2016_01.createDebug(TEST1).solvePart1() == 5;
        assert AoC2016_01.createDebug(TEST2).solvePart1() == 2;
        assert AoC2016_01.createDebug(TEST3).solvePart1() == 12;
        assert AoC2016_01.createDebug(TEST4).solvePart2() == 4;

        final Puzzle puzzle = Aocd.puzzle(2016, 1);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2016_01.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2016_01.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST1 = splitLines("R2, L3");
    private static final List<String> TEST2 = splitLines("R2, R2, R2");
    private static final List<String> TEST3 = splitLines("R5, L5, R5, R3");
    private static final List<String> TEST4 = splitLines("R8, R4, R4, R8");
}

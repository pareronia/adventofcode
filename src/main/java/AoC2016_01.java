import com.github.pareronia.aoc.IntegerSequence.Range;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.NavigationWithHeading;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@SuppressWarnings({"PMD.NoPackage", "PMD.ClassNamingConventions"})
public final class AoC2016_01 extends SolutionBase<List<AoC2016_01.Step>, Integer, Integer> {

    private AoC2016_01(final boolean debug) {
        super(debug);
    }

    public static AoC2016_01 create() {
        return new AoC2016_01(false);
    }

    public static AoC2016_01 createDebug() {
        return new AoC2016_01(true);
    }

    @Override
    protected List<Step> parseInput(final List<String> inputs) {
        return Stream.of(inputs.get(0).split(", ")).map(Step::fromInput).toList();
    }

    private NavigationWithHeading navigate(final List<Step> steps) {
        final NavigationWithHeading nav = new NavigationWithHeading(Position.ORIGIN, Heading.NORTH);
        for (final Step step : steps) {
            nav.turn(step.turn);
            Range.range(step.distance).forEach(i -> nav.forward(1));
        }
        return nav;
    }

    @Override
    public Integer solvePart1(final List<Step> inputs) {
        final Position destination = navigate(inputs).getPosition();
        return destination.manhattanDistance();
    }

    @Override
    public Integer solvePart2(final List<Step> inputs) {
        final List<Position> visited = navigate(inputs).getVisitedPositions(true);
        final Set<Position> seen = new HashSet<>();
        for (final Position position : visited) {
            if (seen.contains(position)) {
                return position.manhattanDistance();
            } else {
                seen.add(position);
            }
        }
        throw new IllegalStateException("Unsolvable");
    }

    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "5"),
        @Sample(method = "part1", input = TEST2, expected = "2"),
        @Sample(method = "part1", input = TEST3, expected = "12"),
        @Sample(method = "part2", input = TEST4, expected = "4"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST1 = "R2, L3";
    private static final String TEST2 = "R2, R2, R2";
    private static final String TEST3 = "R5, L5, R5, R3";
    private static final String TEST4 = "R8, R4, R4, R8";

    record Step(Turn turn, int distance) {

        public static Step fromInput(final String string) {
            return new Step(Turn.fromChar(string.charAt(0)), Integer.parseInt(string.substring(1)));
        }
    }
}

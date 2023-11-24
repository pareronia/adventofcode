import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.NavigationWithHeading;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2015_03 extends SolutionBase<List<Direction>, Long, Long> {

    private AoC2015_03(final boolean debug) {
        super(debug);
    }

    public static AoC2015_03 create() {
        return new AoC2015_03(false);
    }

    public static AoC2015_03 createDebug() {
        return new AoC2015_03(true);
    }
 
    @Override
    protected List<Direction> parseInput(final List<String> inputs) {
        assert inputs.size() == 1;
        return Utils.asCharacterStream(inputs.get(0))
                .map(Direction::fromChar).collect(toList());
    }

    @Override
    public Long solvePart1(final List<Direction> input) {
        final HouseVisits houseVisits = new HouseVisits();
        input.forEach(houseVisits::goVisit);
        return houseVisits.getUniqueVisits().count();
    }

    @Override
    public Long solvePart2(final List<Direction> input) {
        final HouseVisits santaVisits = new HouseVisits();
        range(0, input.size(), 2).intStream()
                .mapToObj(input::get)
                .forEach(santaVisits::goVisit);
        final HouseVisits robotVisits = new HouseVisits();
        range(1, input.size(), 2).intStream()
                .mapToObj(input::get)
                .forEach(robotVisits::goVisit);
        return Stream.concat(
                    santaVisits.getUniqueVisits(), robotVisits.getUniqueVisits())
                .distinct().count();
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "2"),
        @Sample(method = "part1", input = TEST2, expected = "4"),
        @Sample(method = "part1", input = TEST3, expected = "2"),
        @Sample(method = "part2", input = TEST4, expected = "3"),
        @Sample(method = "part2", input = TEST2, expected = "3"),
        @Sample(method = "part2", input = TEST3, expected = "11"),
    })
    public void samples() {
    }

    public static void main(final String[] args) throws Exception {
        AoC2015_03.create().run();
    }

    private static final String TEST1 = ">";
    private static final String TEST2 = "^>v<";
    private static final String TEST3 = "^v^v^v^v^v";
    private static final String TEST4 = "^v";
    
    private static final class HouseVisits {
        private final NavigationWithHeading nav
                = new NavigationWithHeading(Position.ORIGIN, Heading.NORTH);
        
        public void goVisit(final Direction direction) {
            this.nav.navigate(Heading.fromDirection(direction), 1);
        }
        
        public Stream<Position> getUniqueVisits() {
            return nav.getVisitedPositions(true).stream().distinct();
        }
    }
}
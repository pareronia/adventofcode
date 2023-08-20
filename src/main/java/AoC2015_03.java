import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.NavigationWithHeading;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2015_03 extends AoCBase {

    private final transient List<Direction> input;

    private AoC2015_03(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = Utils.asCharacterStream(inputs.get(0))
                .map(Direction::fromChar).collect(toList());
    }

    public static AoC2015_03 create(final List<String> input) {
        return new AoC2015_03(input, false);
    }

    public static AoC2015_03 createDebug(final List<String> input) {
        return new AoC2015_03(input, true);
    }
    
    @Override
    public Long solvePart1() {
        final HouseVisits houseVisits = new HouseVisits();
        this.input.forEach(houseVisits::goVisit);
        return houseVisits.getUniqueVisits().count();
    }

    @Override
    public Long solvePart2() {
        final HouseVisits santaVisits = new HouseVisits();
        range(0, this.input.size(), 2).intStream()
                .mapToObj(this.input::get)
                .forEach(santaVisits::goVisit);
        final HouseVisits robotVisits = new HouseVisits();
        range(1, this.input.size(), 2).intStream()
                .mapToObj(this.input::get)
                .forEach(robotVisits::goVisit);
        return Stream.concat(
                    santaVisits.getUniqueVisits(), robotVisits.getUniqueVisits())
                .distinct().count();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_03.createDebug(TEST1).solvePart1() == 2;
        assert AoC2015_03.createDebug(TEST2).solvePart1() == 4;
        assert AoC2015_03.createDebug(TEST3).solvePart1() == 2;
        assert AoC2015_03.createDebug(TEST4).solvePart2() == 3;
        assert AoC2015_03.createDebug(TEST2).solvePart2() == 3;
        assert AoC2015_03.createDebug(TEST3).solvePart2() == 11;

        final Puzzle puzzle = Aocd.puzzle(2015, 3);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2015_03.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2015_03.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST1 = splitLines(">");
    private static final List<String> TEST2 = splitLines("^>v<");
    private static final List<String> TEST3 = splitLines("^v^v^v^v^v");
    private static final List<String> TEST4 = splitLines("^v");
    
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
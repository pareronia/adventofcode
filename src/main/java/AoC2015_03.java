import static org.apache.commons.collections4.ListUtils.union;

import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aoc.navigation.NavigationWithHeading;
import com.github.pareronia.aocd.Aocd;

public final class AoC2015_03 extends AoCBase {

    private final transient String input;

    private AoC2015_03(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = inputs.get(0);
    }

    public static AoC2015_03 create(final List<String> input) {
        return new AoC2015_03(input, false);
    }

    public static AoC2015_03 createDebug(final List<String> input) {
        return new AoC2015_03(input, true);
    }
    
    private void addNavigationInstruction(final NavigationWithHeading nav, final Character ch) {
        final Heading heading;
        switch (ch) {
        case '>':
            heading = Headings.EAST.get();
            break;
        case 'v':
            heading = Headings.SOUTH.get();
            break;
        case '<':
            heading = Headings.WEST.get();
            break;
        case '^':
            heading = Headings.NORTH.get();
            break;
        default:
            throw new IllegalArgumentException("Invalid input");
        }
        nav.drift(heading, 1);
    }
    
    private Integer countUniquePositions(final List<Position> positions) {
        return (int) positions.stream().distinct().count();
    }
    
    @Override
    public Integer solvePart1() {
        final NavigationWithHeading nav = new NavigationWithHeading(Position.of(0, 0), Headings.NORTH.get());
        Utils.asCharacterStream(this.input).forEach(ch -> addNavigationInstruction(nav, ch));
        return countUniquePositions(nav.getVisitedPositions(true));
    }

    @Override
    public Integer solvePart2() {
        final NavigationWithHeading santaNav = new NavigationWithHeading(Position.of(0, 0), Headings.NORTH.get());
        final NavigationWithHeading robotNav = new NavigationWithHeading(Position.of(0, 0), Headings.NORTH.get());
        Stream.iterate(0, i -> i < this.input.length(), i -> i + 2)
                .forEach(i -> addNavigationInstruction(santaNav, this.input.charAt(i)));
        Stream.iterate(1, i -> i < this.input.length(), i -> i + 2)
                .forEach(i -> addNavigationInstruction(robotNav, this.input.charAt(i)));
        return countUniquePositions(union(santaNav.getVisitedPositions(true), robotNav.getVisitedPositions(true)));
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_03.createDebug(TEST1).solvePart1() == 2;
        assert AoC2015_03.createDebug(TEST2).solvePart1() == 4;
        assert AoC2015_03.createDebug(TEST3).solvePart1() == 2;
        assert AoC2015_03.createDebug(TEST4).solvePart2() == 3;
        assert AoC2015_03.createDebug(TEST2).solvePart2() == 3;
        assert AoC2015_03.createDebug(TEST3).solvePart2() == 11;

        final List<String> input = Aocd.getData(2015, 3);
        lap("Part 1", () -> AoC2015_03.create(input).solvePart1());
        lap("Part 2", () -> AoC2015_03.create(input).solvePart2());
    }

    private static final List<String> TEST1 = splitLines(">");
    private static final List<String> TEST2 = splitLines("^>v<");
    private static final List<String> TEST3 = splitLines("^v^v^v^v^v");
    private static final List<String> TEST4 = splitLines("^v");
}
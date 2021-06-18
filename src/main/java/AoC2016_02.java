import static com.github.pareronia.aoc.Utils.toAString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aoc.navigation.NavigationWithHeading;
import com.github.pareronia.aocd.Aocd;

public class AoC2016_02 extends AoCBase {
    
    private static final Map<Position, Character> KEYPAD1 = Map.of(
            Position.of(0, 0), '7',
            Position.of(1, 0), '8',
            Position.of(2, 0), '9',
            Position.of(0, 1), '4',
            Position.of(1, 1), '5',
            Position.of(2, 1), '6',
            Position.of(0, 2), '1',
            Position.of(1, 2), '2',
            Position.of(2, 2), '3'
    );

    private final List<String> inputs;

    private AoC2016_02(List<String> inputs, boolean debug) {
        super(debug);
        this.inputs = inputs;
    }

    public static final AoC2016_02 create(List<String> input) {
        return new AoC2016_02(input, false);
    }

    public static final AoC2016_02 createDebug(List<String> input) {
        return new AoC2016_02(input, true);
    }
    
    private NavigationWithHeading navigate(
                    String ins, Position start, Predicate<Position> inBounds) {
        final NavigationWithHeading nav
            = new NavigationWithHeading(start, Headings.NORTH.get(), inBounds);
        Utils.asCharacterStream(ins).forEach(step -> {
            if (step == 'R') {
                nav.drift(Headings.EAST.get(), 1);
            } else if (step == 'L') {
                nav.drift(Headings.WEST.get(), 1);
            } else if (step == 'U') {
                nav.drift(Headings.NORTH.get(), 1);
            } else if (step == 'D') {
                nav.drift(Headings.SOUTH.get(), 1);
            } else {
                throw new IllegalArgumentException("Invalid input");
            }
        });
        return nav;
    }

    @Override
    public Integer solvePart1() {
        final List<Character> code = new ArrayList<>();
        Position start = Position.of(1,  1);
        for (final String ins : this.inputs) {
            final Position last
                    = navigate(ins, start, pos -> KEYPAD1.keySet().contains(pos))
                        .getPosition();
            code.add(KEYPAD1.get(last));
            start = last;
        }
        return Integer.valueOf(code.stream().collect(toAString()));
    }

    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(String[] args) throws Exception {
        assert AoC2016_02.createDebug(TEST).solvePart1() == 1985;

        final List<String> input = Aocd.getData(2016, 2);
        lap("Part 1", () -> AoC2016_02.create(input).solvePart1());
        lap("Part 2", () -> AoC2016_02.create(input).solvePart2());
    }

    private static final List<String> TEST = splitLines(
            "ULL\n" +
            "RRDDD\n" +
            "LURDL\n" +
            "UUUUD"
    );
}

import static com.github.pareronia.aoc.Utils.asCharacterStream;
import static com.github.pareronia.aoc.Utils.toAString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aoc.navigation.NavigationWithHeading;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2016_02 extends AoCBase {
    
    private static final Map<Character, Heading> HEADINGS = Map.of(
        'R', Headings.EAST.get(),
        'D', Headings.SOUTH.get(),
        'L', Headings.WEST.get(),
        'U', Headings.NORTH.get()
    );
    private static final Map<Position, Character> KEYPAD1 = Map.of(
            Position.of(-1, 1), '1',
            Position.of(0, 1), '2',
            Position.of(1, 1), '3',
            Position.of(-1, 0), '4',
            Position.of(0, 0), '5',
            Position.of(1, 0), '6',
            Position.of(-1, -1), '7',
            Position.of(0, -1), '8',
            Position.of(1, -1), '9'
    );
    @SuppressWarnings("serial")
    private static final Map<Position, Character> KEYPAD2
        = Collections.unmodifiableMap(new HashMap<>() {{
            put(Position.of(2, 2), '1');
            put(Position.of(1, 1), '2');
            put(Position.of(2, 1), '3');
            put(Position.of(3, 1), '4');
            put(Position.of(0, 0), '5');
            put(Position.of(1, 0), '6');
            put(Position.of(2, 0), '7');
            put(Position.of(3, 0), '8');
            put(Position.of(4, 0), '9');
            put(Position.of(1, -1), 'A');
            put(Position.of(2, -1), 'B');
            put(Position.of(3, -1), 'C');
            put(Position.of(2, -2), 'D');
    }});

    private final List<String> inputs;

    private AoC2016_02(final List<String> inputs, final boolean debug) {
        super(debug);
        this.inputs = inputs;
    }

    public static final AoC2016_02 create(final List<String> input) {
        return new AoC2016_02(input, false);
    }

    public static final AoC2016_02 createDebug(final List<String> input) {
        return new AoC2016_02(input, true);
    }
    
    private NavigationWithHeading navigate(
                    final String ins, final Position start, final Predicate<Position> inBounds) {
        final NavigationWithHeading nav
            = new NavigationWithHeading(start, Headings.NORTH.get(), inBounds);
        asCharacterStream(ins).forEach(step -> nav.drift(HEADINGS.get(step), 1));
        return nav;
    }

    private String solve(
            final Predicate<Position> inBounds,
            final Function<Position, Character> get
        ) {
        final List<Character> code = new ArrayList<>();
        Position start = Position.of(0,  0);
        for (final String ins : this.inputs) {
            final Position last = navigate(ins, start, inBounds).getPosition();
            code.add(get.apply(last));
            start = last;
        }
        return code.stream().collect(toAString());
    }

    @Override
    public String solvePart1() {
        return solve(
                pos -> KEYPAD1.keySet().contains(pos),
                pos -> KEYPAD1.get(pos)
        );
    }
    
    @Override
    public String solvePart2() {
        return solve(
                pos -> KEYPAD2.keySet().contains(pos),
                pos -> KEYPAD2.get(pos)
        );
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2016_02.createDebug(TEST).solvePart1().equals("1985");
        assert AoC2016_02.createDebug(TEST).solvePart2().equals("5DB3");

        final Puzzle puzzle = Aocd.puzzle(2016, 2);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2016_02.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2016_02.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
            "ULL\n" +
            "RRDDD\n" +
            "LURDL\n" +
            "UUUUD"
    );
}

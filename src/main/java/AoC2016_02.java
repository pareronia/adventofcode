import static com.github.pareronia.aoc.Utils.asCharacterStream;

import static java.util.stream.Collectors.toMap;

import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.NavigationWithHeading;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"PMD.NoPackage", "PMD.ClassNamingConventions"})
public final class AoC2016_02 extends SolutionBase<List<List<Direction>>, String, String> {

    private static final List<Key> LAYOUT1 =
            List.of(
                    new Key(Position.of(-1, 1), '1'),
                    new Key(Position.of(0, 1), '2'),
                    new Key(Position.of(1, 1), '3'),
                    new Key(Position.of(-1, 0), '4'),
                    new Key(Position.of(0, 0), '5'),
                    new Key(Position.of(1, 0), '6'),
                    new Key(Position.of(-1, -1), '7'),
                    new Key(Position.of(0, -1), '8'),
                    new Key(Position.of(1, -1), '9'));

    @SuppressWarnings("serial")
    private static final List<Key> LAYOUT2 =
            new ArrayList<>() {
                {
                    add(new Key(Position.of(2, 2), '1'));
                    add(new Key(Position.of(1, 1), '2'));
                    add(new Key(Position.of(2, 1), '3'));
                    add(new Key(Position.of(3, 1), '4'));
                    add(new Key(Position.of(0, 0), '5'));
                    add(new Key(Position.of(1, 0), '6'));
                    add(new Key(Position.of(2, 0), '7'));
                    add(new Key(Position.of(3, 0), '8'));
                    add(new Key(Position.of(4, 0), '9'));
                    add(new Key(Position.of(1, -1), 'A'));
                    add(new Key(Position.of(2, -1), 'B'));
                    add(new Key(Position.of(3, -1), 'C'));
                    add(new Key(Position.of(2, -2), 'D'));
                }
            };

    private AoC2016_02(final boolean debug) {
        super(debug);
    }

    public static AoC2016_02 create() {
        return new AoC2016_02(false);
    }

    public static AoC2016_02 createDebug() {
        return new AoC2016_02(true);
    }

    @Override
    protected List<List<Direction>> parseInput(final List<String> inputs) {
        return inputs.stream()
                .map(s -> asCharacterStream(s).map(Direction::fromChar).toList())
                .toList();
    }

    @Override
    public String solvePart1(final List<List<Direction>> inputs) {
        return Keypad.fromLayout(LAYOUT1).executeInstructions(inputs);
    }

    @Override
    public String solvePart2(final List<List<Direction>> inputs) {
        return Keypad.fromLayout(LAYOUT2).executeInstructions(inputs);
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "1985"),
        @Sample(method = "part2", input = TEST, expected = "5DB3"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            ULL
            RRDDD
            LURDL
            UUUUD
            """;

    record Keypad(Map<Position, Key> keys) {

        public static Keypad fromLayout(final List<Key> positions) {
            return new Keypad(positions.stream().collect(toMap(Key::position, k -> k)));
        }

        public String executeInstructions(final List<List<Direction>> directions) {
            final StringBuilder sb = new StringBuilder();
            Key current = keys.get(Position.ORIGIN);
            for (final List<Direction> d : directions) {
                current = this.executeInstruction(current, d);
                sb.append(current.character);
            }
            return sb.toString();
        }

        private Key executeInstruction(final Key start, final List<Direction> directions) {
            final NavigationWithHeading nav =
                    new NavigationWithHeading(
                            start.position, Heading.NORTH, this.keys.keySet()::contains);
            directions.forEach(step -> nav.navigate(Heading.fromDirection(step), 1));
            return keys.get(nav.getPosition());
        }
    }

    record Key(Position position, Character character) {}
}

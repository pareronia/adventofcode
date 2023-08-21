import static com.github.pareronia.aoc.Utils.asCharacterStream;
import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.NavigationWithHeading;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;

public class AoC2016_02 extends AoCBase {
    
    private static final Map<Position, Character> LAYOUT1 = Map.of(
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
    private static final Map<Position, Character> LAYOUT2
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

    private final List<List<Direction>> inputs;

    private AoC2016_02(final List<String> inputs, final boolean debug) {
        super(debug);
        this.inputs = inputs.stream()
            .map(s -> asCharacterStream(s).map(Direction::fromChar).collect(toList()))
            .collect(toList());
    }

    public static final AoC2016_02 create(final List<String> input) {
        return new AoC2016_02(input, false);
    }

    public static final AoC2016_02 createDebug(final List<String> input) {
        return new AoC2016_02(input, true);
    }
    
    @Override
    public String solvePart1() {
        return Keypad.fromLayout(LAYOUT1).executeInstructions(this.inputs);
    }
    
    @Override
    public String solvePart2() {
        return Keypad.fromLayout(LAYOUT2).executeInstructions(this.inputs);
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
    
    @RequiredArgsConstructor(staticName = "fromLayout")
    private static final class Keypad {
        private final Map<Position, Character> positions;
        private Position current = Position.ORIGIN;
        
        public String executeInstructions(final List<List<Direction>> directions) {
            return directions.stream()
                .map(this::executeInstruction)
                .collect(toAString());
        }
        
        private Character executeInstruction(final List<Direction> directions) {
            final NavigationWithHeading nav = new NavigationWithHeading(
                this.current,
                Heading.NORTH,
                this.positions.keySet()::contains);
            directions.forEach(step -> nav.navigate(Heading.fromDirection(step), 1));
            this.current = nav.getPosition();
            return this.positions.get(this.current);
        }
    }
}

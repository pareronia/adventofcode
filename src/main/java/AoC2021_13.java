import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.OCR;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Draw;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2021_13 extends AoCBase {
    
    private static final char FILL = '\u2592';
    private static final char EMPTY = ' ';

    private final Set<Position> _positions;
    private List<Fold> _folds = new ArrayList<>();
    
    private AoC2021_13(final List<String> input, final boolean debug) {
        super(debug);
        final List<List<String>> blocks = toBlocks(input);
        this._positions = new HashSet<>();
        for (final String line : blocks.get(0)) {
            final String[] split = line.split(",");
            _positions.add(Position.of(Integer.valueOf(split[0]), Integer.valueOf(split[1])));
        }
        this._folds = new ArrayList<>();
        for (final String line : blocks.get(1)) {
            final String[] split = line.substring("fold along ".length()).split("=");
            _folds.add(new Fold("x".equals(split[0]) ? true : false, Integer.parseInt(split[1])));
        }
    }
    
    public static final AoC2021_13 create(final List<String> input) {
        return new AoC2021_13(input, false);
    }

    public static final AoC2021_13 createDebug(final List<String> input) {
        return new AoC2021_13(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        return this._folds.stream()
                .limit(1)
                .map(f -> f.applyTo(this._positions))
                .findFirst().orElseThrow()
                .size();
    }
    
    private List<String> solve2() {
        Set<Position> positions = this._positions;
        for (final Fold fold : this._folds) {
            positions = fold.applyTo(positions);
        }
        return Draw.draw(positions, FILL, EMPTY);
    }

    @Override
    public String solvePart2() {
        final List<String> drawing = solve2();
        Collections.reverse(drawing);
        drawing.forEach(this::log);
        return OCR.convert6(CharGrid.from(drawing), FILL, EMPTY);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_13.create(TEST).solvePart1() == 17;
        assert AoC2021_13.create(TEST).solve2().equals(RESULT1);

        final Puzzle puzzle = Aocd.puzzle(2021, 13);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_13.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_13.createDebug(puzzle.getInputData()).solvePart2())
        );
    }

    private static final List<String> TEST = splitLines(
        "6,10\r\n" +
        "0,14\r\n" +
        "9,10\r\n" +
        "0,3\r\n" +
        "10,4\r\n" +
        "4,11\r\n" +
        "6,0\r\n" +
        "6,12\r\n" +
        "4,1\r\n" +
        "0,13\r\n" +
        "10,12\r\n" +
        "3,4\r\n" +
        "3,0\r\n" +
        "8,4\r\n" +
        "1,10\r\n" +
        "2,14\r\n" +
        "8,10\r\n" +
        "9,0\r\n" +
        "\r\n" +
        "fold along y=7\r\n" +
        "fold along x=5"
    );
    private static final List<String> RESULT1 = splitLines(
        "▒▒▒▒▒ \r\n" +
        "▒   ▒ \r\n" +
        "▒   ▒ \r\n" +
        "▒   ▒ \r\n" +
        "▒▒▒▒▒ "
    );
    
    record Fold(boolean xAxis, int value) {
        
        public Set<Position> applyTo(final Set<Position> positions) {
            if (this.xAxis) {
                return apply(positions, Direction.LEFT, Position::getX);
            } else {
                return apply(positions, Direction.DOWN, Position::getY);
            }
        }
        
        private Set<Position> apply(
                final Set<Position> positions,
                final Direction direction,
                final Function<Position, Integer> dim
        ) {
            return positions.stream()
                    .map(p -> p.translate(direction, amplitude(p, dim)))
                    .collect(toSet());
        }
        
        private int amplitude(
                final Position p, final Function<Position, Integer> dim) {
            final int value = dim.apply(p);
            return value > this.value ? 2 * (value - this.value) : 0;
        }
    }
}

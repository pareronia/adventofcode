import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.OCR;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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
            _folds.add(new Fold(split[0], Integer.valueOf(split[1])));
        }
    }
    
    public static final AoC2021_13 create(final List<String> input) {
        return new AoC2021_13(input, false);
    }

    public static final AoC2021_13 createDebug(final List<String> input) {
        return new AoC2021_13(input, true);
    }
    
    private Position findLargest(final Set<Position> positions) {
        Position max = Position.of(Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (final Position position : positions) {
            if (position.getX() > max.getX()) {
                max = Position.of(position.getX(), max.getY());
            }
            if (position.getY() > max.getY()) {
                max = Position.of(max.getX(),  position.getY());
            }
        }
        return max;
    }

    private Set<Position> foldX(final Set<Position> positions, final int fold) {
        return positions.stream()
                .map(p -> {
                    int x;
                    if (p.getX() > fold) {
                        x = p.getX() - 2 * (p.getX() - fold);
                    } else {
                        x = p.getX();
                    }
                    return Position.of(x, p.getY());
                })
                .collect(toSet());
    }
    
    private Set<Position> foldY(final Set<Position> positions, final int fold) {
        return positions.stream()
            .map(p -> {
                int y;
                if (p.getY() > fold) {
                    y = p.getY() - 2 * (p.getY() - fold);
                } else {
                    y = p.getY();
                }
                return Position.of(p.getX(), y);
            })
            .collect(toSet());
    }

    private Set<Position> fold(Set<Position> positions, final List<Fold> folds) {
        for (final Fold fold : folds) {
            final Position largest = findLargest(positions);
            if ("x".equals(fold.getAxis())) {
                assert largest.getX() / 2 <= fold.getValue();
                positions = foldX(positions, fold.getValue());
            } else {
                assert largest.getY() / 2 <= fold.getValue();
                positions = foldY(positions, fold.getValue());
            }
        }
        return positions;
    }
    
    private List<String> draw(final Set<Position> positions, final char fill, final char empty) {
        final Position max = findLargest(positions);
        final List<String> strings = new ArrayList<>();
        for (int y = 0; y <= max.getY(); y++) {
            final char[] ch = new char[max.getX() + 2];
            for (int x = 0; x <= max.getX() + 1; x++) {
                if (positions.contains(Position.of(x, y))) {
                    ch[x] = fill;
                } else {
                    ch[x] = empty;
                }
            }
            strings.add(String.valueOf(ch));
        }
        return strings;
    }
    
    @Override
    public Integer solvePart1() {
        return fold(this._positions, this._folds.subList(0, 1)).size();
    }
    
    private List<String> solve2() {
        final Set<Position> positions = fold(this._positions, this._folds);
        return draw(positions, FILL, EMPTY);
    }

    @Override
    public String solvePart2() {
        final List<String> drawing = solve2();
        drawing.forEach(this::log);
        return OCR.convert6(Grid.from(drawing), FILL, EMPTY);
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
    
    @RequiredArgsConstructor
    @Getter
    @ToString
    private static final class Fold {
        private final String axis;
        private final int value;
    }
}

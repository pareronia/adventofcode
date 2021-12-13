import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2021_13 extends AoCBase {
    
    private final Set<Position> positions;
    private List<Fold> folds = new ArrayList<>();
    
    private AoC2021_13(final List<String> input, final boolean debug) {
        super(debug);
        final List<List<String>> blocks = toBlocks(input);
        positions = new HashSet<>();
        for (final String line : blocks.get(0)) {
            final String[] split = line.split(",");
            positions.add(Position.of(Integer.valueOf(split[0]), Integer.valueOf(split[1])));
        }
        this.folds = new ArrayList<>();
        for (final String line : blocks.get(1)) {
            final String[] split = line.substring("fold along ".length()).split("=");
            folds.add(new Fold(split[0], Integer.valueOf(split[1])));
        }
    }
    
    public static final AoC2021_13 create(final List<String> input) {
        return new AoC2021_13(input, false);
    }

    public static final AoC2021_13 createDebug(final List<String> input) {
        return new AoC2021_13(input, true);
    }
    
    private Position findLargest(final Set<Position> pos) {
        Position max = Position.of(Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (final Position position : pos) {
            if (position.getX() > max.getX()) {
                max = Position.of(position.getX(), max.getY());
            }
            if (position.getY() > max.getY()) {
                max = Position.of(max.getX(),  position.getY());
            }
        }
        return max;
    }

    private Position findSmallest(final Set<Position> pos) {
        Position min = Position.of(Integer.MAX_VALUE, Integer.MAX_VALUE);
        for (final Position position : pos) {
            if (position.getX() < min.getX()) {
                min = Position.of(position.getX(), min.getY());
            }
            if (position.getY() < min.getY()) {
                min = Position.of(min.getX(),  position.getY());
            }
        }
        return min;
    }

    private Set<Position> foldX(final Set<Position> pos, final int fold) {
        return pos.stream()
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
    
    private Set<Position> foldY(final Set<Position> pos, final int fold) {
        return pos.stream()
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
    
    @Override
    public Integer solvePart1() {
        // fold along x=655
        return foldX(this.positions, 655).size();
    }
    
    private void draw(final Set<Position> pos) {
        final Position max = findLargest(pos);
        for (int y = 0; y <= max.getY(); y++) {
            final char[] ch = new char[max.getX() + 1];
            for (int x = 0; x <= max.getX(); x++) {
                if (pos.contains(Position.of(x, y))) {
                    ch[x] = '\u2592';
                } else {
                    ch[x] = ' ';
                }
            }
            log(String.valueOf(ch));
        }
    }

    @Override
    public Integer solvePart2() {
        Set<Position> pos = this.positions;
        for (final Fold fold : this.folds) {
            final Position largest = findLargest(pos);
            if ("x".equals(fold.getAxis())) {
                assert largest.getX() / 2 <= fold.getValue();
                pos = foldX(pos, fold.getValue());
            } else {
                assert largest.getY() / 2 <= fold.getValue();
                pos = foldY(pos, fold.getValue());
            }
        }
        draw(pos);
        return null;
    }

    public static void main(final String[] args) throws Exception {
//        assert AoC2021_13.createDebug(TEST).foldY(this.positions, 7).size() == 17;
//        assert AoC2021_13.create(TEST).solvePart2() == null;

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
    
    @RequiredArgsConstructor
    @Getter
    @ToString
    private static final class Fold {
        private final String axis;
        private final int value;
    }
}

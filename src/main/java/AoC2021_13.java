import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2021_13 extends AoCBase {
    
    private final Set<Position> positions;
    private List<String> folds = new ArrayList<>();
    
    private AoC2021_13(final List<String> input, final boolean debug) {
        super(debug);
        final List<List<String>> blocks = toBlocks(input);
        positions = new HashSet<>();
        for (final String line : blocks.get(0)) {
            final String[] split = line.split(",");
            positions.add(Position.of(Integer.valueOf(split[0]), Integer.valueOf(split[1])));
        }
        this.folds = blocks.get(1);
    }
    
    public static final AoC2021_13 create(final List<String> input) {
        return new AoC2021_13(input, false);
    }

    public static final AoC2021_13 createDebug(final List<String> input) {
        return new AoC2021_13(input, true);
    }

    private Integer foldX(final int fold) {
        return this.positions.stream()
                .map(p -> {
                    int x;
                    if (p.getX() > fold) {
                        x = p.getX() - 2 * (p.getX() - fold);
                    } else {
                        x = p.getX();
                    }
                    return Position.of(x, p.getY());
                })
                .collect(toSet())
                .size();
    }
    
    private Integer foldY(final int fold) {
        return this.positions.stream()
            .map(p -> {
                int y;
                if (p.getY() > fold) {
                    y = p.getY() - 2 * (p.getY() - fold);
                } else {
                    y = p.getY();
                }
                return Position.of(p.getX(), y);
            })
            .collect(toSet())
            .size();
    }
    
    @Override
    public Integer solvePart1() {
        // fold along x=655
        return foldX(655);
    }

    @Override
    public Integer solvePart2() {
        return null;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_13.create(TEST).foldY(7) == 17;
        assert AoC2021_13.create(TEST).solvePart2() == null;

        final Puzzle puzzle = Aocd.puzzle(2021, 13);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_13.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_13.create(puzzle.getInputData()).solvePart2())
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
}

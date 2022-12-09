import static java.util.stream.Collectors.joining;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_09 extends AoCBase {
    
    private final List<String> input;
    
    private AoC2022_09(final List<String> input, final boolean debug) {
        super(debug);
        this.input = input;
    }
    
    public static final AoC2022_09 create(final List<String> input) {
        return new AoC2022_09(input, false);
    }

    public static final AoC2022_09 createDebug(final List<String> input) {
        return new AoC2022_09(input, true);
    }
    
    private Cell catchup(final Cell head, final Cell tail) {
        final int dr = head.getRow() - tail.getRow();
        final int dc = head.getCol() - tail.getCol();
        if (Math.abs(dr) > 1 || Math.abs(dc) > 1) {
            return Cell.at(
                tail.getRow() + (dr < 0 ? - 1 : dr > 0 ? 1 : 0),
                tail.getCol() + (dc < 0 ? - 1 : dc > 0 ? 1 : 0));
        }
        return tail;
    }
    
    @Override
    public Integer solvePart1() {
        Cell head = Cell.at(0,  0);
        Cell tail = Cell.at(0,  0);
        log(String.format("H: %s, T: %s", head, tail));
        final Set<Cell> seen = new HashSet<>();
        seen.add(tail);
        for (final String line : this.input) {
            log(line);
            final String[] splits = line.split(" ");
            final String dir = splits[0];
            final int amount = Integer.parseInt(splits[1]);
            if ("U".equals(dir)) {
                for (int i = 0; i < amount; i++) {
                    head = Cell.at(head.getRow() - 1, head.getCol());
                    tail = catchup(head, tail);
                    log(String.format("H: %s, T: %s", head, tail));
                    seen.add(tail);
                }
            }
            if ("D".equals(dir)) {
                for (int i = 0; i < amount; i++) {
                    head = Cell.at(head.getRow() + 1, head.getCol());
                    tail = catchup(head, tail);
                    log(String.format("H: %s, T: %s", head, tail));
                    seen.add(tail);
                }
            }
            if ("L".equals(dir)) {
                for (int i = 0; i < amount; i++) {
                    head = Cell.at(head.getRow(), head.getCol() - 1);
                    tail = catchup(head, tail);
                    log(String.format("H: %s, T: %s", head, tail));
                    seen.add(tail);
                }
            }
            if ("R".equals(dir)) {
                for (int i = 0; i < amount; i++) {
                    head = Cell.at(head.getRow(), head.getCol() + 1);
                    tail = catchup(head, tail);
                    log(String.format("H: %s, T: %s", head, tail));
                    seen.add(tail);
                }
            }
        }
        log(seen.size());
        log(seen);
        return seen.size();
    }

    @Override
    public Integer solvePart2() {
        final Cell[] rope = new Cell[10];
        Arrays.fill(rope, Cell.at(0, 0));
        final Set<Cell> seen = new HashSet<>();
        seen.add(rope[9]);
        for (final String line : this.input) {
            log(line);
            final String[] splits = line.split(" ");
            final String dir = splits[0];
            final int amount = Integer.parseInt(splits[1]);
            if ("U".equals(dir)) {
                for (int i = 0; i < amount; i++) {
                    rope[0] = Cell.at(rope[0].getRow() - 1, rope[0].getCol());
                    for (int j = 1; j <= 9; j++) {
                        rope[j] = catchup(rope[j - 1], rope[j]);
                    }
                    printRope(rope);
                    seen.add(rope[9]);
                }
            }
            if ("D".equals(dir)) {
                for (int i = 0; i < amount; i++) {
                    rope[0] = Cell.at(rope[0].getRow() + 1, rope[0].getCol());
                    for (int j = 1; j <= 9; j++) {
                        rope[j] = catchup(rope[j - 1], rope[j]);
                    }
                    printRope(rope);
                    seen.add(rope[9]);
                }
            }
            if ("L".equals(dir)) {
                for (int i = 0; i < amount; i++) {
                    rope[0] = Cell.at(rope[0].getRow(), rope[0].getCol() - 1);
                    for (int j = 1; j <= 9; j++) {
                        rope[j] = catchup(rope[j - 1], rope[j]);
                    }
                    printRope(rope);
                    seen.add(rope[9]);
                }
            }
            if ("R".equals(dir)) {
                for (int i = 0; i < amount; i++) {
                    rope[0] = Cell.at(rope[0].getRow(), rope[0].getCol() + 1);
                    for (int j = 1; j <= 9; j++) {
                        rope[j] = catchup(rope[j - 1], rope[j]);
                    }
                    printRope(rope);
                    seen.add(rope[9]);
                }
            }
        }
        log(seen.size());
        log(seen);
        return seen.size();
    }
    
    void printRope(final Cell[] rope) {
        log(Arrays.stream(rope)
            .map(r -> String.format("(%d,%d)", r.getRow(), r.getCol()))
            .collect(joining(" ")));
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_09.createDebug(TEST1).solvePart1() == 13;
        assert AoC2022_09.createDebug(TEST1).solvePart2() == 1;
        assert AoC2022_09.createDebug(TEST2).solvePart2() == 36;

        final Puzzle puzzle = Aocd.puzzle(2022, 9);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_09.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_09.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST1 = splitLines(
        "R 4\r\n" +
        "U 4\r\n" +
        "L 3\r\n" +
        "D 1\r\n" +
        "R 4\r\n" +
        "D 1\r\n" +
        "L 5\r\n" +
        "R 2"
    );
    private static final List<String> TEST2 = splitLines(
        "R 5\r\n" +
        "U 8\r\n" +
        "L 8\r\n" +
        "D 3\r\n" +
        "R 17\r\n" +
        "D 10\r\n" +
        "L 25\r\n" +
        "U 20"
    );
}

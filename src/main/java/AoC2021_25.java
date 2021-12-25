import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2021_25 extends AoCBase {
    
    private static final char SOUTH = 'v';
    private static final char EAST = '>';
    private static final char EMPTY = '.';

    private final Set<Position> eastHerd;
    private final Set<Position> southHerd;
    private final int rows;
    private final int cols;
    
    private AoC2021_25(final List<String> input, final boolean debug) {
        super(debug);
        this.rows = input.size();
        this.cols = input.get(0).length();
        this.eastHerd = new HashSet<>();
        this.southHerd = new HashSet<>();
        IntStream.range(0, rows)
            .boxed()
            .flatMap(row -> IntStream.range(0, cols)
                                .mapToObj(col -> Position.of(row, col)))
            .forEach(p -> {
                final char ch = input.get(p.getX()).charAt(p.getY());
                if (ch == SOUTH) {
                    this.southHerd.add(p);
                } else if (ch == EAST) {
                    this.eastHerd.add(p);
                } else if (ch != EMPTY) {
                    throw new IllegalArgumentException();
                }
            });
    }
    
    public static final AoC2021_25 create(final List<String> input) {
        return new AoC2021_25(input, false);
    }

    public static final AoC2021_25 createDebug(final List<String> input) {
        return new AoC2021_25(input, true);
    }
    
    @SuppressWarnings("unused")
    private void print() {
        if (!this.debug) {
            return;
        }
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                final Position pos = Position.of(row, col);
                if (this.southHerd.contains(pos)) {
                    System.out.print(SOUTH);
                } else if (this.eastHerd.contains(pos)) {
                    System.out.print(EAST);
                } else {
                    System.out.print(EMPTY);
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    private Position destinationEast(final Position p) {
        return Position.of(p.getX(), (p.getY() + 1) % cols);
    }
    
    private Position destinationSouth(final Position p) {
        return Position.of((p.getX() + 1) % rows, p.getY());
    }

    private Set<Position> findEast() {
        return this.eastHerd.stream()
            .filter(p -> {
                final Position newPos = destinationEast(p);
                return !this.eastHerd.contains(newPos) && !this.southHerd.contains(newPos);
            })
            .collect(toSet());
    }
    
    private Set<Position> findSouth() {
        return this.southHerd.stream()
                .filter(p -> {
                    final Position newPos = destinationSouth(p);
                    return !this.eastHerd.contains(newPos) && !this.southHerd.contains(newPos);
                })
                .collect(toSet());
    }

    private void moveSouth(final Set<Position> toMove) {
        toMove.stream()
            .forEach(p -> {
                this.southHerd.remove(p);
                this.southHerd.add(destinationSouth(p));
            });
    }

    private void moveEast(final Set<Position> toMove) {
        toMove.stream()
            .forEach(p -> {
                this.eastHerd.remove(p);
                this.eastHerd.add(destinationEast(p));
            });
    }
    
    @Override
    public Integer solvePart1() {
        int cnt = 0;
        int moved = -1;
        while (moved != 0) {
            final Set<Position> toMoveEast = findEast();
            moveEast(toMoveEast);
            final Set<Position> toMoveSouth = findSouth();
            moveSouth(toMoveSouth);
            moved = toMoveEast.size() + toMoveSouth.size();
            cnt++;
        }
        return cnt;
    }

    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_25.create(TEST).solvePart1() == 58;

        final Puzzle puzzle = Aocd.puzzle(2021, 25);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_25.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_25.create(puzzle.getInputData()).solvePart2())
        );
    }

    private static final List<String> TEST = splitLines(
        "v...>>.vv>\r\n" +
        ".vv>>.vv..\r\n" +
        ">>.>v>...v\r\n" +
        ">>v>>.>.v.\r\n" +
        "v>v.vv.v..\r\n" +
        ">.>>..v...\r\n" +
        ".vv..>.>v.\r\n" +
        "v.v..>>v.v\r\n" +
        "....v..v.>"
    );
}

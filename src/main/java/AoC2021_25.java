import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2021_25 extends AoCBase {
    
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
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                final char ch = input.get(row).charAt(col);
                if (ch == '.') {
                    continue;
                } else if (ch == 'v') {
                    this.southHerd.add(Position.of(row,  col));
                } else if (ch == '>') {
                    this.eastHerd.add(Position.of(row,  col));
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
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
                    System.out.print("v");
                } else if (this.eastHerd.contains(pos)) {
                    System.out.print(">");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    @Override
    public Integer solvePart1() {
        int moved = -1;
        int cnt = 0;
        while (moved != 0) {
            moved = 0;
            Set<Position> toMove = this.eastHerd.stream()
                .filter(p -> {
                    final Position newPos = Position.of(p.getX(), (p.getY() + 1) % cols);
                    return !this.eastHerd.contains(newPos) && !this.southHerd.contains(newPos);
                })
                .collect(toSet());
            toMove.stream()
                .forEach(p -> {
                    this.eastHerd.remove(p);
                    this.eastHerd.add(Position.of(p.getX(), (p.getY() + 1) % cols));
                });
            moved += toMove.size();
            toMove = this.southHerd.stream()
                    .filter(p -> {
                        final Position newPos = Position.of((p.getX() + 1) % rows, p.getY());
                        return !this.eastHerd.contains(newPos) && !this.southHerd.contains(newPos);
                    })
                    .collect(toSet());
            toMove.stream()
                .forEach(p -> {
                    this.southHerd.remove(p);
                    this.southHerd.add(Position.of((p.getX() + 1) % rows, p.getY()));
                });
            moved += toMove.size();
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
            () -> lap("Part 1", () -> AoC2021_25.createDebug(puzzle.getInputData()).solvePart1()),
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

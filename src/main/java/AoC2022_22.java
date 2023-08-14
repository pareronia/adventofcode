import static com.github.pareronia.aoc.Utils.asCharacterStream;
import static com.github.pareronia.aoc.Utils.last;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2022_22 extends AoCBase {

    private static final Pattern REGEX = Pattern.compile("([LR])([0-9]+)");
    private static final Map<Direction, Delta> DIRS = Map.of(
        Direction.UP, Delta.of(-1, 0), Direction.RIGHT, Delta.of(0, 1),
        Direction.DOWN, Delta.of(1, 0), Direction.LEFT, Delta.of(0, -1)
    );
    private static final char WALL = '#';
    
    private final List<String> grid;
    private final List<Move> moves;
    private final Cell start;
    
    private AoC2022_22(final List<String> input, final boolean debug) {
        super(debug);
        final List<List<String>> blocks = toBlocks(input);
        this.grid = blocks.get(0);
        this.moves = REGEX.matcher("R" + blocks.get(1).get(0)).results()
            .map(r -> new Move(
                    Turn.fromString(r.group(1)), Integer.parseInt(r.group(2))))
            .collect(toList());
        trace(this.moves);
        this.start = Cell.at(
            0,
            IntStream.range(0, this.grid.get(0).length())
                .filter(i -> this.grid.get(0).charAt(i) != ' ')
                .findFirst().orElseThrow());
    }
    
    public static final AoC2022_22 create(final List<String> input) {
        return new AoC2022_22(input, false);
    }

    public static final AoC2022_22 createDebug(final List<String> input) {
        return new AoC2022_22(input, true);
    }
    
    private int ans(final int row, final int col, final Direction facing) {
        return 1000 * (row + 1)
                + 4 * (col + 1)
                + List.of(Direction.RIGHT, Direction.DOWN,
                          Direction.LEFT, Direction.UP).indexOf(facing);
    }
    
    @Override
    public Integer solvePart1() {
        int row = this.start.getRow();
        int col = this.start.getCol();
        trace(Cell.at(row, col));
        Direction facing = Direction.UP;
        final Map<Integer, List<Integer>> rowsCache = new HashMap<>();
        final Map<Integer, List<Integer>> colsCache = new HashMap<>();
        for (final Move move : this.moves) {
            facing = facing.turn(move.turn);
            for (int i = 0; i < move.steps; i++) {
                final Delta d = DIRS.get(facing);
                int rr = row + d.dr;
                int cc = col + d.dc;
                if (facing == Direction.UP || facing == Direction.DOWN) {
                    final List<Integer> rows = rowsCache.computeIfAbsent(
                            col ,
                            c -> IntStream.range(0, this.grid.size())
                                .filter(r -> c < this.grid.get(r).length())
                                .filter(r -> this.grid.get(r).charAt(c) != ' ')
                                .boxed().collect(toList()));
                    if (rr < rows.get(0)) {
                        rr = last(rows);
                    } else if (rr > last(rows)) {
                        rr = rows.get(0);
                    }
                } else {
                    final List<Integer> cols = colsCache.computeIfAbsent(
                            row,
                            r -> IntStream.range(0, this.grid.get(r).length())
                                .filter(c -> this.grid.get(r).charAt(c) != ' ')
                                .boxed().collect(toList()));
                    if (cc < cols.get(0)) {
                        cc = last(cols);
                    } else if (cc > last(cols)) {
                        cc = cols.get(0);
                    }
                }
                if (this.grid.get(rr).charAt(cc) == WALL) {
                    break;
                } else {
                    row = rr;
                    col = cc;
                }
                trace(Cell.at(row, col));
            }
        }
        return ans(row, col, facing);
    }
    
    //  cube layout:  |1|2|
    //                |3|
    //              |4|5|
    //              |6|
    @Override
    public Integer solvePart2() {
        final long area = this.grid.stream()
            .flatMap(line -> asCharacterStream(line).filter(ch -> ch != ' '))
            .count();
        final int size = (int) Math.sqrt(area / 6);
        int row = this.start.getRow();
        int col = this.start.getCol();
        Direction facing = Direction.UP;
        for (final Move move : this.moves) {
            facing = facing.turn(move.turn);
            for (int i = 0; i < move.steps; i++) {
                final Delta d = DIRS.get(facing);
                int rr = row + d.dr;
                int cc = col + d.dc;
                Direction ff = facing;
                if (rr < 0 && size <= cc && cc < 2 * size && ff == Direction.UP) {
                    // top edge 1
                    rr = cc + 2 * size;
                    cc = 0;
                    ff = Direction.RIGHT;
                } else if (cc < size && 0 <= rr && rr < size && ff == Direction.LEFT) {
                    // left edge 1
                    rr = 3 * size - 1 - rr;
                    cc = 0;
                    ff = Direction.RIGHT;
                } else if (rr < 0 && 2 * size <= cc && cc < 3 * size && ff == Direction.UP) {
                    // top edge 2
                    rr = 4 * size - 1;
                    cc -= 2 * size;
                    ff = Direction.UP;
                } else if (cc == 3 * size && 0 <= rr && rr < size && ff == Direction.RIGHT) {
                    // right edge 2
                    rr = 3 * size - 1 - rr;
                    cc = 2 * size - 1;
                    ff = Direction.LEFT;
                } else if (rr == size && 2 * size <= cc && cc < 3 * size && ff == Direction.DOWN) {
                    // bottom edge 2
                    rr = cc - size;
                    cc = 2 * size - 1;
                    ff = Direction.LEFT;
                } else if (cc < size && size <= rr && rr < 2 * size && ff == Direction.LEFT) {
                    // left edge 3
                    cc = rr - size;
                    rr = 2 * size;
                    ff = Direction.DOWN;
                } else if (cc == 2 * size && size <= rr && rr < 2 * size && ff == Direction.RIGHT) {
                    // right edge 3
                    cc = rr + size;
                    rr = size - 1;
                    ff = Direction.UP;
                } else if (cc < 0 && 2 * size <= rr && rr < 3 * size && ff == Direction.LEFT) {
                    // left edge 4
                    rr = 3 * size - 1 - rr;
                    cc = size;
                    ff = Direction.RIGHT;
                } else if (rr < 2 * size && 0 <= cc && cc < size && ff ==  Direction.UP) {
                    // top edge 4
                    rr = cc + size;
                    cc = size;
                    ff = Direction.RIGHT;
                } else if (cc == 2 * size && 2 * size <= rr && rr < 3 * size && ff == Direction.RIGHT) {
                    // right edge 5
                    rr = 3 * size - 1 - rr;
                    cc = 3 * size - 1;
                    ff = Direction.LEFT;
                } else if (rr == 3 * size && size <= cc && cc < 2 * size && ff == Direction.DOWN) {
                    // bottom edge 5
                    rr = 2 * size + cc;
                    cc = size - 1;
                    ff = Direction.LEFT;
                } else if (cc < 0 && 3 * size <= rr && rr < 4 * size && ff == Direction.LEFT) {
                    // left edge 6
                    cc = rr - 2 * size;
                    rr = 0;
                    ff = Direction.DOWN;
                } else if (cc == size && 3 * size <= rr && rr < 4 * size && ff == Direction.RIGHT) {
                    // right edge 6
                    cc = rr - 2 * size;
                    rr = 3 * size - 1;
                    ff = Direction.UP;
                } else if (rr == 4 * size && 0 <= cc && cc < size && ff == Direction.DOWN) {
                    // bottom edge 6
                    rr = 0;
                    cc += 2 * size;
                    ff = Direction.DOWN;
                }
                if (this.grid.get(rr).charAt(cc) == WALL) {
                    break;
                } else {
                    row = rr;
                    col = cc;
                    facing = ff;
                }
            }
        }
        return ans(row, col, facing);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_22.createDebug(TEST).solvePart1() == 6032;
//        assert AoC2022_22.createDebug(TEST).solvePart2() == 0;

        final Puzzle puzzle = Aocd.puzzle(2022, 22);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_22.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_22.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "        ...#\r\n" +
        "        .#..\r\n" +
        "        #...\r\n" +
        "        ....\r\n" +
        "...#.......#\r\n" +
        "........#...\r\n" +
        "..#....#....\r\n" +
        "..........#.\r\n" +
        "        ...#....\r\n" +
        "        .....#..\r\n" +
        "        .#......\r\n" +
        "        ......#.\r\n" +
        "\r\n" +
        "10R5L5R10L4R5L5"
    );
    
    @RequiredArgsConstructor
    @ToString
    private static final class Move {
        private final Turn turn;
        private final int steps;
    }
    
    @RequiredArgsConstructor(staticName = "of")
    private static final class Delta {
        private final int dr;
        private final int dc;
    }
}

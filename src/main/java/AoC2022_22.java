import static com.github.pareronia.aoc.Utils.last;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2022_22 extends AoCBase {

    private enum Direction { NORTH, EAST, SOUTH, WEST };

    private static final Pattern REGEX = Pattern.compile("([LR])([0-9]+)");
    private static final Map<Direction, Delta> DIRS = Map.of(
        Direction.NORTH, Delta.of(-1, 0), Direction.EAST, Delta.of(0, 1),
        Direction.SOUTH, Delta.of(1, 0), Direction.WEST, Delta.of(0, -1)
    );
    private static final Map<String, Map<Direction, Direction>> TURNS = Map.of(
        "R",
        Map.of(Direction.NORTH, Direction.EAST, Direction.EAST, Direction.SOUTH,
               Direction.SOUTH, Direction.WEST, Direction.WEST, Direction.NORTH),
        "L",
        Map.of(Direction.NORTH, Direction.WEST, Direction.EAST, Direction.NORTH,
               Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.SOUTH)
    );
    private static final char WALL = '#';
    
    private final List<String> grid;
    private final List<Move> moves;
    
    private AoC2022_22(final List<String> input, final boolean debug) {
        super(debug);
        final List<List<String>> blocks = toBlocks(input);
        this.grid = blocks.get(0);
        this.moves = REGEX.matcher("R" + blocks.get(1).get(0)).results()
            .map(r -> new Move(r.group(1), Integer.parseInt(r.group(2))))
            .collect(toList());
        trace(this.moves);
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
                + List.of(Direction.EAST, Direction.SOUTH,
                          Direction.WEST, Direction.NORTH).indexOf(facing);
    }
    
    @Override
    public Integer solvePart1() {
        final int size = this.grid.size() / 3;
        int row = 0;
        int col = 2 * size;
        trace(Cell.at(row, col));
        Direction facing = Direction.NORTH;
        for (final Move move : this.moves) {
            facing = TURNS.get(move.turn).get(facing);
            for (int i = 0; i < move.steps; i++) {
                final Delta d = DIRS.get(facing);
                int rr = row + d.dr;
                int cc = col + d.dc;
                if (facing == Direction.NORTH || facing == Direction.SOUTH) {
                    final int theCol = col;
                    final List<Integer> rows = IntStream.range(0, this.grid.size())
                        .filter(r -> theCol < this.grid.get(r).length())
                        .filter(r -> this.grid.get(r).charAt(theCol) != ' ')
                        .boxed().collect(toList());
                    if (rr < rows.get(0)) {
                        rr = last(rows);
                    } else if (rr > last(rows)) {
                        rr = rows.get(0);
                    }
                } else {
                    final int theRow = row;
                    final List<Integer> cols = IntStream.range(0, this.grid.get(row).length())
                        .filter(c -> this.grid.get(theRow).charAt(c) != ' ')
                        .boxed().collect(toList());
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
    
    @Override
    public Integer solvePart2() {
        return 0;
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
        private final String turn;
        private final int steps;
    }
    
    @RequiredArgsConstructor(staticName = "of")
    private static final class Delta {
        private final int dr;
        private final int dc;
    }
}

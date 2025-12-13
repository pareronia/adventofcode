import static com.github.pareronia.aoc.StringOps.toBlocks;
import static com.github.pareronia.aoc.Utils.asCharacterStream;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage", "PMD.GodClass"})
public final class AoC2022_22 extends SolutionBase<AoC2022_22.Input, Integer, Integer> {

    private static final char WALL = '#';

    private AoC2022_22(final boolean debug) {
        super(debug);
    }

    public static AoC2022_22 create() {
        return new AoC2022_22(false);
    }

    public static AoC2022_22 createDebug() {
        return new AoC2022_22(true);
    }

    @Override
    protected Input parseInput(final List<String> inputs) {
        return Input.fromInput(inputs);
    }

    private int ans(final int row, final int col, final Direction facing) {
        return 1000 * (row + 1)
                + 4 * (col + 1)
                + List.of(
                                Direction.RIGHT, Direction.DOWN,
                                Direction.LEFT, Direction.UP)
                        .indexOf(facing);
    }

    @Override
    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.GuardLogStatement"})
    public Integer solvePart1(final Input input) {
        int row = input.start().getRow();
        int col = input.start().getCol();
        trace(Cell.at(row, col));
        Direction facing = Direction.UP;
        final Map<Integer, List<Integer>> rowsCache = new HashMap<>();
        final Map<Integer, List<Integer>> colsCache = new HashMap<>();
        for (final Move move : input.moves()) {
            facing = facing.turn(move.turn);
            for (int i = 0; i < move.steps; i++) {
                final Cell moved = Cell.at(row, col).at(facing);
                int rr = moved.getRow();
                int cc = moved.getCol();
                if (facing == Direction.UP || facing == Direction.DOWN) {
                    final List<Integer> rows =
                            rowsCache.computeIfAbsent(
                                    col,
                                    c ->
                                            IntStream.range(0, input.grid().size())
                                                    .filter(r -> c < input.grid().get(r).length())
                                                    .filter(
                                                            r ->
                                                                    input.grid().get(r).charAt(c)
                                                                            != ' ')
                                                    .boxed()
                                                    .toList());
                    if (rr < rows.get(0)) {
                        rr = rows.getLast();
                    } else if (rr > rows.getLast()) {
                        rr = rows.get(0);
                    }
                } else {
                    final List<Integer> cols =
                            colsCache.computeIfAbsent(
                                    row,
                                    r ->
                                            IntStream.range(0, input.grid().get(r).length())
                                                    .filter(
                                                            c ->
                                                                    input.grid().get(r).charAt(c)
                                                                            != ' ')
                                                    .boxed()
                                                    .toList());
                    if (cc < cols.get(0)) {
                        cc = cols.getLast();
                    } else if (cc > cols.getLast()) {
                        cc = cols.get(0);
                    }
                }
                if (input.grid().get(rr).charAt(cc) == WALL) {
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
    @SuppressWarnings({
        "PMD.NcssCount",
        "PMD.NPathComplexity",
        "PMD.CyclomaticComplexity",
        "PMD.CognitiveComplexity"
    })
    public Integer solvePart2(final Input input) {
        final long area =
                input.grid().stream()
                        .flatMap(line -> asCharacterStream(line).filter(ch -> ch != ' '))
                        .count();
        final int size = (int) Math.sqrt(area / 6);
        int row = input.start().getRow();
        int col = input.start().getCol();
        Direction facing = Direction.UP;
        for (final Move move : input.moves()) {
            facing = facing.turn(move.turn);
            for (int i = 0; i < move.steps; i++) {
                final Cell moved = Cell.at(row, col).at(facing);
                int rr = moved.getRow();
                int cc = moved.getCol();
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
                } else if (rr < 2 * size && 0 <= cc && cc < size && ff == Direction.UP) {
                    // top edge 4
                    rr = cc + size;
                    cc = size;
                    ff = Direction.RIGHT;
                } else if (cc == 2 * size
                        && 2 * size <= rr
                        && rr < 3 * size
                        && ff == Direction.RIGHT) {
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
                if (input.grid().get(rr).charAt(cc) == WALL) {
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

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "6032"),
        //    	@Sample(method = "part2",input=TEST,expected = "0"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
                    ...#
                    .#..
                    #...
                    ....
            ...#.......#
            ........#...
            ..#....#....
            ..........#.
                    ...#....
                    .....#..
                    .#......
                    ......#.

            10R5L5R10L4R5L5
            """;

    record Move(Turn turn, int steps) {}

    record Input(List<String> grid, List<Move> moves, Cell start) {

        private static final Pattern REGEX = Pattern.compile("([LR])([0-9]+)");

        public static Input fromInput(final List<String> input) {
            final List<List<String>> blocks = toBlocks(input);
            final List<String> grid = blocks.get(0);
            final List<Move> moves =
                    REGEX.matcher("R" + blocks.get(1).get(0))
                            .results()
                            .map(
                                    r ->
                                            new Move(
                                                    Turn.fromString(r.group(1)),
                                                    Integer.parseInt(r.group(2))))
                            .toList();
            final Cell start =
                    Cell.at(
                            0,
                            IntStream.range(0, grid.get(0).length())
                                    .filter(i -> grid.get(0).charAt(i) != ' ')
                                    .findFirst()
                                    .orElseThrow());
            return new Input(grid, moves, start);
        }
    }
}

import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_15
        extends SolutionBase<AoC2024_15.Input, Integer, Integer> {
    
    private static final char FLOOR = '.';
    private static final char WALL = '#';
    private static final char ROBOT = '@';
    private static final char BOX = 'O';
    private static final char BIG_BOX_LEFT = '[';
    private static final char BIG_BOX_RIGHT = ']';
    
    private AoC2024_15(final boolean debug) {
        super(debug);
    }
    
    public static AoC2024_15 create() {
        return new AoC2024_15(false);
    }
    
    public static AoC2024_15 createDebug() {
        return new AoC2024_15(true);
    }
    
    @Override
    protected Input parseInput(final List<String> inputs) {
        final List<List<String>> blocks = StringOps.toBlocks(inputs);
        final List<Direction> dirs = Utils.asCharacterStream(
                        blocks.get(1).stream().collect(joining()))
                .map(Direction::fromChar)
                .toList();
        return new Input(new GridSupplier(blocks.get(0)), dirs);
    }

    private int solve(
            final CharGrid grid,
            final List<Direction> dirs,
            final GetToMove getToMove
    ) {
        Cell robot = grid.getAllEqualTo(ROBOT).findFirst().orElseThrow();
        for (final Direction dir : dirs) {
            final List<Cell> toMove = getToMove.getToMove(grid, robot, dir);
            if (toMove.isEmpty()) {
                continue;
            }
            final Map<Cell, Character> vals = toMove.stream()
                .collect(toMap(tm -> tm, grid::getValue));
            robot = robot.at(dir);
            for (final Cell cell : toMove) {
                grid.setValue(cell, FLOOR);
            }
            for (final Cell cell : toMove) {
                grid.setValue(cell.at(dir), vals.get(cell));
            }
        }
        return grid.findAllMatching(Set.of(BOX, BIG_BOX_LEFT)::contains)
                .mapToInt(cell -> cell.getRow() * 100 + cell.getCol())
                .sum();
    }
    
    @Override
    public Integer solvePart1(final Input input) {
        final GetToMove getToMove = (grid, robot, dir) -> {
            final List<Cell> toMove = new ArrayList<>(List.of(robot));
            final Deque<Cell> q = new ArrayDeque<>(toMove);
            while (!q.isEmpty()) {
                final Cell cell = q.pop();
                final Cell nxt = cell.at(dir);
                if (q.contains(nxt)) {
                    continue;
                }
                switch (grid.getValue(nxt)) {
                    case WALL:
                        return List.of();
                    case BOX:
                        q.add(nxt);
                        toMove.add(nxt);
                        break;
                }
            }
            return toMove;
        };
        
        return solve(input.grid.getGrid(), input.dirs, getToMove);
    }
    
    @Override
    public Integer solvePart2(final Input input) {
        final GetToMove getToMove = (grid, robot, dir) -> {
            final List<Cell> toMove = new ArrayList<>(List.of(robot));
            final Deque<Cell> q = new ArrayDeque<>(toMove);
            while (!q.isEmpty()) {
                final Cell cell = q.pop();
                final Cell nxt = cell.at(dir);
                if (q.contains(nxt)) {
                    continue;
                }
                switch (grid.getValue(nxt)) {
                    case WALL:
                        return List.of();
                    case BIG_BOX_LEFT:
                        final Cell right = nxt.at(Direction.RIGHT);
                        q.add(nxt);
                        q.add(right);
                        toMove.add(nxt);
                        toMove.add(right);
                        break;
                    case BIG_BOX_RIGHT:
                        final Cell left = nxt.at(Direction.LEFT);
                        q.add(nxt);
                        q.add(left);
                        toMove.add(nxt);
                        toMove.add(left);
                        break;
                }
            }
            return toMove;
        };
        
        return solve(input.grid.getWideGrid(), input.dirs, getToMove);
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "2028"),
        @Sample(method = "part1", input = TEST2, expected = "10092"),
        @Sample(method = "part2", input = TEST2, expected = "9021"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2024_15.create().run();
    }

    private static final String TEST1 = """
            ########
            #..O.O.#
            ##@.O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########

            <^^>>>vv<v>>v<<
            """;
    private static final String TEST2 = """
            ##########
            #..O..O.O#
            #......O.#
            #.OO..O.O#
            #..O@..O.#
            #O#..O...#
            #O..O..O.#
            #.OO.O.OO#
            #....O...#
            ##########

            <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
            vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
            ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
            <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
            ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
            ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
            >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
            <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
            ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
            v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
            """;

    private interface GetToMove {
        List<Cell> getToMove(CharGrid grid, Cell robot, Direction dir);
    }
    
    record GridSupplier(List<String> gridIn) {
        private static final Map<Character, char[]> SCALE_UP = Map.of(
                FLOOR, new char[] { FLOOR, FLOOR },
                WALL, new char[] { WALL, WALL },
                ROBOT, new char[] { ROBOT, FLOOR },
                BOX, new char[] { BIG_BOX_LEFT, BIG_BOX_RIGHT }
        );

        public CharGrid getGrid() {
            return CharGrid.from(this.gridIn);
        }
        
        public CharGrid getWideGrid() {
            final char[][] chars = new char[this.gridIn.size()][];
            for (final int r : range(this.gridIn.size())) {
                final String line = this.gridIn.get(r);
                final char[] row = new char[2 * line.length()];
                for(final int c : range(line.length())) {
                    final char[] s = SCALE_UP.get(line.charAt(c));
                    row[2 * c] = s[0];
                    row[2 * c + 1] = s[1];
                }
                chars[r] = row;
            }
            return new CharGrid(chars);
        }
    }
    
    record Input(GridSupplier grid, List<Direction> dirs) {}
}
import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
        return new Input(blocks.get(0), dirs);
    }

    private int solve(
            final Warehouse warehouse, final List<Direction> dirs
    ) {
        Cell robot = warehouse.grid().getAllEqualTo(ROBOT)
                        .findFirst().orElseThrow();
        for (final Direction dir : dirs) {
            final List<Cell> toMove = warehouse.getToMove(robot, dir);
            if (toMove.isEmpty()) {
                continue;
            }
            final Map<Cell, Character> vals = toMove.stream()
                .collect(toMap(tm -> tm, warehouse.grid::getValue));
            robot = robot.at(dir);
            for (final Cell cell : toMove) {
                warehouse.grid.setValue(cell, FLOOR);
            }
            for (final Cell cell : toMove) {
                warehouse.grid.setValue(cell.at(dir), vals.get(cell));
            }
        }
        return warehouse.getBoxes()
                .mapToInt(cell -> cell.getRow() * 100 + cell.getCol())
                .sum();
    }
    
    @Override
    public Integer solvePart1(final Input input) {
        return solve(
                Warehouse.create(Warehouse.Type.WAREHOUSE_1, input.grid),
                input.dirs);
    }
    
    @Override
    public Integer solvePart2(final Input input) {
        return solve(
                Warehouse.create(Warehouse.Type.WAREHOUSE_2, input.grid),
                input.dirs);
    }

    record Warehouse(Type type, CharGrid grid) {
        private static final Map<Character, char[]> SCALE_UP = Map.of(
                FLOOR, new char[] { FLOOR, FLOOR },
                WALL, new char[] { WALL, WALL },
                ROBOT, new char[] { ROBOT, FLOOR },
                BOX, new char[] { BIG_BOX_LEFT, BIG_BOX_RIGHT }
        );

        enum Type {
            WAREHOUSE_1, WAREHOUSE_2;
        }
        
        public static Warehouse create(
                final Type type, final List<String> gridIn
        ) {
            final CharGrid grid = switch (type) {
            case WAREHOUSE_1: yield CharGrid.from(gridIn);
            case WAREHOUSE_2 : {
                final char[][] chars = new char[gridIn.size()][];
                for (final int r : range(gridIn.size())) {
                    final String line = gridIn.get(r);
                    final char[] row = new char[2 * line.length()];
                    for(final int c : range(line.length())) {
                        final char[] s = SCALE_UP.get(line.charAt(c));
                        row[2 * c] = s[0];
                        row[2 * c + 1] = s[1];
                    }
                    chars[r] = row;
                }
                yield new CharGrid(chars);
            }
            };
            return new Warehouse(type, grid);
        }

        public List<Cell> getToMove(final Cell robot, final Direction dir) {
            final List<Cell> toMove = new ArrayList<>(List.of(robot));
            final Deque<Cell> q = new ArrayDeque<>(toMove);
            while (!q.isEmpty()) {
                final Cell cell = q.pop();
                final Cell nxt = cell.at(dir);
                if (q.contains(nxt)) {
                    continue;
                }
                final char nxtVal = grid.getValue(nxt);
                if (nxtVal == WALL) {
                    return List.of();
                }
                switch(this.type) {
                    case WAREHOUSE_1: {
                        if (nxtVal == BOX) {
                            q.add(nxt);
                            toMove.add(nxt);
                        }
                    }
                    case WAREHOUSE_2: {
                        final Cell also;
                        if (nxtVal == BIG_BOX_LEFT) {
                            also = nxt.at(Direction.RIGHT);
                        } else if (nxtVal == BIG_BOX_RIGHT) {
                            also = nxt.at(Direction.LEFT);
                        } else {
                            continue;
                        }
                        q.add(nxt);
                        q.add(also);
                        toMove.add(nxt);
                        toMove.add(also);
                    }
                }
            }
            return toMove;
        }

        public Stream<Cell> getBoxes() {
            return switch(this.type) {
                case WAREHOUSE_1 -> grid.getAllEqualTo(BOX);
                case WAREHOUSE_2 -> grid.getAllEqualTo(BIG_BOX_LEFT);
            };
        }
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

    record Input(List<String> grid, List<Direction> dirs) {}
}
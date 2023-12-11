import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.SetUtils;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.graph.BFS;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_10
        extends SolutionBase<CharGrid, Integer, Integer> {
    
    private AoC2023_10(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_10 create() {
        return new AoC2023_10(false);
    }
    
    public static AoC2023_10 createDebug() {
        return new AoC2023_10(true);
    }
    
    @Override
    protected CharGrid parseInput(final List<String> inputs) {
        return CharGrid.from(inputs);
    }

    @Override
    public Integer solvePart1(final CharGrid grid) {
        return LoopFinder.findLoop(grid).size() / 2;
    }
    
    @Override
    public Integer solvePart2(final CharGrid grid) {
        final Set<Cell> loop = LoopFinder.findLoop(grid);
        return EnlargeGridInsideFinder.countInside(grid, loop);
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "4"),
        @Sample(method = "part1", input = TEST2, expected = "8"),
        @Sample(method = "part2", input = TEST1, expected = "1"),
        @Sample(method = "part2", input = TEST2, expected = "1"),
        @Sample(method = "part2", input = TEST3, expected = "4"),
        @Sample(method = "part2", input = TEST4, expected = "8"),
        @Sample(method = "part2", input = TEST5, expected = "10"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_10.create().run();
    }
    
    private static final class LoopFinder {
        private static final Map<Direction, Map<Character, Direction>> TILES = Map.of(
            Direction.UP, Map.of('|', Direction.UP, '7', Direction.LEFT, 'F', Direction.RIGHT),
            Direction.RIGHT, Map.of('-', Direction.RIGHT, 'J', Direction.UP, '7', Direction.DOWN),
            Direction.DOWN, Map.of('|', Direction.DOWN, 'J', Direction.LEFT, 'L', Direction.RIGHT),
            Direction.LEFT, Map.of('-', Direction.LEFT, 'L', Direction.UP, 'F', Direction.DOWN)
        );
        
        public static Set<Cell> findLoop(final CharGrid grid) {
            final Cell start = grid.getAllEqualTo('S').findFirst().orElseThrow();
            final Deque<State> q = new ArrayDeque<>();
            final Set<Node> seen = new HashSet<>();
            final Map<Node, Node> parent = new HashMap<>();
            for (final Direction dir : Direction.CAPITAL) {
                final Node node = new Node(start, dir);
                q.add(new State(0, node));
                seen.add(node);
            }
            while (!q.isEmpty()) {
                final State state = q.poll();
                final Cell curr = state.node().cell();
                final Direction dir = state.node().direction();
                final Cell n = curr.at(dir);
                if (n.equals(start)) {
                    final Set<Cell> path = new HashSet<>();
                    path.add(curr);
                    Node c = new Node(curr, dir);
                    while (parent.containsKey(c)) {
                        c = parent.get(c);
                        path.add(c.cell());
                    }
                    return path;
                }
                if (grid.isInBounds(n)) {
                    final Character val = grid.getValue(n);
                    if (TILES.get(dir).containsKey(val)) {
                        final Direction newDir = TILES.get(dir).get(val);
                        final Node next = new Node(n, newDir);
                        if (seen.contains(next)) {
                            continue;
                        }
                        seen.add(next);
                        parent.put(next, state.node());
                        q.add(new State(state.distance() + 1, next));
                    }
                }
                
            }
            throw new IllegalStateException("Unsolvable");
        }
        
        record Node(Cell cell, Direction direction) {}
        
        record State(int distance, Node node) {}
    }
    
    private static final class EnlargeGridInsideFinder {
        private static final Map<Character, CharGrid> XGRIDS = Map.of(
            '|', CharGrid.from(List.of(".#.", ".#.", ".#.")),
            '-', CharGrid.from(List.of("...", "###", "...")),
            'L', CharGrid.from(List.of(".#.", ".##", "...")),
            'J', CharGrid.from(List.of(".#.", "##.", "...")),
            '7', CharGrid.from(List.of("...", "##.", ".#.")),
            'F', CharGrid.from(List.of("...", ".##", ".#.")),
            '.', CharGrid.from(List.of("...", "...", "...")),
            'S', CharGrid.from(List.of(".S.", "SSS", ".S."))
        );
        
        public static int countInside(final CharGrid grid, final Set<Cell> loop) {
            final CharGrid[][] grids = IntStream.range(0, grid.getHeight())
                .mapToObj(r -> range(grid.getWidth()).intStream()
                    .mapToObj(c -> {
                        if (loop.contains(Cell.at(r, c))) {
                            return XGRIDS.get(grid.getValue(Cell.at(r, c)));
                        } else {
                            return XGRIDS.get('.');
                        }
                    })
                    .toArray(CharGrid[]::new))
                .toArray(CharGrid[][]::new);
            final CharGrid xgrid = CharGrid.merge(grids);
            final Set<Cell> newLoop = xgrid
                    .findAllMatching(Set.of('S', '#')::contains)
                    .collect(toSet());
            final Set<Cell> outside = BFS.floodFill(
                Cell.at(0, 0),
                cell -> xgrid.getCapitalNeighbours(cell).filter(n -> !newLoop.contains(n)));
            final Set<Cell> inside = Stream.of(
                xgrid.getCells().collect(toSet()),
                outside,
                newLoop
            ).reduce(SetUtils::difference).get();
            return (int) grid.getCells()
                .filter(cell -> range(3).intStream().boxed()
                    .flatMap(r -> range(3).intStream()
                        .mapToObj(c -> Cell.at(3 * cell.getRow() + r, 3 * cell.getCol() + c)))
                .allMatch(inside::contains))
                .count();
        }
    }

    private static final String TEST1 = """
            -L|F7
            7S-7|
            L|7||
            -L-J|
            L|-JF
            """;
    private static final String TEST2 = """
            7-F7-
            .FJ|7
            SJLL7
            |F--J
            LJ.LJ
            """;
    private static final String TEST3 = """
            ...........
            .S-------7.
            .|F-----7|.
            .||.....||.
            .||.....||.
            .|L-7.F-J|.
            .|..|.|..|.
            .L--J.L--J.
            ...........
            """;
    private static final String TEST4 = """
            .F----7F7F7F7F-7....
            .|F--7||||||||FJ....
            .||.FJ||||||||L7....
            FJL7L7LJLJ||LJ.L-7..
            L--J.L7...LJS7F-7L7.
            ....F-J..F7FJ|L7L7L7
            ....L7.F7||L7|.L7L7|
            .....|FJLJ|FJ|F7|.LJ
            ....FJL-7.||.||||...
            ....L---J.LJ.LJLJ...
            """;
    private static final String TEST5 = """
            FF7FSF7F7F7F7F7F---7
            L|LJ||||||||||||F--J
            FL-7LJLJ||||||LJL-77
            F--JF--7||LJLJ7F7FJ-
            L---JF-JLJ.||-FJLJJ7
            |F|F-JF---7F7-L7L|7|
            |FFJF7L7F-JF7|JL---7
            7-L-JL7||F7|L7F-7F7|
            L.L7LFJ|||||FJL7||LJ
            L7JLJL-JLJLJL--JLJ.L
            """;
}

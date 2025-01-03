import static com.github.pareronia.aoc.IterTools.product;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aoc.graph.Dijkstra;
import com.github.pareronia.aoc.graph.Dijkstra.AllResults;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_16
        extends SolutionBase<AoC2024_16.ReindeerMaze, Integer, Integer> {
    
    private AoC2024_16(final boolean debug) {
        super(debug);
    }
    
    public static AoC2024_16 create() {
        return new AoC2024_16(false);
    }
    
    public static AoC2024_16 createDebug() {
        return new AoC2024_16(true);
    }
    
    @Override
    protected ReindeerMaze parseInput(final List<String> inputs) {
        return ReindeerMaze.fromInput(inputs);
    }

    @Override
    public Integer solvePart1(final ReindeerMaze maze) {
        return (int) Dijkstra.distance(
                new State(maze.start, Direction.RIGHT),
                state -> state.cell.equals(maze.end),
                state -> maze.adjacent(state),
                (curr, next) -> curr.direction == next.direction ? 1 : 1001);
    }
    
    @Override
    public Integer solvePart2(final ReindeerMaze maze) {
        final AllResults<State> result = Dijkstra.all(
                new State(maze.start, Direction.RIGHT),
                state -> state.cell.equals(maze.end),
                state -> maze.adjacent(state),
                (curr, next) -> curr.direction == next.direction ? 1 : 1001);
        return (int) product(List.of(maze.end), Direction.CAPITAL).stream()
            .flatMap(pp -> result.getPaths(new State(pp.first(), pp.second())).stream())
            .flatMap(List::stream)
            .map(State::cell)
            .distinct()
            .count();
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "7036"),
        @Sample(method = "part1", input = TEST2, expected = "11048"),
        @Sample(method = "part2", input = TEST1, expected = "45"),
        @Sample(method = "part2", input = TEST2, expected = "64"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2024_16.create().run();
    }

    private static final String TEST1 = """
            ###############
            #.......#....E#
            #.#.###.#.###.#
            #.....#.#...#.#
            #.###.#####.#.#
            #.#.#.......#.#
            #.#.#####.###.#
            #...........#.#
            ###.#.#####.#.#
            #...#.....#.#.#
            #.#.#.###.#.#.#
            #.....#...#.#.#
            #.###.#.#.#.#.#
            #S..#.....#...#
            ###############
            """;
    private static final String TEST2 = """
            #################
            #...#...#...#..E#
            #.#.#.#.#.#.#.#.#
            #.#.#.#...#...#.#
            #.#.#.#.###.#.#.#
            #...#.#.#.....#.#
            #.#.#.#.#.#####.#
            #.#...#.#.#.....#
            #.#.#####.#.###.#
            #.#.#.......#...#
            #.#.###.#####.###
            #.#.#...#.....#.#
            #.#.#.#####.###.#
            #.#.#.........#.#
            #.#.#.#########.#
            #S#.............#
            #################
            """;
    
    record State(Cell cell, Direction direction) {}

    record ReindeerMaze(char[][] grid, Cell start, Cell end) {

        public static ReindeerMaze fromInput(final List<String> strings) {
            final int h = strings.size();
            final int w = strings.get(0).length();
            final char[][] cells = new char[h][w];
            for (int i = 0; i < h; i++) {
                cells[i] = strings.get(i).toCharArray();
            }
            return new ReindeerMaze(cells, Cell.at(h - 2, 1), Cell.at(1, w - 2));
        }
        
        public Stream<State> adjacent(final State state) {
            return Set.of(
                        state.direction,
                        state.direction.turn(Turn.RIGHT),
                        state.direction.turn(Turn.LEFT)
                    ).stream()
                    .map(dir -> new State(state.cell.at(dir), dir))
                    .filter(st -> grid[st.cell.getRow()][st.cell.getCol()] != '#');
        }
    }
}
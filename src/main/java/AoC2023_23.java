import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.solution.Logger;
import com.github.pareronia.aoc.solution.LoggerEnabled;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_23
        extends SolutionBase<CharGrid, Integer, Integer> {
    
    private AoC2023_23(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_23 create() {
        return new AoC2023_23(false);
    }
    
    public static AoC2023_23 createDebug() {
        return new AoC2023_23(true);
    }
    
    @Override
    protected CharGrid parseInput(final List<String> inputs) {
        return CharGrid.from(inputs);
    }

    @Override
    public Integer solvePart1(final CharGrid grid) {
        return new PathFinder(grid, this.logger)
                .findLongestHikeLengthWithDownwardSlopesOnly();
    }
    
    @Override
    public Integer solvePart2(final CharGrid grid) {
        return new PathFinder(grid, this.logger).findLongestHikeLength();
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "94"),
        @Sample(method = "part2", input = TEST, expected = "154"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_23.create().run();
    }
    
    static final class PathFinder implements LoggerEnabled {
        private final CharGrid grid;
        private final Cell start;
        private final Cell end;
        private final Logger logger;

        protected PathFinder(final CharGrid grid, final Logger logger) {
            this.grid = grid;
            this.start = Cell.at(0, 1);
            this.end = Cell.at(grid.getMaxRowIndex(), grid.getMaxColIndex() - 1);
            this.logger = logger;
        }
 
        @Override
        public Logger getLogger() {
            return this.logger;
        }

        public int findLongestHikeLength() {
            final Set<Cell> pois = this.findPois();
            log(pois);
            final Map<Cell, Set<Edge>> graph = this.buildGraph(pois, false);
            log(graph);
            final int distFromStart = graph.get(this.start).iterator().next().weight;
            final int distToEnd = graph.get(this.end).iterator().next().weight;
            final Cell newStart = graph.entrySet().stream()
                .filter(e -> e.getValue().contains(new Edge(this.start, distFromStart)))
                .map(Entry::getKey)
                .findFirst().orElseThrow();
            final Cell newEnd = graph.entrySet().stream()
                .filter(e -> e.getValue().contains(new Edge(this.end, distToEnd)))
                .map(Entry::getKey)
                .findFirst().orElseThrow();
            return distFromStart + distToEnd
                + this.findLongest(graph, newStart, newEnd, new HashSet<>());
        }
        
        public int findLongestHikeLengthWithDownwardSlopesOnly() {
            final Set<Cell> pois = this.findPois();
            log(pois);
            final Map<Cell, Set<Edge>> graph = this.buildGraph(pois, true);
            log(graph);
            return this.findLongest(graph, this.start, this.end, new HashSet<>());
        }
        
        private int findLongest(
                final Map<Cell, Set<Edge>> graph,
                final Cell curr,
                final Cell end,
                final Set<Cell> seen
        ) {
            if (curr.equals(end)) {
                return 0;
            }
            int ans = (int) -1e9;
            seen.add(curr);
            for (final Edge e : graph.get(curr)) {
                if (seen.contains(e.vertex)) {
                    continue;
                }
                ans = Math.max(
                    ans,
                    e.weight + this.findLongest(graph, e.vertex, end, seen));
            }
            seen.remove(curr);
            return ans;
        }
        
        private Set<Cell> findPois() {
            final Set<Cell> ans = new HashSet<>();
            this.grid.getCells().forEach(cell -> {
                if (cell.equals(this.start) || cell.equals(this.end)) {
                    ans.add(cell);
                    return;
                }
                if (this.grid.getValue(cell) == '#') {
                    return;
                }
                if (this.grid.getCapitalNeighbours(cell)
                        .filter(n -> this.grid.getValue(n) != '#')
                        .count() > 2) {
                    ans.add(cell);
                }
            });
            return ans;
        }
        
        record Edge(Cell vertex, int weight) {}

        private Map<Cell, Set<Edge>> buildGraph(
                final Set<Cell> pois, final boolean downwardSlopesOnly
        ) {
            record State(Cell cell, int distance) {}
            
            final Map<Cell, Set<Edge>> edges = new HashMap<>();
            for (final Cell poi : pois) {
                final Deque<State> q = new ArrayDeque<>(Set.of(new State(poi, 0)));
                final Set<Cell> seen = new HashSet<>(Set.of(poi));
                while (!q.isEmpty()) {
                    final State node = q.poll();
                    if (pois.contains(node.cell) && !node.cell.equals(poi)) {
                        edges.computeIfAbsent(poi, k -> new HashSet<>())
                            .add(new Edge(node.cell, node.distance));
                        continue;
                    }
                    for (final Direction d : Direction.CAPITAL) {
                        final Cell n = node.cell.at(d);
                        if (!this.grid.isInBounds(n)) {
                            continue;
                        }
                        final char val = this.grid.getValue(n);
                        if (val == '#') {
                            continue;
                        }
                        if (downwardSlopesOnly
                                && Set.of('<', '>', 'v', '^').contains(val)
                                && Direction.fromChar(val) != d) {
                            continue;
                        }
                        if (seen.contains(n)) {
                            continue;
                        }
                        seen.add(n);
                        q.add(new State(n, node.distance + 1));
                    }
                }
            }
            return edges;
        }
    }

    private static final String TEST = """
            #.#####################
            #.......#########...###
            #######.#########.#.###
            ###.....#.>.>.###.#.###
            ###v#####.#v#.###.#.###
            ###.>...#.#.#.....#...#
            ###v###.#.#.#########.#
            ###...#.#.#.......#...#
            #####.#.#.#######.#.###
            #.....#.#.#.......#...#
            #.#####.#.#.#########v#
            #.#...#...#...###...>.#
            #.#.#v#######v###.###v#
            #...#.>.#...>.>.#.###.#
            #####v#.#.###v#.#.###.#
            #.....#...#...#.#.#...#
            #.#########.###.#.#.###
            #...###...#...#...#.###
            ###.###.#.###v#####v###
            #...#...#.#.>.>.#.>.###
            #.###.###.#.###.#.#v###
            #.....###...###...#...#
            #####################.#
            """;
}

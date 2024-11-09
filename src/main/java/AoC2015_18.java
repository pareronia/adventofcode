import static com.github.pareronia.aoc.StringOps.splitLines;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.SetUtils;
import com.github.pareronia.aoc.game_of_life.GameOfLife;
import com.github.pareronia.aoc.game_of_life.GameOfLife.Type;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2015_18 extends SolutionBase<AoC2015_18.FixedGrid, Integer, Integer> {

    private AoC2015_18(final boolean debug) {
        super(debug);
    }

    public static final AoC2015_18 create() {
        return new AoC2015_18(false);
    }

    public static final AoC2015_18 createDebug() {
        return new AoC2015_18(true);
    }

    @Override
    protected FixedGrid parseInput(final List<String> inputs) {
        return FixedGrid.fromInput(inputs);
    }

    @SuppressWarnings("unchecked")
    private int solve1(final FixedGrid grid, final int generations) {
        GameOfLife<Cell> gol = new GameOfLife<>(
                grid,
                GameOfLife.classicRules,
                grid.initialAlive);
        for (int i = 0; i < generations; i++) {
            gol = gol.nextGeneration();
        }
        return gol.alive().size();
    }
    
    @Override
    public Integer solvePart1(final FixedGrid input) {
        return solve1(input, 100);
    }
    
    @SuppressWarnings("unchecked")
    private int solve2(final FixedGrid grid, final int generations) {
        GameOfLife<Cell> gol = new GameOfLife<>(
                grid,
                GameOfLife.classicRules,
                SetUtils.union(grid.initialAlive, grid.stuckPositions));
        for (int i = 0; i < generations; i++) {
            gol = gol.nextGeneration();
            gol = gol.withAlive(SetUtils.union(gol.alive(), grid.stuckPositions));
        }
        return gol.alive().size();
    }
    
    @Override
    public Integer solvePart2(final FixedGrid input) {
        return solve2(input, 100);
    }

    public static void main(final String[] args) throws Exception {
        final AoC2015_18 test = AoC2015_18.createDebug();
        final FixedGrid input = test.parseInput(TEST);
        assert test.solve1(input, 4) == 4;
        assert test.solve2(input, 5) == 17;
        
        AoC2015_18.create().run();
    }
    
    private static final List<String> TEST = splitLines(
            """
                .#.#.#\r
                ...##.\r
                #....#\r
                ..#...\r
                #.#..#\r
                ####.."""
    );

    public static final class FixedGrid implements Type<Cell> {

        private static final char ON = '#';
        
        private final Map<Cell, Set<Cell>> neighboursCache = new HashMap<>();
        private final int height;
        private final int width;
        private final Set<Cell> initialAlive;
        private final Set<Cell> stuckPositions;

        private FixedGrid(
                final int height,
                final int width,
                final Set<Cell> initialAlive,
                final Set<Cell> stuckPositions
        ) {
            this.height = height;
            this.width = width;
            this.initialAlive = initialAlive;
            this.stuckPositions = stuckPositions;
        }

        public static FixedGrid fromInput(final List<String> input) {
            final int height = input.size();
            final int width = input.get(0).length();
            final Set<Cell> alive = IntStream.range(0, height).boxed()
                .flatMap(r -> IntStream.range(0, width).mapToObj(c -> Cell.at(r, c)))
                .filter(c -> input.get(c.getRow()).charAt(c.getCol()) == ON)
                .collect(toSet());
            final Set<Cell> stuckPositions = Set.of(
                    Cell.at(0, 0),
                    Cell.at(height - 1, 0),
                    Cell.at(0, width - 1),
                    Cell.at(height- 1, width - 1));
            return new FixedGrid(height, width, alive, stuckPositions);
        }

        @Override
        public Map<Cell, Long> getNeighbourCounts(final Set<Cell> alive) {
            return alive.stream()
                .flatMap(cell -> neighbours(cell).stream())
                .collect(groupingBy(cell -> cell, counting()));
        }
        
        private Set<Cell> neighbours(final Cell c) {
            return this.neighboursCache.computeIfAbsent(c, this::getNeighbours);
        }
        
        private Set<Cell> getNeighbours(final Cell c) {
            return c.allNeighbours()
                .filter(n -> n.getRow() >= 0)
                .filter(n -> n.getRow() < this.height)
                .filter(n -> n.getCol() >= 0)
                .filter(n -> n.getCol() < this.width)
                .collect(toSet());
        }
    }
}

import static com.github.pareronia.aoc.StringOps.splitLines;
import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.solution.SolutionBase;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

public class AoC2015_18 extends SolutionBase<GameOfLife, Integer, Integer> {
    
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
    protected GameOfLife parseInput(final List<String> inputs) {
        return GameOfLife.fromInput(inputs);
    }

    private final Map<Cell, Set<Cell>> neighboursCache = new HashMap<>();
    private Set<Cell> findNeighbours(final GameOfLife gameOfLife, final Cell c) {
        if (neighboursCache.containsKey(c)) {
            return neighboursCache.get(c);
        }
        final Set<Cell> neighbours = c.allNeighbours()
            .filter(n -> n.getRow() >= 0)
            .filter(n -> n.getRow() < gameOfLife.height)
            .filter(n -> n.getCol() >= 0)
            .filter(n -> n.getCol() < gameOfLife.width)
            .collect(toSet());
        neighboursCache.put(c, neighbours);
        return neighbours;
    }
    
    private void nextGen(final GameOfLife gameOfLife, final Set<Cell> stuckPositions) {
        final Set<Cell> toOn = new HashSet<>();
        final Set<Cell> toOff = new HashSet<>();
        for (final Cell cell : gameOfLife.grid) {
            final Set<Cell> neighbours = findNeighbours(gameOfLife, cell);
            final int neighbours_on = (int) neighbours.stream().filter(gameOfLife.grid::contains).count();
            if (neighbours_on == 2 || neighbours_on == 3 || stuckPositions.contains(cell)) {
                toOn.add(cell);
            } else {
                toOff.add(cell);
            }
            neighbours.stream()
                .filter(n -> !gameOfLife.grid.contains(n))
                .filter(n -> (int) findNeighbours(gameOfLife, n).stream().filter(gameOfLife.grid::contains).count() == 3)
                .forEach(toOn::add);
        }
        for (final Cell cell : toOn) {
            gameOfLife.grid.add(cell);
        }
        for (final Cell cell : toOff) {
            gameOfLife.grid.remove(cell);
        }
    }
    
    private int solve1(final GameOfLife gameOfLife, final int generations) {
        for (int i = 0; i < generations; i++) {
            nextGen(gameOfLife, Set.of());
        }
        return gameOfLife.grid.size();
    }
    
    @Override
    public Integer solvePart1(final GameOfLife gameOfLife) {
        return solve1(GameOfLife.clone(gameOfLife), 100);
    }
    
    private int solve2(final GameOfLife gameOfLife, final int generations) {
        final Set<Cell> stuckPositions = Set.of(
                Grid.ORIGIN,
                Cell.at(gameOfLife.height - 1, 0),
                Cell.at(0, gameOfLife.width - 1),
                Cell.at(gameOfLife.height- 1, gameOfLife.width - 1));
        for (final Cell stuck : stuckPositions) {
            gameOfLife.grid.add(stuck);
        }
        for (int i = 0; i < generations; i++) {
            nextGen(gameOfLife, stuckPositions);
        }
        return gameOfLife.grid.size();
    }
    
    @Override
    public Integer solvePart2(final GameOfLife gameOfLife) {
        return solve2(GameOfLife.clone(gameOfLife), 100);
    }

    public static void main(final String[] args) throws Exception {
        final AoC2015_18 test = AoC2015_18.createDebug();
        final GameOfLife input = test.parseInput(TEST);
        assert test.solve1(GameOfLife.clone(input), 4) == 4;
        assert test.solve2(GameOfLife.clone(input), 5) == 17;
        
        AoC2015_18.create().run();
    }
    
    private static final List<String> TEST = splitLines(
            ".#.#.#\r\n" +
            "...##.\r\n" +
            "#....#\r\n" +
            "..#...\r\n" +
            "#.#..#\r\n" +
            "####.."
    );
}

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class GameOfLife implements Cloneable {
    private static final char ON = '#';
    
    final Set<Cell> grid;
    final int height;
    final int width;
    
    public static GameOfLife fromInput(final List<String> inputs) {
        final int height = inputs.size();
        final int width = inputs.get(0).length();
        final Set<Cell> grid = IntStream.range(0, height).boxed()
            .flatMap(r -> IntStream.range(0, width).mapToObj(c -> Cell.at(r, c)))
            .filter(c -> inputs.get(c.getRow()).charAt(c.getCol()) == ON)
            .collect(toSet());
        return new GameOfLife(Collections.unmodifiableSet(grid), height, width);
    }
    
    public static GameOfLife clone(final GameOfLife gameOfLife) {
        try {
            return gameOfLife.clone();
        } catch (final CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected GameOfLife clone() throws CloneNotSupportedException {
        return new GameOfLife(
                grid.stream().collect(toSet()),
                height,
                width);
    }
}

import static com.github.pareronia.aoc.IntegerSequence.Range.range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_06
        extends SolutionBase<CharGrid, Integer, Integer> {
    
    private AoC2024_06(final boolean debug) {
        super(debug);
    }
    
    public static AoC2024_06 create() {
        return new AoC2024_06(false);
    }
    
    public static AoC2024_06 createDebug() {
        return new AoC2024_06(true);
    }
    
    @Override
    protected CharGrid parseInput(final List<String> inputs) {
        return new CharGrid(inputs);
    }
    
    private LinkedHashMap<Cell, List<Direction>> visited(
        final CharGrid grid, Direction dir
    ) {
        Cell pos = grid.getAllEqualTo('^').findFirst().orElseThrow();
        final LinkedHashMap<Cell, List<Direction>> visited
            = new LinkedHashMap<>(Map.of(pos, new ArrayList<>(List.of(dir))));
        while (true) {
            final Cell nxt = pos.at(dir);
            if (!grid.isInBounds(nxt)) {
                return visited;
            }
            if (grid.getValue(nxt) == '#') {
                dir = dir.turn(Turn.RIGHT);
            } else {
                pos = nxt;
            }
            visited.computeIfAbsent(pos, k -> new ArrayList<>()).add(dir);
        }
    }
    
    @Override
    public Integer solvePart1(final CharGrid grid) {
        return visited(grid, Direction.UP).size();
    }
    
    private boolean isLoop(
            Cell pos, Direction dir, final Obstacles obs, final Cell extra
    ) {
        final ToIntFunction<Direction> bits = d -> switch (d) {
            case UP -> 1;
            case RIGHT -> 1 << 1;
            case DOWN -> 1 << 2;
            case LEFT -> 1 << 3;
            default -> throw new IllegalArgumentException();
        };
        final Map<Cell, Integer> seen = new HashMap<>();
        seen.put(pos, bits.applyAsInt(dir));
        while (true) {
            final Optional<Cell> nextObs = obs.getNext(pos, dir, extra);
            if (nextObs.isEmpty()) {
                return false;
            }
            pos = nextObs.get().at(dir.turn(Turn.AROUND));
            dir = dir.turn(Turn.RIGHT);
            final int bDir = bits.applyAsInt(dir);
            if (seen.containsKey(pos) && (seen.get(pos) & bDir) != 0) {
                return true;
            }
            seen.merge(pos, bDir, (a, b) -> a |= b);
        }
    }
    
    @Override
    public Integer solvePart2(final CharGrid grid) {
        final Obstacles obs = Obstacles.from(grid);
        final LinkedHashMap<Cell, List<Direction>> visited
                = visited(grid, Direction.UP);
        final Iterator<Entry<Cell, List<Direction>>> it
                = visited.entrySet().iterator();
        Entry<Cell, List<Direction>> prev = it.next();
        int ans = 0;
        while (it.hasNext()) {
            final Entry<Cell, List<Direction>> curr = it.next();
            final Cell start = prev.getKey();
            final Direction dir = prev.getValue().remove(0);
            if (isLoop(start, dir, obs, curr.getKey())) {
                ans += 1;
            }
            prev = curr;
        }
        return ans;
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "41"),
        @Sample(method = "part2", input = TEST, expected = "6"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2024_06.create().run();
    }

    private static final String TEST = """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
            """;

    record Obstacles(Map<Direction, Cell[][]> obs) {
        private static Cell[][] obstacles(
                final CharGrid grid,
                final Stream<Cell> starts,
                final Direction dir
        ) {
            final Cell[][] obs = new Cell[grid.getHeight()][grid.getWidth()];
            starts.forEach(start -> {
                Cell last = grid.getValue(start) == '#' ? start : null;
                final Iterator<Cell> it
                        = grid.getCells(start, dir).iterator();
                while (it.hasNext()) {
                    final Cell cell = it.next();
                    obs[cell.getRow()][cell.getCol()] = last;
                    if (grid.getValue(cell) == '#') {
                        last = cell;
                    }
                }
            });
            return obs;
        }

        public static Obstacles from(final CharGrid grid) {
            final Map<Direction, Cell[][]> obs = new HashMap<>();
            obs.put(Direction.RIGHT, Obstacles.obstacles(
                    grid,
                    range(grid.getHeight()).intStream()
                        .mapToObj(r -> Cell.at(r, grid.getMaxColIndex())),
                    Direction.LEFT));
            obs.put(Direction.LEFT, Obstacles.obstacles(
                    grid,
                    range(grid.getHeight()).intStream()
                        .mapToObj(r -> Cell.at(r, 0)),
                    Direction.RIGHT));
            obs.put(Direction.UP, Obstacles.obstacles(
                    grid,
                    range(grid.getWidth()).intStream()
                        .mapToObj(c -> Cell.at(0, c)),
                    Direction.DOWN));
            obs.put(Direction.DOWN, Obstacles.obstacles(
                    grid,
                    range(grid.getWidth()).intStream()
                        .mapToObj(c -> Cell.at(grid.getMaxRowIndex(), c)),
                    Direction.UP));
            return new Obstacles(obs);
        }

        public Optional<Cell> getNext(
                final Cell start, final Direction dir, final Cell extra
        ) {
            final Cell obs = this.obs.get(dir)[start.getRow()][start.getCol()];
            if (obs == null) {
                if (dir.isHorizontal()
                        && start.getRow() == extra.getRow()
                        && start.to(extra) == dir
                ) {
                    return Optional.of(extra);
                } else if (dir.isVertical()
                        && start.getCol() == extra.getCol()
                        && start.to(extra) == dir
                ) {
                    return Optional.of(extra);
                }
                return Optional.empty();
            } else {
                int dExtra = 0;
                int dObs = 0;
                if (dir.isHorizontal()
                        && obs.getRow() == extra.getRow()
                        && start.to(extra) == dir
                ) {
                    dExtra = Math.abs(extra.getCol() - start.getCol());
                    dObs = Math.abs(obs.getCol() - start.getCol());
                    return Optional.of(dObs < dExtra ? obs : extra);
                } else if (dir.isVertical()
                        && obs.getCol() == extra.getCol()
                        && start.to(extra) == dir
                ) {
                    dExtra = Math.abs(extra.getRow() - start.getRow());
                    dObs = Math.abs(obs.getRow() - start.getRow());
                    return Optional.of(dObs < dExtra ? obs : extra);
                }
                return Optional.of(obs);
            }
        }
    }
}
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_06
        extends SolutionBase<AoC2024_06.Input, Integer, Integer> {
    
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
    protected Input parseInput(final List<String> inputs) {
        final CharGrid grid = new CharGrid(inputs);
        final Cell start = grid.getAllEqualTo('^').findFirst().orElseThrow();
        final Set<Cell> obs = grid.getAllEqualTo('#').collect(toSet());
        return new Input(grid, obs, start);
    }
    
    private Route route(
        final CharGrid grid, final Set<Cell> obs, Cell pos, Direction dir
    ) {
        final LinkedHashMap<Cell, List<Direction>> seen = new LinkedHashMap<>();
        seen.computeIfAbsent(pos, k -> new ArrayList<>()).add(dir);
        while (true) {
            final Cell nxt = pos.at(dir);
            if (!grid.isInBounds(nxt)) {
                return new Route(false, seen);
            }
            if (obs.contains(nxt)) {
                dir = dir.turn(Turn.RIGHT);
            } else {
                pos = nxt;
            }
            if (seen.containsKey(pos) && seen.get(pos).contains(dir)) {
                return new Route(true, null);
            }
            seen.computeIfAbsent(pos, k -> new ArrayList<>()).add(dir);
        }
    }
    
    @Override
    public Integer solvePart1(final Input input) {
        final Route route
                = route(input.grid, input.obs, input.start, Direction.UP);
        return route.path.size();
    }
    
    @Override
    public Integer solvePart2(final Input input) {
        Route route = route(input.grid, input.obs, input.start, Direction.UP);
        final Iterator<Entry<Cell, List<Direction>>> it
                = route.path.entrySet().iterator();
        Entry<Cell, List<Direction>> prev = it.next();
        int ans = 0;
        while (it.hasNext()) {
            final Entry<Cell, List<Direction>> curr = it.next();
            input.obs.add(curr.getKey());
            final Cell start = prev.getKey();
            final Direction dir = prev.getValue().remove(0);
            route = route(input.grid, input.obs, start, dir);
            if (route.loop) {
                ans += 1;
            }
            input.obs.remove(curr.getKey());
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

    record Input(CharGrid grid, Set<Cell> obs, Cell start) {}

    record Route(boolean loop, LinkedHashMap<Cell, List<Direction>> path) {}
}
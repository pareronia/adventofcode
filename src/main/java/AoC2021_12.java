import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import com.github.pareronia.aoc.MutableInt;
import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2021_12
                extends SolutionBase<AoC2021_12.System, Integer, Integer> {
    
    private AoC2021_12(final boolean debug) {
        super(debug);
    }
    
    public static final AoC2021_12 create() {
        return new AoC2021_12(false);
    }

    public static final AoC2021_12 createDebug() {
        return new AoC2021_12(true);
    }
    
    @Override
    protected System parseInput(final List<String> inputs) {
        return System.fromInput(inputs);
    }

    private void dfs(final System system, final Cave start, final Cave end,
                     final State state,
                     final BiFunction<Cave, State, Boolean> proceed,
                     final Runnable onPath) {
        if (start.equals(end)) {
            onPath.run();
            return;
        }
        for (final Cave to : system.tunnels().get(start)) {
            if (proceed.apply(to, state)) {
                final State newState = State.copyOf(state);
                if (to.isSmall()) {
                    if (newState.smallCavesSeen.contains(to)) {
                        newState.smallCavesSeenTwice.add(to);
                    }
                    newState.smallCavesSeen.add(to);
                }
                dfs(system, to, end, newState, proceed, onPath);
            }
        }
    }
    
    private int solve(final System system, final BiFunction<Cave, State, Boolean> proceed) {
        final Cave start = system.start();
        final State state = new State(new HashSet<>(Set.of(start)), new HashSet<>());
        final MutableInt count = new MutableInt();
        
        dfs(system, start, system.end(), state, proceed, () -> count.increment());
        
        return count.intValue();
    }
    
    @Override
    public Integer solvePart1(final System system) {
        return solve(system, (to, state)
                        -> !to.isSmall()
                               || !state.smallCavesSeen.contains(to));
    }

    @Override
    public Integer solvePart2(final System system) {
        return solve(system, (to, state)
                        -> !to.isSmall()
                                || !state.smallCavesSeen.contains(to)
                                || (!to.isStart()
                                        && !to.isEnd()
                                        && state.smallCavesSeenTwice.isEmpty()));
    }

    @Samples({
        @Sample(method = "part1", input = TEST1, debug = false, expected = "10"),
        @Sample(method = "part1", input = TEST2, debug = false, expected = "19"),
        @Sample(method = "part1", input = TEST3, debug = false, expected = "226"),
        @Sample(method = "part2", input = TEST1, debug = false, expected = "36"),
        @Sample(method = "part2", input = TEST2, debug = false, expected = "103"),
        @Sample(method = "part2", input = TEST3, debug = false, expected = "3509"),
    })
    public static void main(final String[] args) throws Exception {
        AoC2021_12.create().run();
    }

    private static final String TEST1 = """
            start-A\r
            start-b\r
            A-c\r
            A-b\r
            b-d\r
            A-end\r
            b-end
            """;
    private static final String TEST2 = """
            dc-end\r
            HN-start\r
            start-kj\r
            dc-start\r
            dc-HN\r
            LN-dc\r
            HN-end\r
            kj-sa\r
            kj-HN\r
            kj-dc
            """;
    private static final String TEST3 = """
            fs-end\r
            he-DX\r
            fs-he\r
            start-DX\r
            pj-DX\r
            end-zg\r
            zg-sl\r
            zg-pj\r
            pj-he\r
            RW-he\r
            fs-DX\r
            pj-RW\r
            zg-RW\r
            start-pj\r
            he-WI\r
            zg-he\r
            pj-fs\r
            start-RW
            """;
    
    record State(Set<Cave> smallCavesSeen, Set<Cave> smallCavesSeenTwice) {
        
        public static State copyOf(final State state) {
            return new State(new HashSet<>(Set.copyOf(state.smallCavesSeen)),
                             new HashSet<>(Set.copyOf(state.smallCavesSeenTwice)));
        }
    }
    
    record Cave(String name) {
        
        public boolean isSmall() {
            return StringUtils.isAllLowerCase(name);
        }
        
        public boolean isStart() {
            return this.name.equals("start");
        }

        public boolean isEnd() {
            return this.name.equals("end");
        }
    }
    
    record Tunnel(Cave from, Cave to) {}
    
    record System(Map<Cave, Set<Cave>> tunnels, Cave start, Cave end) {

        public static System fromInput(final List<String> inputs) {
            final Map<Cave, Set<Cave>> tunnels = new HashMap<>();
            Cave start = null, end = null;
            for (final String string : inputs) {
               final String[] split = string.split("-");
               final Cave from = new Cave(split[0]);
               final Cave to = new Cave(split[1]);
               tunnels.computeIfAbsent(from, c -> new HashSet<>()).add(to);
               tunnels.computeIfAbsent(to, c -> new HashSet<>()).add(from);
               for (final Cave cave : Set.of(from, to)) {
                   if (cave.isStart()) {
                       start = cave;
                   }
                   if (cave.isEnd()) {
                       end = cave;
                   }
               }
            }
            return new System(Collections.unmodifiableMap(tunnels), start, end);
        }}
}

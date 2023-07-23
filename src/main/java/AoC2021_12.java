import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import org.apache.commons.lang3.mutable.MutableInt;

import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2021_12 extends AoCBase {
    
    private final System system;
    private AoC2021_12(final List<String> input, final boolean debug) {
        super(debug);
        final Map<Cave, Set<Cave>> tunnels = new HashMap<>();
        Cave start = null, end = null;
        for (final String string : input) {
           final String[] split = string.split("-");
           final Cave from = new Cave(split[0]);
           final Cave to = new Cave(split[1]);
           tunnels.merge(from, new HashSet<>(Set.of(to)), (s1, s2) -> {
               s1.addAll(s2);
               return s1;
           });
           if (from.isStart()) {
               start = from;
           } else if (to.isEnd()) {
               end = to;
           }
           tunnels.merge(to, new HashSet<>(Set.of(from)), (s1, s2) -> {
               s1.addAll(s2);
               return s1;
           });
        }
        system = new System(Collections.unmodifiableMap(tunnels), start, end);
        log(system.tunnels.size());
        log(system);
    }
    
    public static final AoC2021_12 create(final List<String> input) {
        return new AoC2021_12(input, false);
    }

    public static final AoC2021_12 createDebug(final List<String> input) {
        return new AoC2021_12(input, true);
    }
    
    private void dfs(final Cave start, final Cave end, final State state,
                     final BiFunction<Cave, State, Boolean> proceed,
                     final Runnable onPath) {
        if (start.equals(end)) {
            onPath.run();
            return;
        }
        for (final Cave to : this.system.getTunnels().get(start)) {
            if (proceed.apply(to, state)) {
                final State newState = State.copyOf(state);
                if (to.isSmall()) {
                    if (newState.smallCavesSeen.contains(to)) {
                        newState.smallCavesSeenTwice.add(to);
                    }
                    newState.smallCavesSeen.add(to);
                }
                dfs(to, end, newState, proceed, onPath);
            }
        }
    }
    
    private int solve(final BiFunction<Cave, State, Boolean> proceed) {
        final Cave start = this.system.getStart();
        final State state = new State(new HashSet<>(Set.of(start)), new HashSet<>());
        final MutableInt count = new MutableInt();
        
        dfs(start, this.system.getEnd(), state, proceed, () -> count.increment());
        
        return count.intValue();
    }
    
    @Override
    public Integer solvePart1() {
        return solve((to, state)
                        -> !to.isSmall()
                               || !state.smallCavesSeen.contains(to));
    }

    @Override
    public Integer solvePart2() {
        return solve((to, state)
                        -> !to.isSmall()
                                || !state.smallCavesSeen.contains(to)
                                || (!to.isStart()
                                        && !to.isEnd()
                                        && state.smallCavesSeenTwice.isEmpty()));
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_12.create(TEST1).solvePart1() == 10;
        assert AoC2021_12.create(TEST2).solvePart1() == 19;
        assert AoC2021_12.create(TEST3).solvePart1() == 226;
        assert AoC2021_12.create(TEST1).solvePart2() == 36;
        assert AoC2021_12.create(TEST2).solvePart2() == 103;
        assert AoC2021_12.create(TEST3).solvePart2() == 3509;

        final Puzzle puzzle = Aocd.puzzle(2021, 12);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2021_12.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2021_12.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST1 = splitLines(
        "start-A\r\n" +
        "start-b\r\n" +
        "A-c\r\n" +
        "A-b\r\n" +
        "b-d\r\n" +
        "A-end\r\n" +
        "b-end"
    );
    private static final List<String> TEST2 = splitLines(
        "dc-end\r\n" +
        "HN-start\r\n" +
        "start-kj\r\n" +
        "dc-start\r\n" +
        "dc-HN\r\n" +
        "LN-dc\r\n" +
        "HN-end\r\n" +
        "kj-sa\r\n" +
        "kj-HN\r\n" +
        "kj-dc"
    );
    private static final List<String> TEST3 = splitLines(
        "fs-end\r\n" +
        "he-DX\r\n" +
        "fs-he\r\n" +
        "start-DX\r\n" +
        "pj-DX\r\n" +
        "end-zg\r\n" +
        "zg-sl\r\n" +
        "zg-pj\r\n" +
        "pj-he\r\n" +
        "RW-he\r\n" +
        "fs-DX\r\n" +
        "pj-RW\r\n" +
        "zg-RW\r\n" +
        "start-pj\r\n" +
        "he-WI\r\n" +
        "zg-he\r\n" +
        "pj-fs\r\n" +
        "start-RW"
    );
    
    @RequiredArgsConstructor
    private static final class State {
        private final Set<Cave> smallCavesSeen;
        private final Set<Cave> smallCavesSeenTwice;
        
        public static State copyOf(final State state) {
            return new State(new HashSet<>(Set.copyOf(state.smallCavesSeen)),
                             new HashSet<>(Set.copyOf(state.smallCavesSeenTwice)));
        }
    }
    
    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    private static final class Cave {
        private final String name;
        
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
    
    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    private static final class Tunnel {
        private final Cave from;
        private final Cave to;
    }
    
    @RequiredArgsConstructor
    @Getter
    @ToString
    private static final class System {
        private final Map<Cave, Set<Cave>> tunnels;
        private final Cave start;
        private final Cave end;
    }
}

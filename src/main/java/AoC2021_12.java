import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;

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
        final Set<Tunnel> tunnels = new HashSet<>();
        Cave start = null, end = null;
        for (final String string : input) {
           final String[] split = string.split("-");
           final Cave from = new Cave(split[0]);
           final Cave to = new Cave(split[1]);
           tunnels.add(new Tunnel(from, to));
           if (!(from.isStart() || to.isEnd())) {
               tunnels.add(new Tunnel(to, from));
           }
           if (from.isStart()) {
               start = from;
           }
           if (to.isEnd()) {
               end = to;
           }
        }
        system = new System(tunnels, start, end);
        log(system);
    }
    
    public static final AoC2021_12 create(final List<String> input) {
        return new AoC2021_12(input, false);
    }

    public static final AoC2021_12 createDebug(final List<String> input) {
        return new AoC2021_12(input, true);
    }
    
    private void dfs(final Cave start, final Cave end,
                     final List<Cave> path, final Consumer<List<Cave>> onEnd,
                     final BiFunction<List<Cave>, Cave, Boolean> proceed) {
        if (start.equals(end)) {
            onEnd.accept(path);
            return;
        }
        for (final Tunnel tunnel : system.getTunnelsFrom(start)) {
            final Cave to = tunnel.getTo();
            if (proceed.apply(path, to)) {
                path.add(to);
                dfs(to, end, path, onEnd, proceed);
                path.remove(path.size() - 1);
            }
        }
    }

    private Integer solve(final BiFunction<List<Cave>, Cave, Boolean> proceed) {
        final List<Cave> path = new ArrayList<>(List.of(this.system.getStart()));
        final MutableInt count = new MutableInt();
        dfs(this.system.getStart(),
            this.system.getEnd(),
            path,
            p -> {
                count.increment();
                log(p.stream().map(Cave::getName).collect(joining(",")));},
            proceed);
        log(count.intValue());
        return count.intValue();
    }
    
    @Override
    public Integer solvePart1() {
        return solve((p, cave) -> !(cave.isSmall() && p.contains(cave)));
    }

    @Override
    public Integer solvePart2() {
        return solve((p, cave) -> {
            if (!cave.isSmall()) {
                return true;
            }
            final Map<Cave, Long> counters = p.stream()
                .filter(Cave::isSmall)
                .collect(groupingBy(pp -> pp, counting()));
            if (cave.equals(this.system.getStart()) || cave.equals(this.system.getEnd())) {
                return !counters.containsKey(cave);
            }
            final long twices = counters.values().stream().filter(v -> v > 1).count();
            if (twices == 0) {
                return counters.getOrDefault(cave, 0L) <= 1;
            }
            if (counters.getOrDefault(cave, 0L) >= 1) {
                return false;
            }
            return true;
        });
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_12.create(TEST1).solvePart1() == 10;
        assert AoC2021_12.create(TEST2).solvePart1() == 19;
        assert AoC2021_12.create(TEST3).solvePart1() == 226;
        assert AoC2021_12.create(TEST1).solvePart2() == 36;
        assert AoC2021_12.create(TEST2).solvePart2() == 103;
        assert AoC2021_12.create(TEST3).solvePart2() == 3509;

        final Puzzle puzzle = Aocd.puzzle(2021, 12);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_12.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_12.create(puzzle.getInputData()).solvePart2())
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
        private final Set<Tunnel> tunnels;
        private final Cave start;
        private final Cave end;
        
        public Set<Tunnel> getTunnelsFrom(final Cave from) {
            return this.tunnels.stream()
                    .filter(t -> t.getFrom().equals(from))
                    .collect(toSet());
        }
    }
}

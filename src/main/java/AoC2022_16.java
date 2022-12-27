import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.SetUtils.intersection;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.graph.AStar;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2022_16 extends AoCBase {
    
    private final String[] valves;
    private final int[] rates;
    private final Set<Integer>[] tunnels;
    private int start;
    
    @SuppressWarnings("unchecked")
    private AoC2022_16(final List<String> input, final boolean debug) {
        super(debug);
        final int size = input.size();
        this.valves = new String[size];
        this.rates = new int[size];
        this.tunnels = new Set[size];
        final var edges = new HashMap<String, Set<String>>();
        final var map = new HashMap<String, Integer>();
        for (int i = 0; i < size; i++) {
            final var line = input.get(i).replaceAll("[,;]", "");
            final var splits = line.split(" ");
            final var name = splits[1];
            if ("AA".equals(name)) {
                this.start = i;
            }
            valves[i] = name;
            map.put(name, i);
            final int rate = Integer.parseInt(splits[4].split("=")[1]);
            rates[i] = rate;
            IntStream.range(9, splits.length).mapToObj(j -> splits[j])
                .forEach(v -> edges.computeIfAbsent(name, k -> new HashSet<>()).add(v));
        }
        edges.entrySet().forEach(e -> {
            final int idx = map.get(e.getKey());
            this.tunnels[idx] = e.getValue().stream().map(map::get).collect(toSet());
        });
        log(() -> "valves: " + IntStream.range(0, this.valves.length)
                .mapToObj(i -> String.format("%d: %s", i, this.valves[i]))
                .collect(joining(", ")));
        log(() -> "rates: " + IntStream.range(0, this.rates.length)
                .mapToObj(i -> String.format("%d: %s", i, this.rates[i]))
                .collect(joining(", ")));
        log(() -> "tunnels: " + IntStream.range(0, this.tunnels.length)
                .mapToObj(i -> String.format("%d: %s", i, this.tunnels[i]))
                .collect(joining(", ")));
    }
    
    public static final AoC2022_16 create(final List<String> input) {
        return new AoC2022_16(input, false);
    }

    public static final AoC2022_16 createDebug(final List<String> input) {
        return new AoC2022_16(input, true);
    }
    
    @RequiredArgsConstructor
    private final class DFS {
        private final int[][] distances;
        private final int maxTime;
        private final Flow current = new Flow();
        private final Map<Set<Integer>, Integer> bestPerUsed = new HashMap<>();
        
        public void dfs(final int start, final int time) {
            final var used = current.getUsed();
            final IntStream rest = IntStream.range(0, valves.length)
                    .filter(i -> rates[i] > 0)
                    .filter(i-> !used.contains(i));
            rest.forEach(valve -> {
                int newTime = time + 1;
                newTime += distances[start][valve];
                if (newTime < maxTime) {
                    current.add(valve, rates[valve], newTime);
                    dfs(valve, newTime);
                    current.removeLast();
                }
            });
            bestPerUsed.merge(used, current.getTotal(maxTime), Math::max);
        }
    }

    private int[][] getDistances() {
        final int[] relevantValves = IntStream.range(0, this.valves.length)
                .filter(i -> this.rates[i] > 0 || i == this.start)
                .toArray();
        final int size = valves.length;
        final var distances = new int[size][size];
        for (final int i : relevantValves) {
            final var result = AStar.execute(
                    i,
                    v -> false,
                    v -> this.tunnels[v].stream(),
                    v -> 1);
            for (final int j : relevantValves) {
                distances[i][j] = (int) result.getDistance(j);
            }
        }
        log(() -> "    " + Arrays.stream(relevantValves)
                .mapToObj(i -> this.valves[i]).collect(joining(" | ")));
        Arrays.stream(relevantValves).forEach(i -> {
            log(() -> this.valves[i] + "  "
                    + Arrays.stream(relevantValves)
                            .mapToObj(j -> String.format("%2d", distances[i][j]))
                            .collect(joining(" | ")));
        });
        return distances;
    }

    @Override
    public Integer solvePart1() {
        final int[][] distances = getDistances();
        final DFS dfs = new DFS(distances, 30);
        dfs.dfs(this.start, 0);
        return dfs.bestPerUsed.values().stream()
                .mapToInt(Integer::valueOf)
                .max().orElseThrow();
    }

    @Override
    public Integer solvePart2() {
        final int[][] distances = getDistances();
        final DFS dfs2 = new DFS(distances, 26);
        dfs2.dfs(this.start, 0);
        return dfs2.bestPerUsed.entrySet().stream()
            .flatMapToInt(e1 -> dfs2.bestPerUsed.entrySet().stream()
                .filter(e2 -> !e2.equals(e1))
                .filter(e2 -> intersection(e1.getKey(), e2.getKey()).isEmpty())
                .mapToInt(e2 -> e1.getValue() + e2.getValue()))
            .max().orElseThrow();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_16.createDebug(TEST).solvePart1() == 1651;
        assert AoC2022_16.createDebug(TEST).solvePart2() == 1707;

        final Puzzle puzzle = Aocd.puzzle(2022, 16);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_16.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_16.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines("""
        Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
        Valve BB has flow rate=13; tunnels lead to valves CC, AA
        Valve CC has flow rate=2; tunnels lead to valves DD, BB
        Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
        Valve EE has flow rate=3; tunnels lead to valves FF, DD
        Valve FF has flow rate=0; tunnels lead to valves EE, GG
        Valve GG has flow rate=0; tunnels lead to valves FF, HH
        Valve HH has flow rate=22; tunnel leads to valve GG
        Valve II has flow rate=0; tunnels lead to valves AA, JJ
        Valve JJ has flow rate=21; tunnel leads to valve II
        """);
    
    @ToString
    private final static class Flow {
        private final Deque<Step> steps = new ArrayDeque<>();
        
        public void add(final int valve, final int rate, final int time) {
            assert !getUsed().contains(valve);
            this.steps.addLast(new Step(valve, rate, time));
        }
        
        public void removeLast() {
            this.steps.removeLast();
        }
        
        public int getTotal(final int time) {
            return steps.stream()
                .mapToInt(s -> s.rate * (time - s.since))
                .sum();
        }
        
        public Set<Integer> getUsed() {
            return steps.stream().map(Step::valve).collect(toSet());
        }
        
        private static final record Step(int valve, int rate, int since) { }
    }
}

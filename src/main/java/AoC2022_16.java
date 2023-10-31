import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.graph.AStar;
import com.github.pareronia.aoc.graph.AStar.Result;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;

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
        final Map<String, Set<String>> edges = new HashMap<>();
        final Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            final String line = input.get(i).replaceAll("[,;]", "");
            final String[] splits = line.split(" ");
            final String name = splits[1];
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
        trace(() -> "valves: " + IntStream.range(0, this.valves.length)
                .mapToObj(i -> String.format("%d: %s", i, this.valves[i]))
                .collect(joining(", ")));
        trace(() -> "rates: " + IntStream.range(0, this.rates.length)
                .mapToObj(i -> String.format("%d: %s", i, this.rates[i]))
                .collect(joining(", ")));
        trace(() -> "tunnels: " + IntStream.range(0, this.tunnels.length)
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
        private final boolean sample;
        private final Map<Long, Integer> bestPerUsed = new HashMap<>();
        private long used = 0L;
        private int maxFlow = 0;
        private int cnt = 0;
        
        public void dfs(final int start, final int time) {
            cnt++;
            for (int i = 0; i < valves.length; i++) {
                final long idx = 1L << i;
                if (rates[i] == 0 || (used & idx) != 0) {
                    continue;
                }
                final int newTime = time + 1 + distances[start][i];
                if (newTime < maxTime) {
                    final int flow = rates[i] * (maxTime - newTime);
                    if (!this.sample && maxFlow + flow
                            < bestPerUsed.getOrDefault(used + idx, 0)) {
                        continue;
                    }
                    maxFlow += flow;
                    used += idx;
                    dfs(i, newTime);
                    used -= idx;
                    maxFlow -= flow;
                }
            }
            bestPerUsed.merge(used, maxFlow, Math::max);
        }
    }

    private int[][] getDistances() {
        final int[] relevantValves = IntStream.range(0, this.valves.length)
                .filter(i -> this.rates[i] > 0 || i == this.start)
                .toArray();
        final int size = valves.length;
        final int[][] distances = new int[size][size];
        for (final int i : relevantValves) {
            final Result<Integer> result = AStar.execute(
                    i,
                    v -> false,
                    v -> this.tunnels[v].stream(),
                    v -> 1);
            for (final int j : relevantValves) {
                distances[i][j] = (int) result.getDistance(j);
            }
        }
        trace(() -> "    " + Arrays.stream(relevantValves)
                .mapToObj(i -> this.valves[i]).collect(joining(" | ")));
        Arrays.stream(relevantValves).forEach(i -> {
            trace(() -> this.valves[i] + "  "
                    + Arrays.stream(relevantValves)
                            .mapToObj(j -> String.format("%2d", distances[i][j]))
                            .collect(joining(" | ")));
        });
        return distances;
    }

    @Override
    public Integer solvePart1() {
        final int[][] distances = getDistances();
        final DFS dfs = new DFS(distances, 30, false);
        dfs.dfs(this.start, 0);
        log("dfs: " + dfs.cnt);
        return dfs.bestPerUsed.values().stream()
                .mapToInt(Integer::valueOf)
                .max().orElseThrow();
    }
    
    private int solve2(final boolean sample) {
        final int[][] distances = getDistances();
        final DFS dfs = new DFS(distances, 26, sample);
        dfs.dfs(this.start, 0);
        log("dfs: " + dfs.cnt);
        return dfs.bestPerUsed.entrySet().stream()
            .flatMapToInt(e1 -> dfs.bestPerUsed.entrySet().stream()
                .filter(e2 -> (e1.getKey() & e2.getKey()) == 0)
                .mapToInt(e2 -> e1.getValue() + e2.getValue()))
            .max().orElseThrow();
    }

    @Override
    public Integer solvePart2() {
        return solve2(false);
    }
    
    public static void main(final String[] args) throws Exception {
        assert AoC2022_16.createDebug(TEST).solvePart1() == 1651;
        assert AoC2022_16.createDebug(TEST).solve2(true) == 1707;

        final Puzzle puzzle = Aocd.puzzle(2022, 16);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_16.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_16.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "Valve AA has flow rate=0; tunnels lead to valves DD, II, BB\r\n" +
        "Valve BB has flow rate=13; tunnels lead to valves CC, AA\r\n" +
        "Valve CC has flow rate=2; tunnels lead to valves DD, BB\r\n" +
        "Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE\r\n" +
        "Valve EE has flow rate=3; tunnels lead to valves FF, DD\r\n" +
        "Valve FF has flow rate=0; tunnels lead to valves EE, GG\r\n" +
        "Valve GG has flow rate=0; tunnels lead to valves FF, HH\r\n" +
        "Valve HH has flow rate=22; tunnel leads to valve GG\r\n" +
        "Valve II has flow rate=0; tunnels lead to valves AA, JJ\r\n" +
        "Valve JJ has flow rate=21; tunnel leads to valve II"
    );
}

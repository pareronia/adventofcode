import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.collections4.SetUtils;

import com.github.pareronia.aoc.graph.AStar;
import com.github.pareronia.aoc.graph.AStar.Result;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2022_16 extends AoCBase {
    
    private final Map<String, Integer> nodes;
    private final Map<String, Set<String>> edges;
    private final int maxFlow;
    private final List<String> usableValves;
    
    private AoC2022_16(final List<String> input, final boolean debug) {
        super(debug);
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
        for (String line : input) {
            line = line.replaceAll("[,;]", "");
            final String[] splits = line.split(" ");
            final String name = splits[1];
            final int rate = Integer.parseInt(splits[4].split("=")[1]);
            nodes.put(name, rate);
            IntStream.range(9, splits.length).mapToObj(i -> splits[i])
                .forEach(v -> edges.computeIfAbsent(name, k -> new HashSet<>()).add(v));
        }
        this.maxFlow = nodes.values().stream().mapToInt(Integer::valueOf).sum();
        this.usableValves = new ArrayList<>(List.of("AA"));
        this.nodes.keySet().stream()
                .filter(k -> this.nodes.get(k) > 0)
                .forEach(this.usableValves::add);
        log(this.nodes);
        log(this.edges);
        log(this.maxFlow);
        log("usableValves: " + this.usableValves);
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
        private Result best = new Result(0, Set.of());
        private final Map<Set<String>, Integer> bestPerUsed = new HashMap<>();
        
        public void dfs(final String start, final int time) {
            final int start_idx = usableValves.indexOf(start);
            final Set<String> used = current.getUsed();
            final Stream<String> rest = usableValves.stream()
                    .filter(v -> !v.equals("AA"))
                    .filter(v -> !used.contains(v));
            rest.forEach(valve -> {
                int newTime = time + 1;
                final int valve_idx = usableValves.indexOf(valve);
                newTime += distances[start_idx][valve_idx];
                if (newTime < maxTime) {
                    current.add(valve, nodes.get(valve), newTime);
                    dfs(valve, newTime);
                    current.removeLast();
                }
            });
            final Result result = new Result(current.getTotal(maxTime), used);
            if (best.compareTo(result) < 0) {
                best = result;
            }
            bestPerUsed.put(used, Math.max(bestPerUsed.getOrDefault(used, 0), result.pressureRelease));
//            bestPerUsed.merge(used, result.pressureRelease, Math::max);
        }
        
        @RequiredArgsConstructor
        private final class Result implements Comparable<Result> {
            private final int pressureRelease;
            private final Set<String> valves;

            @Override
            public int compareTo(final Result other) {
                return Integer.compare(pressureRelease, other.pressureRelease);
            }
        }
    }

    private int[][] getDistances() {
        final int size = this.usableValves.size();
        final int[][] distances = new int[size][size];
        for (int i = 0; i < size; i++) {
            final Result<String> result = AStar.execute(
                    this.usableValves.get(i),
                    v -> false,
                    v -> this.edges.get(v).stream(),
                    v -> 1);
            for (int j = 0; j < size; j++) {
               final String dst = this.usableValves.get(j);
               distances[i][j] = (int) result.getDistance(dst);
            }
        }
        log(() -> "    " + this.usableValves.stream().collect(joining(" | ")));
        IntStream.range(0, distances.length).forEach(i -> {
            log(() -> this.usableValves.get(i) + "  "
                    + Arrays.stream(distances[i])
                            .mapToObj(d -> String.format("%02d", d))
                            .collect(joining(" | ")));
        });
        return distances;
    }
    
    private void testFlow() {
        final Flow flow1 = new Flow();
        flow1.add("DD", 20, 2);
        flow1.add("BB", 13, 5);
        flow1.add("JJ", 21, 9);
        flow1.add("HH", 22, 17);
        flow1.add("EE", 3, 21);
        flow1.add("CC", 2, 24);
        assert flow1.getTotal(30) == 1651;
        final Flow flow2 = new Flow();
        flow2.add("DD", 20, 1);
        flow2.add("BB", 13, 4);
        flow2.add("JJ", 21, 8);
        flow2.add("EE", 3, 13);
        flow2.add("HH", 22, 17);
        flow2.add("CC", 2, 23);
        assert flow2.getTotal(30) == 1731;
    }
    
    @Override
    public Integer solvePart1() {
        final int[][] distances = getDistances();
        final DFS dfs = new DFS(distances, 30);
        dfs.dfs("AA", 0);
        final int ans = dfs.best.pressureRelease;
        log(ans);
        return ans;
    }

    @Override
    public Integer solvePart2() {
        final int[][] distances = getDistances();
        final DFS dfs = new DFS(distances, 26);
        dfs.dfs("AA", 0);
        final List<Entry<Set<String>, Integer>> sorted = dfs.bestPerUsed.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .collect(toList());
        int ans = 0;
        for (final Entry<Set<String>, Integer> entry1 : sorted) {
            for (final Entry<Set<String>, Integer> entry2 : sorted) {
                if (entry1.equals(entry2)) {
                    continue;
                }
                if (SetUtils.intersection(entry1.getKey(), entry2.getKey()).isEmpty()) {
                    ans = Math.max(entry1.getValue() + entry2.getValue(), ans);
                }
            }
        }
        return ans;
    }

    public static void main(final String[] args) throws Exception {
        AoC2022_16.createDebug(TEST).testFlow();
        assert AoC2022_16.createDebug(TEST).solvePart1() == 1651;
        assert AoC2022_16.createDebug(TEST).solvePart2() == 1707;

        final Puzzle puzzle = Aocd.puzzle(2022, 16);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_16.createDebug(inputData)::solvePart1),
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
    
    @ToString
    private final static class Flow {
        private final Deque<Step> steps = new ArrayDeque<>();
        
        public void add(final String name, final int rate, final int time) {
            assert !getUsed().contains(name);
            this.steps.addLast(new Step(name, rate, time));
        }
        
        public void removeLast() {
            this.steps.removeLast();
        }
        
        public int getTotal(final int time) {
            return steps.stream()
                .mapToInt(s -> s.rate * (time - s.since))
                .sum();
        }
        
        public Set<String> getUsed() {
            return steps.stream().map(Step::getValve).collect(toSet());
        }
        
        @RequiredArgsConstructor
        @ToString
        private static final class Step {
            @Getter
            private final String valve;
            @Getter
            private final int rate;
            private final int since;
        }
    }
}

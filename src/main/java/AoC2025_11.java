import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import com.github.pareronia.aoc.itertools.IterTools;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2025_11 extends SolutionBase<AoC2025_11.Graph, Long, Long> {

    private static final String YOU = "you";
    private static final String SVR = "svr";
    private static final String FFT = "fft";
    private static final String DAC = "dac";
    private static final String OUT = "out";

    private AoC2025_11(final boolean debug) {
        super(debug);
    }

    public static AoC2025_11 create() {
        return new AoC2025_11(false);
    }

    public static AoC2025_11 createDebug() {
        return new AoC2025_11(true);
    }

    @Override
    protected Graph parseInput(final List<String> inputs) {
        return Graph.fromInput(inputs);
    }

    @Override
    public Long solvePart1(final Graph graph) {
        return graph.countAllPathsAlong(List.of(YOU, OUT));
    }

    @Override
    public Long solvePart2(final Graph graph) {
        return Stream.of(List.of(SVR, DAC, FFT, OUT), List.of(SVR, FFT, DAC, OUT))
                .mapToLong(graph::countAllPathsAlong)
                .sum();
    }

    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "5"),
        @Sample(method = "part2", input = TEST2, expected = "2"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST1 =
            """
            aaa: you hhh
            you: bbb ccc
            bbb: ddd eee
            ccc: ddd eee fff
            ddd: ggg
            eee: out
            fff: out
            ggg: out
            hhh: ccc fff iii
            iii: out
            """;
    private static final String TEST2 =
            """
            svr: aaa bbb
            aaa: fft
            fft: ccc
            bbb: tty
            tty: ccc
            ccc: ddd eee
            ddd: hub
            hub: fff
            eee: dac
            dac: fff
            fff: ggg hhh
            ggg: out
            hhh: out
            """;

    record Graph(Map<String, Set<String>> edges) {

        public static Graph fromInput(final List<String> inputs) {
            final Map<String, Set<String>> edges =
                    inputs.stream()
                            .collect(
                                    toMap(
                                            line -> line.split(": ")[0],
                                            line ->
                                                    Arrays.stream(line.split(": ")[1].split(" "))
                                                            .collect(toSet())));
            return new Graph(edges);
        }

        private long countAllPaths(final String start, final String end) {
            class DFS {
                private final Map<String, Long> cache;

                protected DFS() {
                    this.cache = new HashMap<>();
                }

                @SuppressWarnings("PMD.OnlyOneReturn")
                public long dfs(final String node) {
                    if (this.cache.containsKey(node)) {
                        return this.cache.get(node);
                    }
                    if (end.equals(node)) {
                        return 1;
                    }
                    final long ans =
                            Graph.this.edges.getOrDefault(node, Set.of()).stream()
                                    .mapToLong(this::dfs)
                                    .sum();
                    this.cache.put(node, ans);
                    return ans;
                }
            }

            return new DFS().dfs(start);
        }

        public long countAllPathsAlong(final List<String> along) {
            return IterTools.pairwise(along).stream()
                    .mapToLong(pair -> this.countAllPaths(pair.first(), pair.second()))
                    .reduce((acc, cnt) -> acc * cnt)
                    .getAsLong();
        }
    }
}

import static java.util.stream.Collectors.toMap;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2017_12 extends SolutionBase<Map<String, List<String>>, Integer, Integer> {

    private AoC2017_12(final boolean debug) {
        super(debug);
    }

    public static AoC2017_12 create() {
        return new AoC2017_12(false);
    }

    public static AoC2017_12 createDebug() {
        return new AoC2017_12(true);
    }

    @Override
    protected Map<String, List<String>> parseInput(final List<String> inputs) {
        return inputs.stream()
                .map(s -> StringOps.splitOnce(s, " <-> "))
                .collect(
                        toMap(
                                StringSplit::left,
                                sp -> Arrays.stream(sp.right().split(", ")).toList()));
    }

    private static final class DFS {
        private final Map<String, List<String>> input;

        public DFS(final Map<String, List<String>> input) {
            this.input = input;
        }

        public void dfs(final Set<String> vs, final String v) {
            vs.add(v);
            this.input
                    .get(v)
                    .forEach(
                            w -> {
                                if (!vs.contains(w)) {
                                    dfs(vs, w);
                                }
                            });
        }
    }

    @Override
    public Integer solvePart1(final Map<String, List<String>> input) {
        final Set<String> vs = new HashSet<>();
        new DFS(input).dfs(vs, "0");
        return vs.size();
    }

    @Override
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public Integer solvePart2(final Map<String, List<String>> input) {
        final Map<String, Set<String>> trees = new HashMap<>();
        final DFS dfs = new DFS(input);
        for (final String k : input.keySet()) {
            if (trees.containsKey(k)) {
                continue;
            }
            final Set<String> vs = new HashSet<>();
            dfs.dfs(vs, k);
            vs.forEach(v -> trees.put(v, vs));
        }
        return (int) trees.values().stream().distinct().count();
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "6"),
        @Sample(method = "part2", input = TEST, expected = "2"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            0 <-> 2
            1 <-> 1
            2 <-> 0, 3, 4
            3 <-> 2, 4
            4 <-> 2, 3, 6
            5 <-> 6
            6 <-> 4, 5\
            """;
}

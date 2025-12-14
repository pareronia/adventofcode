import static com.github.pareronia.aoc.IntegerSequence.Range.range;

import static java.util.stream.Collectors.toMap;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.itertools.IterTools;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2025_10 extends SolutionBase<List<AoC2025_10.Machine>, Integer, Integer> {

    private AoC2025_10(final boolean debug) {
        super(debug);
    }

    public static AoC2025_10 create() {
        return new AoC2025_10(false);
    }

    public static AoC2025_10 createDebug() {
        return new AoC2025_10(true);
    }

    @Override
    protected List<Machine> parseInput(final List<String> inputs) {
        return inputs.parallelStream().map(Machine::fromInput).toList();
    }

    @Override
    public Integer solvePart1(final List<Machine> machines) {
        return machines.stream().mapToInt(Machine::buttonPressesForLights).sum();
    }

    @Override
    public Integer solvePart2(final List<Machine> machines) {
        return machines.stream().mapToInt(Machine::buttonPressesForJoltages).sum();
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "7"),
        @Sample(method = "part2", input = TEST, expected = "33"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
            [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
            [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
            """;

    private static List<Integer> parity(final List<Integer> lst) {
        return lst.stream().map(i -> i % 2).toList();
    }

    record Machine(
            List<Integer> lights,
            List<Integer> joltages,
            Map<List<Integer>, Map<List<Integer>, Integer>> patterns) {

        public static Machine fromInput(final String input) {
            final String[] splits = input.split(" ");
            final List<Integer> lights =
                    Utils.asCharacterStream(splits[0].substring(1, splits[0].length() - 1))
                            .map(ch -> ch == '#' ? 1 : 0)
                            .toList();
            final List<int[]> presses = new ArrayList<>();
            for (int i = 1; i < splits.length - 1; i++) {
                presses.add(
                        Arrays.stream(splits[i].substring(1, splits[i].length() - 1).split(","))
                                .mapToInt(Integer::parseInt)
                                .toArray());
            }
            final String sj = splits[splits.length - 1];
            final List<Integer> joltages =
                    Arrays.stream(sj.substring(1, sj.length() - 1).split(","))
                            .map(Integer::valueOf)
                            .toList();
            return new Machine(lights, joltages, createPatterns(lights, joltages, presses));
        }

        private static Map<List<Integer>, Map<List<Integer>, Integer>> createPatterns(
                final List<Integer> lights,
                final List<Integer> joltages,
                final List<int[]> presses) {
            final int numButtons = presses.size();
            final int numVariables = joltages.size();
            assert lights.size() == numVariables;
            final Map<List<Integer>, Map<List<Integer>, Integer>> patterns =
                    IterTools.product(List.of(0, 1).iterator(), numVariables).stream()
                            .collect(toMap(p -> p, p -> new HashMap<>()));
            for (int n = 0; n <= numButtons; n++) {
                final Iterable<int[]> combos = IterTools.combinations(numButtons, n).iterable();
                for (final int[] combo : combos) {
                    final int[] pattern = new int[joltages.size()];
                    for (final int idx : combo) {
                        for (final int p : presses.get(idx)) {
                            pattern[p] += 1;
                        }
                    }
                    final List<Integer> key = Arrays.stream(pattern).boxed().toList();
                    final List<Integer> parityPattern = parity(key);
                    if (!patterns.get(parityPattern).containsKey(key)) {
                        patterns.get(parityPattern).put(key, n);
                    }
                }
            }
            return patterns;
        }

        public int buttonPressesForLights() {
            return this.patterns.get(this.lights()).values().stream()
                    .mapToInt(Integer::intValue)
                    .min()
                    .orElseThrow();
        }

        public int buttonPressesForJoltages() {
            class DFS {
                private final Map<List<Integer>, Integer> cache;

                public DFS() {
                    this.cache = new HashMap<>();
                }

                public int dfs(final List<Integer> joltages) {
                    if (this.cache.containsKey(joltages)) {
                        return this.cache.get(joltages);
                    }
                    int ans;
                    if (joltages.stream().allMatch(j -> j == 0)) {
                        ans = 0;
                    } else {
                        ans = 10_000_000;
                        final List<Integer> parityPattern = parity(joltages);
                        for (final Entry<List<Integer>, Integer> e :
                                Machine.this.patterns.get(parityPattern).entrySet()) {
                            final List<Integer> pattern = e.getKey();
                            if (!range(pattern.size())
                                    .intStream()
                                    .allMatch(i -> pattern.get(i) <= joltages.get(i))) {
                                continue;
                            }
                            final List<Integer> newJoltages =
                                    range(pattern.size()).stream()
                                            .map(i -> (joltages.get(i) - pattern.get(i)) / 2)
                                            .toList();
                            ans = Math.min(ans, 2 * dfs(newJoltages) + e.getValue());
                        }
                    }
                    this.cache.put(joltages, ans);
                    return ans;
                }
            }

            return new DFS().dfs(this.joltages());
        }
    }
}

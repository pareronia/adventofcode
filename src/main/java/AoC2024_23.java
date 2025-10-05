import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;

import com.github.pareronia.aoc.IterTools.ProductPair;
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

public final class AoC2024_23 extends SolutionBase<Map<String, Set<String>>, Integer, String> {

    private AoC2024_23(final boolean debug) {
        super(debug);
    }

    public static AoC2024_23 create() {
        return new AoC2024_23(false);
    }

    public static AoC2024_23 createDebug() {
        return new AoC2024_23(true);
    }

    @Override
    protected Map<String, Set<String>> parseInput(final List<String> inputs) {
        final Map<String, Set<String>> edges = new HashMap<>();
        for (final String line : inputs) {
            final StringSplit sp = StringOps.splitOnce(line, "-");
            edges.computeIfAbsent(sp.left(), k -> new HashSet<>()).add(sp.right());
            edges.computeIfAbsent(sp.right(), k -> new HashSet<>()).add(sp.left());
        }
        return edges;
    }

    @Override
    public Integer solvePart1(final Map<String, Set<String>> edges) {
        record Triangle(String v1, String v2, String v3) {
            public Triangle sorted() {
                final String[] v = {v1, v2, v3};
                Arrays.sort(v);
                return new Triangle(v[0], v[1], v[2]);
            }
        }
        return (int)
                edges.keySet().stream()
                        .filter(comp -> comp.startsWith("t"))
                        .flatMap(t -> edges.get(t).stream().map(n1 -> new ProductPair<>(t, n1)))
                        .flatMap(
                                pp ->
                                        edges.get(pp.second()).stream()
                                                .filter(n2 -> edges.get(n2).contains(pp.first()))
                                                .map(
                                                        n2 ->
                                                                new Triangle(
                                                                        pp.first(),
                                                                        pp.second(),
                                                                        n2)))
                        .map(Triangle::sorted)
                        .distinct()
                        .count();
    }

    @Override
    public String solvePart2(final Map<String, Set<String>> edges) {
        final List<HashSet<String>> cliques =
                edges.keySet().stream().map(comp -> new HashSet<>(Set.of(comp))).toList();
        cliques.forEach(
                clique ->
                        edges.entrySet().stream()
                                .filter(e -> e.getValue().containsAll(clique))
                                .forEach(e -> clique.add(e.getKey())));
        final Set<String> largest =
                cliques.stream().max(comparing(HashSet<String>::size)).orElseThrow();
        return largest.stream().sorted().collect(joining(","));
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "7"),
        @Sample(method = "part2", input = TEST, expected = "co,de,ka,ta")
    })
    public void samples() {}

    public static void main(final String[] args) throws Exception {
        AoC2024_23.create().run();
    }

    private static final String TEST =
            """
            kh-tc
            qp-kh
            de-cg
            ka-co
            yn-aq
            qp-ub
            cg-tb
            vc-aq
            tb-ka
            wh-tc
            yn-cg
            kh-ub
            ta-co
            de-co
            tc-td
            tb-wq
            wh-td
            ta-ka
            td-qp
            aq-cg
            wq-ub
            ub-vc
            de-ta
            wq-aq
            wq-vc
            wh-yn
            ka-de
            kh-ta
            co-tc
            wh-qp
            tb-vc
            td-yn
            """;
}

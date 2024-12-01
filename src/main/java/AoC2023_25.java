import static com.github.pareronia.aoc.Utils.concatAll;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_25
        extends SolutionBase<AoC2023_25.Graph, Integer, String> {
    
    private AoC2023_25(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_25 create() {
        return new AoC2023_25(false);
    }
    
    public static AoC2023_25 createDebug() {
        return new AoC2023_25(true);
    }
    
    @Override
    protected Graph parseInput(final List<String> inputs) {
        return Graph.fromInput(inputs);
    }

    @Override
    public Integer solvePart1(final Graph graph) {
        final Random rand = new Random();
        while (true) {
            Graph g = graph.copy();
            final Map<String, Integer> counts = g.edges.keySet().stream()
                    .collect(toMap(k -> k, k -> 1));
            while (g.edges.size() > 2) {
                final String a = g.edges.keySet().stream()
                        .skip(rand.nextInt(g.edges.size() - 1))
                        .findFirst().orElseThrow();
                final String b = g.edges.get(a).get(rand.nextInt(g.edges.get(a).size()));
                final String newNode = "%s-%s".formatted(a, b);
                counts.put(newNode, counts.get(a) + counts.get(b));
                counts.remove(a);
                counts.remove(b);
                g = g.combineNodes(a, b, newNode);
            }
            final List<String> nodes = List.copyOf(g.edges.keySet());
            if (g.edges.get(nodes.get(0)).size() == 3) {
                return counts.get(nodes.get(0)) * counts.get(nodes.get(1));
            }
        }
    }
    
    @Override
    public String solvePart2(final Graph graph) {
        return "🎄";
    }
    
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "54"),
    })
    public static void main(final String[] args) throws Exception {
        AoC2023_25.create().run();
    }

    private static final String TEST = """
            jqt: rhn xhk nvd
            rsh: frs pzl lsr
            xhk: hfx
            cmg: qnr nvd lhk bvb
            rhn: xhk bvb hfx
            bvb: xhk hfx
            pzl: lsr hfx nvd
            qnr: nvd
            ntq: jqt hfx bvb xhk
            nvd: lhk
            lsr: lhk
            rzs: qnr cmg lsr rsh
            frs: qnr lhk lsr
            """;
    
    record Graph(Map<String, List<String>> edges) {
        
        public static Graph fromInput(final List<String> input) {
            final HashMap<String, List<String>> edges = new HashMap<>();
            for (final String line : input) {
                final StringSplit splits = StringOps.splitOnce(line, ": ");
                final List<String> lst
                    = new ArrayList<>(Arrays.asList(splits.right().split(" ")));
                edges.computeIfAbsent(splits.left(), s -> new ArrayList<>()).addAll(lst);
                lst.forEach(dst ->
                    edges.computeIfAbsent(dst, d -> new ArrayList<String>())
                        .add(splits.left()));
            }
            return new Graph(edges);
        }
        
        public Graph copy() {
            return new Graph(this.edges.keySet().stream()
                .collect(toMap(
                        k -> k,
                        k -> this.edges.get(k).stream().toList())));
        }
        
        public Graph combineNodes(
                final String node1, final String node2, final String newNode
        ) {
            this.edges.put(newNode, concatAll(
                this.edges.get(node1).stream().filter(d -> !d.equals(node2)).toList(),
                this.edges.get(node2).stream().filter(d -> !d.equals(node1)).toList()
            ));
            for (final String node : Set.of(node1, node2)) {
                for (final String dst : this.edges.get(node)) {
                    final List<String> lst = this.edges.get(dst).stream()
                        .map(d -> d.equals(node) ? newNode : d)
                        .toList();
                    this.edges.put(dst, lst);
                }
                this.edges.remove(node);
            }
            return new Graph(this.edges);
        }
    }
}

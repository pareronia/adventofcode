import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_07 extends SolutionBase<AoC2020_07.Graph, Long, Integer> {
	
	private static final String SHINY_GOLD = "shiny gold";
	
	private AoC2020_07(final boolean debug) {
		super(debug);
	}
	
	public static AoC2020_07 create() {
		return new AoC2020_07(false);
	}

	public static AoC2020_07 createDebug() {
		return new AoC2020_07(true);
	}
	
	@Override
    protected Graph parseInput(final List<String> inputs) {
	    return Graph.fromInput(inputs);
	}

	@Override
	public Long solvePart1(final Graph graph) {
		return graph.edges().stream()
				.map(Edge::src)
				.distinct()
				.filter(src -> !src.equals(SHINY_GOLD))
				.filter(src -> graph.containsPath(src, SHINY_GOLD))
				.count();
	}

	@Override
	public Integer solvePart2(final Graph graph) {
		return graph.countWeights(SHINY_GOLD);
	}

	@Samples({
	    @Sample(method = "part1", input = TEST1, expected = "4"),
	    @Sample(method = "part2", input = TEST1, expected = "32"),
	    @Sample(method = "part2", input = TEST2, expected = "126"),
	})
	public static void main(final String[] args) throws Exception {
	    AoC2020_07.create().run();
	}
	
	private static final String TEST1 = """
	        light red bags contain 1 bright white bag, 2 muted yellow bags.
	        dark orange bags contain 3 bright white bags, 4 muted yellow bags.
	        bright white bags contain 1 shiny gold bag.
	        muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
	        shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
	        dark olive bags contain 3 faded blue bags, 4 dotted black bags.
	        vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
	        faded blue bags contain no other bags.
	        dotted black bags contain no other bags.
	        """;
	private static final String TEST2 = """
	        shiny gold bags contain 2 dark red bags.
	        dark red bags contain 2 dark orange bags.
	        dark orange bags contain 2 dark yellow bags.
	        dark yellow bags contain 2 dark green bags.
	        dark green bags contain 2 dark blue bags.
	        dark blue bags contain 2 dark violet bags.
	        dark violet bags contain no other bags.
	        """;
	
	record Edge(String src, String dst, int weight) { }
	
	record Graph(Set<Edge> edges, Map<String, Boolean> paths, Map<String, Integer> weights) {
	    
	    private Graph(final Set<Edge> edges) {
	        this(edges, new HashMap<>(), new HashMap<>());
	    }
	    
	    public static Graph fromInput(final List<String> inputs) {
            final Set<Edge> edges = inputs.stream()
                .flatMap(line -> {
                    final String[] sp = line.split(" contain ");
                    final String source = sp[0].split(" bags")[0];
                    return Arrays.stream(sp[1].split(", "))
                            .filter(r -> !r.equals("no other bags."))
                            .map(r -> {
                                final String[] contained = r.split(" ");
                                return new Edge(
                                        source,
                                        contained[1] + " " + contained[2],
                                        Integer.parseInt(contained[0]));
                            });
                })
                .collect(toSet());
            return new Graph(edges);
	    }
		
		public boolean containsPath(final String src, final String dst) {
			if (!paths.containsKey(src)) {
				paths.put(
					src,
					this.edges.stream()
						.filter(e -> e.src().equals(src))
						.anyMatch(e -> e.dst().equals(dst) || containsPath(e.dst(), dst)));
			}
			return paths.get(src);
		}
		
		public int countWeights(final String src) {
			if (!weights.containsKey(src)) {
				weights.put(
					src,
					this.edges.stream()
						.filter(e -> e.src().equals(src))
						.mapToInt(e -> e.weight() * (1 + countWeights(e.dst())))
						.sum());
			}
			return weights.get(src);
		}
	}
}
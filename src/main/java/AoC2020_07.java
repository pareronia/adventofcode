import static java.lang.Boolean.FALSE;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.aocd.Aocd;

public class AoC2020_07 extends AoCBase {
	
	private static final String SHINY_GOLD = "shiny gold";
	
	private final Graph graph;
	
	private AoC2020_07(final List<String> input, final boolean debug) {
		super(debug);
		this.graph = parse(input);
	}
	
	public static AoC2020_07 create(final List<String> input) {
		return new AoC2020_07(input, false);
	}

	public static AoC2020_07 createDebug(final List<String> input) {
		return new AoC2020_07(input, true);
	}
	
	private Graph parse(final List<String> inputs) {
		return new Graph(
			inputs.stream()
			.flatMap(line -> {
				final String[] sp = line.split(" contain ");
				final String source = sp[0].split(" bags")[0];
				return asList(sp[1].split(", ")).stream()
						.filter(r -> !r.equals("no other bags."))
						.map(r -> {
							final String[] contained = r.split(" ");
							return new Edge(source,
									contained[1] + " " + contained[2],
									Integer.parseInt(contained[0]));
						});
			})
			.collect(toSet())
		);
	}

	@Override
	public Long solvePart1() {
		return this.graph.edges().stream()
				.map(Edge::src)
				.filter(src -> !src.equals(SHINY_GOLD))
				.distinct()
				.filter(src -> this.graph.containsPath(src, SHINY_GOLD))
				.count();
	}

	@Override
	public Integer solvePart2() {
		return this.graph.countWeights(SHINY_GOLD);
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2020_07.createDebug(TEST1).solvePart1() == 4;
		assert AoC2020_07.createDebug(TEST1).solvePart2() == 32;
		assert AoC2020_07.createDebug(TEST2).solvePart2() == 126;

		final List<String> input = Aocd.getData(2020, 7);
		lap("Part 1", () -> AoC2020_07.create(input).solvePart1());
		lap("Part 2", () -> AoC2020_07.create(input).solvePart2());
	}
	
	private static final List<String> TEST1 = splitLines(
			"light red bags contain 1 bright white bag, 2 muted yellow bags.\r\n" +
			"dark orange bags contain 3 bright white bags, 4 muted yellow bags.\r\n" +
			"bright white bags contain 1 shiny gold bag.\r\n" +
			"muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.\r\n" +
			"shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.\r\n" +
			"dark olive bags contain 3 faded blue bags, 4 dotted black bags.\r\n" +
			"vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.\r\n" +
			"faded blue bags contain no other bags.\r\n" +
			"dotted black bags contain no other bags."
	);
	private static final List<String> TEST2 = splitLines(
			"shiny gold bags contain 2 dark red bags.\r\n" +
			"dark red bags contain 2 dark orange bags.\r\n" +
			"dark orange bags contain 2 dark yellow bags.\r\n" +
			"dark yellow bags contain 2 dark green bags.\r\n" +
			"dark green bags contain 2 dark blue bags.\r\n" +
			"dark blue bags contain 2 dark violet bags.\r\n" +
			"dark violet bags contain no other bags."
	);
	
	record Edge(String src, String dst, int weight) { }
	
	record Graph(Set<Edge> edges, Map<String, Boolean> paths, Map<String, Integer> weights) {
	    
	    public Graph(final Set<Edge> edges) {
	        this(edges, new HashMap<>(), new HashMap<>());
	    }
		
		public boolean containsPath(final String src, final String dst) {
			if (!paths.containsKey(src)) {
				paths.put(
					src,
					this.edges.stream()
						.filter(e -> e.src().equals(src))
						.map(e -> e.dst().equals(dst) || containsPath(e.dst(), dst))
						.reduce(FALSE, (a, b) -> a || b)
				);
			}
			return paths.get(src);
		}
		
		public Integer countWeights(final String src) {
			if (!weights.containsKey(src)) {
				weights.put(
					src,
					this.edges.stream()
						.filter(e -> e.src().equals(src))
						.map(e -> e.weight() * (1 + countWeights(e.dst())))
						.reduce(0, (a, b) -> a + b)
				);
			}
			return weights.get(src);
		}
	}
}
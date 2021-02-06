import static java.lang.Boolean.FALSE;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.aocd.Aocd;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

public class AoC2020_07 extends AoCBase {
	
	private static final String SHINY_GOLD = "shiny gold";
	
	private final Graph graph;
	
	private AoC2020_07(List<String> input, boolean debug) {
		super(debug);
		this.graph = parse(input);
	}
	
	public static AoC2020_07 create(List<String> input) {
		return new AoC2020_07(input, false);
	}

	public static AoC2020_07 createDebug(List<String> input) {
		return new AoC2020_07(input, true);
	}
	
	private Graph parse(List<String> inputs) {
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
									Integer.valueOf(contained[0]));
						});
			})
			.collect(toSet())
		);
	}

	@Override
	public long solvePart1() {
		return this.graph.getEdges().stream()
				.map(Edge::getSrc)
				.filter(src -> !src.equals(SHINY_GOLD))
				.distinct()
				.filter(src -> this.graph.containsPath(src, SHINY_GOLD))
				.count();
	}

	@Override
	public long solvePart2() {
		return this.graph.countWeights(SHINY_GOLD);
	}

	public static void main(String[] args) throws Exception {
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
	
	@Value
	private static final class Edge {
		private final String src;
		private final String dst;
		private final Integer weight;
	}
	
	@RequiredArgsConstructor
	@Getter
	private static final class Graph {
		private final Set<Edge> edges;
		private final Map<String, Boolean> paths = new HashMap<>();
		private final Map<String, Integer> weights = new HashMap<>();
		
		public boolean containsPath(String src, String dst) {
			if (!paths.containsKey(src)) {
				paths.put(
					src,
					this.edges.stream()
						.filter(e -> e.getSrc().equals(src))
						.map(e -> e.getDst().equals(dst) || containsPath(e.getDst(), dst))
						.reduce(FALSE, (a, b) -> a || b)
				);
			}
			return paths.get(src);
		}
		
		public Integer countWeights(String src) {
			if (!weights.containsKey(src)) {
				weights.put(
					src,
					this.edges.stream()
						.filter(e -> e.getSrc().equals(src))
						.map(e -> e.getWeight() * (1 + countWeights(e.getDst())))
						.reduce(0, (a, b) -> a + b)
				);
			}
			return weights.get(src);
		}
	}
}
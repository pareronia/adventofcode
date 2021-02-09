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

public class AoC2019_06 extends AoCBase {
	
	private static final String CENTER_OF_MASS = "COM";
	private static final String YOU = "YOU";
	private static final String SANTA = "SAN";
	
	private final Graph graph;
	
	private AoC2019_06(List<String> input, boolean debug) {
		super(debug);
		this.graph = parse(input);
	}
	
	public static AoC2019_06 create(List<String> input) {
		return new AoC2019_06(input, false);
	}

	public static AoC2019_06 createDebug(List<String> input) {
		return new AoC2019_06(input, true);
	}
	
	private Graph parse(List<String> inputs) {
		return new Graph(
			inputs.stream()
			.map(line -> {
				final String[] sp = line.split("\\)");
				return new Edge(sp[0], sp[1]);
			})
			.collect(toSet())
		);
	}

	@Override
	public long solvePart1() {
		return this.graph.getEdges().stream()
				.map(Edge::getDst)
				.map(dst -> this.graph.countStepsUp(dst, CENTER_OF_MASS))
				.reduce(0, (a, b) -> a + b);
	}

	@Override
	public long solvePart2() {
		return this.graph.edges.stream()
			.map(Edge::getDst)
			.filter(dst -> !asList(YOU, SANTA).contains(dst))
			.filter(dst -> this.graph.containsPath(dst, YOU))
			.filter(dst -> this.graph.containsPath(dst, SANTA))
			.map(dst -> this.graph.countStepsUp(YOU, dst)
						+ this.graph.countStepsUp(SANTA, dst)
						- 2)
			.reduce(Integer.MAX_VALUE, (a, b) -> a < b ? a : b);
	}

	public static void main(String[] args) throws Exception {
		assert AoC2019_06.createDebug(TEST1).solvePart1() == 42;
		assert AoC2019_06.createDebug(TEST2).solvePart2() == 4;

		final List<String> input = Aocd.getData(2019, 6);
		lap("Part 1", () -> AoC2019_06.create(input).solvePart1());
		lap("Part 2", () -> AoC2019_06.create(input).solvePart2());
	}
	
	private static final List<String> TEST1 = splitLines(
			"COM)B\r\n" +
			"B)C\r\n" +
			"C)D\r\n" +
			"D)E\r\n" +
			"E)F\r\n" +
			"B)G\r\n" +
			"G)H\r\n" +
			"D)I\r\n" +
			"E)J\r\n" +
			"J)K\r\n" +
			"K)L"
	);
	private static final List<String> TEST2 = splitLines(
			"COM)B\r\n" +
			"B)C\r\n" +
			"C)D\r\n" +
			"D)E\r\n" +
			"E)F\r\n" +
			"B)G\r\n" +
			"G)H\r\n" +
			"D)I\r\n" +
			"E)J\r\n" +
			"J)K\r\n" +
			"K)L\r\n" +
			"K)YOU\r\n" +
			"I)SAN"
	);
	
	@Value
	private static final class Edge {
		private final String src;
		private final String dst;
	}
	
	@RequiredArgsConstructor
	@Getter
	private static final class Graph {
		private final Set<Edge> edges;
		private final Map<String, Boolean> paths = new HashMap<>();
		private final Map<String, Integer> stepsUp = new HashMap<>();
		
		public Integer countStepsUp(String from, String to) {
			final String key = from + to;
			if (!stepsUp.containsKey(key)) {
				stepsUp.put(
						key,
						this.edges.stream()
							.filter(e -> e.getDst().equals(from))
							.map(e -> {
								if (e.getSrc().equals(to)) {
									return 1;
								} else {
									return 1 + countStepsUp(e.getSrc(), to);
								}
							})
							.findFirst().orElseThrow(() -> new RuntimeException())
				);
			}
			return stepsUp.get(key);
		}
		
		public boolean containsPath(String src, String dst) {
			final String key = src + dst;
			if (!paths.containsKey(key)) {
				paths.put(
					key,
					this.edges.stream()
						.filter(e -> e.getSrc().equals(src))
						.map(e -> e.getDst().equals(dst) || containsPath(e.getDst(), dst))
						.reduce(FALSE, (a, b) -> a || b)
				);
			}
			return paths.get(key);
		}
	}
}
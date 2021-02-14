import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.CollectionUtils.disjunction;

import java.util.ArrayList;
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
	public Integer solvePart1() {
		return this.graph.getEdges().stream()
				.map(Edge::getDst)
				.map(dst -> this.graph.pathToRoot(dst).size() - 1)
				.collect(summingInt(Integer::intValue));
	}

	@Override
	public Integer solvePart2() {
	    return disjunction(this.graph.pathToRoot(YOU),
	                       this.graph.pathToRoot(SANTA))
	            .size() - 2;
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
		private final String root = CENTER_OF_MASS;
		private final Map<String, List<String>> paths = new HashMap<>();
		
		public List<String> pathToRoot(String from) {
		    if (!paths.containsKey(from)) {
                final Edge edge = findEdgeWithDst(from);
                final List<String> path = new ArrayList<>();
                path.add(from);
                if (edge.getSrc().equals(root)) {
                    path.add(root);
                } else {
                    path.addAll(pathToRoot(edge.getSrc()));
                }
                paths.put(from, path);
		    }
		    return paths.get(from);
		}
		
		private Edge findEdgeWithDst(String dst) {
		    final List<Edge> found = this.edges.stream()
		        .filter(e -> e.getDst().equals(dst))
		        .collect(toList());
		    assert found.size() == 1;
		    return found.get(0);
		}
	}
}
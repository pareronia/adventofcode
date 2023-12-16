import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2019_06 extends AoCBase {
	
	private static final String CENTER_OF_MASS = "COM";
	private static final String YOU = "YOU";
	private static final String SANTA = "SAN";
	
	private final Graph graph;
	
	private AoC2019_06(final List<String> input, final boolean debug) {
		super(debug);
		this.graph = parse(input);
	}
	
	public static AoC2019_06 create(final List<String> input) {
		return new AoC2019_06(input, false);
	}

	public static AoC2019_06 createDebug(final List<String> input) {
		return new AoC2019_06(input, true);
	}
	
	private Graph parse(final List<String> inputs) {
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
		return this.graph.edges().stream()
				.map(Edge::dst)
				.map(dst -> this.graph.pathToRoot(dst).size() - 1)
				.collect(summingInt(Integer::intValue));
	}

	@Override
	public Integer solvePart2() {
	    final Set<String> you = new HashSet<>(this.graph.pathToRoot(YOU));
	    final Set<String> santa = new HashSet<>(this.graph.pathToRoot(SANTA));
	    return (int) Stream.concat(
	            you.stream().filter(y -> !santa.contains(y)),
	            santa.stream().filter(s -> !you.contains(s)))
	        .distinct().count() - 2;
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2019_06.createDebug(TEST1).solvePart1() == 42;
		assert AoC2019_06.createDebug(TEST2).solvePart2() == 4;

        final Puzzle puzzle = Aocd.puzzle(2019, 6);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2019_06.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2019_06.create(inputData)::solvePart2)
        );
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
	
	record Edge(String src, String dst) { }
	
	record Graph(Set<Edge> edges, String root, Map<String, List<String>> paths) {
		
		public Graph(final Set<Edge> edges) {
		    this(edges, CENTER_OF_MASS, new HashMap<>());
		}
		
		public List<String> pathToRoot(final String from) {
		    if (!paths.containsKey(from)) {
                final Edge edge = findEdgeWithDst(from);
                final List<String> path = new ArrayList<>();
                path.add(from);
                if (edge.src().equals(root)) {
                    path.add(root);
                } else {
                    path.addAll(pathToRoot(edge.src()));
                }
                paths.put(from, path);
		    }
		    return paths.get(from);
		}
		
		private Edge findEdgeWithDst(final String dst) {
		    final List<Edge> found = this.edges.stream()
		        .filter(e -> e.dst().equals(dst))
		        .collect(toList());
		    assert found.size() == 1;
		    return found.get(0);
		}
	}
}
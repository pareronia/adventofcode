import static java.util.stream.Collectors.toList;

import java.util.List;

import com.github.pareronia.aocd.Aocd;

public class AoC2016_03 extends AoCBase {

	private final List<List<Integer>> triangles;
	
	private AoC2016_03(List<String> input, boolean debug) {
		super(debug);
		this.triangles = input.stream()
				.map(s -> List.of(s.substring(0, 5), s.substring(5, 10), s.substring(10, 15)).stream()
							.map(String::strip).map(Integer::valueOf).collect(toList())
				)
				.collect(toList());
	}
	
	private boolean valid(List<Integer> t) {
		assert t.size() == 3;
		return t.get(0) + t.get(1) > t.get(2) 
				&& t.get(0) + t.get(2) > t.get(1)
				&& t.get(1) + t.get(2) > t.get(0);
	}
	
	public static final AoC2016_03 create(List<String> input) {
		return new AoC2016_03(input, false);
	}

	public static final AoC2016_03 createDebug(List<String> input) {
		return new AoC2016_03(input, true);
	}
	
	@Override
	public Integer solvePart1() {
		return (int) triangles.stream().filter(this::valid).count();
	}

	@Override
	public Integer solvePart2() {
		int valid = 0;
		for (int i = 0; i < triangles.size(); i += 3) {
			for (int j = 0; j < 3; j++) {
				if (valid(List.of(triangles.get(i).get(j), triangles.get(i+1).get(j), triangles.get(i+2).get(j)))) {
					valid++;
				}
			}
		}
		return valid;
	}

	public static void main(String[] args) throws Exception {
		assert AoC2016_03.createDebug(TEST1).solvePart1() == 0;
		assert AoC2016_03.createDebug(TEST2).solvePart1() == 1;
		assert AoC2016_03.createDebug(TEST3).solvePart2() == 2;
		
		final List<String> input = Aocd.getData(2016, 3);
		lap("Part 1", () -> AoC2016_03.create(input).solvePart1());
		lap("Part 2", () -> AoC2016_03.create(input).solvePart2());
	}

	private static final List<String> TEST1 = splitLines("    5   10   25");
	private static final List<String> TEST2 = splitLines("    3    4    5");
	private static final List<String> TEST3 = splitLines(
			"    5    3    6\r\n" + 
			"   10    4    8\r\n" + 
			"   25    5   10"
	);
}

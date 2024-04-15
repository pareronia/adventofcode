import static com.github.pareronia.aoc.AssertUtils.unreachable;
import static java.util.stream.Collectors.toList;

import java.util.List;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_05 extends SolutionBase<List<String>, Integer, Integer> {
	
	private AoC2020_05(final boolean debug) {
		super(debug);
	}
	
	public static AoC2020_05 create() {
		return new AoC2020_05(false);
	}

	public static AoC2020_05 createDebug() {
		return new AoC2020_05(true);
	}
	
	private List<String> translated(final List<String> inputs) {
		return inputs.stream()
				.map(s -> s.replace('F', '0').replace('B', '1')
							.replace('L', '0').replace('R', '1'))
				.sorted()
				.collect(toList());
	}
	
	private int asInt(final String s) {
	    return Integer.parseInt(s, 2);
	}
	
	@Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }

    @Override
	public Integer solvePart1(final List<String> inputs) {
		final List<String> list = translated(inputs);
	    return asInt(list.get(list.size() - 1));
	}

	@Override
	public Integer solvePart2(final List<String> inputs) {
		final int last = inputs.get(0).length() - 1;
		final List<String> list = translated(inputs);
		for (int i = 1; i < list.size() - 1; i++) {
			if (list.get(i).charAt(last) == list.get(i - 1).charAt(last)) {
				return asInt(list.get(i)) - 1;
			}
		}
		throw unreachable();
	}

	@Samples({
	    @Sample(method = "part1", input = TEST1, expected = "820"),
	    @Sample(method = "part2", input = TEST2, expected = "3"),
	})
	public static void main(final String[] args) throws Exception {
		AoC2020_05.create().run();
	}
	
	private static final String TEST1 = """
	        FBFBBFFRLR
	        BFFFBBFRRR
	        FFFBBBFRRR
	        BBFFBBFRLL
	        """;
	private static final String TEST2 = """
	        FFFFFFFLLL
	        FFFFFFFLLR
	        FFFFFFFLRL
	        FFFFFFFRLL
	        FFFFFFFRLR
	        """;
}
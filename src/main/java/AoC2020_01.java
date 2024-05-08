import static com.github.pareronia.aoc.AssertUtils.unreachable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_01 extends SolutionBase<List<Integer>, Long, Long> {
	
	private AoC2020_01(final boolean debug) {
		super(debug);
	}
	
	public static AoC2020_01 create() {
		return new AoC2020_01(false);
	}

	public static AoC2020_01 createDebug() {
		return new AoC2020_01(true);
	}

	@Override
    protected List<Integer> parseInput(final List<String> inputs) {
        return inputs.stream().map(Integer::valueOf).toList();
    }

    @Override
	public Long solvePart1(final List<Integer> numbers) {
		final Set<Integer> seen = new HashSet<>(numbers.size());
		for (final Integer n1 : numbers) {
			seen.add(n1);
			final Integer n2 = 2020 - n1;
			if (seen.contains(n2)) {
				return (long) (n1 * n2);
			}
		}
		throw unreachable();
	}

	@Override
	public Long solvePart2(final List<Integer> numbers) {
		final Set<Integer> seen = new HashSet<>(numbers.size());
		for (int i = 0; i < numbers.size(); i++) {
			final Integer n1 = numbers.get(i);
			seen.add(n1);
			for (final Integer n2 : numbers.subList(i, numbers.size())) {
				final Integer n3 = 2020 - n1 - n2;
				if (seen.contains(n3)) {
					return (long) (n1 * n2 * n3);
				}
			}
		}
		throw unreachable();
	}

	@Samples({
	    @Sample(method = "part1", input = TEST, expected = "514579"),
	    @Sample(method = "part2", input = TEST, expected = "241861950"),
	})
	public static void main(final String[] args) throws Exception {
	    AoC2020_01.create().run();
	}
	
	private static final String TEST = """
	        1721
	        979
	        366
	        299
	        675
	        1456
	        """;
}

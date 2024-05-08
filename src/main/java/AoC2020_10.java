import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.IterTools.enumerate;
import static com.github.pareronia.aoc.Utils.last;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aoc.Counter;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_10 extends SolutionBase<List<Integer>, Long, Long> {
	
	private AoC2020_10(final boolean debug) {
		super(debug);
	}
	
	public static AoC2020_10 create() {
		return new AoC2020_10(false);
	}

	public static AoC2020_10 createDebug() {
		return new AoC2020_10(true);
	}
	
	@Override
    protected List<Integer> parseInput(final List<String> inputs) {
		final List<Integer> numbers = new ArrayList<>(List.of(0));
		inputs.stream()
		    .map(Integer::valueOf)
		    .sorted()
		    .collect(toCollection(() -> numbers));
	    numbers.add(last(numbers) + 3);
        return numbers;
    }

    @Override
	public Long solvePart1(final List<Integer> numbers) {
	    final Counter<Integer> jumps = new Counter<>(
	        range(numbers.size()).intStream().skip(1)
	            .mapToObj(i -> numbers.get(i) - numbers.get(i - 1)));
	    return jumps.get(1) * jumps.get(3);
	}
	
    // 0: 1, 1: 1, 4: 1, 5: 1, 6: 2, 7: 4, 10: 4, 11: 4, 12: 8, 15: 8, 16: 8, 19: 8
	@Override
	public Long solvePart2(final List<Integer> numbers) {
	    final Map<Integer, Long> map = new HashMap<>();
	    map.put(0, 1L);
	    enumerate(numbers.stream().skip(1)).forEach(e -> {
	        range(e.index(), -1, -1).intStream()
	                .map(numbers::get)
	                .filter(j -> e.value() - j <= 3)
                    .forEach(j -> map.merge(e.value(), map.get(j), Long::sum));
	        log(map);
	    });
		return map.get(last(numbers));
	}

	@Samples({
	    @Sample(method = "part1", input = TEST1, expected = "35"),
	    @Sample(method = "part1", input = TEST2, expected = "220"),
	    @Sample(method = "part2", input = TEST1, expected = "8"),
	    @Sample(method = "part2", input = TEST2, expected = "19208"),
	})
	public static void main(final String[] args) throws Exception {
	    AoC2020_10.create().run();
	}
	
	private static final String TEST1 = """
	        16
	        10
	        15
	        5
	        1
	        11
	        7
	        19
	        6
	        12
	        4
	        """;
	private static final String TEST2 = """
	        28
	        33
	        18
	        42
	        31
	        14
	        46
	        20
	        48
	        47
	        24
	        23
	        49
	        45
	        19
	        38
	        39
	        11
	        1
	        32
	        25
	        35
	        8
	        17
	        7
	        9
	        4
	        2
	        34
	        10
	        3
	        """;
}
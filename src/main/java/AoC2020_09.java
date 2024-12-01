import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.StringOps.splitLines;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_09 extends SolutionBase<List<Long>, Long, Long> {
	
	private AoC2020_09(final boolean debug) {
		super(debug);
	}
	
	public static AoC2020_09 create() {
		return new AoC2020_09(false);
	}

	public static AoC2020_09 createDebug() {
		return new AoC2020_09(true);
	}
	
	@Override
    protected List<Long> parseInput(final List<String> inputs) {
        return inputs.stream().map(Long::parseLong).toList();
    }

    private Long findInvalidNumber(final List<Long> numbers, final int windowSize) {
	    return IntStream.range(windowSize, numbers.size())
            .filter(i -> numbers.subList(i - windowSize, i).stream()
                    .noneMatch(n -> numbers.subList(i - windowSize, i)
                                .contains(numbers.get(i) - n)))
            .mapToObj(numbers::get)
            .findFirst().orElseThrow();
	}
	
	@Override
	public Long solvePart1(final List<Long> numbers) {
		return findInvalidNumber(numbers, 25);
	}
	
	private long findWeakness(final List<Long> numbers, final int windowSize) {
		final long target = findInvalidNumber(numbers, windowSize);
		final int targetPos = numbers.indexOf(target);
		final Stream<List<Long>> sublists = range(targetPos + 1).intStream().boxed()
		    .flatMap(i -> IntStream.range(i + 1, targetPos + 1)
                        .filter(j -> j - i >= 2)
                        .mapToObj(j -> numbers.subList(i, j)));
		return sublists
		    .filter(s -> s.stream().mapToLong(Long::longValue).sum() == target)
		    .mapToLong(s ->
		        s.stream().mapToLong(Long::longValue).min().getAsLong()
		        + s.stream().mapToLong(Long::longValue).max().getAsLong())
		    .findFirst().getAsLong();
	}

	@Override
	public Long solvePart2(final List<Long> numbers) {
		return findWeakness(numbers, 25);
	}

	@Override
    public void samples() {
	    final AoC2020_09 test = AoC2020_09.createDebug();
	    assert test.findInvalidNumber(test.parseInput(splitLines(TEST)), 5) == 127;
	    assert test.findWeakness(test.parseInput(splitLines(TEST)), 5) == 62;
    }

    public static void main(final String[] args) throws Exception {
        AoC2020_09.create().run();
	}
	
	private static final String TEST = """
	        35
	        20
	        15
	        25
	        47
	        40
	        62
	        55
	        65
	        95
	        102
	        117
	        150
	        182
	        127
	        219
	        299
	        277
	        309
	        576
	        """;
}
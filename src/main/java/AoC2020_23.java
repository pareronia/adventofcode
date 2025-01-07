import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_23 extends SolutionBase<List<Integer>, String, Long> {
	
	private AoC2020_23(final boolean debug) {
		super(debug);
	}
	
	public static final AoC2020_23 create() {
		return new AoC2020_23(false);
	}

	public static final AoC2020_23 createDebug() {
		return new AoC2020_23(true);
	}
	
	@Override
    protected List<Integer> parseInput(final List<String> inputs) {
		final Integer[] digits
		        = StringOps.getDigits(inputs.get(0), inputs.get(0).length());
        return Arrays.stream(digits).toList();
    }

    private int[] prepareCups(final List<Integer> labels) {
        final int[] cups = new int[labels.size() + 1];
        range(labels.size()).forEach(i -> {
            cups[labels.get(i)] = labels.get((i + 1) % labels.size());
        });
        return cups;
    }

    private int doMove(
            final int[] cups,
            final int current,
            final int min,
            final int max
    ) {
        final int c = current;
        final int p1 = cups[c];
        final int p2 = cups[p1];
        final int p3 = cups[p2];
        cups[c] = cups[p3];
        final Set<Integer> pickup = Set.of(p1, p2, p3);
        int d = c - 1;
        if (d < min) {
            d = max;
        }
		while (pickup.contains(d)) {
			d--;
			if (d < min) {
				d = max;
			}
		}
		cups[p3] = cups[d];
		cups[d] = p1;
		return cups[c];
    }

	@Override
	public String solvePart1(final List<Integer> labels) {
	    final int[] cups = prepareCups(labels);
		int current = labels.get(0);
		for (int i = 0; i < 100; i++) {
			current = doMove(cups, current, 1, 9);
		}
		return IntStream.iterate(cups[1], cup -> cups[cup])
		        .takeWhile(cup -> cup != 1)
		        .mapToObj(String::valueOf)
		        .collect(joining());
	}
	
	@Override
	public Long solvePart2(final List<Integer> labels) {
		final List<Integer> newLabels = new ArrayList<>(1_000_000);
		newLabels.addAll(labels);
		Stream.iterate(10, i -> i + 1).limit(1_000_000 - labels.size())
			.collect(toCollection(() -> newLabels));
		final int[] cups = prepareCups(newLabels);
		int current = labels.get(0);
		for (int i = 0; i < 10_000_000; i++) {
			current = doMove(cups, current, 1, 1_000_000);
		}
		final long star1 = cups[1];
		final long star2 = cups[cups[1]];
		return star1 * star2;
	}
	
	@Samples({
        @Sample(method = "part1", input = TEST, expected = "67384529"),
        @Sample(method = "part2", input = TEST, expected = "149245887792"),
	})
	public static void main(final String[] args) throws Exception {
	    AoC2020_23.create().run();
	}
	
	private static final String TEST = "389125467";
}

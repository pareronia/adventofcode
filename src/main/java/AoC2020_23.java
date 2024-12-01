import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_23 extends SolutionBase<List<Integer>, Long, Long> {
	
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

    private Map<Integer, Cup> prepareCups(final List<Integer> labels) {
		final Map<Integer, Cup> cups = new HashMap<>();
		final Integer first = labels.get(0);
		final Integer last = labels.get(labels.size() - 1);
		final Cup tail = new Cup(last, (Cup) null);
		Cup prev = tail;
		Cup cup;
		for (int i = labels.size() - 2; i > 0; i--) {
			final Integer label = labels.get(i);
			cup = new Cup(label, prev);
			cups.put(label, cup);
			prev = cup;
		}
		final Cup head = new Cup(first, prev);
		cups.put(first, head);
		tail.setNext(head);
		cups.put(last, tail);
		return cups;
	}
	
    private Cup doMove(
            final Map<Integer, Cup> cups,
            final Cup current,
            final Integer size,
            final Integer min,
            final Integer max) {
		final Cup p1 = current.getNext();
		final Cup p2 = p1.getNext();
		final Cup p3 = p2.getNext();
		current.setNext(p3.getNext());
		final List<Integer> pickup
		        = List.of(p1.getLabel(), p2.getLabel(), p3.getLabel());
		Integer d = current.getLabel() - 1;
		if (d < min) {
			d = max;
		}
		while (pickup.contains(d)) {
			d--;
			if (d < min) {
				d = max;
			}
		}
		final Cup destination = cups.get(d);
		p3.setNext(destination.getNext());
		destination.setNext(p1);
		return current.getNext();
	}
	
	@Override
	public Long solvePart1(final List<Integer> labels) {
		final Map<Integer, Cup> cups = prepareCups(labels);
		final int size = labels.size();
		final IntSummaryStatistics stats
		    = labels.stream().mapToInt(Integer::valueOf).summaryStatistics();
		final Integer min = stats.getMin();
		final Integer max = stats.getMax();
		Cup current = cups.get(labels.get(0));
		for (int i = 0; i < 100; i++) {
			current = doMove(cups, current, size, min, max);
		}
		Cup cup = cups.get(1);
		final StringBuilder result = new StringBuilder();
		while (cup.getNext().getLabel() != 1) {
			result.append(cup.getNext().getLabel());
			cup = cup.getNext();
		}
		return Long.valueOf(result.toString());
	}
	
	@Override
	public Long solvePart2(final List<Integer> labels) {
		final IntSummaryStatistics stats
		    = labels.stream().mapToInt(Integer::valueOf).summaryStatistics();
		final Integer min = stats.getMin();
		final Integer max = stats.getMax();
		final List<Integer> newLabels = new ArrayList<>(labels);
		Stream.iterate(max + 1, i -> i + 1).limit(1_000_000 - labels.size())
			.collect(toCollection(() -> newLabels));
		final Map<Integer, Cup> cups = prepareCups(newLabels);
		Cup current = cups.get(newLabels.get(0));
		for (int i = 0; i < 10_000_000; i++) {
			current = doMove(cups, current, 1_000_000, min, 1_000_000);
		}
		final Cup one = cups.get(1);
		final long star1 = one.getNext().getLabel().longValue();
		final long star2 = one.getNext().getNext().getLabel().longValue();
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
	
	
	private static final class Cup {
		private final Integer label;
		private Cup next;

        public Cup(final Integer label, final Cup next) {
            this.label = label;
            this.next = next;
        }

        public Integer getLabel() {
            return label;
        }

        public Cup getNext() {
            return next;
        }

        public void setNext(final Cup next) {
            this.next = next;
        }
        
		@Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Cup [label=").append(label).append("]");
            return builder.toString();
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Cup other = (Cup) obj;
            return Objects.equals(label, other.label) && Objects.equals(next, other.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(label, next);
        }
	}
}

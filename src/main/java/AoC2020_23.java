import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;

public class AoC2020_23 extends AoCBase {
	
	private final List<Integer> labels;
	
	private AoC2020_23(final List<String> input, final boolean debug) {
		super(debug);
		assert input.size() == 1;
		this.labels = Utils.asCharacterStream(input.get(0))
				.map(c -> new String(new char[] { c.charValue() }))
				.map(Integer::valueOf)
				.collect(toList());
	}
	
	public static final AoC2020_23 create(final List<String> input) {
		return new AoC2020_23(input, false);
	}

	public static final AoC2020_23 createDebug(final List<String> input) {
		return new AoC2020_23(input, true);
	}
	
	private Map<Integer, Cup> prepareCups() {
		final Map<Integer, Cup> cups = new HashMap<>();
		final Integer first = this.labels.get(0);
		final Integer last = this.labels.get(this.labels.size() - 1);
		final Cup tail = new Cup(last, (Cup) null);
		Cup prev = tail;
		Cup cup;
		for (int i = this.labels.size() - 2; i > 0; i--) {
			final Integer label = this.labels.get(i);
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
	
	private Cup doMove(final Map<Integer, Cup> cups, final Cup current, final Integer size, final Integer min, final Integer max) {
		final Cup p1 = current.getNext();
		final Cup p2 = p1.getNext();
		final Cup p3 = p2.getNext();
		current.setNext(p3.getNext());
		final List<Integer> pickup = asList(p1.getLabel(), p2.getLabel(), p3.getLabel());
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
	public Long solvePart1() {
		final Map<Integer, Cup> cups = prepareCups();
		final int size = this.labels.size();
		final IntSummaryStatistics stats
		    = this.labels.stream().mapToInt(Integer::valueOf).summaryStatistics();
		final Integer min = stats.getMin();
		final Integer max = stats.getMax();
		Cup current = cups.get(this.labels.get(0));
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
	public Long solvePart2() {
		final IntSummaryStatistics stats
		    = this.labels.stream().mapToInt(Integer::valueOf).summaryStatistics();
		final Integer min = stats.getMin();
		final Integer max = stats.getMax();
		Stream.iterate(max + 1, i -> i + 1).limit(1_000_000 - this.labels.size())
			.collect(toCollection(() -> this.labels));
		final Map<Integer, Cup> cups = prepareCups();
		Cup current = cups.get(this.labels.get(0));
		for (int i = 0; i < 10_000_000; i++) {
			current = doMove(cups, current, 1_000_000, min, 1_000_000);
		}
		final Cup one = cups.get(1);
		final long star1 = one.getNext().getLabel().longValue();
		final long star2 = one.getNext().getNext().getLabel().longValue();
		return star1 * star2;
	}
	
	public static void main(final String[] args) throws Exception {
		assert AoC2020_23.createDebug(TEST).solvePart1() == 67384529;
		assert AoC2020_23.createDebug(TEST).solvePart2() == 149245887792L;
		
		final List<String> input = Aocd.getData(2020, 23);
		lap("Part 1", () -> AoC2020_23.create(input).solvePart1());
		lap("Part 2", () -> AoC2020_23.create(input).solvePart2());
	}
	
	private static final List<String> TEST = splitLines(
			"389125467"
	);
	
	
	private static final class Cup {
		private final Integer label;
		private Cup next;

        public Cup(final Integer label, final AoC2020_23.Cup next) {
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

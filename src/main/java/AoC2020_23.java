import static com.github.pareronia.aoc.Utils.max;
import static com.github.pareronia.aoc.Utils.min;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class AoC2020_23 extends AoCBase {
	
	private final List<Integer> labels;
	
	private AoC2020_23(List<String> input, boolean debug) {
		super(debug);
		assert input.size() == 1;
		this.labels = Utils.asCharacterStream(input.get(0))
				.map(c -> new String(new char[] { c.charValue() }))
				.map(Integer::valueOf)
				.collect(toList());
	}
	
	public static final AoC2020_23 create(List<String> input) {
		return new AoC2020_23(input, false);
	}

	public static final AoC2020_23 createDebug(List<String> input) {
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
	
	private Cup doMove(Map<Integer, Cup> cups, Cup current, Integer size, Integer min, Integer max) {
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
		final Integer min = min(this.labels.stream());
		final Integer max = max(this.labels.stream());
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
		final Integer min = min(this.labels.stream());
		final Integer max = max(this.labels.stream());
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
	
	public static void main(String[] args) throws Exception {
		assert AoC2020_23.createDebug(TEST).solvePart1() == 67384529;
		assert AoC2020_23.createDebug(TEST).solvePart2() == 149245887792L;
		
		final List<String> input = Aocd.getData(2020, 23);
		lap("Part 1", () -> AoC2020_23.create(input).solvePart1());
		lap("Part 2", () -> AoC2020_23.create(input).solvePart2());
	}
	
	private static final List<String> TEST = splitLines(
			"389125467"
	);
	
	
	@AllArgsConstructor
	@Getter
	@Setter
	@EqualsAndHashCode
	@ToString
	private static final class Cup {
		private final Integer label;
		@ToString.Exclude private Cup next;
	}
}

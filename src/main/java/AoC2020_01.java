import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aocd.Aocd;

public class AoC2020_01 extends AoCBase {
	
	private final List<Integer> numbers;

	private AoC2020_01(List<String> input, boolean debug) {
		super(debug);
		this.numbers = input.stream().map(Integer::valueOf).collect(toList());
	}
	
	public static AoC2020_01 create(List<String> input) {
		return new AoC2020_01(input, false);
	}
	
	@Override
	public long solvePart1() {
		final Set<Integer> seen = new HashSet<>();
		for (final Integer n1 : this.numbers) {
			seen.add(n1);
			final Integer n2 = 2020 - n1;
			if (seen.contains(n2)) {
				return n1 * n2;
			}
		}
		return 0L;
	}

	@Override
	public long solvePart2() {
		final Set<Integer> seen = new HashSet<>();
		for (int i = 0; i < this.numbers.size(); i++) {
			final Integer n1 = this.numbers.get(i);
			seen.add(n1);
			for (int j = i; j < this.numbers.size(); j++) {
				final Integer n2 = this.numbers.get(j);
				final Integer n3 = 2020 - n1 - n2;
				if (seen.contains(n3)) {
					return n1 * n2 * n3;
				}
			}
		}
		
		return 0L;
	}

	public static void main(String[] args) throws Exception {
		assert AoC2020_01.create(TEST).solvePart1() == 514579;
		assert AoC2020_01.create(TEST).solvePart2() == 241861950;
		
		final List<String> input = Aocd.getData(2020, 1);
		lap("Part 1", () -> AoC2020_01.create(input).solvePart1());
		lap("Part 2", () -> AoC2020_01.create(input).solvePart2());
	}
	
	private static final List<String> TEST = splitLines(
			"1721\r\n" +
			"979\r\n" +
			"366\r\n" +
			"299\r\n" +
			"675\r\n" +
			"1456"
	);
}

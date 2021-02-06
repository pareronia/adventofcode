import static com.github.pareronia.aoc.Utils.max;
import static com.github.pareronia.aoc.Utils.min;
import static java.util.stream.Collectors.summingLong;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.Range;

import com.github.pareronia.aocd.Aocd;

public class AoC2020_09 extends AoCBase {
	
	private final List<Long> numbers;
	
	private AoC2020_09(List<String> input, boolean debug) {
		super(debug);
		this.numbers = input.stream().map(Long::valueOf).collect(toList());
	}
	
	public static AoC2020_09 create(List<String> input) {
		return new AoC2020_09(input, false);
	}

	public static AoC2020_09 createDebug(List<String> input) {
		return new AoC2020_09(input, true);
	}
	
	private boolean containsTwoSummands(List<Long> numbers, Long sum) {
		final Set<Long> seen = new HashSet<>();
		for (final Long n1 : numbers) {
			seen.add(n1);
			final Long n2 = sum - n1;
			if (seen.contains(n2)) {
				return true;
			}
		}
		return false;
	}
	
	private long findInvalidNumber(Integer windowSize) {
		final Range<Integer> range = Range.between(windowSize, this.numbers.size());
		for (int i = range.getMinimum(); i < range.getMaximum(); i++) {
			final Long number = this.numbers.get(i);
			final List<Long> searchWindow = this.numbers.subList(i - windowSize, i);
			if (!containsTwoSummands(searchWindow, number)) {
				return number;
			}
		}
		throw new RuntimeException("Unsolvable");
	}
	
	@Override
	public long solvePart1() {
		return findInvalidNumber(25);
	}
	
	private List<List<Long>> collectAllSublistsBeforeTargetWithMinimumSize2(Integer targetPos) {
		final List<Long> window = this.numbers.subList(0, targetPos);
		final ArrayList<List<Long>> sublists = new ArrayList<>();
		for (int i = 0; i <= window.size(); i++) {
			for (int j = i + 1; j <= window.size(); j++) {
				if (j - i < 2) {
					continue;
				}
				sublists.add(window.subList(i, j));
			}
		}
		return sublists;
	}
	
	private long findWeakness(Integer windowSize) {
		final long target = findInvalidNumber(windowSize);
		final List<List<Long>> sublists
				= collectAllSublistsBeforeTargetWithMinimumSize2(this.numbers.indexOf(target));
		final List<List<Long>> sublistsWithSumEqualToTarget = sublists.stream()
			.filter(sl -> sl.stream().collect(summingLong(Long::longValue)) == target)
			.collect(toList());
		if (sublistsWithSumEqualToTarget.size() != 1) {
			throw new RuntimeException("Unsolvable");
		}
		final List<Long> subListWithSumEqualToTarget = sublistsWithSumEqualToTarget.get(0);
		return min(subListWithSumEqualToTarget.stream())
				+ max(subListWithSumEqualToTarget.stream());
	}

	@Override
	public long solvePart2() {
		return findWeakness(25);
	}

	public static void main(String[] args) throws Exception {
		assert AoC2020_09.createDebug(TEST).findInvalidNumber(5) == 127;
		assert AoC2020_09.createDebug(TEST).findWeakness(5) == 62;

		final List<String> input = Aocd.getData(2020, 9);
		lap("Part 1", () -> AoC2020_09.create(input).solvePart1());
		lap("Part 2", () -> AoC2020_09.create(input).solvePart2());
	}
	
	private static final List<String> TEST = splitLines(
			"35\r\n" +
			"20\r\n" +
			"15\r\n" +
			"25\r\n" +
			"47\r\n" +
			"40\r\n" +
			"62\r\n" +
			"55\r\n" +
			"65\r\n" +
			"95\r\n" +
			"102\r\n" +
			"117\r\n" +
			"150\r\n" +
			"182\r\n" +
			"127\r\n" +
			"219\r\n" +
			"299\r\n" +
			"277\r\n" +
			"309\r\n" +
			"576"
	);
}
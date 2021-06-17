import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.pareronia.aocd.Aocd;

public class AoC2016_06 extends AoCBase {

	private final List<String> inputs;
	
	private AoC2016_06(List<String> input, boolean debug) {
		super(debug);
		this.inputs = input;
	}
	
	private List<Map<Character, Integer>> getCounters() {
		final List<Map<Character, Integer>> counters = new ArrayList<>();
		for (int i = 0; i < inputs.get(0).length(); i ++) {
			counters.add(new HashMap<>());
		}
		for (String input : inputs) {
			for (int i = 0; i < input.length(); i ++) {
				counters.get(i).merge(input.charAt(i), 1, Integer::sum);
			}
		}
		return counters;
	}
	
	public static final AoC2016_06 create(List<String> input) {
		return new AoC2016_06(input, false);
	}

	public static final AoC2016_06 createDebug(List<String> input) {
		return new AoC2016_06(input, true);
	}
	
	@Override
	public String solvePart1() {
		return getCounters().stream()
				.map(c -> c.entrySet().stream()
						.max(comparing(Entry::getValue))
						.map(Entry::getKey).orElseThrow())
				.collect(toAString());
	}

	@Override
	public String solvePart2() {
		return getCounters().stream()
				.map(c -> c.entrySet().stream()
						.min(comparing(Entry::getValue))
						.map(Entry::getKey).orElseThrow())
				.collect(toAString());
	}

	public static void main(String[] args) throws Exception {
		assert AoC2016_06.createDebug(TEST).solvePart1().equals("easter");
		assert AoC2016_06.createDebug(TEST).solvePart2().equals("advent");
		
		final List<String> input = Aocd.getData(2016, 6);
		lap("Part 1", () -> AoC2016_06.create(input).solvePart1());
		lap("Part 2", () -> AoC2016_06.create(input).solvePart2());
	}

	private static final List<String> TEST = splitLines(
			"eedadn\r\n" +
			"drvtee\r\n" +
			"eandsr\r\n" +
			"raavrd\r\n" +
			"atevrs\r\n" +
			"tsrnev\r\n" +
			"sdttsa\r\n" +
			"rasrtv\r\n" +
			"nssdts\r\n" +
			"ntnada\r\n" +
			"svetve\r\n" +
			"tesnvt\r\n" +
			"vntsnd\r\n" +
			"vrdear\r\n" +
			"dvrsen\r\n" +
			"enarar"
	);
}

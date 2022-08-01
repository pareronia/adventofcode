import static java.util.stream.Collectors.toList;

import java.util.List;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2020_05 extends AoCBase {
	
	private final List<String> inputs;
	
	private AoC2020_05(final List<String> input, final boolean debug) {
		super(debug);
		this.inputs = input;
	}
	
	public static AoC2020_05 create(final List<String> input) {
		return new AoC2020_05(input, false);
	}

	public static AoC2020_05 createDebug(final List<String> input) {
		return new AoC2020_05(input, true);
	}
	
	private List<String> translated(final List<String> inputs) {
		return inputs.stream()
				.map(s -> s.replaceAll("F", "0").replaceAll("B", "1")
							.replaceAll("L", "0").replaceAll("R", "1"))
				.sorted()
				.collect(toList());
	}
	
	private int asInt(final String s) {
	    return Integer.parseInt(s, 2);
	}
	
	@Override
	public Integer solvePart1() {
		return translated(this.inputs).stream()
			.mapToInt(this::asInt)
			.max().orElseThrow();
	}

	@Override
	public Integer solvePart2() {
		final int last = this.inputs.get(0).length() - 1;
		final List<String> list = translated(this.inputs);
		for (int i = 0; i < list.size(); i++) {
			if (i + 1 == list.size()) {
				break;
			}
			if (list.get(i).charAt(last) == list.get(i + 1).charAt(last)) {
				return asInt(list.get(i)) + 1;
			}
		}
		throw new RuntimeException("Unsolvable");
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2020_05.createDebug(TEST1).solvePart1() == 820;
		assert AoC2020_05.createDebug(TEST2).solvePart2() == 3;
		
        final Puzzle puzzle = Aocd.puzzle(2020, 5);
		final List<String> input = puzzle.getInputData();
        puzzle.check(
           () -> lap("Part 1", AoC2020_05.create(input)::solvePart1),
           () -> lap("Part 2", AoC2020_05.create(input)::solvePart2)
	    );
	}
	
	private static final List<String> TEST1 = splitLines(
			"FBFBBFFRLR\r\n" +
			"BFFFBBFRRR\r\n" +
			"FFFBBBFRRR\r\n" +
			"BBFFBBFRLL"
	);
	private static final List<String> TEST2 = splitLines(
			"FFFFFFFLLL\r\n" +
			"FFFFFFFLLR\r\n" +
			"FFFFFFFLRL\r\n" +
			"FFFFFFFRLL\r\n" +
			"FFFFFFFRLR"
	);
}
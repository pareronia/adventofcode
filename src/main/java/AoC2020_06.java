import static com.github.pareronia.aoc.Utils.asCharacterStream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.SetUtils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2020_06 extends AoCBase {
	
	private final List<String> inputs;
	
	private AoC2020_06(final List<String> input, final boolean debug) {
		super(debug);
		this.inputs = input;
	}
	
	public static AoC2020_06 create(final List<String> input) {
		return new AoC2020_06(input, false);
	}

	public static AoC2020_06 createDebug(final List<String> input) {
		return new AoC2020_06(input, true);
	}
	
	@Override
	public Integer solvePart1() {
		return toBlocks(this.inputs).stream()
				.map(b -> String.join("", b))
				.collect(toList()).stream()
				.map(s -> asCharacterStream(s).collect(toSet()))
				.mapToInt(Set::size)
				.sum();
	}

	@Override
	public Integer solvePart2() {
		return toBlocks(this.inputs).stream()
				.map(bl -> bl.stream()
						.map(s -> asCharacterStream(s).collect(toSet()))
						.reduce(SetUtils::intersection)
						.orElseThrow())
				.mapToInt(Set::size)
				.sum();
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2020_06.createDebug(TEST).solvePart1() == 11;
		assert AoC2020_06.createDebug(TEST).solvePart2() == 6;
		
        final Puzzle puzzle = Aocd.puzzle(2020, 6);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2020_06.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2020_06.create(inputData)::solvePart2)
        );
	}
	
	private static final List<String> TEST = splitLines(
			"abc\r\n" +
			"\r\n" +
			"a\r\n" +
			"b\r\n" +
			"c\r\n" +
			"\r\n" +
			"ab\r\n" +
			"ac\r\n" +
			"\r\n" +
			"a\r\n" +
			"a\r\n" +
			"a\r\n" +
			"a\r\n" +
			"\r\n" +
			"b"
	);
}
import static com.github.pareronia.aoc.Utils.asCharacterStream;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.SetUtils;

import com.github.pareronia.aocd.Aocd;

public class AoC2020_06 extends AoCBase {
	
	private final List<String> inputs;
	
	private AoC2020_06(List<String> input, boolean debug) {
		super(debug);
		this.inputs = input;
	}
	
	public static AoC2020_06 create(List<String> input) {
		return new AoC2020_06(input, false);
	}

	public static AoC2020_06 createDebug(List<String> input) {
		return new AoC2020_06(input, true);
	}
	
	@Override
	public Integer solvePart1() {
		return toBlocks(this.inputs).stream()
				.map(b -> String.join("", b))
				.collect(toList()).stream()
				.map(s -> asCharacterStream(s).collect(toSet()))
				.map(Set::size)
				.collect(summingInt(Integer::intValue));
	}

	@Override
	public Integer solvePart2() {
		return toBlocks(this.inputs).stream()
				.map(bl -> bl.stream()
						.map(s -> asCharacterStream(s).collect(toSet()))
						.reduce(SetUtils::intersection)
						.orElseThrow(() -> new RuntimeException()))
				.map(Set::size)
				.collect(summingInt(Integer::intValue));
	}

	public static void main(String[] args) throws Exception {
		assert AoC2020_06.createDebug(TEST).solvePart1() == 11;
		assert AoC2020_06.createDebug(TEST).solvePart2() == 6;
		
		final List<String> input = Aocd.getData(2020, 6);
		lap("Part 1", () -> AoC2020_06.create(input).solvePart1());
		lap("Part 2", () -> AoC2020_06.create(input).solvePart2());
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
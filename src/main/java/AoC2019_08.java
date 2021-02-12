import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.StringUtils.countMatches;

import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.OCR;
import com.github.pareronia.aocd.Aocd;

public class AoC2019_08 extends AoCBase {
	
	private static final Integer WIDTH = 25;
	private static final Integer HEIGHT = 6;
	
	private final List<String> input;
	
	private AoC2019_08(List<String> input, boolean debug) {
		super(debug);
		this.input = input;
	}
	
	public static AoC2019_08 create(List<String> input) {
		return new AoC2019_08(input, false);
	}

	public static AoC2019_08 createDebug(List<String> input) {
		return new AoC2019_08(input, true);
	}
	
	private Grid parse(Integer width, Integer height) {
		assert this.input.size() == 1;
		return Grid.from(this.input.get(0), width * height);
	}
	
	@Override
	public Integer solvePart1() {
		final Grid layers = parse(WIDTH, HEIGHT);
		return layers.getRowsAsStringList().stream()
				.min(comparing(s -> countMatches(s, '0')))
				.map(s -> countMatches(s, '1') * countMatches(s, '2'))
				.orElseThrow(() -> new RuntimeException());
	}
	
	private String getImage(Integer width, Integer height) {
		final Grid layers = parse(width, height);
		return Stream.iterate(0, i -> i + 1).limit(layers.getWidth())
				.map(i -> layers.getRowsAsStringList().stream()
						.map(row -> row.charAt(i))
						.filter(ch -> ch !=  '2')
						.findFirst()
						.orElseThrow(() -> new RuntimeException()))
				.collect(toAString());
	}

	@Override
	public String solvePart2() {
		final Grid image = Grid.from(getImage(WIDTH, HEIGHT), WIDTH);
		log(image.replace('1', '\u2592').replace('0', ' '));
		return OCR.convert6(image, '1', '0');
	}

	public static void main(String[] args) throws Exception {
		assert AoC2019_08.createDebug(TEST).getImage(2, 2).equals("0110");

		final List<String> input = Aocd.getData(2019, 8);
		lap("Part 1", () -> AoC2019_08.create(input).solvePart1());
		lap("Part 2", () -> AoC2019_08.create(input).solvePart2());
	}
	
	private static final List<String> TEST = splitLines(
			"0222112222120000"
	);
}
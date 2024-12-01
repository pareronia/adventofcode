import static com.github.pareronia.aoc.StringUtils.countMatches;
import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.Comparator.comparing;

import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.OCR;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2019_08 extends SolutionBase<String, Integer, String> {
	
	private static final Integer WIDTH = 25;
	private static final Integer HEIGHT = 6;
	
	private AoC2019_08(final boolean debug) {
		super(debug);
	}
	
	public static AoC2019_08 create() {
		return new AoC2019_08(false);
	}

	public static AoC2019_08 createDebug() {
		return new AoC2019_08(true);
	}

	@Override
    protected String parseInput(final List<String> inputs) {
        return inputs.get(0);
    }
	
	private CharGrid parse(
	        final String input, final int width, final int height
	) {
	    return CharGrid.from(input, width * height);
	}

    @Override
	public Integer solvePart1(final String input) {
		final CharGrid layers = parse(input, WIDTH, HEIGHT);
		return layers.getRowsAsStringList().stream()
				.min(comparing(s -> countMatches(s, '0')))
				.map(s -> countMatches(s, '1') * countMatches(s, '2'))
				.orElseThrow();
	}
	
	private String getImage(
	        final String input, final int width, final int height
    ) {
		final CharGrid layers = parse(input, width, height);
		return Stream.iterate(0, i -> i + 1).limit(layers.getWidth())
				.map(i -> layers.getRowsAsStringList().stream()
						.map(row -> row.charAt(i))
						.filter(ch -> ch !=  '2')
						.findFirst()
						.orElseThrow())
				.collect(toAString());
	}

	@Override
	public String solvePart2(final String input) {
		final CharGrid image
		        = CharGrid.from(getImage(input, WIDTH, HEIGHT), WIDTH);
		log(image.replace('1', '\u2592').replace('0', ' '));
		return OCR.convert6(image, '1', '0');
	}

	@Override
    public void samples() {
	    final AoC2019_08 test = AoC2019_08.createDebug();
	    assert test.getImage(TEST, 2, 2).equals("0110");
    }

    public static void main(final String[] args) throws Exception {
        AoC2019_08.create().run();
	}
	
	private static final String TEST = "0222112222120000";
}
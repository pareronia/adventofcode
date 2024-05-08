import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.List;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2016_09 extends SolutionBase<String, Long, Long> {

	private AoC2016_09(final boolean debug) {
		super(debug);
	}
	
	public static final AoC2016_09 create() {
		return new AoC2016_09(false);
	}

	public static final AoC2016_09 createDebug() {
		return new AoC2016_09(true);
	}

	@Override
    protected String parseInput(final List<String> inputs) {
        return inputs.get(0);
    }

    private Long decompressedLength(final String input, final boolean recursive) {
	    if (!input.contains("(")) {
	        return (long) input.length();
	    }

	    long cnt = 0;
	    boolean inMarker = false;
	    String marker = "";
        for (int i = 0; i < input.length(); i++) {
            final char c = input.charAt(i);
            if (c == '(') {
                inMarker = true;
            } else if (c == ')') {
                final String[] splits = marker.split("x");
                final Long skip = Long.valueOf(splits[0]);
                final Long repeat = Long.valueOf(splits[1]);
                if (recursive) {
                    final String skipped = input.substring(i + 1, i + 1 + skip.intValue());
                    cnt += decompressedLength(skipped, true) * repeat;
                } else {
                    cnt += skip * repeat;
                }
                i += skip;
                marker = "";
                inMarker = false;
            } else {
                if (inMarker) {
                    marker += String.valueOf(c);
                } else {
                    cnt++;
                }
            }
	    }
		return cnt;
	}

	@Override
	public Long solvePart1(final String input) {
	    return decompressedLength(input, FALSE);
	}
	
	@Override
	public Long solvePart2(final String input) {
	    return decompressedLength(input, TRUE);
	}

	@Samples({
	    @Sample(method = "part1", input = TEST1, expected = "6"),
	    @Sample(method = "part1", input = TEST2, expected = "7"),
	    @Sample(method = "part1", input = TEST3, expected = "9"),
	    @Sample(method = "part1", input = TEST4, expected = "11"),
	    @Sample(method = "part1", input = TEST5, expected = "6"),
	    @Sample(method = "part1", input = TEST6, expected = "18"),
	    @Sample(method = "part2", input = TEST3, expected = "9"),
	    @Sample(method = "part2", input = TEST6, expected = "20"),
	    @Sample(method = "part2", input = TEST7, expected = "241920"),
	    @Sample(method = "part2", input = TEST8, expected = "445"),
	})
	public static void main(final String[] args) throws Exception {
	    AoC2016_09.create().run();
	}

	private static final String TEST1 = "ADVENT";
	private static final String TEST2 = "A(1x5)BC";
	private static final String TEST3 = "(3x3)XYZ";
	private static final String TEST4 = "A(2x2)BCD(2x2)EFG";
	private static final String TEST5 = "(6x1)(1x3)A";
	private static final String TEST6 = "X(8x2)(3x3)ABCY";
	private static final String TEST7 = "(27x12)(20x12)(13x14)(7x10)(1x12)A";
	private static final String TEST8
	            = "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN";
}

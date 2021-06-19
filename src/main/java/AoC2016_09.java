import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.List;

import com.github.pareronia.aocd.Aocd;

public class AoC2016_09 extends AoCBase {

	private final String input;
	
	private AoC2016_09(List<String> inputs, boolean debug) {
		super(debug);
	    assert inputs.size() == 1;
		this.input = inputs.get(0);
	}
	
	public static final AoC2016_09 create(List<String> input) {
		return new AoC2016_09(input, false);
	}

	public static final AoC2016_09 createDebug(List<String> input) {
		return new AoC2016_09(input, true);
	}
	
	private Long decompressedLength(String input, boolean recursive) {
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
	public Long solvePart1() {
	    return decompressedLength(this.input, FALSE);
	}
	
	@Override
	public Long solvePart2() {
	    return decompressedLength(this.input, TRUE);
	}

	public static void main(String[] args) throws Exception {
		assert AoC2016_09.createDebug(TEST1).solvePart1() == 6;
		assert AoC2016_09.createDebug(TEST2).solvePart1() == 7;
		assert AoC2016_09.createDebug(TEST3).solvePart1() == 9;
		assert AoC2016_09.createDebug(TEST4).solvePart1() == 11;
		assert AoC2016_09.createDebug(TEST5).solvePart1() == 6;
		assert AoC2016_09.createDebug(TEST6).solvePart1() == 18;
		assert AoC2016_09.createDebug(TEST3).solvePart2() == 9;
		assert AoC2016_09.createDebug(TEST6).solvePart2() == 20;
		assert AoC2016_09.createDebug(TEST7).solvePart2() == 241920;
		assert AoC2016_09.createDebug(TEST8).solvePart2() == 445;
		
		final List<String> input = Aocd.getData(2016, 9);
		lap("Part 1", () -> AoC2016_09.create(input).solvePart1());
		lap("Part 2", () -> AoC2016_09.create(input).solvePart2());
	}

	private static final List<String> TEST1 = splitLines("ADVENT");
	private static final List<String> TEST2 = splitLines("A(1x5)BC");
	private static final List<String> TEST3 = splitLines("(3x3)XYZ");
	private static final List<String> TEST4 = splitLines("A(2x2)BCD(2x2)EFG");
	private static final List<String> TEST5 = splitLines("(6x1)(1x3)A");
	private static final List<String> TEST6 = splitLines("X(8x2)(3x3)ABCY");
	private static final List<String> TEST7
	                        = splitLines("(27x12)(20x12)(13x14)(7x10)(1x12)A");
	private static final List<String> TEST8
	    = splitLines("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN");
}

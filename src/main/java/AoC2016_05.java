import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.CharArrayUtils;
import com.github.pareronia.aoc.codec.MD5;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2016_05 extends AoCBase {

	private final String input;
	
	private AoC2016_05(final List<String> input, final boolean debug) {
		super(debug);
		assert input.size() == 1;
		this.input = input.get(0);
	}
	
	public static final AoC2016_05 create(final List<String> input) {
		return new AoC2016_05(input, false);
	}

	public static final AoC2016_05 createDebug(final List<String> input) {
		return new AoC2016_05(input, true);
	}
	
	private ValueAndIndex findMd5StartingWith5Zeroes(final Integer index) {
		String val = "XXXXX";
		int i = index;
		while (!val.substring(0, 5).equals("00000")) {
			i++;
			final String toHash = input + String.valueOf(i);
			val = MD5.md5Hex(toHash);
		}
		return ValueAndIndex.of(val, i);
	}

	@Override
	public String solvePart1() {
		Integer index = 0;
		String result = "";
		for (int i = 0; i < 8; i++) {
			log(i);
			final ValueAndIndex md5 = findMd5StartingWith5Zeroes(index);
			index = md5.index;
			result = result + md5.value.charAt(5);
			log(result);
		}
		return result;
	}

	@Override
	public String solvePart2() {
		final char[] validPositions = { '0', '1', '2', '3', '4', '5', '6', '7' };
		Integer index = 0;
		final char[] result = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
		final Set<Character> seen = new HashSet<>();
		while (true) {
			final ValueAndIndex md5 = findMd5StartingWith5Zeroes(index);
			final String val = md5.value;
			index = md5.index;
			final char temp = val.charAt(5);
			if (!CharArrayUtils.contains(validPositions, temp) || seen.contains(temp)) {
				continue;
			}
			seen.add(temp);
			final Integer position = Integer.valueOf(String.valueOf(temp));
			result[position] = val.charAt(6);
			log(String.valueOf(result));
			if (!CharArrayUtils.contains(result, ' ')) {
				break;
			}
		}
		return String.valueOf(result);
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2016_05.createDebug(TEST).solvePart1().equals("18f47a30");
		assert AoC2016_05.createDebug(TEST).solvePart2().equals("05ace8e3");
		
        final Puzzle puzzle = Aocd.puzzle(2016, 5);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2016_05.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2016_05.create(inputData)::solvePart2)
        );
	}

	private static final List<String> TEST = splitLines("abc");
	
	record ValueAndIndex(String value, int index) {
	    public static ValueAndIndex of(final String value, final int index) {
	        return new ValueAndIndex(value, index);
	    }
    }
}

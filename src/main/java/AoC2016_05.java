import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

import com.github.pareronia.aocd.Aocd;

public class AoC2016_05 extends AoCBase {

	private final String input;
	
	private AoC2016_05(List<String> input, boolean debug) {
		super(debug);
		assert input.size() == 1;
		this.input = input.get(0);
	}
	
	public static final AoC2016_05 create(List<String> input) {
		return new AoC2016_05(input, false);
	}

	public static final AoC2016_05 createDebug(List<String> input) {
		return new AoC2016_05(input, true);
	}
	
	private Pair<String, Integer> findMd5StartingWith5Zeroes(Integer index) {
		String val = "XXXXX";
		int i = index;
		while (!val.substring(0, 5).equals("00000")) {
			i++;
			final String toHash = input + String.valueOf(i);
			val = DigestUtils.md5Hex(toHash);
		}
		return Tuples.pair(val, i);
	}
	
	@Override
	public String solvePart1() {
		Integer index = 0;
		String result = "";
		for (int i = 0; i < 8; i++) {
			log(i);
			final Pair<String, Integer> md5 = findMd5StartingWith5Zeroes(index);
			final String val = md5.getOne();
			index = md5.getTwo();
			result = result + val.charAt(5);
			log(result);
		}
		return result;
	}

	@Override
	public String solvePart2() {
		final char[] validPositions = new char[] { '0', '1', '2', '3', '4', '5', '6', '7' };
		Integer index = 0;
		char[] result = new char[] { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };
		final Set<Character> seen = new HashSet<>();
		while (true) {
			final Pair<String, Integer> md5 = findMd5StartingWith5Zeroes(index);
			final String val = md5.getOne();
			index = md5.getTwo();
			final char temp = val.charAt(5);
			if (!ArrayUtils.contains(validPositions, temp) || seen.contains(temp)) {
				continue;				
			}
			seen.add(temp);
			final Integer position = Integer.valueOf(String.valueOf(temp));
			result[position] = val.charAt(6);
			log(String.valueOf(result));
			if (!ArrayUtils.contains(result, ' ')) {
				break;
			}
		}
		return String.valueOf(result);
	}

	public static void main(String[] args) throws Exception {
		assert AoC2016_05.createDebug(TEST).solvePart1().equals("18f47a30");
		assert AoC2016_05.createDebug(TEST).solvePart2().equals("05ace8e3");
		
		final List<String> input = Aocd.getData(2016, 5);
		lap("Part 1", () -> AoC2016_05.create(input).solvePart1());
		lap("Part 2", () -> AoC2016_05.create(input).solvePart2());
	}

	private static final List<String> TEST = splitLines("abc");
}

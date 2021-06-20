import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.pareronia.aocd.Aocd;

public class AoC2016_16 extends AoCBase {
	
    private final String initialState;
	
	private AoC2016_16(List<String> input, boolean debug) {
		super(debug);
		assert input.size() == 1;
		this.initialState = input.get(0);
	}
	
	public static AoC2016_16 create(List<String> input) {
		return new AoC2016_16(input, false);
	}

	public static AoC2016_16 createDebug(List<String> input) {
		return new AoC2016_16(input, true);
	}
	
	private String dragonCurve(String input) {
	    final String a = new String(input);
	    final String b = StringUtils.reverse(input).replace('0', '-').replace('1', '0').replace('-', '1');
	    return new StringBuilder().append(a).append("0").append(b).toString();
	}
	
	private char[] checkSum(char[] data) {
	    final char[] pairs = new char[data.length / 2];
	    for (int i = 0; i < data.length - 1; i += 2) {
	        if (data[i] == data[i + 1]) {
	            pairs[i / 2] = '1';
	        } else {
	            pairs[i / 2] = '0';
	        }
	    }
	    if (pairs.length % 2 != 0) {
	        return pairs;
	    } else {
	        return checkSum(pairs);
	    }
	}
	
	private String solve(Integer size) {
	    String data = this.initialState;
	    while (data.length() < size) {
	        data = dragonCurve(data);
	    }
	    return String.valueOf(checkSum(data.substring(0, size).toCharArray()));
	}

	@Override
	public String solvePart1() {
	    return solve(272);
	}
	
	@Override
	public String solvePart2() {
	    return solve(35651584);
	}
	
	public static void main(String[] args) throws Exception {
		assert AoC2016_16.createDebug(TEST).solve(20).equals("01100");

		final List<String> input = Aocd.getData(2016, 16);
		lap("Part 1", () -> AoC2016_16.create(input).solvePart1());
		lap("Part 2", () -> AoC2016_16.create(input).solvePart2());
	}
	
	private static final List<String> TEST = splitLines("10000");
}
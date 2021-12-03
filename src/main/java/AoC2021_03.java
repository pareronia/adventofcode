import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;

import com.github.pareronia.aocd.Aocd;

import lombok.RequiredArgsConstructor;

public class AoC2021_03 extends AoCBase {
    
    private final List<String> lines;
    
	private AoC2021_03(final List<String> input, final boolean debug) {
		super(debug);
		this.lines = input;
	}
	
	public static final AoC2021_03 create(final List<String> input) {
		return new AoC2021_03(input, false);
	}

	public static final AoC2021_03 createDebug(final List<String> input) {
		return new AoC2021_03(input, true);
	}
	
	private BitCount bitCounts(final List<String> inputs, final int pos) {
	    final int zeroes = (int) inputs.stream()
	            .filter(s -> s.charAt(pos) == '0')
	            .count();
	    return new BitCount(inputs.size() - zeroes, zeroes);
	}
	
	private int ans(final String value1, final String value2) {
	    return Integer.parseInt(value1, 2) * Integer.parseInt(value2, 2);
	}
	
	@Override
    public Integer solvePart1() {
	    final StringBuilder gamma = new StringBuilder();
	    final StringBuilder epsilon = new StringBuilder();
	    for (int i = 0; i < this.lines.get(0).length(); i++) {
	        final BitCount bitCounts = bitCounts(this.lines, i);
	        gamma.append(bitCounts.mostCommon());
	        epsilon.append(bitCounts.leastCommon());
	    }
	    return ans(gamma.toString(), epsilon.toString());
	}
	
	private String reduce(List<String> strings, final Function<BitCount, Character> keep) {
	    int pos = 0;
	    while (strings.size() > 1) {
	        final BitCount bitCounts = bitCounts(strings, pos);
	        final int fpos = pos++;
	        final char toKeep = keep.apply(bitCounts);
	        strings = strings.stream()
	                .filter(s -> s.charAt(fpos) == toKeep)
	                .collect(toList());
	    }
	    return strings.get(0);
	}
	
	@Override
	public Integer solvePart2() {
	    final String o2 = reduce(List.copyOf(this.lines), BitCount::mostCommon);
	    final String co2 = reduce(List.copyOf(this.lines), BitCount::leastCommon);
	    return ans(o2, co2);
	}
	
	public static void main(final String[] args) throws Exception {
		assert AoC2021_03.createDebug(TEST).solvePart1() == 198;
		assert AoC2021_03.createDebug(TEST).solvePart2() == 230;
		
		final List<String> input = Aocd.getData(2021, 3);
		lap("Part 1", () -> AoC2021_03.create(input).solvePart1());
		lap("Part 2", () -> AoC2021_03.create(input).solvePart2());
	}
	
	private static final List<String> TEST = splitLines(
	        "00100\r\n" +
	        "11110\r\n" +
	        "10110\r\n" +
	        "10111\r\n" +
	        "10101\r\n" +
	        "01111\r\n" +
	        "00111\r\n" +
	        "11100\r\n" +
	        "10000\r\n" +
	        "11001\r\n" +
	        "00010\r\n" +
	        "01010"
    );
	
	@RequiredArgsConstructor
	private static final class BitCount {
	    private final int ones;
	    private final int zeroes;
	    
	    public char mostCommon() {
	        return ones >= zeroes ? '1' : '0';
	    }
	    
	    public char leastCommon() {
	        return ones < zeroes ? '1' : '0';
	    }
	}
}

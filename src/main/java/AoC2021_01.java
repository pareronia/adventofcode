import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

import java.util.List;

import com.github.pareronia.aocd.Aocd;

public class AoC2021_01 extends AoCBase {
    
    private final List<Integer> depths;
	
	private AoC2021_01(final List<String> input, final boolean debug) {
		super(debug);
		this.depths = input.stream().map(Integer::valueOf).collect(toList());
	}
	
	public static final AoC2021_01 create(final List<String> input) {
		return new AoC2021_01(input, false);
	}

	public static final AoC2021_01 createDebug(final List<String> input) {
		return new AoC2021_01(input, true);
	}
	
	@Override
    public Integer solvePart1() {
	    int cnt = 0;
	    for (int i = 1; i < this.depths.size(); i++) {
	        if (this.depths.get(i) > this.depths.get(i - 1)) {
	            cnt++;
	        }
	    }
		return cnt;
	}
	
	@Override
	public Integer solvePart2() {
	    int cnt = 0;
	    for (int i = 1; i < this.depths.size() - 2; i++) {
            if (sum(this.depths.subList(i, i + 3)) > sum(this.depths.subList(i - 1, i + 2))) {
	            cnt++;
	        }
	    }
		return cnt;
	}
	
	private int sum(final List<Integer> list) {
	    return list.stream().collect(summingInt(Integer::intValue));
	}
	
	public static void main(final String[] args) throws Exception {
		assert AoC2021_01.createDebug(TEST).solvePart1() == 7;
		assert AoC2021_01.createDebug(TEST).solvePart2() == 5;
		
		final List<String> input = Aocd.getData(2021, 1);
		lap("Part 1", () -> AoC2021_01.create(input).solvePart1());
		lap("Part 2", () -> AoC2021_01.create(input).solvePart2());
	}
	
	private static final List<String> TEST = splitLines(
	        "199\r\n" +
            "200\r\n" +
            "208\r\n" +
            "210\r\n" +
            "200\r\n" +
            "207\r\n" +
            "240\r\n" +
            "269\r\n" +
            "260\r\n" +
            "263"	);
}

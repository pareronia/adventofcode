import java.util.ArrayList;
import java.util.List;

import com.github.pareronia.aocd.Aocd;

import lombok.Value;

public class AoC2020_13 extends AoCBase {
	
    private final Integer target;
	private final List<Bus> buses;
	
	private AoC2020_13(List<String> input, boolean debug) {
		super(debug);
		assert input.size() == 2;
		this.target = Integer.valueOf(input.get(0));
		this.buses = new ArrayList<>();
		final String[] splits = input.get(1).split(",");
		for (int i = 0; i < splits.length; i++) {
		    final String s = splits[i];
		    if (!s.equals("x")) {
		        buses.add(new Bus(Integer.valueOf(s), i));
		    }
		}
	}
	
	public static AoC2020_13 create(List<String> input) {
		return new AoC2020_13(input, false);
	}

	public static AoC2020_13 createDebug(List<String> input) {
		return new AoC2020_13(input, true);
	}
	
	@Override
	public Integer solvePart1() {
	    int cnt = 0;
	    while (true) {
	        final int t = this.target + cnt;
	        for (final Bus b : this.buses) {
	            if (t % b.getPeriod() == 0) {
	                return b.getPeriod() * cnt;
	            }
            }
	        cnt++;
	    }
	}
	
	@Override
	public Long solvePart2() {
	    long r = 0;
	    long lcm = 1;
	    for (int i = 0; i < this.buses.size() - 1; i++) {
	        final Bus cur = this.buses.get(i);
	        final Bus nxt = this.buses.get(i + 1);
	        lcm = lcm * cur.getPeriod();
	        while (true) {
	            r += lcm;
	            if ((r + (long) nxt.getOffset()) % nxt.getPeriod() == 0)  {
	                break;
	            }
	        }
	    }
		return r;
	}

	public static void main(String[] args) throws Exception {
		assert AoC2020_13.createDebug(TEST1).solvePart1() == 295;
		assert AoC2020_13.createDebug(TEST2).solvePart2() == 108;
		assert AoC2020_13.createDebug(TEST3).solvePart2() == 3417;
		assert AoC2020_13.createDebug(TEST4).solvePart2() == 754018;
		assert AoC2020_13.createDebug(TEST5).solvePart2() == 779210;
		assert AoC2020_13.createDebug(TEST6).solvePart2() == 1261476;
		assert AoC2020_13.createDebug(TEST7).solvePart2() == 1202161486;

		final List<String> input = Aocd.getData(2020, 13);
		lap("Part 1", () -> AoC2020_13.create(input).solvePart1());
		lap("Part 2", () -> AoC2020_13.create(input).solvePart2());
	}
	
	private static final List<String> TEST1 = splitLines(
	        "939\r\n" +
	        "7,13,x,x,59,x,31,19"
	);
	private static final List<String> TEST2 = splitLines(
	        "999\r\n" +
	        "3,x,5,x,7"
	);
	private static final List<String> TEST3 = splitLines(
	        "999\r\n" +
	        "17,x,13,19"
	);
	private static final List<String> TEST4 = splitLines(
	        "999\r\n" +
	        "67,7,59,61"
	);
	private static final List<String> TEST5 = splitLines(
	        "999\r\n" +
	        "67,x,7,59,61"
	);
	private static final List<String> TEST6 = splitLines(
	        "999\r\n" +
	        "67,7,x,59,61"
	);
	private static final List<String> TEST7 = splitLines(
	        "999\r\n" +
	        "1789,37,47,1889"
	);
	
	@Value
	private static final class Bus {
	    private final Integer period;
	    private final Integer offset;
	}
}
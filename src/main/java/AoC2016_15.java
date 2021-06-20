import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.mutable.MutableInt;

import com.github.pareronia.aocd.Aocd;

import lombok.Value;

public class AoC2016_15 extends AoCBase {
	
	private static final Pattern REGEX = Pattern.compile("[0-9]+");
	
    private final List<Disc> discs;
	
	private AoC2016_15(List<String> input, boolean debug) {
		super(debug);
		this.discs = input.stream()
		        .map(REGEX::matcher)
		        .map(m -> m.results().map(r -> m.group(0)).collect(toList()))
		        .map(l -> {
                    final Integer period = Integer.valueOf(l.get(1));
                    final Integer position = Integer.valueOf(l.get(3));
                    final Integer delay = Integer.valueOf(l.get(0));
                    final int offset = (period - position) % period;
                    return new Disc(period, offset, delay);
                })
		        .collect(toList());
	}
	
	public static AoC2016_15 create(List<String> input) {
		return new AoC2016_15(input, false);
	}

	public static AoC2016_15 createDebug(List<String> input) {
		return new AoC2016_15(input, true);
	}
	
	private Integer solve(List<Disc> discs) {
	    final MutableInt r = new MutableInt(0);
	    while (!discs.stream().allMatch(d -> d.alignedAt(r.getValue()))) {
	        r.increment();
	    }
	    return r.getValue();
	}

	@Override
	public Integer solvePart1() {
	    return solve(this.discs);
	}
	
	@Override
	public Integer solvePart2() {
	    this.discs.add(this.discs.size(), new Disc(11, 0, this.discs.size() + 1));
	    return solve(this.discs);
	}
	
	public static void main(String[] args) throws Exception {
		assert AoC2016_15.createDebug(TEST1).solvePart1() == 5;
		assert AoC2016_15.createDebug(TEST2).solvePart1() == 1;

		final List<String> input = Aocd.getData(2016, 15);
		lap("Part 1", () -> AoC2016_15.create(input).solvePart1());
		lap("Part 2", () -> AoC2016_15.create(input).solvePart2());
	}
	
	private static final List<String> TEST1 = splitLines(
	        "Disc #1 has 5 positions; at time=0, it is at position 4.\r\n" +
	        "Disc #2 has 2 positions; at time=0, it is at position 1."
	);
	private static final List<String> TEST2 = splitLines(
	        "Disc #1 has 5 positions; at time=0, it is at position 3.\r\n" +
	        "Disc #2 has 2 positions; at time=0, it is at position 1.\r\n" +
	        "Disc #3 has 3 positions; at time=0, it is at position 2."
	);
	
	@Value
	private static final class Disc {
	    private final Integer period;
	    private final Integer offset;
	    private final Integer delay;
	    
	    public boolean alignedAt(Integer time) {
	        return (time + this.delay) % this.period == this.offset;
	    }
	}
}
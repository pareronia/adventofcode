import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import com.github.pareronia.aoc.Range;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2016_20 extends AoCBase {
    
    private static final long TOTAL_IPS = 4294967296L;
    
    private final List<Range<Long>> ranges;
	
	private AoC2016_20(final List<String> input, final boolean debug) {
		super(debug);
		this.ranges = input.stream()
		        .map(s -> s.split("-"))
		        .map(sp -> Range.between(Long.valueOf(sp[0]), Long.valueOf(sp[1])))
		        .collect(toList());
	}
	
	public static AoC2016_20 create(final List<String> input) {
		return new AoC2016_20(input, false);
	}

	public static AoC2016_20 createDebug(final List<String> input) {
		return new AoC2016_20(input, true);
	}
	
    private List<Range<Long>> combineRanges() {
        final List<Range<Long>> combines = new ArrayList<>();
        this.ranges.sort(comparing(Range::getMinimum)); // why doesn't this work without sorting?
	    combines.add(this.ranges.get(0));
	    for (int i = 1; i < ranges.size(); i++) {
            final Range<Long> r = ranges.get(i);
            boolean combined = false;
            for (int j = 0; j < combines.size(); j++) {
                final Range<Long> combine = combines.get(j);
                if (combine.isOverlappedBy(r)) {
                    final long min = Math.min(combine.getMinimum(), r.getMinimum());
                    final long max = Math.max(combine.getMaximum(), r.getMaximum());
                    combines.set(j, Range.between(min, max));
                    combined = true;
                } else if (r.getMaximum() + 1 == combine.getMinimum()) {
                    combines.set(j, Range.between(r.getMinimum(), combine.getMaximum()));
                    combined = true;
                } else if (combine.getMaximum() + 1 == r.getMinimum()) {
                    combines.set(j, Range.between(combine.getMinimum(), r.getMaximum()));
                    combined = true;
                }
            }
            if (!combined) {
                combines.add(r);
            }
        }
        return combines;
    }
	 
	@Override
	public Long solvePart1() {
	    return combineRanges().stream()
	            .sorted(comparing(Range::getMinimum))
	            .findFirst()
	            .map(c -> c.getMaximum() + 1)
	            .orElseThrow();
	}

	@Override
	public Long solvePart2() {
	    return combineRanges().stream()
	            .map(c -> c.getMaximum() - c.getMinimum() + 1)
	            .reduce(TOTAL_IPS, (a, b) -> a - b);
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2016_20.createDebug(TEST1).solvePart1() == 3;
		assert AoC2016_20.createDebug(TEST2).solvePart1() == 90000101;
		assert AoC2016_20.createDebug(TEST1).solvePart2() == 4294967288L;
		assert AoC2016_20.createDebug(TEST2).solvePart2() == 4194967190L;

        final Puzzle puzzle = Aocd.puzzle(2016, 20);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2016_20.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2016_20.create(inputData)::solvePart2)
        );
	}
	
	private static final List<String> TEST1 = splitLines(
	        "5-8\r\n" +
	        "0-2\r\n" +
	        "4-7"
	);
	private static final List<String> TEST2 = splitLines(
	        "0-90000000\r\n" +
	        "100000005-110000005\r\n" +
	        "90000001-90000100\r\n" +
	        "1000000054-1000000057"
	);
}
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.pareronia.aoc.MutableInt;
import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.codec.MD5;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2016_14 extends AoCBase {
    
    private static final Pattern TRIPLET = Pattern.compile("([a-f0-9])\\1\\1");

    private final String salt;
    private final Map<Integer, String> table = new HashMap<>();

	private AoC2016_14(final List<String> inputs, final boolean debug) {
		super(debug);
		assert inputs.size() == 1;
		this.salt = inputs.get(0);
	}
	
	public static final AoC2016_14 create(final List<String> input) {
		return new AoC2016_14(input, false);
	}

	public static final AoC2016_14 createDebug(final List<String> input) {
		return new AoC2016_14(input, true);
	}
	
	private String calculateMD5(final String salt, final Integer stretch, final Integer index) {
	    String toHash = salt + String.valueOf(index);
	    for (int i = 0; i < 1 + stretch; i++) {
	        toHash = MD5.md5Hex(toHash);
	    }
	    return toHash;
	}
	
	private String getMD5(final String salt, final Integer stretch, final Integer index) {
	   return this.table.computeIfAbsent(index, i -> calculateMD5(salt, stretch, i));
	}
	
	private Optional<String> findTriplet(final String string) {
	    return Optional.of(string)
	            .map(TRIPLET::matcher)
	            .filter(Matcher::find)
	            .map(m -> m.group(1));
	}

    private Integer solve(final int stretch) {
        final MutableInt index = new MutableInt(-1);
	    final MutableInt cnt = new MutableInt(0);
	    while (cnt.getValue() < 64) {
	        final int idx = index.incrementAndGet();
	        findTriplet(getMD5(this.salt, stretch, idx)).ifPresent(triplet -> {
	            final String quintet = StringUtils.repeat(triplet.charAt(0), 5);
	            for (int i = idx + 1; i < idx + 1001; i++) {
	                if (getMD5(this.salt, stretch, i).contains(quintet)) {
	                    cnt.increment();
	                    break;
	                }
	            }
	        });
	    }
	    return index.getValue();
    }
    
    @Override
    public Integer solvePart1() {
        return solve(0);
    }
	
	@Override
	public Integer solvePart2() {
	    return solve(2016);
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2016_14.createDebug(TEST).solvePart1() == 22728;
		assert AoC2016_14.createDebug(TEST).solvePart2() == 22551;
		
        final Puzzle puzzle = Aocd.puzzle(2016, 14);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2016_14.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2016_14.create(inputData)::solvePart2)
        );
	}

	private static final List<String> TEST = splitLines("abc");
}

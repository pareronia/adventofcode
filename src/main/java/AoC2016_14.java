import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;

import com.github.pareronia.aocd.Aocd;

public class AoC2016_14 extends AoCBase {
    
    private static final Pattern TRIPLET = Pattern.compile("([a-f0-9])\\1\\1");

    private final String salt;
    private final Map<Integer, String> table = new HashMap<>();

	private AoC2016_14(List<String> inputs, boolean debug) {
		super(debug);
		assert inputs.size() == 1;
		this.salt = inputs.get(0);
	}
	
	public static final AoC2016_14 create(List<String> input) {
		return new AoC2016_14(input, false);
	}

	public static final AoC2016_14 createDebug(List<String> input) {
		return new AoC2016_14(input, true);
	}
	
	private String calculateMD5(String salt, Integer index) {
	    final String toHash = salt + String.valueOf(index);
	    return DigestUtils.md5Hex(toHash);
	}
	
	private String getMD5(String salt, Integer index) {
	   return this.table.computeIfAbsent(index, i -> calculateMD5(salt, i));
	}
	
	private Optional<String> findTriplet(String string) {
	    final Matcher matcher = TRIPLET.matcher(string);
	    if (matcher.find()) {
	        return Optional.of(matcher.group(1));
	    }
	    return Optional.empty();
	}
	
	@Override
	public Integer solvePart1() {
	    final MutableInt index = new MutableInt(0);
	    final MutableInt cnt = new MutableInt(0);
	    while (cnt.getValue() < 64) {
	        final int idx = index.incrementAndGet();
	        findTriplet(getMD5(this.salt, idx)).ifPresent(triplet -> {
	            final String quintet = StringUtils.repeat(triplet.charAt(0), 5);
	            for (int i = idx + 1; i < idx + 1001; i++) {
	                if (getMD5(this.salt, i).contains(quintet)) {
	                    cnt.increment();
	                    break;
	                }
	            }
	        });
	    }
	    return index.getValue();
	}
	
	@Override
	public Integer solvePart2() {
	    return 0;
	}

	public static void main(String[] args) throws Exception {
		assert AoC2016_14.createDebug(TEST).solvePart1() == 22728;
		
		final List<String> input = Aocd.getData(2016, 14);
		lap("Part 1", () -> AoC2016_14.create(input).solvePart1());
		lap("Part 2", () -> AoC2016_14.create(input).solvePart2());
	}

	private static final List<String> TEST = splitLines("abc");
}

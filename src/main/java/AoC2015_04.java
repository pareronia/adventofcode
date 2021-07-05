import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.pareronia.aocd.Aocd;

public final class AoC2015_04 extends AoCBase {

    private final transient String input;

    private AoC2015_04(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = inputs.get(0);
    }

    public static AoC2015_04 create(final List<String> input) {
        return new AoC2015_04(input, false);
    }

    public static AoC2015_04 createDebug(final List<String> input) {
        return new AoC2015_04(input, true);
    }
    
    private Integer findMd5StartingWithZeroes(final String seed, final Integer zeroes) {
        final String target = StringUtils.repeat("0", zeroes);
		int i = 0;
		String val = seed;
		while (!val.substring(0, zeroes).equals(target)) {
			i++;
			final String toHash = seed + String.valueOf(i);
			val = DigestUtils.md5Hex(toHash);
		}
		return i;
    }
    
    @Override
    public Integer solvePart1() {
        return findMd5StartingWithZeroes(this.input, 5);
    }

    @Override
    public Integer solvePart2() {
        return findMd5StartingWithZeroes(this.input, 6);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_04.createDebug(TEST1).solvePart1() == 609043;
        assert AoC2015_04.createDebug(TEST2).solvePart1() == 1048970;

        final List<String> input = Aocd.getData(2015, 4);
        lap("Part 1", () -> AoC2015_04.create(input).solvePart1());
        lap("Part 2", () -> AoC2015_04.create(input).solvePart2());
    }

    private static final List<String> TEST1 = splitLines("abcdef");
    private static final List<String> TEST2 = splitLines("pqrstuv");
}
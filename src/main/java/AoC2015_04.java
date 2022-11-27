import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.github.pareronia.aoc.Range;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

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
    
    private boolean checkZeroes(final byte[] digest, final int zeroes) {
        int cnt = 0;
        for (final int j : Range.range(zeroes / 2 + zeroes % 2)) {
            final byte c = digest[j];
            if ((c & 0xF0) == 0) {
                cnt++;
                if ((c & 0x0F) == 0) {
                    cnt++;
                    continue;
                }
            }
            break;
        }
        return cnt == zeroes;
    }
    
    private int findMd5StartingWithZeroes(final String seed, final int zeroes) {
		for (int i = 1; ; i++) {
			final byte[] digest = DigestUtils.md5(seed + String.valueOf(i));
			if (checkZeroes(digest, zeroes)) {
			    return i;
			}
		}
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

        final Puzzle puzzle = Aocd.puzzle(2015, 4);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2015_04.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2015_04.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST1 = splitLines("abcdef");
    private static final List<String> TEST2 = splitLines("pqrstuv");
}
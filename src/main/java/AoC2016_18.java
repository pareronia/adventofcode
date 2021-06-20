import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;

public class AoC2016_18 extends AoCBase {
    
    private static final char SAFE = '.';
    private static final char TRAP = '^';
    private static final Set<String> TRAPS = Set.of(
            String.valueOf(new char[] { TRAP, TRAP, SAFE }),
            String.valueOf(new char[] { SAFE, TRAP, TRAP }),
            String.valueOf(new char[] { TRAP, SAFE, SAFE }),
            String.valueOf(new char[] { SAFE, SAFE, TRAP })
    );
	
    private final String startingRow;
	
	private AoC2016_18(List<String> input, boolean debug) {
		super(debug);
		assert input.size() == 1;
		this.startingRow = input.get(0);
	}
	
	public static AoC2016_18 create(List<String> input) {
		return new AoC2016_18(input, false);
	}

	public static AoC2016_18 createDebug(List<String> input) {
		return new AoC2016_18(input, true);
	}
	
	private char[] getPrevious(char[] s, Integer i) {
	    final char first = (i - 1 < 0) ? SAFE : s[i - 1];
	    final char second = s[i];
	    final char third = (i + 1 == s.length) ? SAFE : s[i + 1];
        return new char[] { first, second, third };
	}
	
	private Long solve(Integer rows) {
	    final int width = this.startingRow.length();
	    long safeCount = Utils.asCharacterStream(this.startingRow)
	            .filter(c -> c == SAFE)
	            .count();
	    char[] previousRow = this.startingRow.toCharArray();
	    for (int row = 1; row < rows; row++) {
	        final char[] newRow = new char[width];
	        for (int j = 0; j < width; j++) {
	            final char[] prev = getPrevious(previousRow, j);
	            if (TRAPS.contains(String.valueOf(prev))) {
                    newRow[j] = TRAP;
                } else {
                    newRow[j] = SAFE;
                    safeCount++;
                }
	        }
	        previousRow = newRow;
	    }
	    return safeCount;
	}
	
	@Override
	public Long solvePart1() {
	    return solve(40);
	}
	
	@Override
	public Long solvePart2() {
	    return solve(400_000);
	}
	
	public static void main(String[] args) throws Exception {
		assert AoC2016_18.createDebug(TEST1).solve(3) == 6;
		assert AoC2016_18.createDebug(TEST2).solve(10) == 38;

		final List<String> input = Aocd.getData(2016, 18);
		lap("Part 1", () -> AoC2016_18.create(input).solvePart1());
		lap("Part 2", () -> AoC2016_18.create(input).solvePart2());
	}
	
	private static final List<String> TEST1 = splitLines("..^^.");
	private static final List<String> TEST2 = splitLines(".^^.^.^^^^");
}
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2016_18 extends SolutionBase<String, Long, Long> {
    
    private static final char SAFE = '.';
    private static final char TRAP = '^';
    private static final Set<String> TRAPS = Set.of(
            String.valueOf(new char[] { TRAP, TRAP, SAFE }),
            String.valueOf(new char[] { SAFE, TRAP, TRAP }),
            String.valueOf(new char[] { TRAP, SAFE, SAFE }),
            String.valueOf(new char[] { SAFE, SAFE, TRAP })
    );
	
	private AoC2016_18(final boolean debug) {
		super(debug);
	}
	
	public static AoC2016_18 create() {
		return new AoC2016_18(false);
	}

	public static AoC2016_18 createDebug() {
		return new AoC2016_18(true);
	}
	
	private char[] getPrevious(final char[] s, final Integer i) {
	    final char first = (i - 1 < 0) ? SAFE : s[i - 1];
	    final char second = s[i];
	    final char third = (i + 1 == s.length) ? SAFE : s[i + 1];
        return new char[] { first, second, third };
	}
	
	@Override
    protected String parseInput(final List<String> inputs) {
        return inputs.get(0);
    }

    private Long solve(final String input, final Integer rows) {
	    final int width = input.length();
	    long safeCount = Utils.asCharacterStream(input)
	            .filter(c -> c == SAFE)
	            .count();
	    char[] previousRow = input.toCharArray();
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
	public Long solvePart1(final String input) {
	    return solve(input, 40);
	}
	
	@Override
	public Long solvePart2(final String input) {
	    return solve(input, 400_000);
	}
	
	@Override
    public void samples() {
	    final AoC2016_18 test = AoC2016_18.createDebug();
	    assert test.solve(TEST1, 3) == 6;
	    assert test.solve(TEST2, 10) == 38;
    }

    public static void main(final String[] args) throws Exception {
        AoC2016_18.create().run();
	}
	
	private static final String TEST1 = "..^^.";
	private static final String TEST2 = ".^^.^.^^^^";
}
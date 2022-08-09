import java.util.List;

import com.github.pareronia.aocd.Puzzle;

public class AoC2020_25 extends AoCBase {
    
    private static final int MOD = 20201227;

    private final List<String> input;
    
	private AoC2020_25(final List<String> input, final boolean debug) {
		super(debug);
		this.input = input;
	}
    
    public static AoC2020_25 create(final List<String> input) {
        return new AoC2020_25(input, false);
    }

    public static AoC2020_25 createDebug(final List<String> input) {
        return new AoC2020_25(input, true);
    }
    
    private long findLoopSize(final long key) {
        long loopSize = 0;
        long val = 1;
        while (val != key) {
            loopSize++;
            val = val * 7 % MOD;
        }
        return loopSize;
    }
    
    private long findEncryptionKey(final long pubKey, final long loopSize) {
        long val = 1;
        for (int i = 0; i < loopSize; i++) {
            val = val * pubKey % MOD;
        }
        return val;
    }
    
    @Override
	public Long solvePart1() {
        assert this.input.size() == 2;
        final long pubKey1 = Integer.parseInt(this.input.get(0));
        final long pubKey2 = Integer.parseInt(this.input.get(1));
        final long loopSize2 = findLoopSize(pubKey2);
        return findEncryptionKey(pubKey1, loopSize2);
	}

	@Override
	public Integer solvePart2() {
	    return 0;
	}

	public static void main(final String[] args) throws Exception {
		assert createDebug(TEST).solvePart1() == 14897079;
		
        final Puzzle puzzle = puzzle(AoC2020_25.class);
		final List<String> input = puzzle.getInputData();
        puzzle.check(
           () -> lap("Part 1", create(input)::solvePart1),
           () -> lap("Part 2", create(input)::solvePart2)
	    );
	}
	
	private static final List<String> TEST = splitLines(
			"5764801\r\n" +
			"17807724"
	);
}
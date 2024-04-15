import java.util.List;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_25
                extends SolutionBase<AoC2020_25.PublicKeys, Long, String> {
    
    private static final int MOD = 20201227;

	private AoC2020_25(final boolean debug) {
		super(debug);
	}
    
    public static AoC2020_25 create() {
        return new AoC2020_25(false);
    }

    public static AoC2020_25 createDebug() {
        return new AoC2020_25(true);
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
    protected PublicKeys parseInput(final List<String> inputs) {
        return new PublicKeys(
                Long.parseLong(inputs.get(0)),
                Long.parseLong(inputs.get(1)));
    }

    @Override
	public Long solvePart1(final PublicKeys keys) {
        final long loopSize2 = findLoopSize(keys.key2);
        return findEncryptionKey(keys.key1, loopSize2);
	}

	@Override
	public String solvePart2(final PublicKeys keys) {
	    return "🎄";
	}

	@Samples({
	    @Sample(method = "part1", input = TEST, expected = "14897079")
	})
	public static void main(final String[] args) throws Exception {
		AoC2020_25.create().run();
	}
	
	private static final String TEST = """
			5764801
			17807724
	        """;
	
	record PublicKeys(long key1, long key2) {}
}
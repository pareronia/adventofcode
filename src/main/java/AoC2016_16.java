import java.util.List;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2016_16
        extends SolutionBase<AoC2016_16.DragonCurve, String, String> {
	
	private AoC2016_16(final boolean debug) {
		super(debug);
	}
	
	public static AoC2016_16 create() {
		return new AoC2016_16(false);
	}

	public static AoC2016_16 createDebug() {
		return new AoC2016_16(true);
	}
	
	private String solve(final DragonCurve dragonCurve, final int size) {
	    return dragonCurve.checkSumForSize(size);
	}

	@Override
    protected DragonCurve parseInput(final List<String> inputs) {
        return new DragonCurve(inputs.get(0));
    }

    @Override
	public String solvePart1(final DragonCurve dragonCurve) {
	    return solve(dragonCurve, 272);
	}
	
	@Override
	public String solvePart2(final DragonCurve dragonCurve) {
	    return solve(dragonCurve, 35651584);
	}
	
	@Override
    public void samples() {
	    final AoC2016_16 test = AoC2016_16.createDebug();
	    assert test.solve(test.parseInput(List.of(TEST)), 20).equals("01100");
    }

    public static void main(final String[] args) throws Exception {
        AoC2016_16.create().run();
	}
	
	private static final String TEST = "10000";
	
	record DragonCurve(String initialState) {
	    private String dragonCurve(final String input) {
	        final String b = StringOps.translate(
	                StringUtils.reverse(input), "01", "10");
	        return new StringBuilder().append(input)
	                .append("0").append(b).toString();
	    }
	    
	    private char[] checkSum(final char[] data) {
	        final char[] pairs = new char[data.length / 2];
	        for (int i = 0; i < data.length - 1; i += 2) {
	            if (data[i] == data[i + 1]) {
	                pairs[i / 2] = '1';
	            } else {
	                pairs[i / 2] = '0';
	            }
	        }
	        if (pairs.length % 2 != 0) {
	            return pairs;
	        } else {
	            return checkSum(pairs);
	        }
	    }
        
	    public String checkSumForSize(final int size) {
            String data = this.initialState;
            while (data.length() < size) {
                data = dragonCurve(data);
            }
            return String.valueOf(checkSum(data.substring(0, size).toCharArray()));
        }
	}
}
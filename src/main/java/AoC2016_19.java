import java.util.Arrays;
import java.util.List;

import com.github.pareronia.aocd.Aocd;

public class AoC2016_19 extends AoCBase {
    
    private final Integer numberOfElves;
	
	private AoC2016_19(List<String> input, boolean debug) {
		super(debug);
		assert input.size() == 1;
		this.numberOfElves = Integer.valueOf(input.get(0));
	}
	
	public static AoC2016_19 create(List<String> input) {
		return new AoC2016_19(input, false);
	}

	public static AoC2016_19 createDebug(List<String> input) {
		return new AoC2016_19(input, true);
	}
	
	@Override
	public Integer solvePart1() {
	    final boolean[] a = new boolean[this.numberOfElves];
	    int count = this.numberOfElves;
        Arrays.fill(a , true);
        int i = 0;
        int lasti = 0;
        while (count > 1) {
            int j = (i + 1) % this.numberOfElves;
            if (a[i]) {
                while (!a[j]) {
                    j = (j + 1) % this.numberOfElves;
                }
                lasti = i;
                if (a[j]) {
                    a[j] = false;
                    count--;
                }
            }
            i = j;
        }
        return lasti + 1;
	}
	
	@Override
	public Integer solvePart2() {
	    return 0;
	}
	
	public static void main(String[] args) throws Exception {
		assert AoC2016_19.createDebug(TEST).solvePart1() == 3;

		final List<String> input = Aocd.getData(2016, 19);
		lap("Part 1", () -> AoC2016_19.create(input).solvePart1());
		lap("Part 2", () -> AoC2016_19.create(input).solvePart2());
	}
	
	private static final List<String> TEST = splitLines("5");
}
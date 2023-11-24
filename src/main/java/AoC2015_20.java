import java.util.List;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2015_20 extends SolutionBase<Integer, Integer, Integer> {

	private AoC2015_20(final boolean debug) {
		super(debug);
	}
	
	public static AoC2015_20 createDebug() {
		return new AoC2015_20(true);
	}
	
	public static AoC2015_20 create() {
		return new AoC2015_20(false);
	}
	
	private int divSum(final int number, final int max) {
		if (number == 1) {
			return 1;
		}
		
		int result = 0;
		for (int divider = 1; divider <= max; divider++) {
			if (number % divider == 0) {
				final int otherDivider = number / divider;
				if (divider != otherDivider) {
					result += otherDivider;
				}
				result += divider;
			}
		}
		return result;
	}
	
	private int sumOfFactors(int number) {
		if (number == 1) {
			return 1;
		}
		
		int result = 1;
		for (int divider = 2; divider <= Math.sqrt(number); divider++) {
			int curr_sum = 1;
            int curr_term = 1;
            while (number % divider == 0) {
                number = number / divider;
                curr_term *= divider;
                curr_sum += curr_term;
            }
            result *= curr_sum;
		}
		if (number >= 2) {
			result *= (1 + number);
		}
		return result;
	}

	@Override
    protected Integer parseInput(final List<String> inputs) {
	    assert inputs.size() == 1;
        return Integer.valueOf(inputs.get(0));
    }

    @Override
    protected Integer solvePart1(final Integer input) {
        final int numberOfGifts = input / 10;
        int houseNumber = 1;
        while (sumOfFactors(houseNumber) < numberOfGifts) {
            houseNumber++;
        }
        return houseNumber;
    }

    @Override
    protected Integer solvePart2(final Integer input) {
		final int numberOfGifts = Double.valueOf(Math.ceil(input / 11.0)).intValue();
		int houseNumber = 1;
		while (divSum(houseNumber, 50) < numberOfGifts) {
			houseNumber++;
		}
		return houseNumber;
	}

	@Override
	@Samples({
        @Sample(method = "part1", input = "10", expected = "1"),
        @Sample(method = "part1", input = "20", expected = "2"),
        @Sample(method = "part1", input = "30", expected = "2"),
        @Sample(method = "part1", input = "40", expected = "3"),
        @Sample(method = "part1", input = "50", expected = "4"),
        @Sample(method = "part1", input = "60", expected = "4"),
        @Sample(method = "part1", input = "70", expected = "4"),
        @Sample(method = "part1", input = "80", expected = "6"),
        @Sample(method = "part1", input = "90", expected = "6"),
        @Sample(method = "part1", input = "100", expected = "6"),
	})
    public void samples() {
    }

    public static void main(final String[] args) throws Exception {
        AoC2015_20.create().run();
	}
}

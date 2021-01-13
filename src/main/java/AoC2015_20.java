import static java.util.Arrays.asList;

import java.util.List;

public class AoC2015_20 extends AoCBase {

	private final List<String> inputs;

	private AoC2015_20(String input, boolean debug) {
		super(debug);
		this.inputs = asList((input + "\n").split("\\r?\\n"));
	}
	
	public static AoC2015_20 createDebug(String input) {
		return new AoC2015_20(input, true);
	}
	
	public static AoC2015_20 create(String input) {
		return new AoC2015_20(input, false);
	}
	
	private int divSum(int number, int max) {
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
	public long solvePart1() {
		assert inputs.size() == 1;
		final int numberOfGifts = Integer.valueOf(inputs.get(0)) / 10;
		int houseNumber = 1;
		while (sumOfFactors(houseNumber) < numberOfGifts) {
			houseNumber++;
		}
		return houseNumber;
	}

	@Override
	public long solvePart2() {
		assert inputs.size() == 1;
		int numberOfGifts = Double.valueOf(Math.ceil(Integer.valueOf(inputs.get(0)) / 11.0)).intValue();
		int houseNumber = 1;
		while (divSum(houseNumber, 50) < numberOfGifts) {
			houseNumber++;
		}
		return houseNumber;
	}

	public static void main(String[] args) throws Exception {
		assert AoC2015_20.createDebug("10").solvePart1() == 1;
		assert AoC2015_20.createDebug("20").solvePart1() == 2;
		assert AoC2015_20.createDebug("30").solvePart1() == 2;
		assert AoC2015_20.createDebug("40").solvePart1() == 3;
		assert AoC2015_20.createDebug("50").solvePart1() == 4;
		assert AoC2015_20.createDebug("60").solvePart1() == 4;
		assert AoC2015_20.createDebug("70").solvePart1() == 4;
		assert AoC2015_20.createDebug("80").solvePart1() == 6;
		assert AoC2015_20.createDebug("90").solvePart1() == 6;
		assert AoC2015_20.createDebug("100").solvePart1() == 6;

		lap("Part 1", () -> AoC2015_20.createDebug("33100000").solvePart1());
		lap("Part 2", () -> AoC2015_20.createDebug("33100000").solvePart2());
	}
}

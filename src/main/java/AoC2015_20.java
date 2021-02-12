import java.util.List;

import com.github.pareronia.aocd.Aocd;

public class AoC2015_20 extends AoCBase {

	private final List<String> inputs;

	private AoC2015_20(List<String> input, boolean debug) {
		super(debug);
		this.inputs = input;
	}
	
	public static AoC2015_20 createDebug(List<String> input) {
		return new AoC2015_20(input, true);
	}
	
	public static AoC2015_20 create(List<String> input) {
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
	public Integer solvePart1() {
		assert inputs.size() == 1;
		final int numberOfGifts = Integer.valueOf(inputs.get(0)) / 10;
		int houseNumber = 1;
		while (sumOfFactors(houseNumber) < numberOfGifts) {
			houseNumber++;
		}
		return houseNumber;
	}

	@Override
	public Integer solvePart2() {
		assert inputs.size() == 1;
		final int numberOfGifts = Double.valueOf(Math.ceil(Integer.valueOf(inputs.get(0)) / 11.0)).intValue();
		int houseNumber = 1;
		while (divSum(houseNumber, 50) < numberOfGifts) {
			houseNumber++;
		}
		return houseNumber;
	}

	public static void main(String[] args) throws Exception {
		assert AoC2015_20.createDebug(splitLines("10")).solvePart1() == 1;
		assert AoC2015_20.createDebug(splitLines("20")).solvePart1() == 2;
		assert AoC2015_20.createDebug(splitLines("30")).solvePart1() == 2;
		assert AoC2015_20.createDebug(splitLines("40")).solvePart1() == 3;
		assert AoC2015_20.createDebug(splitLines("50")).solvePart1() == 4;
		assert AoC2015_20.createDebug(splitLines("60")).solvePart1() == 4;
		assert AoC2015_20.createDebug(splitLines("70")).solvePart1() == 4;
		assert AoC2015_20.createDebug(splitLines("80")).solvePart1() == 6;
		assert AoC2015_20.createDebug(splitLines("90")).solvePart1() == 6;
		assert AoC2015_20.createDebug(splitLines("100")).solvePart1() == 6;
		
		final List<String> input = Aocd.getData(2015, 20);
		lap("Part 1", () -> AoC2015_20.createDebug(input).solvePart1());
		lap("Part 2", () -> AoC2015_20.createDebug(input).solvePart2());
	}
}

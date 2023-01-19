import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2020_15 extends AoCBase {
	
	private final List<Integer> numbers;
	
	private AoC2020_15(final List<String> input, final boolean debug) {
		super(debug);
		assert input.size() == 1;
		this.numbers = Stream.of(input.get(0).split(",")).map(Integer::valueOf).collect(toList());
	}
	
	public static AoC2020_15 create(final List<String> input) {
		return new AoC2020_15(input, false);
	}

	public static AoC2020_15 createDebug(final List<String> input) {
		return new AoC2020_15(input, true);
	}
	
	private int play(final int numberOfTurns) {
	    final int[] last = new int[numberOfTurns];
	    Arrays.fill(last, -1);
	    for (int i = 0; i < this.numbers.size() - 1; i++) {
	        last[this.numbers.get(i)] = i + 1;
	    }
	    int prev = Utils.last(this.numbers);
	    for (int i = this.numbers.size(); i < numberOfTurns; i++) {
	        final int prevPrev = last[prev];
	        last[prev] = i;
	        prev = prevPrev == -1 ? 0 : i - prevPrev;
	    }
	    return prev;
	}
	
	@Override
	public Integer solvePart1() {
		return play(2020);
	}
	
	@Override
	public Integer solvePart2() {
		return play(30_000_000);
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2020_15.createDebug(splitLines("0,3,6")).solvePart1() == 436;
		assert AoC2020_15.createDebug(splitLines("1,3,2")).solvePart1() == 1;
		assert AoC2020_15.createDebug(splitLines("2,1,3")).solvePart1() == 10;
		assert AoC2020_15.createDebug(splitLines("1,2,3")).solvePart1() == 27;
		assert AoC2020_15.createDebug(splitLines("2,3,1")).solvePart1() == 78;
		assert AoC2020_15.createDebug(splitLines("3,2,1")).solvePart1() == 438;
		assert AoC2020_15.createDebug(splitLines("3,1,2")).solvePart1() == 1836;
		assert AoC2020_15.createDebug(splitLines("0,3,6")).solvePart2() == 175594;
		assert AoC2020_15.createDebug(splitLines("1,3,2")).solvePart2() == 2578;
		assert AoC2020_15.createDebug(splitLines("2,1,3")).solvePart2() == 3544142;
		assert AoC2020_15.createDebug(splitLines("1,2,3")).solvePart2() == 261214;
		assert AoC2020_15.createDebug(splitLines("2,3,1")).solvePart2() == 6895259;
		assert AoC2020_15.createDebug(splitLines("3,2,1")).solvePart2() == 18;
		assert AoC2020_15.createDebug(splitLines("3,1,2")).solvePart2() == 362;

        final Puzzle puzzle = Aocd.puzzle(2020, 15);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", () -> AoC2020_15.create(inputData).solvePart1()),
            () -> lap("Part 2", () -> AoC2020_15.create(inputData).solvePart2())
        );
	}
}
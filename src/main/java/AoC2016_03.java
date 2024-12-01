import static com.github.pareronia.aoc.IntegerSequence.Range.range;

import java.util.Arrays;
import java.util.List;

import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2016_03 extends SolutionBase<int[][], Integer, Integer> {

	private AoC2016_03(final boolean debug) {
		super(debug);
	}
	
	public static final AoC2016_03 create() {
		return new AoC2016_03(false);
	}

	public static final AoC2016_03 createDebug() {
		return new AoC2016_03(true);
	}

	@Override
    protected int[][] parseInput(final List<String> inputs) {
        return inputs.stream()
		    .map(s -> Arrays.stream(s.split(" "))
		                .filter(StringUtils::isNotBlank)
		                .mapToInt(Integer::parseInt)
		                .toArray())
		    .toArray(int[][]::new);
    }

    @Override
	public Integer solvePart1(final int[][] nums) {
		return (int) Arrays.stream(nums)
            .map(t -> new Triangle(t[0], t[1], t[2]))
            .filter(Triangle::valid)
            .count();
	}

	@Override
	public Integer solvePart2(final int[][] nums) {
	    return range(0, nums.length, 3).intStream()
	        .map(i -> (int) range(3).intStream()
                .mapToObj(j -> new Triangle(
                                nums[i][j], nums[i + 1][j], nums[i + 2][j]))
                .filter(Triangle::valid)
                .count())
	        .sum();
	}

	@Samples({
	    @Sample(method = "part1", input = TEST1, expected = "0"),
	    @Sample(method = "part1", input = TEST2, expected = "1"),
	    @Sample(method = "part2", input = TEST3, expected = "2"),
	})
	public static void main(final String[] args) throws Exception {
	    AoC2016_03.create().run();
	}

	private static final String TEST1 = "    5   10   25";
	private static final String TEST2 = "    3    4    5";
	private static final String TEST3 = """
	         5    3    6
	        10    4    8
	        25    5   10
	        """;
	
	record Triangle(int a, int b, int c) {
	    
        public boolean valid() {
            return a + b > c && a + c > b && b + c > a;
        }
	}
}

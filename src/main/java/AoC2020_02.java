import static com.github.pareronia.aoc.Utils.asCharacterStream;

import java.util.List;
import java.util.function.Predicate;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_02
    extends SolutionBase<List<AoC2020_02.PasswordAndPolicy>, Long, Long> {
	
	private AoC2020_02(final boolean debug) {
		super(debug);
	}
	
	public static AoC2020_02 create() {
		return new AoC2020_02(false);
	}

	public static AoC2020_02 createDebug() {
		return new AoC2020_02(true);
	}
	
	@Override
    protected List<PasswordAndPolicy> parseInput(final List<String> inputs) {
        return inputs.stream().map(PasswordAndPolicy::create).toList();
    }

    @Override
	public Long solvePart1(final List<PasswordAndPolicy> inputs) {
		return countValid(inputs, PasswordAndPolicy::isValid1);
	}

	@Override
	public Long solvePart2(final List<PasswordAndPolicy> inputs) {
		return countValid(inputs, PasswordAndPolicy::isValid2);
	}

	private long countValid(
	        final List<PasswordAndPolicy> inputs,
	        final Predicate<PasswordAndPolicy> predicate
    ) {
		return inputs.stream().filter(predicate).count();
	}

	@Samples({
	    @Sample(method = "part1", input = TEST, expected = "2"),
	    @Sample(method = "part2", input = TEST, expected = "1"),
	})
	public static void main(final String[] args) throws Exception {
	    AoC2020_02.create().run();
	}
	
	private static final String TEST = """
	        1-3 a: abcde
	        1-3 b: cdefg
	        2-9 c: ccccccccc
	        """;
	
	record PasswordAndPolicy(int first, int second, char wanted, String password) {
		
		public static PasswordAndPolicy create(final String input) {
			final String[] splits = input.split(": ");
			final String[] leftAndRight = splits[0].split(" ");
			final String[] firstAndSecond = leftAndRight[0].split("-");
			final int first = Integer.parseInt(firstAndSecond[0]);
			final int second = Integer.parseInt(firstAndSecond[1]);
			final char wanted = leftAndRight[1].charAt(0);
			final String password = splits[1];
			return new PasswordAndPolicy(first, second, wanted, password);
		}
		
		public boolean isValid1() {
			final long count = asCharacterStream(password)
					.filter(c -> c == wanted)
					.count();
			return first <= count && count <= second;
		}
		
		public boolean isValid2() {
			return password.charAt(first - 1) ==  wanted
			     ^ password.charAt(second - 1) == wanted;
		}
	}
}

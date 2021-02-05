import static com.github.pareronia.aoc.Utils.asCharacterStream;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import com.github.pareronia.aocd.Aocd;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2020_02 extends AoCBase {
	
	private final List<String> inputs;

	private AoC2020_02(List<String> input, boolean debug) {
		super(debug);
		this.inputs = input;
	}
	
	public static AoC2020_02 create(List<String> input) {
		return new AoC2020_02(input, false);
	}

	public static AoC2020_02 createDebug(List<String> input) {
		return new AoC2020_02(input, true);
	}
	
	@Override
	public long solvePart1() {
		return countValid(PasswordAndPolicy::isValid1);
	}

	@Override
	public long solvePart2() {
		return countValid(PasswordAndPolicy::isValid2);
	}

	private long countValid(final Predicate<PasswordAndPolicy> predicate) {
		return this.inputs.stream()
				.map(PasswordAndPolicy::create)
				.filter(predicate)
				.count();
	}

	public static void main(String[] args) throws Exception {
		assert AoC2020_02.createDebug(TEST).solvePart1() == 2;
		assert AoC2020_02.createDebug(TEST).solvePart2() == 1;
		
		final List<String> input = Aocd.getData(2020, 2);
		lap("Part 1", () -> AoC2020_02.create(input).solvePart1());
		lap("Part 2", () -> AoC2020_02.create(input).solvePart2());
	}
	
	private static final List<String> TEST = splitLines(
			"1-3 a: abcde\r\n" +
			"1-3 b: cdefg\r\n" +
			"2-9 c: ccccccccc"
	);
	
	@RequiredArgsConstructor
	@ToString
	private static final class PasswordAndPolicy {
		private final Integer first;
		private final Integer second;
		private final String wanted;
		private final String password;
		
		public static PasswordAndPolicy create(String input) {
			final String[] splits = requireNonNull(input).split(": ");
			final String[] leftAndRight = splits[0].split(" ");
			final String[] firstAndSecond = leftAndRight[0].split("-");
			final Integer first = Integer.valueOf(firstAndSecond[0]);
			final Integer second = Integer.valueOf(firstAndSecond[1]);
			final String wanted = leftAndRight[1];
			final String password = splits[1];
			return new PasswordAndPolicy(first, second, wanted, password);
		}
		
		private boolean equal(char c, String string) {
			return String.valueOf(c).equals(string);
		}
		
		public boolean isValid1() {
			final long count = asCharacterStream(password)
					.filter(c -> equal(c, wanted))
					.count();
			return first <= count && count <= second;
		}
		
		public boolean isValid2() {
			return equal(password.charAt(first - 1), wanted)
					^ equal(password.charAt(second - 1), wanted);
		}
	}
}

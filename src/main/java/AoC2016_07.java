import static com.github.pareronia.aoc.Utils.toAString;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2016_07 extends SolutionBase<List<String>, Integer, Integer> {

    private static final Pattern ABBA = Pattern.compile("([a-z])(?!\\1)([a-z])\\2\\1");
    private static final Pattern HYPERNET = Pattern.compile("\\[([a-z]*)\\]");
    private static final Pattern ABA = Pattern.compile("([a-z])(?!\\1)[a-z]\\1");

	private AoC2016_07(final boolean debug) {
		super(debug);
	}
	
	public static final AoC2016_07 create() {
		return new AoC2016_07(false);
	}

	public static final AoC2016_07 createDebug() {
		return new AoC2016_07(true);
	}
	
	@Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }

    private boolean isTLS(final String ip) {
	    final List<String> mhs = HYPERNET.matcher(ip).results()
	            .map(m -> m.group(1))
	            .toList();
	    String temp = ip;
	    for (final String mh : mhs) {
	        if (ABBA.matcher(mh).find()) {
	            return false;
	        }
	        temp = temp.replace("[" + mh + "]", "/");
	    }
	    return ABBA.matcher(temp).find();
	}
	
	private String aba2bab(final String string) {
	    assert string.length() == 3;
	    return Stream.of(string.charAt(1), string.charAt(0), string.charAt(1))
	            .collect(toAString());
	}
	
	private boolean isSLS(final String ip) {
	    final List<String> mhs = HYPERNET.matcher(ip).results()
	            .map(m -> m.group(1))
	            .toList();
	    String temp = ip;
	    for (final String mh : mhs) {
	        temp = temp.replace("[" + mh + "]", "/");
	    }
	    final String temp2 = temp;
	    return Stream.iterate(0, i -> i + 1)
	            .limit(temp2.length() - 3)
	            .flatMap(i -> ABA.matcher(temp2.substring(i)).results())
	            .map(m -> m.group(0))
	            .map(this::aba2bab)
	            .anyMatch(aba -> mhs.stream().anyMatch(mh -> mh.contains(aba)));
	}
	
	@Override
	public Integer solvePart1(final List<String> inputs) {
		return (int) inputs.stream().filter(this::isTLS).count();
	}

	@Override
	public Integer solvePart2(final List<String> inputs) {
		return (int) inputs.stream().filter(this::isSLS).count();
	}

	@Samples({
	    @Sample(method = "part1", input = TEST1, expected = "2"),
	    @Sample(method = "part2", input = TEST2, expected = "3"),
	})
	public static void main(final String[] args) throws Exception {
	    AoC2016_07.create().run();
	}

	private static final String TEST1 = """
	        abba[mnop]qrst\r
	        abcd[bddb]xyyx\r
	        aaaa[qwer]tyui\r
	        ioxxoj[asdfgh]zxcvbn\r
	        abcox[ooooo]xocba
	        """;
	private static final String TEST2 = """
	        aba[bab]xyz\r
	        xyx[xyx]xyx\r
	        aaa[kek]eke\r
	        zazbz[bzb]cdb
	        """;
}

import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.github.pareronia.aocd.Aocd;

public class AoC2016_07 extends AoCBase {

    private static final Pattern ABBA = Pattern.compile("([a-z])(?!\\1)([a-z])\\2\\1");
    private static final Pattern HYPERNET = Pattern.compile("\\[([a-z]*)\\]");
    private static final Pattern ABA = Pattern.compile("([a-z])(?!\\1)[a-z]\\1");

	private final List<String> inputs;
	
	private AoC2016_07(List<String> inputs, boolean debug) {
		super(debug);
		this.inputs = inputs;
	}
	
	public static final AoC2016_07 create(List<String> input) {
		return new AoC2016_07(input, false);
	}

	public static final AoC2016_07 createDebug(List<String> input) {
		return new AoC2016_07(input, true);
	}
	
	private boolean isTLS(String ip) {
	    final List<String> mhs = HYPERNET.matcher(ip).results()
	            .map(m -> m.group(1))
	            .collect(toList());
	    String temp = ip;
	    for (final String mh : mhs) {
	        if (ABBA.matcher(mh).find()) {
	            return false;
	        }
	        temp = temp.replace("[" + mh + "]", "/");
	    }
	    return ABBA.matcher(temp).find();
	}
	
	private String aba2bab(String string) {
	    assert string.length() == 3;
	    return Stream.of(string.charAt(1), string.charAt(0), string.charAt(1))
	            .collect(toAString());
	}
	
	private boolean isSLS(String ip) {
	    final List<String> mhs = HYPERNET.matcher(ip).results()
	            .map(m -> m.group(1))
	            .collect(toList());
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
	public Integer solvePart1() {
		return (int) this.inputs.stream()
		        .filter(this::isTLS)
		        .count();
	}

	@Override
	public Integer solvePart2() {
		return (int) this.inputs.stream()
		        .filter(this::isSLS)
		        .count();
	}

	public static void main(String[] args) throws Exception {
		assert AoC2016_07.createDebug(TEST1).solvePart1() == 2;
		assert AoC2016_07.createDebug(TEST2).solvePart2() == 3;
		
		final List<String> input = Aocd.getData(2016, 7);
		lap("Part 1", () -> AoC2016_07.create(input).solvePart1());
		lap("Part 2", () -> AoC2016_07.create(input).solvePart2());
	}

	private static final List<String> TEST1 = splitLines(
	        "abba[mnop]qrst\r\n" +
	        "abcd[bddb]xyyx\r\n" +
	        "aaaa[qwer]tyui\r\n" +
	        "ioxxoj[asdfgh]zxcvbn\r\n" +
	        "abcox[ooooo]xocba"
	);
	private static final List<String> TEST2 = splitLines(
	        "aba[bab]xyz\r\n" +
	        "xyx[xyx]xyx\r\n" +
	        "aaa[kek]eke\r\n" +
	        "zazbz[bzb]cdb"
	);
}

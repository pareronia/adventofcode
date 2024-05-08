import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.Utils.last;
import static com.github.pareronia.aoc.Utils.toAString;

import java.util.List;

import com.github.pareronia.aoc.Counter;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2016_06
        extends SolutionBase<List<Counter<Character>>, String, String> {

	private AoC2016_06(final boolean debug) {
		super(debug);
	}
	
	public static final AoC2016_06 create() {
		return new AoC2016_06(false);
	}

	public static final AoC2016_06 createDebug() {
		return new AoC2016_06(true);
	}
	
	@Override
    protected List<Counter<Character>> parseInput(final List<String> inputs) {
        return range(inputs.get(0).length()).intStream()
            .mapToObj(i -> new Counter<>(inputs.stream()
                                            .map(s -> s.charAt(i))))
            .toList();
    }

    @Override
	public String solvePart1(final List<Counter<Character>> counters) {
	    return counters.stream()
	            .map(c -> c.mostCommon().get(0).value())
	            .collect(toAString());
	}

	@Override
	public String solvePart2(final List<Counter<Character>> counters) {
	    return counters.stream()
	            .map(c -> last(c.mostCommon()).value())
	            .collect(toAString());
	}

	@Samples({
	    @Sample(method = "part1", input = TEST, expected = "easter"),
	    @Sample(method = "part2", input = TEST, expected = "advent"),
	})
	public static void main(final String[] args) throws Exception {
	    AoC2016_06.create().run();
	}

	private static final String TEST = """
	        eedadn
	        drvtee
	        eandsr
	        raavrd
	        atevrs
	        tsrnev
	        sdttsa
	        rasrtv
	        nssdts
	        ntnada
	        svetve
	        tesnvt
	        vntsnd
	        vrdear
	        dvrsen
	        enarar
	        """;
}

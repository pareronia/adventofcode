import static java.util.stream.Collectors.toList;

import com.github.pareronia.aoc.knothash.KnotHash;
import com.github.pareronia.aoc.knothash.KnotHash.State;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2017_10 extends SolutionBase<String, Integer, String> {

    private AoC2017_10(final boolean debug) {
        super(debug);
    }

    public static AoC2017_10 create() {
        return new AoC2017_10(false);
    }

    public static AoC2017_10 createDebug() {
        return new AoC2017_10(true);
    }

    @Override
    protected String parseInput(final List<String> inputs) {
        return inputs.getFirst();
    }

    private Integer solve1(final String input, final List<Integer> elements) {
        final List<Integer> lengths =
                Arrays.stream(input.split(",")).map(Integer::valueOf).collect(toList());
        final State state = new State(elements, lengths, 0, 0);
        final State ans = KnotHash.round(state);
        return ans.elements().get(0) * ans.elements().get(1);
    }

    @Override
    public Integer solvePart1(final String input) {
        return solve1(input, new ArrayList<>(KnotHash.SEED));
    }

    @Override
    public String solvePart2(final String input) {
        return KnotHash.hexString(input);
    }

    @Override
	protected void samples() {
    	final AoC2017_10 test = createDebug();
    	final String input = test.parseInput(List.of("3,4,1,5"));
    	assert test.solve1(input, new ArrayList<>(List.of(0, 1, 2, 3, 4))) == 12;
	}

	@Samples({
        @Sample(
                method = "part2",
                input = "AoC 2017",
                expected = "33efeb34ea91902bb2f59c9920caa6cd"),
        @Sample(method = "part2", input = "1,2,3", expected = "3efbe78a8d82f29979031a4aa0b16a9d"),
        @Sample(method = "part2", input = "1,2,4", expected = "63960835bcdc130f0b66d7ff4f6a5a8e"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }
}

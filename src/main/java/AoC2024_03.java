import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_03
        extends SolutionBase<String, Integer, Integer> {
    
    private final Pattern REGEX = Pattern.compile
            ("(do(n't)?)\\(\\)|mul\\((\\d{1,3}),(\\d{1,3})\\)");

    private AoC2024_03(final boolean debug) {
        super(debug);
    }
    
    public static AoC2024_03 create() {
        return new AoC2024_03(false);
    }
    
    public static AoC2024_03 createDebug() {
        return new AoC2024_03(true);
    }
    
    @Override
    protected String parseInput(final List<String> inputs) {
        return inputs.stream().collect(joining());
    }
    
    private int solve(final String input, final boolean useConditionals) {
        int ans = 0;
        boolean enabled = true;
        final Matcher matcher = REGEX.matcher(input);
        while (matcher.find()) {
            final MatchResult m = matcher.toMatchResult();
            if (m.group(0).equals("do()")) {
                enabled = true;
            } else if (m.group(0).equals("don't()")) {
                enabled = false;
            } else {
                if (!useConditionals || enabled) {
                    ans += parseInt(m.group(3)) * parseInt(m.group(4));
                }
            }
        }
        return ans;
    }
    
    @Override
    public Integer solvePart1(final String input) {
        return solve(input, false);
    }
    
    @Override
    public Integer solvePart2(final String input) {
        return solve(input, true);
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "161"),
        @Sample(method = "part2", input = TEST2, expected = "48"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2024_03.create().run();
    }

    private static final String TEST1 = """
            xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
            """;

    private static final String TEST2 = """
            xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
            """;
}
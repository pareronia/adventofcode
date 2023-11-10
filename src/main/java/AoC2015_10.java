import java.util.List;

import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2015_10 extends SolutionBase<String, Integer, Integer> {
    
    private AoC2015_10(final boolean debug) {
        super(debug);
    }

    public static final AoC2015_10 create() {
        return new AoC2015_10(false);
    }

    public static final AoC2015_10 createDebug() {
        return new AoC2015_10(true);
    }
    
    private String lookAndSay(final String string) {
        final StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < string.length()) {
            final char digit = string.charAt(i);
            int j = 0;
            while (i + j < string.length() && string.charAt(i + j) == digit) {
                j++;
            }
            result.append(j).append(digit);
            i += j;
        }
        return result.toString();
    }
    
    @Override
    protected String parseInput(final List<String> inputs) {
        assert inputs.size() == 1;
        return inputs.get(0);
    }

    private String solve(final String input, final int iterations) {
        String string = input;
        for (int i = 0; i < iterations; i++) {
            string = lookAndSay(string);
            log(i + ": " + string.length());
        }
        return string;
    }

    @Override
    public Integer solvePart1(final String input) {
        return solve(input, 40).length();
    }

    @Override
    public Integer solvePart2(final String input) {
        return solve(input, 50).length();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_10.createDebug().solve("1", 5).equals("312211");

        AoC2015_10.create().run();
    }
}

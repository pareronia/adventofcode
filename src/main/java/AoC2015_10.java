import java.util.List;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2015_10 extends AoCBase {
    
    private final String input;
    
    private AoC2015_10(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = inputs.get(0);
    }

    public static final AoC2015_10 create(final List<String> input) {
        return new AoC2015_10(input, false);
    }

    public static final AoC2015_10 createDebug(final List<String> input) {
        return new AoC2015_10(input, true);
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
    
    private String solve(final int iterations) {
        String string = this.input;
        for (int i = 0; i < iterations; i++) {
            string = lookAndSay(string);
            log(i + ": " + string.length());
        }
        return string;
    }

    @Override
    public Integer solvePart1() {
        return solve(40).length();
    }

    @Override
    public Integer solvePart2() {
        return solve(50).length();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_10.createDebug(List.of("1")).solve(5).equals("312211");

        final Puzzle puzzle = Aocd.puzzle(2015, 10);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2015_10.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2015_10.create(puzzle.getInputData()).solvePart2())
        );
    }
}

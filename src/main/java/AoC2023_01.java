import static com.github.pareronia.aoc.Utils.last;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_01
        extends SolutionBase<List<String>, Integer, Integer> {
    
    private AoC2023_01(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_01 create() {
        return new AoC2023_01(false);
    }
    
    public static AoC2023_01 createDebug() {
        return new AoC2023_01(true);
    }
    
    @Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }
    
    private int solve(
            final List<String> input,
            final Function<String, List<Integer>> getDigits
    ) {
        return input.stream()
                .map(getDigits)
                .mapToInt(digits -> digits.get(0) * 10 + last(digits))
                .sum();
    }

    @Override
    public Integer solvePart1(final List<String> input) {
        final Function<String, List<Integer>> getDigits = line ->
            Utils.asCharacterStream(line)
                .filter(Character::isDigit)
                .map(c -> Integer.parseInt(String.valueOf(c)))
                .toList();
        return solve(input, getDigits);
    }
    
    @Override
    public Integer solvePart2(final List<String> input) {
        final List<String> nums = List.of(
            "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
        final Function<String, Integer> findDigit = s -> {
            if (Character.isDigit(s.charAt(0))) {
                return Integer.parseInt(String.valueOf(s.charAt(0)));
            } else {
                for (int j = 0; j < nums.size(); j ++) {
                    if (s.startsWith(nums.get(j))) {
                        return j + 1;
                    }
                }
            }
            return null;
        };
        final Function<String, List<Integer>> getDigits = line ->
            Stream.iterate(0, i -> i < line.length(), i -> i + 1)
                .map(line::substring)
                .map(findDigit)
                .filter(Objects::nonNull)
                .toList();
        return solve(input, getDigits);
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "142"),
        @Sample(method = "part2", input = TEST2, expected = "281"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_01.create().run();
    }

    private static final String TEST1 = """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
            """;

    private static final String TEST2 = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
            """;
}

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

public class AoC2015_15 extends AoCBase {

    private final Ingredients ingredients;
    
    private AoC2015_15(final List<String> inputs, final boolean debug) {
        super(debug);
        this.ingredients = Ingredients.fromInput(inputs);
    }

    public static final AoC2015_15 create(final List<String> input) {
        return new AoC2015_15(input, false);
    }

    public static final AoC2015_15 createDebug(final List<String> input) {
        return new AoC2015_15(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        return this.ingredients.getHighestScore();
    }
    
    @Override
    public Integer solvePart2() {
        return this.ingredients.getHighestScoreWithCalorieLimit(500);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_15.createDebug(TEST).solvePart1() == 62_842_880;
        assert AoC2015_15.createDebug(TEST).solvePart2() == 57_600_000;

        final Puzzle puzzle = Aocd.puzzle(2015, 15);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2015_15.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2015_15.create(inputData)::solvePart2)
        );
    }
    
    private static final List<String> TEST = splitLines(
        "Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8\r\n" +
        "Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3"
    );
    
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class Ingredients {
        private enum Property { CAPACITY, DURABILITY, FLAVOR, TEXTURE, CALORIES  }
        
        private final int[][] ingredients;
        
        public static Ingredients fromInput(final List<String> inputs) {
            return new Ingredients(inputs.stream()
                    .map(line -> Utils.integerNumbers(line))
                    .toArray(int[][]::new));
        }
        
        public int getHighestScore() {
            return getMaximumScore(null);
        }
        
        public int getHighestScoreWithCalorieLimit(final int limit) {
            return getMaximumScore(limit);
        }
        
        private int getMaximumScore(final Integer limit) {
            return generateMeasures()
                    .mapToInt(m -> calculateScore(m, limit))
                    .max().orElseThrow();
        }
        
        private Stream<int[]> generateMeasures() {
            final Stream.Builder<int[]> builder = Stream.builder();
            for (int i = 0; i <= 100; i++) {
                if (this.ingredients.length == 2) {
                    builder.add(new int[] { i, 100 - i });
                    continue;
                }
                for (int j = 0; j <= 100 - i; j++) {
                    for (int k = 0; k <= 100 - i - j; k++) {
                        final int m = 100 - i - j - k;
                        builder.add(new int[] { i, j, k, m });
                    }
                }
            }
            return builder.build();
        }

        private int getPropertyScore(final int[] measures, final Property p) {
            return IntStream.range(0, this.ingredients.length)
                    .map(i -> this.ingredients[i][p.ordinal()] * measures[i])
                    .sum();
        }
        
        private int calculateScore(final int[] measures, final Integer caloriesTarget) {
            if (caloriesTarget != null
                    && getPropertyScore(measures, Property.CALORIES) != caloriesTarget) {
                return 0;
            }
            return Stream.of(Property.values())
                .filter(p -> p != Property.CALORIES)
                .mapToInt(p -> Math.max(0, getPropertyScore(measures, p)))
                .reduce(1, (a, b) -> a * b);
        }
    }
}

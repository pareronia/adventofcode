import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.IterTools;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2015_15 extends AoCBase {
    
    private static final String CAPACITY = "capacity";
    private static final String DURABILITY = "durability";
    private static final String FLAVOR = "flavor";
    private static final String TEXTURE = "texture";
    private static final String CALORIES = "calories";

    private static final String REGEXP =
        "([A-Za-z]+): capacity ([-0-9]+), durability ([-0-9]+), flavor ([-0-9]+), texture ([-0-9]+), calories ([-0-9]+)";
    
    private List<Map<String, Integer>> ingredients;
    
    private AoC2015_15(final List<String> inputs, final boolean debug) {
        super(debug);
        final Pattern pattern = Pattern.compile(REGEXP);
        this.ingredients = inputs.stream()
            .map(s -> {
                final Matcher m = pattern.matcher(s);
                if (m.matches()) {
                    return Map.of(
                            CAPACITY, Integer.valueOf(m.group(2)),
                            DURABILITY, Integer.valueOf(m.group(3)),
                            FLAVOR, Integer.valueOf(m.group(4)),
                            TEXTURE, Integer.valueOf(m.group(5)),
                            CALORIES, Integer.valueOf(m.group(6))
                    );
                } else {
                    throw new IllegalArgumentException();
                }
            })
            .collect(toList());
        log(this.ingredients);
        assert this.ingredients.size() == 2 || this.ingredients.size() == 4;
    }

    public static final AoC2015_15 create(final List<String> input) {
        return new AoC2015_15(input, false);
    }

    public static final AoC2015_15 createDebug(final List<String> input) {
        return new AoC2015_15(input, true);
    }
    
    private void generatePermutations(final List<Integer> amounts, final Stream.Builder<List<Integer>> builder) {
        if (amounts.stream().mapToInt(Integer::intValue).sum() != 100) {
            return;
        }
        IterTools.permutations(amounts, builder::add);
    }
    
    private Stream<List<Integer>> generateMeasures() {
        final Stream.Builder<List<Integer>> builder = Stream.builder();
        for (int i = 100; i >= 0; i--) {
            for (int j = i; j >= 0; j--) {
                if (ingredients.size() == 2) {
                    generatePermutations(List.of(i, j), builder);
                    continue;
                }
                for (int k = j; k >= 0; k--) {
                    for (int m = k; m >= 0; m--) {
                        generatePermutations(List.of(i, j, k, m), builder);
                    }
                }
            }
        }
        return builder.build();
    }
    
    private int calculateScore(final List<Integer> measures) {
        return calculateScore(measures, null);
    }
        
    private int calculateScore(final List<Integer> measures, final Integer caloriesTarget) {
        assert ingredients.size() == measures.size();
        final int size = ingredients.size();
        if (caloriesTarget != null) {
            final int total = IntStream.range(0, size).map(i -> ingredients.get(i).get(CALORIES) * measures.get(i)).sum();
            if (total != caloriesTarget) {
                return 0;
            }
        }
        final Map<String, Integer> totals = new HashMap<>();
        for (final String attribute : List.of(CAPACITY, DURABILITY, FLAVOR, TEXTURE)) {
            totals.put(
                    attribute,
                    IntStream.range(0, size).map(i -> ingredients.get(i).get(attribute) * measures.get(i)).sum());
            if (totals.get(attribute) <= 0) {
                return 0;
            }
        }
        return totals.values().stream().reduce(1, (a, b) -> a * b);
    }
    
    @Override
    public Integer solvePart1() {
        return generateMeasures()
            .mapToInt(this::calculateScore)
            .max().orElseThrow();
    }
    
    @Override
    public Integer solvePart2() {
        return generateMeasures()
            .mapToInt(m -> calculateScore(m, 500))
            .max().orElseThrow();
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
            "Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8\r\n"
            + "Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3"
    );
}

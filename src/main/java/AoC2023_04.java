import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.SetUtils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_04
        extends SolutionBase<List<AoC2023_04.ScratchCard>, Integer, Integer> {
    
    private AoC2023_04(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_04 create() {
        return new AoC2023_04(false);
    }
    
    public static AoC2023_04 createDebug() {
        return new AoC2023_04(true);
    }
    
    @Override
    protected List<ScratchCard> parseInput(final List<String> inputs) {
        return inputs.stream().map(ScratchCard::fromInput).toList();
    }
    
    @Override
    public Integer solvePart1(final List<ScratchCard> cards) {
        return cards.stream()
            .filter(c -> c.matched() > 0)
            .mapToInt(c -> 1 << (c.matched() - 1))
            .sum();
    }
    
    @Override
    public Integer solvePart2(final List<ScratchCard> cards) {
        final int[] count = new int[cards.size()];
        Arrays.fill(count, 1);
        IntStream.range(0, cards.size()).forEach(i ->
            IntStream.range(0, cards.get(i).matched()).forEach(j ->
                count[i + j + 1] += count[i]));
        return Arrays.stream(count).sum();
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "13"),
        @Sample(method = "part2", input = TEST, expected = "30"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_04.create().run();
    }

    private static final String TEST = """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
            """;
    
    record ScratchCard(int matched) {
        private static Set<Integer> getNumbers(final String s) {
            return Arrays.stream(s.strip().split("\\s+"))
                    .map(String::strip)
                    .map(Integer::parseInt)
                    .collect(toSet());
        }
        
        public static ScratchCard fromInput(final String line) {
            final String[] splits = line.split(": ")[1].split(" \\| ");
            final Set<Integer> matching = SetUtils.intersection(
                    getNumbers(splits[0]), getNumbers(splits[1]));
            return new ScratchCard(matching.size());
        }
    }
}

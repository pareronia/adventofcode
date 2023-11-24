import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_20 extends AoCBase {
    
    private static final long ENCRYPTION_KEY = 811589153L;
    private final List<Integer> numbers;
    
    private AoC2022_20(final List<String> input, final boolean debug) {
        super(debug);
        this.numbers = input.stream().map(Integer::valueOf).collect(toList());
    }
    
    public static final AoC2022_20 create(final List<String> input) {
        return new AoC2022_20(input, false);
    }

    public static final AoC2022_20 createDebug(final List<String> input) {
        return new AoC2022_20(input, true);
    }
    
    private long solve(final int rounds, final long factor) {
        final var nums = this.numbers.stream()
                .map(num -> factor * num)
                .collect(toList());
        final int size = nums.size();
        final var idxs = range(size).intStream().boxed().collect(toList());
        range(rounds).forEach(round -> {
            Stream.iterate(0, i -> i < size, i -> i + 1).forEach(i -> {
                final int idx = idxs.indexOf(i);
                idxs.remove(i);
                final int new_idx = Math.floorMod(idx + nums.get(i), idxs.size());
                idxs.add(new_idx, i);
            });
        });
        final int zero = idxs.indexOf(nums.indexOf(0L));
        return IntStream.of(1000, 2000, 3000)
            .mapToLong(i -> nums.get(idxs.get((zero + i) % size)))
            .sum();
    }
    
    @Override
    public Long solvePart1() {
        return solve(1, 1L);
    }

    @Override
    public Long solvePart2() {
        return solve(10, ENCRYPTION_KEY);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_20.createDebug(TEST).solvePart1() == 3;
        assert AoC2022_20.createDebug(TEST).solvePart2() == 1_623_178_306;

        final Puzzle puzzle = Aocd.puzzle(2022, 20);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_20.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_20.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines("""
        1
        2
        -3
        3
        -2
        0
        4
        """);
}

import static com.github.pareronia.aoc.Range.range;

import java.util.List;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_06 extends AoCBase {
    
    private final String buffer;
    
    private AoC2022_06(final List<String> input, final boolean debug) {
        super(debug);
        this.buffer = input.get(0);
    }
    
    public static final AoC2022_06 create(final List<String> input) {
        return new AoC2022_06(input, false);
    }

    public static final AoC2022_06 createDebug(final List<String> input) {
        return new AoC2022_06(input, true);
    }
    
    private int solve(final int size) {
        for (final int i : range(size, buffer.length(), 1)) {
            if (range(i - size, i, 1).stream().map(buffer::charAt).distinct().count() == size) {
                return i;
            }
        };
        throw new IllegalStateException("Unsolvable");
    }
    
    @Override
    public Integer solvePart1() {
        return solve(4);
    }

    @Override
    public Integer solvePart2() {
        return solve(14);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_06.createDebug(TEST1).solvePart1() == 7;
        assert AoC2022_06.createDebug(TEST2).solvePart1() == 5;
        assert AoC2022_06.createDebug(TEST3).solvePart1() == 6;
        assert AoC2022_06.createDebug(TEST4).solvePart1() == 10;
        assert AoC2022_06.createDebug(TEST5).solvePart1() == 11;
        assert AoC2022_06.createDebug(TEST1).solvePart2() == 19;
        assert AoC2022_06.createDebug(TEST2).solvePart2() == 23;
        assert AoC2022_06.createDebug(TEST3).solvePart2() == 23;
        assert AoC2022_06.createDebug(TEST4).solvePart2() == 29;
        assert AoC2022_06.createDebug(TEST5).solvePart2() == 26;

        final Puzzle puzzle = Aocd.puzzle(2022, 6);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_06.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_06.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST1 = splitLines("mjqjpqmgbljsphdztnvjfqwrcgsmlb");
    private static final List<String> TEST2 = splitLines("bvwbjplbgvbhsrlpgdmjqwftvncz");
    private static final List<String> TEST3 = splitLines("nppdvjthqldpwncqszvftbrmjlhg");
    private static final List<String> TEST4 = splitLines("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg");
    private static final List<String> TEST5 = splitLines("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw");
}

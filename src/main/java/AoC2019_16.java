import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;

public class AoC2019_16 extends AoCBase {
    
    private final List<Integer> numbers;
    
    private AoC2019_16(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 1;
        this.numbers = Utils.asCharacterStream(input.get(0))
                .map(c -> c - '0')
                .collect(toList());
    }

    public static AoC2019_16 create(final List<String> input) {
        return new AoC2019_16(input, false);
    }

    public static AoC2019_16 createDebug(final List<String> input) {
        return new AoC2019_16(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        final int[] nums = this.numbers.stream().mapToInt(Integer::intValue).toArray();
        final int[] digits = Arrays.copyOf(nums, nums.length);
        range(100).forEach(i -> {
            final Patterns patterns = new Patterns();
            range(nums.length).forEach(j -> {
                final Pattern p = patterns.next();
                digits[j] = Math.abs(
                        Arrays.stream(nums).map(n -> n * p.next()).sum() % 10);
            });
            System.arraycopy(digits, 0, nums, 0, nums.length);
        });
        return Integer.parseInt(Arrays.stream(nums).limit(8)
                .mapToObj(n -> Character.valueOf((char) ('0' + n)))
                .collect(toAString()));
    }
    
    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2019_16.createDebug(TEST1).solvePart1() == 24176176;
        assert AoC2019_16.createDebug(TEST2).solvePart1() == 73745418;
        assert AoC2019_16.createDebug(TEST3).solvePart1() == 52432133;
        
        final Puzzle puzzle = Aocd.puzzle(2019, 16);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2019_16.createDebug(inputData)::solvePart1),
            () -> lap("Part 2", AoC2019_16.create(inputData)::solvePart2)
        );
    }
    
    private static final List<String> TEST1 = splitLines(
            "80871224585914546619083218645595"
    );
    private static final List<String> TEST2 = splitLines(
            "19617804207202209144916044189917"
    );
    private static final List<String> TEST3 = splitLines(
            "69317163492948606335995924319873"
    );

    @RequiredArgsConstructor
    private static final class Pattern implements Iterator<Integer> {
        private final int[] elements = new int[] { 0, 1, 0, -1 };
        private final int repeat;
        private int i = 0;
        private int j = 0;

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Integer next() {
            final int ans = elements[j];
            i++;
            if (i == repeat) {
                i = 0;
                j = (j + 1) % elements.length;
            }
            return ans;
        }
    }

    private static final class Patterns implements Iterator<Pattern> {
        private int n = 1;

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Pattern next() {
            final Pattern pattern = new Pattern(n);
            n++;
            pattern.next();
            return pattern;
        }
    }
}
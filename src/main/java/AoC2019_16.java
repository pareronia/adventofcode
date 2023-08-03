import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;

public class AoC2019_16 extends AoCBase {
    
    private static final int[] ELEMENTS = new int[] { 0, 1, 0, -1 };
    private static final int PHASES = 100;
    
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
    
    private int asInt(final IntStream stream) {
        return Integer.parseInt(stream
            .mapToObj(n -> Character.valueOf((char) ('0' + n)))
            .collect(toAString()));
    }
    
    @Override
    public Integer solvePart1() {
        final int[] nums = this.numbers.stream().mapToInt(Integer::intValue).toArray();
        final int[] digits = Arrays.copyOf(nums, nums.length);
        range(PHASES).forEach(i -> {
            final Patterns patterns = new Patterns();
            range(nums.length).forEach(j -> {
                final Pattern p = patterns.next();
                digits[j] = Math.abs(
                        Arrays.stream(nums).map(n -> n * p.next()).sum() % 10);
            });
            System.arraycopy(digits, 0, nums, 0, nums.length);
        });
        return asInt(Arrays.stream(nums).limit(8));
    }
    
    @Override
    public Integer solvePart2() {
        final int offset = asInt(
                    this.numbers.stream().limit(7).mapToInt(Integer::intValue));
        final int tailsize = 10_000 * this.numbers.size() - offset;
        final int repeat = tailsize / this.numbers.size() + 1;
        final int[] nums = new int[repeat * this.numbers.size()];
        final int[] copy = this.numbers.stream().mapToInt(Integer::intValue).toArray();
        range(repeat).forEach(i ->
            System.arraycopy(copy, 0, nums, i * copy.length, copy.length));
        range(PHASES).forEach(i -> {
            for (int j = nums.length - 2; j >= 0; j--) {
                nums[j] = (nums[j] + nums[j + 1]) % 10;
            }
        });
        return asInt(
            range(8).intStream().map(i -> nums[nums.length - tailsize + i]));
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2019_16.createDebug(TEST1).solvePart1() == 24176176;
        assert AoC2019_16.createDebug(TEST2).solvePart1() == 73745418;
        assert AoC2019_16.createDebug(TEST3).solvePart1() == 52432133;
        assert AoC2019_16.createDebug(TEST4).solvePart2() == 84462026;
        assert AoC2019_16.createDebug(TEST5).solvePart2() == 78725270;
        assert AoC2019_16.createDebug(TEST6).solvePart2() == 53553731;
        
        final Puzzle puzzle = Aocd.puzzle(2019, 16);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2019_16.createDebug(inputData)::solvePart1),
            () -> lap("Part 2", AoC2019_16.create(inputData)::solvePart2)
        );
    }
    
    private static final List<String> TEST1 = splitLines(
            "80871224585914546619083218645595");
    private static final List<String> TEST2 = splitLines(
            "19617804207202209144916044189917");
    private static final List<String> TEST3 = splitLines(
            "69317163492948606335995924319873");
    private static final List<String> TEST4 = splitLines(
            "03036732577212944063491565474664");
    private static final List<String> TEST5 = splitLines(
            "02935109699940807407585447034323");
    private static final List<String> TEST6 = splitLines(
            "03081770884921959731165446850517");

    @RequiredArgsConstructor
    private static final class Pattern implements Iterator<Integer> {
        private final int repeat;
        private int i = 0;
        private int j = 0;

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Integer next() {
            final int ans = ELEMENTS[j];
            i++;
            if (i == repeat) {
                i = 0;
                j = (j + 1) % ELEMENTS.length;
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
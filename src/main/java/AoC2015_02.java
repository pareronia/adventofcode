import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aocd.Aocd;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

public final class AoC2015_02 extends AoCBase {

    private final transient List<Present> input;

    private AoC2015_02(final List<String> inputs, final boolean debug) {
        super(debug);
        this.input = inputs.stream().map(Present::fromInput).collect(toList());
    }

    public static AoC2015_02 create(final List<String> input) {
        return new AoC2015_02(input, false);
    }

    public static AoC2015_02 createDebug(final List<String> input) {
        return new AoC2015_02(input, true);
    }

    @Override
    public Integer solvePart1() {
        return this.input.stream()
                .mapToInt(Present::calculateRequiredArea)
                .sum();
    }

    @Override
    public Integer solvePart2() {
        return this.input.stream()
                .mapToInt(Present::calculateRequiredLength)
                .sum();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_02.createDebug(TEST1).solvePart1() == 58;
        assert AoC2015_02.createDebug(TEST2).solvePart1() == 43;
        assert AoC2015_02.createDebug(TEST1).solvePart2() == 34;
        assert AoC2015_02.createDebug(TEST2).solvePart2() == 14;

        final List<String> input = Aocd.getData(2015, 2);
        lap("Part 1", () -> AoC2015_02.create(input).solvePart1());
        lap("Part 2", () -> AoC2015_02.create(input).solvePart2());
    }

    private static final List<String> TEST1 = splitLines("2x3x4");
    private static final List<String> TEST2 = splitLines("1x1x10");
    
    @RequiredArgsConstructor
    @ToString
    private static final class Present {
        private final Integer length;
        private final Integer width;
        private final Integer height;
        
        public static Present fromInput(final String s) {
            final int[] sp = Stream.of(s.split("x")).mapToInt(Integer::valueOf).toArray();
            return new Present(sp[0], sp[1], sp[2]);
        }
        
        public int calculateRequiredArea() {
            final int[] sides = new int[] {
                    2 * this.length * this.width,
                    2 * this.width * this.height,
                    2 * this.height * this.length};
            return Arrays.stream(sides).sum() + Arrays.stream(sides).min().getAsInt() / 2;
        }

        public int calculateRequiredLength() {
            final int[] circumferences = new int[] {
                    2 * (this.length + this.width),
                    2 * (this.width + this.height),
                    2 * (this.height + this.length)};
            return Arrays.stream(circumferences).min().getAsInt()
                    + this.length * this.width * this.height;
        }
    }
}
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

import com.github.pareronia.aocd.Aocd;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public final class AoC2015_02 extends AoCBase {

    private final transient List<Present> input;

    private AoC2015_02(final List<String> inputs, final boolean debug) {
        super(debug);
        this.input = inputs.stream()
                .map(s -> {
                    final String[] sp = s.split("x");
                    return new Present(Integer.valueOf(sp[0]), Integer.valueOf(sp[1]), Integer.valueOf(sp[2]));
                })
                .collect(toList());
    }

    public static AoC2015_02 create(final List<String> input) {
        return new AoC2015_02(input, false);
    }

    public static AoC2015_02 createDebug(final List<String> input) {
        return new AoC2015_02(input, true);
    }
    
    private Integer caclculateRequiredArea(final Present present) {
        final int[] sides = new int[] {
                2 * present.getLength() * present.getWidth(),
                2 * present.getWidth() * present.getHeight(),
                2 * present.getHeight() * present.getLength()};
        return Arrays.stream(sides).sum() + Arrays.stream(sides).min().getAsInt() / 2;
    }

    private Integer caclculateRequiredLength(final Present present) {
        final int[] circumferences = new int[] {
                2 * (present.getLength() + present.getWidth()),
                2 * (present.getWidth() + present.getHeight()),
                2 * (present.getHeight() + present.getLength())};
        return Arrays.stream(circumferences).min().getAsInt()
                + present.getLength() * present.getWidth() * present.getHeight();
    }

    @Override
    public Integer solvePart1() {
        return this.input.stream()
                .map(this::caclculateRequiredArea)
                .collect(summingInt(Integer::valueOf));
    }

    @Override
    public Integer solvePart2() {
        return this.input.stream()
                .map(this::caclculateRequiredLength)
                .collect(summingInt(Integer::valueOf));
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
        @Getter
        private final Integer length;
        @Getter
        private final Integer width;
        @Getter
        private final Integer height;
    }
}
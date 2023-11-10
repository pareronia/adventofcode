import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

public final class AoC2015_02 extends SolutionBase<List<Present>, Integer, Integer> {

    private AoC2015_02(final boolean debug) {
        super(debug);
    }

    public static AoC2015_02 create() {
        return new AoC2015_02(false);
    }

    public static AoC2015_02 createDebug() {
        return new AoC2015_02(true);
    }

    @Override
    protected List<Present> parseInput(final List<String> inputs) {
        return inputs.stream().map(Present::fromInput).collect(toList());
    }

    @Override
    public Integer solvePart1(final List<Present> input) {
        return input.stream()
                .mapToInt(Present::calculateRequiredArea)
                .sum();
    }

    @Override
    public Integer solvePart2(final List<Present> input) {
        return input.stream()
                .mapToInt(Present::calculateRequiredLength)
                .sum();
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "58"),
        @Sample(method = "part1", input = TEST2, expected = "43"),
        @Sample(method = "part2", input = TEST1, expected = "34"),
        @Sample(method = "part2", input = TEST2, expected = "14"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2015_02.create().run();
    }

    private static final String TEST1 = "2x3x4";
    private static final String TEST2 = "1x1x10";
}
    
@RequiredArgsConstructor
@ToString
final class Present {
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
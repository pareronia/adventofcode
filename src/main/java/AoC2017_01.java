import static java.util.stream.Collectors.summingInt;

import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aocd.Aocd;

public final class AoC2017_01 extends AoCBase {

    private final transient String input;
    
    private AoC2017_01(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = inputs.get(0);
    }

    public static AoC2017_01 create(final List<String> input) {
        return new AoC2017_01(input, false);
    }

    public static AoC2017_01 createDebug(final List<String> input) {
        return new AoC2017_01(input, true);
    }

    private Integer sumSameCharsAt(final int distance) {
        final String test = this.input + this.input.substring(0, distance);
        return Stream.iterate(0, i -> i < this.input.length(), i -> i + 1)
                .filter(i -> test.charAt(i) == test.charAt(i + distance))
                .map(test::charAt)
                .map(c -> Character.digit(c, 10))
                .collect(summingInt(Integer::valueOf));
    }
    
    @Override
    public Integer solvePart1() {
        return sumSameCharsAt(1);
    }
    
    @Override
    public Integer solvePart2() {
        assert this.input.length() % 2 == 0;
        return sumSameCharsAt(this.input.length() / 2);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_01.createDebug(splitLines("1122")).solvePart1() == 3;
        assert AoC2017_01.createDebug(splitLines("1111")).solvePart1() == 4;
        assert AoC2017_01.createDebug(splitLines("1234")).solvePart1() == 0;
        assert AoC2017_01.createDebug(splitLines("91212129")).solvePart1() == 9;
        assert AoC2017_01.createDebug(splitLines("1212")).solvePart2() == 6;
        assert AoC2017_01.createDebug(splitLines("1221")).solvePart2() == 0;
        assert AoC2017_01.createDebug(splitLines("123425")).solvePart2() == 4;
        assert AoC2017_01.createDebug(splitLines("123123")).solvePart2() == 12;
        assert AoC2017_01.createDebug(splitLines("12131415")).solvePart2() == 4;

        final List<String> input = Aocd.getData(2017, 1);
        lap("Part 1", () -> AoC2017_01.create(input).solvePart1());
        lap("Part 2", () -> AoC2017_01.create(input).solvePart2());
    }
}

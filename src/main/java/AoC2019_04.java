import static com.github.pareronia.aoc.Utils.asCharacterStream;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.github.pareronia.aocd.Aocd;

public class AoC2019_04 extends AoCBase {
    
    private final Integer min;
    private final Integer max;
    
    private AoC2019_04(List<String> input, boolean debug) {
        super(debug);
        assert input.size() == 1;
        final String[] split = input.get(0).split("-");
        this.min = Integer.valueOf(split[0]);
        this.max = Integer.valueOf(split[1]);
    }

    public static AoC2019_04 create(List<String> input) {
        return new AoC2019_04(input, false);
    }

    public static AoC2019_04 createDebug(List<String> input) {
        return new AoC2019_04(input, true);
    }
    
    private boolean doesNotDecrease(String passw) {
       final List<Character> chars = asCharacterStream(passw).collect(toList());
       final List<Character> sorted = asCharacterStream(passw).sorted().collect(toList());
       return chars.equals(sorted);
    }
    
    private boolean isValid1(String string) {
        return doesNotDecrease(string) &&
                asCharacterStream(string).collect(toSet()).size() != string.length();
    }
    
    private boolean isValid2(String string) {
        return doesNotDecrease(string) &&
                asCharacterStream(string).collect(groupingBy(c -> c, counting()))
                    .values().contains(2L);
    }
    
    private long countValid(Predicate<String> isValid) {
        return Stream.iterate(min, i -> i + 1).limit(max - min + 1)
                .map(i -> i.toString())
                .filter(isValid::test)
                .count();
    }

    @Override
    public Long solvePart1() {
        return countValid(this::isValid1);
    }

    @Override
    public Long solvePart2() {
        return countValid(this::isValid2);
    }

    public static void main(String[] args) throws Exception {
        assert AoC2019_04.createDebug(TEST).isValid1("122345") == true;
        assert AoC2019_04.createDebug(TEST).isValid1("111123") == true;
        assert AoC2019_04.createDebug(TEST).isValid1("111111") == true;
        assert AoC2019_04.createDebug(TEST).isValid1("223450") == false;
        assert AoC2019_04.createDebug(TEST).isValid1("123789") == false;
        assert AoC2019_04.createDebug(TEST).isValid2("112233") == true;
        assert AoC2019_04.createDebug(TEST).isValid2("123444") == false;
        assert AoC2019_04.createDebug(TEST).isValid2("111122") == true;

        final List<String> input = Aocd.getData(2019, 4);
        lap("Part 1", () -> AoC2019_04.create(input).solvePart1());
        lap("Part 2", () -> AoC2019_04.create(input).solvePart2());
    }

    private static final List<String> TEST = splitLines(
            "0-0"
    );
}
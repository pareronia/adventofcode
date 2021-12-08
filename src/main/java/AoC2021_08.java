import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.collections4.SetUtils;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;

public class AoC2021_08 extends AoCBase {
    
    private final List<List<String>> signals;
    private final List<List<String>> outputs;
    
    private AoC2021_08(final List<String> input, final boolean debug) {
        super(debug);
        this.signals = input.stream()
                .map(s -> s.split(" \\| ")[0])
                .map(s -> Arrays.stream(s.split(" ")).map(this::sort).collect(toList()))
                .collect(toList());
        log(signals);
        this.outputs = input.stream()
                .map(s -> s.split(" \\| ")[1])
                .map(s -> Arrays.stream(s.split(" ")).map(this::sort).collect(toList()))
                .collect(toList());
        log(outputs);
    }
    
    private String sort(final String in) {
        return Utils.asCharacterStream(in).sorted().collect(toAString());
    }

    public static final AoC2021_08 create(final List<String> input) {
        return new AoC2021_08(input, false);
    }

    public static final AoC2021_08 createDebug(final List<String> input) {
        return new AoC2021_08(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        return (int) this.outputs.stream()
            .flatMap(s -> s.stream())
            .filter(s -> Set.of(2, 4, 3, 7).contains(s.length()))
            .count();
    }

    private Stream<String> all(final int i) {
        return Stream.concat(this.signals.get(i).stream(), this.outputs.get(i).stream());
    }
    
    private Set<Character> asCharSet(final String in) {
        return Utils.asCharacterStream(in).collect(toSet());
    }

    @Override
    public Integer solvePart2() {
        return IntStream.range(0, this.outputs.size())
                .map(this::solve)
                .sum();
    }
    
    private Integer solve(final int i) {
        final Map<String, Character> digits = new HashMap<>();
        final Map<Integer, String> patterns = new HashMap<>();
        all(i).forEach(s -> {
            if (s.length() == 2) {
                digits.put(s, '1');
                patterns.put(1,  s);
            }
            if (s.length() == 3) {
                digits.put(s, '7');
                patterns.put(7, s);
            }
            if (s.length() == 4) {
                digits.put(s, '4');
                patterns.put(4, s);
            }
            if (s.length() == 7) {
                digits.put(s, '8');
                patterns.put(8, s);
            }
        });
        // 9
        final Set<String> sixes = all(i).filter(s -> s.length() == 6).collect(toSet());
        assert sixes.size() == 3;
        final Set<Character> four = asCharSet(patterns.get(4));
        final Set<String> possible_nines = sixes.stream()
            .filter(six -> SetUtils.intersection(asCharSet(six), four).toSet().equals(four))
            .collect(toSet());
        log("possible_nines: " + possible_nines);
        assert possible_nines.size() == 1;
        final String nine = possible_nines.iterator().next();
        digits.put(nine, '9');
        patterns.put(9, nine);
        // 0
        final Set<Character> one = asCharSet(patterns.get(1));
        final Set<String> possible_zeroes = sixes.stream()
            .filter(six -> !six.equals(nine))
            .filter(six -> SetUtils.intersection(asCharSet(six), one).toSet().equals(one))
            .collect(toSet());
        assert possible_zeroes.size() == 1;
        final String zero = possible_zeroes.iterator().next();
        digits.put(zero, '0');
        patterns.put(0, zero);
        // 6
        final Set<String> possible_sixes = sixes.stream()
            .filter(six -> !six.equals(nine) && !six.equals(zero))
            .collect(toSet());
        assert possible_sixes.size() == 1;
        final String six = possible_sixes.iterator().next();
        digits.put(six, '6');
        patterns.put(6, six);
        final Set<String> fives = all(i).filter(s -> s.length() == 5).collect(toSet());
        assert fives.size() == 3;
        // 3
        final Set<String> possible_threes = fives.stream()
            .filter(five -> SetUtils.intersection(asCharSet(five), one).toSet().equals(one))
            .collect(toSet());
        assert possible_threes.size() == 1;
        final String three = possible_threes.iterator().next();
        digits.put(three, '3');
        patterns.put(3, three);
        // 5
        final Set<String> possible_fives = fives.stream()
            .filter(five -> !five.equals(three))
            .filter(five -> SetUtils.intersection(asCharSet(five), asCharSet(nine)).toSet().equals(asCharSet(five)))
            .collect(toSet());
        assert possible_fives.size() == 1;
        final String five = possible_fives.iterator().next();
        digits.put(five, '5');
        patterns.put(5, five);
        final Set<String> possible_twos = fives.stream()
            .filter(f -> !f.equals(five) && !f.equals(three))
            .collect(toSet());
        assert possible_twos.size() == 1;
        final String two = possible_twos.iterator().next();
        digits.put(two, '2');
        patterns.put(2, two);
        
        log(digits);
        log(patterns);
        
        final Integer number = Integer.valueOf(this.outputs.get(i).stream()
            .map(o -> digits.get(o))
            .collect(toAString()));
        log(number);
        return number;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_08.create(TEST).solvePart1() == 26;
        assert AoC2021_08.createDebug(TEST).solvePart2() == 61229;

        final List<String> input = Aocd.getData(2021, 8);
        lap("Part 1", () -> AoC2021_08.create(input).solvePart1());
        lap("Part 2", () -> AoC2021_08.create(input).solvePart2());
    }

    private static final List<String> TEST = splitLines(
        "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe\r\n" +
        "edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc\r\n" +
        "fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg\r\n" +
        "fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb\r\n" +
        "aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea\r\n" +
        "fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb\r\n" +
        "dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe\r\n" +
        "bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef\r\n" +
        "egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb\r\n" +
        "gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce"
    );
}

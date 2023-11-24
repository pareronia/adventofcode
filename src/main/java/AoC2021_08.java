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

import com.github.pareronia.aoc.SetUtils;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

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
            .flatMap(List<String>::stream)
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
    
    private Set<String> find(final Stream<String> input, final int numberOfSegments, final int expected) {
        final Set<String> ans = input
                .filter(s -> s.length() == numberOfSegments)
                .collect(toSet());
        assert ans.size() == expected;
        return ans;
    }
    
    private String getSingle(final Set<String> set) {
        assert set.size() == 1;
        return set.iterator().next();
    }

    private boolean sharesAllSegmentsWith(final String container, final String contained) {
        final Set<Character> cd = asCharSet(contained);
        return SetUtils.intersection(cd, asCharSet(container)).equals(cd);
    }
    
    private Integer solve(final int i) {
        final Map<String, Character> digits = new HashMap<>();
        // 1
        final Set<String> twos = find(all(i), 2, 1);
        digits.put(getSingle(twos), '1');
        final String one = getSingle(twos);
        // 7
        final Set<String> threes = find(all(i), 3, 1);
        digits.put(getSingle(threes), '7');
        // 4
        final Set<String> fours = find(all(i), 4, 1);
        final String four = getSingle(fours);
        digits.put(four, '4');
        // 8
        final Set<String> sevens = find(all(i), 7, 1);
        digits.put(getSingle(sevens), '8');
        // 9
        final Set<String> sixes = find(all(i), 6, 3);
        final Set<String> possible_nines = sixes.stream()
            .filter(six -> sharesAllSegmentsWith(six, four))
            .collect(toSet());
        final String nine = getSingle(possible_nines);
        digits.put(nine, '9');
        // 0
        final Set<String> possible_zeroes = sixes.stream()
            .filter(six -> !six.equals(nine))
            .filter(six -> sharesAllSegmentsWith(six, one))
            .collect(toSet());
        final String zero = getSingle(possible_zeroes);
        digits.put(zero, '0');
        // 6
        final Set<String> possible_sixes = sixes.stream()
            .filter(six -> !six.equals(nine) && !six.equals(zero))
            .collect(toSet());
        digits.put(getSingle(possible_sixes), '6');
        // 3
        final Set<String> fives = find(all(i), 5, 3);
        final Set<String> possible_threes = fives.stream()
            .filter(five -> sharesAllSegmentsWith(five, one))
            .collect(toSet());
        final String three = getSingle(possible_threes);
        digits.put(three, '3');
        // 5
        final Set<String> possible_fives = fives.stream()
            .filter(five -> !five.equals(three))
            .filter(five -> sharesAllSegmentsWith(nine, five))
            .collect(toSet());
        final String five = getSingle(possible_fives);
        digits.put(five, '5');
        // 2
        final Set<String> possible_twos = fives.stream()
            .filter(f -> !f.equals(five) && !f.equals(three))
            .collect(toSet());
        digits.put(getSingle(possible_twos), '2');
        
        log(digits);
        final Integer number = Integer.valueOf(this.outputs.get(i).stream()
            .map(digits::get)
            .collect(toAString()));
        log(number);
        return number;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_08.create(TEST).solvePart1() == 26;
        assert AoC2021_08.create(TEST).solvePart2() == 61229;

        final Puzzle puzzle = Aocd.puzzle(2021, 8);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2021_08.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2021_08.create(inputData)::solvePart2)
        );
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

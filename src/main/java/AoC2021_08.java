import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aocd.Aocd;

public class AoC2021_08 extends AoCBase {
    
    private final List<List<String>> outputs;
    
    private AoC2021_08(final List<String> input, final boolean debug) {
        super(debug);
        this.outputs = input.stream()
                .map(s -> s.split(" \\| ")[1])
                .map(s -> Arrays.stream(s.split(" ")).collect(toList()))
                .collect(toList());
        log(outputs);
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

    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_08.createDebug(TEST).solvePart1() == 26;
        assert AoC2021_08.create(TEST).solvePart2() == 0;

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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Range;
import com.github.pareronia.aoc.SetUtils;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_03 extends AoCBase {
    
    private final List<String> input;
    
    private AoC2022_03(final List<String> input, final boolean debug) {
        super(debug);
        this.input = input;
    }
    
    public static final AoC2022_03 create(final List<String> input) {
        return new AoC2022_03(input, false);
    }

    public static final AoC2022_03 createDebug(final List<String> input) {
        return new AoC2022_03(input, true);
    }
    
    private int priority(final char ch) {
        if ('a' <= ch && ch <= 'z') {
            return ch - 'a' + 1;
        } else {
            return ch - 'A' + 27;
        }
    }
    
    private Set<Character> toSet(final String s) {
       return Utils.asCharacterStream(s).collect(Collectors.toSet());
    }
    
    @SafeVarargs
    private Set<Character> intersection(final Set<Character> set1, final Set<Character>... sets) {
        return Stream.of(sets).reduce(set1, SetUtils::intersection);
    }
    
    @Override
    public Integer solvePart1() {
        return Range.range(this.input.size()).intStream()
                .map(i -> {
                    final String line = input.get(i);
                    final int cuttoff = line.length() / 2;
                    final Set<Character> s1 = toSet(line.substring(0, cuttoff));
                    final Set<Character> s2 = toSet(line.substring(cuttoff));
                    return priority(intersection(s1, s2).iterator().next());
                })
                .sum();
    }

    @Override
    public Integer solvePart2() {
        return Range.range(0, this.input.size(), 3).intStream()
                .map(i -> {
                    final Set<Character> s1 = toSet(this.input.get(i));
                    final Set<Character> s2 = toSet(this.input.get(i + 1));
                    final Set<Character> s3 = toSet(this.input.get(i + 2));
                    return priority(intersection(s1, s2, s3).iterator().next());
                })
                .sum();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_03.createDebug(TEST).solvePart1() == 157;
        assert AoC2022_03.createDebug(TEST).solvePart2() == 70;

        final Puzzle puzzle = Aocd.puzzle(2022, 3);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_03.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_03.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "vJrwpWtwJgWrhcsFMMfFFhFp\r\n" +
        "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL\r\n" +
        "PmmdzqPrVvPwwTWBwg\r\n" +
        "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn\r\n" +
        "ttgJtRGJQctTZtZT\r\n" +
        "CrZsJsPPZsGzwwsLwLmpwMDw"
    );
}

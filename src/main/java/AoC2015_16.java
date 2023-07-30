import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.pareronia.aoc.AssertUtils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;

public class AoC2015_16 extends AoCBase {
    
    private static final String REGEXP = "Sue ([0-9]+): ([a-z]+): ([0-9]+), ([a-z]+): ([0-9]+), ([a-z]+): ([0-9]+)";
    
    private final List<AuntSue> auntSues;
    
    private AoC2015_16(final List<String> inputs, final boolean debug) {
        super(debug);
        final Pattern pattern = Pattern.compile(REGEXP);
        this.auntSues = inputs.stream()
                .map(s -> {
                    final Matcher m = pattern.matcher(s);
                    AssertUtils.assertTrue(m.matches(), () -> "No match found");
                    final int nbr = Integer.valueOf(m.group(1));
                    final Map<String, Integer> things = Map.of(
                            m.group(2), Integer.valueOf(m.group(3)),
                            m.group(4), Integer.valueOf(m.group(5)),
                            m.group(6), Integer.valueOf(m.group(7)));
                    return new AuntSue(nbr, things);
                })
                .collect(toList());
    }

    public static final AoC2015_16 create(final List<String> input) {
        return new AoC2015_16(input, false);
    }

    public static final AoC2015_16 createDebug(final List<String> input) {
        return new AoC2015_16(input, true);
    }
    
    private Integer findAuntSueWithBestScore(final Map<String, Rule> rules) {
        final Map<Integer, Integer> scores = new HashMap<>();
        for (final AuntSue sue : this.auntSues) {
            for (final String thing : rules.keySet()) {
                if (rules.get(thing).matches(sue.getThing(thing))) {
                    scores.merge(sue.nbr, 1, Integer::sum);
                }
            }
        }
        return scores.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .findFirst()
                .map(Entry::getKey)
                .orElseThrow();
    }
    
    @Override
    public Integer solvePart1() {
        final Map<String, Rule> rules = Map.of(
                "children", Rule.eq(3),
                "cats", Rule.eq(7),
                "samoyeds", Rule.eq(2),
                "pomeranians", Rule.eq(3),
                "akitas", Rule.eq(0),
                "vizslas", Rule.eq(0),
                "goldfish", Rule.eq(5),
                "trees", Rule.eq(3),
                "cars", Rule.eq(2),
                "perfumes", Rule.eq(1)
        );
        return findAuntSueWithBestScore(rules);
    }
    
    @Override
    public Integer solvePart2() {
        final Map<String, Rule> rules = Map.of(
                "children", Rule.eq(3),
                "cats", Rule.gt(7),
                "samoyeds", Rule.eq(2),
                "pomeranians", Rule.lt(3),
                "akitas", Rule.eq(0),
                "vizslas", Rule.eq(0),
                "goldfish", Rule.lt(5),
                "trees", Rule.gt(3),
                "cars", Rule.eq(2),
                "perfumes", Rule.eq(1)
        );
        return findAuntSueWithBestScore(rules);
    }

    public static void main(final String[] args) throws Exception {
        final Puzzle puzzle = Aocd.puzzle(2015, 16);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2015_16.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2015_16.create(puzzle.getInputData()).solvePart2())
        );
    }
    
    @RequiredArgsConstructor
    private static final class AuntSue {
        private final int nbr;
        private final Map<String, Integer> things;
        
        public Integer getThing(final String thing) {
            return this.things.get(thing);
        }
    }
    
    @RequiredArgsConstructor
    private static final class Rule {
        private enum Op { EQ, LT, GT }
        
        private final Op operation;
        private final int operand;
        
        public static Rule eq(final int operand) {
            return new Rule(Op.EQ, operand);
        }

        public static Rule lt(final int operand) {
            return new Rule(Op.LT, operand);
        }
        
        public static Rule gt(final int operand) {
            return new Rule(Op.GT, operand);
        }
        
        public boolean matches(final Integer value) {
            if (this.operation == Op.EQ) {
                return value != null && value == this.operand;
            } else if (this.operation == Op.LT) {
                return value != null && value < this.operand;
            } else {
                return value != null && value > this.operand;
            }
        }
    }
}

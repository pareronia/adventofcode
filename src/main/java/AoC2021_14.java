import static java.util.stream.Collectors.summarizingLong;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Map.Entry;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2021_14 extends AoCBase {
    
    private final char[] template;
    private final Map<CharacterPair, Character> rules;

    private AoC2021_14(final List<String> input, final boolean debug) {
        super(debug);
        this.template = input.get(0).toCharArray();
        this.rules = input.subList(2, input.size()).stream()
            .map(s -> s.split(" -> "))
            .collect(toMap(sp -> CharacterPair.from(sp[0]), sp -> sp[1].charAt(0)));
    }
    
    public static final AoC2021_14 create(final List<String> input) {
        return new AoC2021_14(input, false);
    }

    public static final AoC2021_14 createDebug(final List<String> input) {
        return new AoC2021_14(input, true);
    }

    private Long solve(final int cycles) {
        Map<CharacterPair, Long> pairCounters = new HashMap<>();
        final Map<Character, Long> elemCounters = new HashMap<>();
        for (int i = 0; i < this.template.length; i++) {
            elemCounters.merge(this.template[i], 1L, Long::sum);
            if (i == 0) {
                continue;
            }
            final CharacterPair pair
                    = new CharacterPair(this.template[i - 1], this.template[i]);
            pairCounters.merge(pair, 1L, Long::sum);
        }
        
        for (int i = 0; i < cycles; i++) {
            final Map<CharacterPair, Long> pairCounters2 = new HashMap<>();
            for (final Entry<CharacterPair, Long> e : pairCounters.entrySet()) {
                final CharacterPair pair = e.getKey();
                final Character elem = rules.get(pair);
                final Long count = e.getValue();
                elemCounters.merge(elem, count, Long::sum);
                pairCounters2.merge(new CharacterPair(pair.left(), elem), count, Long::sum);
                pairCounters2.merge(new CharacterPair(elem, pair.right()), count, Long::sum);
            }
            pairCounters = pairCounters2;
        }
        
        final LongSummaryStatistics summary = elemCounters.values().stream()
                .collect(summarizingLong(Long::valueOf));
        return summary.getMax() - summary.getMin();
    }
    
    @Override
    public Long solvePart1() {
        return solve(10);
    }
    
    @Override
    public Long solvePart2() {
        return solve(40);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_14.create(TEST).solvePart1() == 1588;
        assert AoC2021_14.create(TEST).solvePart2() == 2_188_189_693_529L;

        final Puzzle puzzle = Aocd.puzzle(2021, 14);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_14.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_14.create(puzzle.getInputData()).solvePart2())
        );
    }

    private static final List<String> TEST = splitLines(
        "NNCB\r\n" +
        "\r\n" +
        "CH -> B\r\n" +
        "HH -> N\r\n" +
        "CB -> H\r\n" +
        "NH -> C\r\n" +
        "HB -> C\r\n" +
        "HC -> B\r\n" +
        "HN -> C\r\n" +
        "NN -> C\r\n" +
        "BH -> H\r\n" +
        "NC -> B\r\n" +
        "NB -> B\r\n" +
        "BN -> B\r\n" +
        "BB -> N\r\n" +
        "BC -> B\r\n" +
        "CC -> N\r\n" +
        "CN -> C"
    );
    
    record CharacterPair(Character left, Character right) {
        
        public static CharacterPair from(final String string) {
            return new CharacterPair(string.charAt(0), string.charAt(1));
        }
    }
}
import static com.github.pareronia.aoc.IterTools.enumerateFrom;
import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.Comparator.comparing;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import com.github.pareronia.aoc.Counter;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_07
        extends SolutionBase<List<AoC2023_07.Hand>, Integer, Integer> {
    
    private AoC2023_07(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_07 create() {
        return new AoC2023_07(false);
    }
    
    public static AoC2023_07 createDebug() {
        return new AoC2023_07(true);
    }
    
    @Override
    protected List<Hand> parseInput(final List<String> inputs) {
        return inputs.stream().map(Hand::fromInput).toList();
    }
    
    private int solve(final List<Hand> hands, final Comparator<Hand> order) {
        return enumerateFrom(1, hands.stream().sorted(order))
            .mapToInt(e -> e.index() * e.value().bid())
            .sum();
    }

    @Override
    public Integer solvePart1(final List<Hand> hands) {
        return solve(hands, Hand.comparator());
    }
    
    @Override
    public Integer solvePart2(final List<Hand> hands) {
        return solve(hands, Hand.comparatorWithJokers());
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "6440"),
        @Sample(method = "part2", input = TEST, expected = "5905"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_07.create().run();
    }
    
    record Hand(
        int bid,
        int value,
        String strength,
        int valueWithJokers,
        String strengthWithJokers
    ) {
        private static final Character JOKER = 'J';
        private static final String BEST = "AAAAA";
        
        public static Hand fromInput(final String line) {
            final Function<String, Integer> getValue = cards -> {
                final List<Counter.Entry<Character>> mc
                    = new Counter<>(Utils.asCharacterStream(cards)).mostCommon();
                return (int) (2 * mc.get(0).count() + (mc.size() > 1 ? mc.get(1).count() : 0));
            };
            final Function<String, String> withJokers = cards -> {
                final Counter<Character> c = new Counter<>(
                    Utils.asCharacterStream(cards).filter(ch -> ch != JOKER));
                if (c.isEmpty()) {
                    return BEST;
                }
                final Character best = c.mostCommon().get(0).value();
                return Utils.asCharacterStream(cards)
                    .map(ch -> ch == JOKER ? best : ch)
                    .collect(toAString());
            };
            
            final StringSplit split = StringOps.splitOnce(line, " ");
            final int bid = Integer.parseInt(split.right());
            final int value = getValue.apply(split.left());
            final String strength = StringOps.translate(split.left(), "TJQKA", "BCDEF");
            final int valueWithJokers = getValue.apply(withJokers.apply(split.left()));
            final String strengthWithJokers = StringOps.translate(split.left(), "TJQKA", "B0DEF");
            return new Hand(bid, value, strength, valueWithJokers, strengthWithJokers);
        }
        
        public static Comparator<Hand> comparator() {
            return comparing(Hand::value).thenComparing(comparing(Hand::strength));
        }

        public static Comparator<Hand> comparatorWithJokers() {
            return comparing(Hand::valueWithJokers)
                        .thenComparing(comparing(Hand::strengthWithJokers));
        }
    }

    private static final String TEST = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
            """;
}

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.IterTools;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2015_13 extends AoCBase {
    
    private final Map<String, Integer> seatings;
    private final Set<String> diners;
    
    private AoC2015_13(final List<String> inputs, final boolean debug) {
        super(debug);
        this.seatings = new HashMap<>();
        this.diners = new HashSet<>();
        for (final String input : inputs) {
            final String[] s = input.substring(0, input.length() - 1).split(" ");
            final String d1 = s[0];
            final String d2 = s[10];
            final Integer value = Integer.valueOf(s[3]);
            this.seatings.put(d1 + "-" + d2, "gain".equals(s[2]) ? value : -value);
            this.diners.addAll(Set.of(d1, d2));
        }
    }

    public static final AoC2015_13 create(final List<String> input) {
        return new AoC2015_13(input, false);
    }

    public static final AoC2015_13 createDebug(final List<String> input) {
        return new AoC2015_13(input, true);
    }
    
    private int solve() {
        return IterTools.permutations(this.diners)
            .mapToInt(ds ->
                IntStream.range(0, ds.size())
                    .map(i -> {
                        final String d1 = ds.get(i);
                        final String d2 = ds.get((i + 1) % ds.size());
                        return this.seatings.get(d1 + "-" + d2) + this.seatings.get(d2 + "-" + d1);
                    })
                    .sum())
            .max().orElseThrow();
    }

    @Override
    public Integer solvePart1() {
        return solve();
    }
    
    @Override
    public Integer solvePart2() {
        for (final String diner : this.diners) {
            this.seatings.put("me-" + diner, 0);
            this.seatings.put(diner + "-me", 0);
        }
        this.diners.add("me");
        return solve();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_13.createDebug(TEST).solvePart1() == 330;

        final Puzzle puzzle = Aocd.puzzle(2015, 13);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2015_13.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2015_13.create(inputData)::solvePart2)
        );
    }
    
    private static final List<String> TEST = splitLines(
            "Alice would gain 54 happiness units by sitting next to Bob.\r\n"
            + "Alice would lose 79 happiness units by sitting next to Carol.\r\n"
            + "Alice would lose 2 happiness units by sitting next to David.\r\n"
            + "Bob would gain 83 happiness units by sitting next to Alice.\r\n"
            + "Bob would lose 7 happiness units by sitting next to Carol.\r\n"
            + "Bob would lose 63 happiness units by sitting next to David.\r\n"
            + "Carol would lose 62 happiness units by sitting next to Alice.\r\n"
            + "Carol would gain 60 happiness units by sitting next to Bob.\r\n"
            + "Carol would gain 55 happiness units by sitting next to David.\r\n"
            + "David would gain 46 happiness units by sitting next to Alice.\r\n"
            + "David would lose 7 happiness units by sitting next to Bob.\r\n"
            + "David would gain 41 happiness units by sitting next to Carol."
    );
}

import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.IntegerSequence.Range.rangeClosed;
import static com.github.pareronia.aoc.Utils.last;
import static com.github.pareronia.aoc.Utils.toAString;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2022_05
        extends SolutionBase<AoC2022_05.RearrangementProcedure, String, String> {

    private AoC2022_05(final boolean debug) {
        super(debug);
    }

    public static AoC2022_05 create() {
        return new AoC2022_05(false);
    }

    public static AoC2022_05 createDebug() {
        return new AoC2022_05(true);
    }

    private String simulateProcedureFor(
            final RearrangementProcedure procedure, final CrateMover crateMover) {

        final List<? extends Deque<Character>> stacks =
                procedure.stacks().stream().map(ArrayDeque::new).toList();
        final Deque<Character> tmp = new ArrayDeque<>();
        for (final Move move : procedure.moves()) {
            final int from = move.from - 1;
            final int to = move.to - 1;
            tmp.clear();
            range(move.amount)
                    .forEach(
                            i -> {
                                final Character crate = stacks.get(from).removeLast();
                                if (crateMover == CrateMover.CM_9000) {
                                    tmp.addLast(crate);
                                } else {
                                    tmp.addFirst(crate);
                                }
                            });
            tmp.forEach(stacks.get(to)::addLast);
        }
        return stacks.stream().map(Deque::peekLast).collect(toAString());
    }

    @Override
    protected RearrangementProcedure parseInput(final List<String> inputs) {
        return RearrangementProcedure.fromInput(inputs);
    }

    @Override
    public String solvePart1(final RearrangementProcedure procedure) {
        return simulateProcedureFor(procedure, CrateMover.CM_9000);
    }

    @Override
    public String solvePart2(final RearrangementProcedure procedure) {
        return simulateProcedureFor(procedure, CrateMover.CM_9001);
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "CMZ"),
        @Sample(method = "part2", input = TEST, expected = "MCD"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
                [D]    \s
            [N] [C]    \s
            [Z] [M] [P]\s
             1   2   3 \s

            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2
            """;

    private record Move(int amount, int from, int to) {}

    record RearrangementProcedure(List<List<Character>> stacks, List<Move> moves) {

        public static RearrangementProcedure fromInput(final List<String> input) {
            final List<List<Character>> stacks = new ArrayList<>();
            final List<Move> moves = new ArrayList<>();
            final List<List<String>> blocks = StringOps.toBlocks(input);
            final String nums = blocks.get(0).getLast().replaceAll("\\s", "");
            final int size = Integer.parseInt(String.valueOf(last(nums)));
            range(size).forEach(i -> stacks.add(new ArrayList<>()));
            rangeClosed(blocks.get(0).size() - 2, 0, -1)
                    .forEach(
                            i -> {
                                final String line = blocks.get(0).get(i);
                                range(line.length())
                                        .intStream()
                                        .filter(j -> j % 4 == 1)
                                        .filter(j -> line.charAt(j) != ' ')
                                        .forEach(j -> stacks.get(j / 4).add(line.charAt(j)));
                            });
            blocks.get(1).stream()
                    .map(Utils::naturalNumbers)
                    .forEach(n -> moves.add(new Move(n[0], n[1], n[2])));
            return new RearrangementProcedure(
                    Collections.unmodifiableList(stacks), Collections.unmodifiableList(moves));
        }
    }

    private enum CrateMover {
        CM_9000,
        CM_9001
    }
}

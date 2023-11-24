import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.IntegerSequence.Range.rangeClosed;
import static com.github.pareronia.aoc.Utils.last;
import static com.github.pareronia.aoc.Utils.toAString;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AoC2022_05 extends AoCBase {
    
    private final RearrangementProcedure procedure;
    
    private AoC2022_05(final List<String> input, final boolean debug) {
        super(debug);
        final List<Deque<Character>> stacks = new ArrayList<>();
        final List<Move> moves = new ArrayList<>();
        final List<List<String>> blocks = toBlocks(input);
        final String nums = last(blocks.get(0)).replaceAll("\\s", "");
        final int size = Integer.parseInt(String.valueOf(last(nums)));
        range(size).forEach(i -> stacks.add(new ArrayDeque<>()));
        rangeClosed(blocks.get(0).size() - 2, 0, -1).forEach(i -> {
            final String line = blocks.get(0).get(i);
            range(line.length()).intStream()
                .filter(j -> j % 4 == 1)
                .filter(j -> line.charAt(j) != ' ')
                .forEach(j -> stacks.get(j / 4).addLast(line.charAt(j)));
        });
        blocks.get(1).stream()
            .map(Utils::naturalNumbers)
            .forEach(n -> moves.add(new Move(n[0], n[1], n[2])));
        this.procedure = new RearrangementProcedure(stacks, moves);
    }
    
    public static final AoC2022_05 create(final List<String> input) {
        return new AoC2022_05(input, false);
    }

    public static final AoC2022_05 createDebug(final List<String> input) {
        return new AoC2022_05(input, true);
    }
    
    private String simulateProcedureFor(final CrateMover crateMover) {
        for (final Move move : this.procedure.moves) {
            final int from = move.getTwo() - 1;
            final int to = move.getThree() - 1;
            final Deque<Character> tmp = new ArrayDeque<>();
            range(move.getOne()).forEach(i -> {
                final Character crate = this.procedure.stacks.get(from).removeLast();
                if (crateMover == CrateMover.CM_9000) {
                    tmp.addLast(crate);
                } else {
                    tmp.addFirst(crate);
                }
            });
            tmp.forEach(this.procedure.stacks.get(to)::addLast);
        }
        final String ans = this.procedure.stacks.stream()
            .map(Deque::peekLast)
            .collect(toAString());
        return ans;
    }
    
    @Override
    public String solvePart1() {
        return simulateProcedureFor(CrateMover.CM_9000);
    }

    @Override
    public String solvePart2() {
        return simulateProcedureFor(CrateMover.CM_9001);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_05.createDebug(TEST).solvePart1().equals("CMZ");
        assert AoC2022_05.createDebug(TEST).solvePart2().equals("MCD");

        final Puzzle puzzle = Aocd.puzzle(2022, 5);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_05.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_05.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "    [D]    \r\n" +
        "[N] [C]    \r\n" +
        "[Z] [M] [P]\r\n" +
        " 1   2   3 \r\n" +
        "\r\n" +
        "move 1 from 2 to 1\r\n" +
        "move 3 from 1 to 3\r\n" +
        "move 2 from 2 to 1\r\n" +
        "move 1 from 1 to 2"
    );
    
    @RequiredArgsConstructor
    @Getter
    private static final class Move {
        private final int one;
        private final int two;
        private final int three;
    }
    
    @RequiredArgsConstructor
    private static final class RearrangementProcedure {
        private final List<Deque<Character>> stacks;
        private final List<Move> moves;
    }
    
    private enum CrateMover { CM_9000, CM_9001 }
}

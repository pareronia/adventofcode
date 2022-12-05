import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import org.eclipse.collections.api.tuple.Triplet;
import org.eclipse.collections.impl.tuple.Tuples;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_05 extends AoCBase {
    
    private final List<Deque<Character>> stacks;
    private final List<Triplet<Integer>> moves;
    
    private AoC2022_05(final List<String> input, final boolean debug) {
        super(debug);
        this.stacks = new ArrayList<>();
        this.moves = new ArrayList<>();
        final List<List<String>> blocks = toBlocks(input);
        final String nums = blocks.get(0).get(blocks.get(0).size() - 1).replaceAll("(\\[\\]])", "").strip();
        final int height = blocks.get(0).size() - 1;
        final int size = Integer.parseInt(String.valueOf(nums.charAt(nums.length() - 1)));
        for (int i = 0; i < size; i++) {
            this.stacks.add(new ArrayDeque<>());
        }
        for (int i = height - 1; i >= 0; i--) {
            final char[] line = blocks.get(0).get(i).toCharArray();
            for (int j = 1; j <= size * 4; j += 4) {
                if (line[j] != ' ') {
                    stacks.get(j / 4).addLast(line[j]);
                }
            }
        }
        this.stacks.forEach(this::log);
        
        for (final String s : blocks.get(1)) {
           final int[] n = numbers(s);
           this.moves.add(Tuples.triplet(n[0], n[1], n[2]));
        }
        this.moves.forEach(this::log);
    }
    
    private static final Pattern REGEX = Pattern.compile("[0-9]+");
    
    private final int[] numbers(final String string) {
        return REGEX.matcher(string).results()
                .map(MatchResult::group)
                .mapToInt(Integer::parseInt)
                .toArray();
    }
    
    public static final AoC2022_05 create(final List<String> input) {
        return new AoC2022_05(input, false);
    }

    public static final AoC2022_05 createDebug(final List<String> input) {
        return new AoC2022_05(input, true);
    }
    
    @Override
    public String solvePart1() {
        for (final Triplet<Integer> move : this.moves) {
            log("move");
            final int from = move.getTwo() - 1;
            final int to = move.getThree() - 1;
            for (int i = 0; i < move.getOne(); i++) {
                this.stacks.get(to).addLast(this.stacks.get(from).removeLast());
            }
            this.stacks.forEach(this::log);
        }
        final String ans = this.stacks.stream()
            .map(Deque::peekLast)
            .collect(Utils.toAString());
        log(ans);
        return ans;
    }

    @Override
    public String solvePart2() {
        for (final Triplet<Integer> move : this.moves) {
            log("move");
            final int from = move.getTwo() - 1;
            final int to = move.getThree() - 1;
            final Deque<Character> tmp = new ArrayDeque<>();
            for (int i = 0; i < move.getOne(); i++) {
                tmp.addFirst(this.stacks.get(from).removeLast());
            }
            tmp.forEach(this.stacks.get(to)::addLast);
            this.stacks.forEach(this::log);
        }
        final String ans = this.stacks.stream()
            .map(Deque::peekLast)
            .collect(Utils.toAString());
        log(ans);
        return ans;
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
          "    [D]    \r\n"
        + "[N] [C]    \r\n"
        + "[Z] [M] [P]\r\n"
        + " 1   2   3 \r\n"
        + "\r\n"
        + "move 1 from 2 to 1\r\n"
        + "move 3 from 1 to 3\r\n"
        + "move 2 from 2 to 1\r\n"
        + "move 1 from 1 to 2"
    );
}

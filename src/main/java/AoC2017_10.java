import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.pareronia.aoc.knothash.KnotHash;
import com.github.pareronia.aoc.knothash.KnotHash.State;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2017_10 extends AoCBase {

    private final transient String input;

    private AoC2017_10(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = inputs.get(0);
    }

    public static AoC2017_10 create(final List<String> input) {
        return new AoC2017_10(input, false);
    }

    public static AoC2017_10 createDebug(final List<String> input) {
        return new AoC2017_10(input, true);
    }
    
    private Integer solve1(final List<Integer> elements) {
        final List<Integer> lengths = Arrays.stream(this.input.split(","))
                .map(Integer::valueOf)
                .collect(toList());
        final State state = new State(elements, lengths, 0, 0);
        final State ans = KnotHash.round(state);
        return ans.elements().get(0) * ans.elements().get(1);
    }
    
    @Override
    public Integer solvePart1() {
        return solve1(new ArrayList<>(KnotHash.SEED));
    }
    
    @Override
    public String solvePart2() {
        return KnotHash.hexString(this.input);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_10.createDebug(splitLines("3,4,1,5")).solve1(asList(0, 1, 2, 3, 4)) == 12;
        assert AoC2017_10.createDebug(splitLines("AoC 2017")).solvePart2().equals("33efeb34ea91902bb2f59c9920caa6cd");
        assert AoC2017_10.createDebug(splitLines("1,2,3")).solvePart2().equals("3efbe78a8d82f29979031a4aa0b16a9d");
        assert AoC2017_10.createDebug(splitLines("1,2,4")).solvePart2().equals("63960835bcdc130f0b66d7ff4f6a5a8e");

        final Puzzle puzzle = Aocd.puzzle(2017, 10);
        final List<String> input = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", create(input)::solvePart1),
            () -> lap("Part 2", create(input)::solvePart2)
        );
    }
}

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2017_06 extends AoCBase {

    private final transient List<Integer> input;

    private AoC2017_06(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = Arrays.stream(inputs.get(0).split("\\s+"))
                        .map(Integer::valueOf).collect(toList());
    }

    public static AoC2017_06 create(final List<String> input) {
        return new AoC2017_06(input, false);
    }

    public static AoC2017_06 createDebug(final List<String> input) {
        return new AoC2017_06(input, true);
    }
    
    private Result getRedistributionCycles() {
        final Map<List<Integer>, Integer> seen = new HashMap<>();
        List<Integer> banks = new ArrayList<>(this.input);
        int cnt = 0;
        while (!seen.containsKey(banks)) {
            seen.put(banks, cnt);
            final int max = banks.stream()
                    .mapToInt(Integer::valueOf)
                    .max().getAsInt();
            final int idx = banks.indexOf(max);
            final Integer blocks = banks.get(idx);
            banks = new ArrayList<>(banks);
            banks.set(idx, 0);
            for (int i = 1; i <= blocks; i++) {
                final int j = (idx + i) % banks.size();
                final int newBlocks = banks.get(j) + 1;
                banks.set(j, newBlocks);
            }
            cnt++;
        }
        return new Result(seen, banks);
    }
    
    @Override
    public Integer solvePart1() {
        return getRedistributionCycles().map.size();
    }
    
    @Override
    public Integer solvePart2() {
        final Result result = getRedistributionCycles();
        return result.map.size() - result.map.get(result.last);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_06.createDebug(splitLines("0 2 7 0")).solvePart1() == 5;
        assert AoC2017_06.createDebug(splitLines("0 2 7 0")).solvePart2() == 4;

        final Puzzle puzzle = Aocd.puzzle(2017, 6);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2017_06.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2017_06.create(puzzle.getInputData()).solvePart2())
        );
    }
    
    record Result(Map<List<Integer>, Integer> map, List<Integer> last) { }
}

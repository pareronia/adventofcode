import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aocd.Aocd;

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
    
    private Integer getRedistributionCycles() {
        String banks = this.input.stream()
                .map(String::valueOf)
                .collect(joining());
        final Set<String> seen = new HashSet<>();
        int cnt = 0;
        while (!seen.contains(banks)) {
            seen.add(banks);
            final Integer max = this.input.stream()
                    .max(naturalOrder()).orElseThrow();
            final int idx = this.input.indexOf(max);
            final Integer blocks = this.input.get(idx);
            this.input.set(idx, 0);
            for (int i = 1; i <= blocks; i++) {
                final int j = (idx + i) % this.input.size();
                final int newBlocks = this.input.get(j) + 1;
                this.input.set(j, newBlocks);
            }
            banks = this.input.stream()
                    .map(String::valueOf)
                    .collect(joining());
            cnt++;
        }
        return cnt;
    }
    
    @Override
    public Integer solvePart1() {
        return getRedistributionCycles();
    }
    
    @Override
    public Integer solvePart2() {
        getRedistributionCycles();
        return getRedistributionCycles();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_06.createDebug(splitLines("0 2 7 0")).solvePart1() == 5;
        assert AoC2017_06.createDebug(splitLines("0 2 7 0")).solvePart2() == 4;

        final List<String> input = Aocd.getData(2017, 6);
        lap("Part 1", () -> AoC2017_06.create(input).solvePart1());
        lap("Part 2", () -> AoC2017_06.create(input).solvePart2());
    }
}

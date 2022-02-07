import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.math3.util.CombinatoricsUtils.combinationsIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2015_17 extends AoCBase {
    
    private final List<Integer> containers;
    
    private AoC2015_17(final List<String> inputs, final boolean debug) {
        super(debug);
        this.containers = inputs.stream().map(Integer::valueOf).collect(toList());
    }

    public static final AoC2015_17 create(final List<String> input) {
        return new AoC2015_17(input, false);
    }

    public static final AoC2015_17 createDebug(final List<String> input) {
        return new AoC2015_17(input, true);
    }
    
    private List<List<Integer>> getCoCos(final int eggnogVolume) {
        Collections.sort(this.containers, reverseOrder());
        final List<Integer> minimalContainers = new ArrayList<>();
        int j = 0;
        while (minimalContainers.stream().mapToInt(Integer::intValue).sum() + this.containers.get(j) > eggnogVolume) {
            minimalContainers.add(this.containers.get(j));
            j++;
        }
        final List<List<Integer>> cocos = new ArrayList<>();
        for (int i = minimalContainers.size(); i < this.containers.size(); i++) {
            combinationsIterator(this.containers.size(), i).forEachRemaining(c -> {
                if (Arrays.stream(c).map(this.containers::get).sum() == eggnogVolume) {
                    cocos.add(Arrays.stream(c).mapToObj(this.containers::get).collect(toList()));
                }
            });
        }
        return cocos;
    }
    
    private int solve1(final int eggnogVolume) {
        return getCoCos(eggnogVolume).size();
    }
    
    @Override
    public Integer solvePart1() {
        return solve1(150);
    }
    
    private int solve2(final int eggnogVolume) {
        final List<List<Integer>> cocos = getCoCos(eggnogVolume);
        final int min = cocos.stream().mapToInt(List::size).min().orElseThrow();
        return (int) cocos.stream().filter(c -> c.size() == min).count();
    }
    
    @Override
    public Integer solvePart2() {
        return solve2(150);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_17.createDebug(TEST).solve1(25) == 4;
        assert AoC2015_17.createDebug(TEST).solve2(25) == 3;
        
        final Puzzle puzzle = Aocd.puzzle(2015, 17);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2015_17.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2015_17.create(puzzle.getInputData()).solvePart2())
        );
    }
    
    private static final List<String> TEST = splitLines(
            "20\r\n"
            + "15\r\n"
            + "10\r\n"
            + "5\r\n"
            + "5"
    );
}

import static com.github.pareronia.aoc.StringOps.splitLines;
import static com.github.pareronia.aoc.itertools.IterTools.combinations;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2015_17 extends SolutionBase<List<Integer>, Integer, Integer> {
    
    private AoC2015_17(final boolean debug) {
        super(debug);
    }

    public static final AoC2015_17 create() {
        return new AoC2015_17(false);
    }

    public static final AoC2015_17 createDebug() {
        return new AoC2015_17(true);
    }
    
    @Override
    protected List<Integer> parseInput(final List<String> inputs) {
       return inputs.stream().map(Integer::valueOf).collect(toList());
    }

    private List<List<Integer>> getCoCos(final List<Integer> containers, final int eggnogVolume) {
        Collections.sort(containers, reverseOrder());
        final List<Integer> minimalContainers = new ArrayList<>();
        int j = 0;
        while (minimalContainers.stream().mapToInt(Integer::intValue).sum() + containers.get(j) > eggnogVolume) {
            minimalContainers.add(containers.get(j));
            j++;
        }
        final List<List<Integer>> cocos = new ArrayList<>();
        for (int i = minimalContainers.size(); i < containers.size(); i++) {
            combinations(containers.size(), i).stream().forEach(c -> {
                if (Arrays.stream(c).map(containers::get).sum() == eggnogVolume) {
                    cocos.add(Arrays.stream(c).mapToObj(containers::get).collect(toList()));
                }
            });
        }
        return cocos;
    }
    
    private int solve1(final List<Integer> containers, final int eggnogVolume) {
        return getCoCos(containers, eggnogVolume).size();
    }
    
    @Override
    public Integer solvePart1(final List<Integer> containers) {
        return solve1(containers, 150);
    }
    
    private int solve2(final List<Integer> containers, final int eggnogVolume) {
        final List<List<Integer>> cocos = getCoCos(containers, eggnogVolume);
        final int min = cocos.stream().mapToInt(List::size).min().orElseThrow();
        return (int) cocos.stream().filter(c -> c.size() == min).count();
    }
    
    @Override
    public Integer solvePart2(final List<Integer> containers) {
        return solve2(containers, 150);
    }

    public static void main(final String[] args) throws Exception {
        final AoC2015_17 test = AoC2015_17.createDebug();
        final List<Integer> input = test.parseInput(TEST);
        assert AoC2015_17.createDebug().solve1(input, 25) == 4;
        assert AoC2015_17.createDebug().solve2(input, 25) == 3;
        
        AoC2015_17.create().run();
    }
    
    private static final List<String> TEST = splitLines(
            """
                20\r
                15\r
                10\r
                5\r
                5"""
    );
}

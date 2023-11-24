import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2017_12 extends AoCBase {

    private final transient Map<String, List<String>> input;

    private AoC2017_12(final List<String> inputs, final boolean debug) {
        super(debug);
        this.input = inputs.stream()
                .map(s -> {
                    final String[] sp1 = s.split(" <-> ");
                    final List<String> list = new ArrayList<>(List.of(sp1[0]));
                    list.addAll(asList(sp1[1].split(", ")));
                    return list;
                })
                .collect(toMap(l -> l.get(0), l -> l.subList(1, l.size())));
        log(this.input);
    }

    public static AoC2017_12 create(final List<String> input) {
        return new AoC2017_12(input, false);
    }

    public static AoC2017_12 createDebug(final List<String> input) {
        return new AoC2017_12(input, true);
    }
    
    private void dfs(final Set<String> vs, final String v) {
        vs.add(v);
        this.input.get(v).forEach(w -> {
            if (!vs.contains(w)) {
                dfs(vs, w);
            }
        });
    }
    
    @Override
    public Integer solvePart1() {
        final Set<String> vs = new HashSet<>();
        dfs(vs, "0");
        return vs.size();
    }
    
    @Override
    public Integer solvePart2() {
        final Map<String, Set<String>> trees = new HashMap<>();
        for (final String k : this.input.keySet()) {
            if (trees.containsKey(k)) {
                continue;
            }
            final Set<String> vs = new HashSet<>();
            dfs(vs, k);
            vs.forEach(v -> trees.put(v, vs));
        }
        return (int) trees.values().stream().distinct().count();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_12.createDebug(TEST).solvePart1() == 6;
        assert AoC2017_12.createDebug(TEST).solvePart2() == 2;

        final Puzzle puzzle = Aocd.puzzle(2017, 12);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2017_12.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2017_12.create(inputData)::solvePart2)
        );
    }
    
    private static final List<String> TEST = splitLines(
            "0 <-> 2\n" +
            "1 <-> 1\n" +
            "2 <-> 0, 3, 4\n" +
            "3 <-> 2, 4\n" +
            "4 <-> 2, 3, 6\n" +
            "5 <-> 6\n" +
            "6 <-> 4, 5"
    );
}

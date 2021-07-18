import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

import com.github.pareronia.aocd.Aocd;

public final class AoC2017_12 extends AoCBase {

    private final transient Map<String, List<String>> input;

    private AoC2017_12(final List<String> inputs, final boolean debug) {
        super(debug);
        this.input = inputs.stream()
                .map(s -> {
                    final String[] sp = s.split(" <-> ");
                    return Tuples.pair(sp[0], asList(sp[1].split(", ")));
                })
                .collect(toMap(Pair::getOne, Pair::getTwo));
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

        final List<String> input = Aocd.getData(2017, 12);
        lap("Part 1", () -> AoC2017_12.create(input).solvePart1());
        lap("Part 2", () -> AoC2017_12.create(input).solvePart2());
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

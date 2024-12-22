import static com.github.pareronia.aoc.IterTools.combinationsIterator;
import static com.github.pareronia.aoc.IterTools.product;
import static com.github.pareronia.aoc.SetUtils.difference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import com.github.pareronia.aoc.IterTools.ProductPair;
import com.github.pareronia.aoc.SetUtils;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Vector;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_08
        extends SolutionBase<AoC2024_08.Input, Integer, Integer> {
    
    private AoC2024_08(final boolean debug) {
        super(debug);
    }
    
    public static AoC2024_08 create() {
        return new AoC2024_08(false);
    }
    
    public static AoC2024_08 createDebug() {
        return new AoC2024_08(true);
    }
    
    @Override
    protected Input parseInput(final List<String> inputs) {
        final int h = inputs.size();
        final int w = inputs.get(0).length();
        final Map<Character, List<Position>> antennae = new HashMap<>();
        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                final char ch = inputs.get(r).charAt(c);
                if (ch != '.') {
                    antennae.computeIfAbsent(ch, k -> new ArrayList<>())
                        .add(Position.of(c, h - r - 1));
                }
            }
        }
        return new Input(w, h, antennae.values().stream().toList());
    }
    
    private Set<Position> getAntinodes(
            final AntennaPair pair,
            final int h,
            final int w,
            final int maxCount
    ) {
        final Vector vec = Vector.of(
                pair.first.getX() - pair.second.getX(),
                pair.first.getY() - pair.second.getY());
        final Set<Position> antinodes = new HashSet<>();
        for (final ProductPair<Position, Integer> pp : product(pair, Set.of(1, -1))) {
            for (int a = 1; a <= maxCount; a++) {
                final Position antinode = pp.first().translate(vec, pp.second() * a);
                if (0 <= antinode.getX() && antinode.getX() < w
                        && 0 <= antinode.getY() && antinode.getY() < h) {
                    antinodes.add(antinode);
                } else {
                    break;
                }
            }
        }
        return antinodes;
    }
    
    private int solve(
            final List<List<Position>> antennae,
            final Function<AntennaPair, Set<Position>> collectAntinodes
    ) {
        final Function<List<Position>, Stream<AntennaPair>> antennaPairs =
            sameFrequency ->
                Utils.stream(combinationsIterator(sameFrequency.size(), 2))
                    .map(comb_idx -> new AntennaPair(
                            sameFrequency.get(comb_idx[0]),
                            sameFrequency.get(comb_idx[1])));
        return antennae.stream()
                .flatMap(antennaPairs)
                .map(collectAntinodes)
                .reduce(SetUtils::union)
                .map(Set::size).orElseThrow();
    }
    
    @Override
    public Integer solvePart1(final Input input) {
        return solve(input.antennae, pair ->
                difference(
                        getAntinodes(pair, input.h, input.w, 1),
                        Set.of(pair.first, pair.second)));
    }
    
    @Override
    public Integer solvePart2(final Input input) {
        return solve(input.antennae, pair ->
                getAntinodes(pair, input.h, input.w, Integer.MAX_VALUE));
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "14"),
        @Sample(method = "part2", input = TEST, expected = "34"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2024_08.create().run();
    }

    private static final String TEST = """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............
            """;

    record Input(int w, int h, List<List<Position>> antennae) {}

    record AntennaPair(Position first, Position second) implements Iterable<Position> {

        @Override
        public Iterator<Position> iterator() {
            return Set.of(first, second).iterator();
        }
    }
}
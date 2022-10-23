import static java.util.stream.Collectors.toList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.knothash.KnotHash;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2017_14 extends AoCBase {
    
    private static final char ON = '1';

    private final String input;

    private AoC2017_14(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = inputs.get(0);
    }

    public static AoC2017_14 create(final List<String> input) {
        return new AoC2017_14(input, false);
    }

    public static AoC2017_14 createDebug(final List<String> input) {
        return new AoC2017_14(input, true);
    }
    
    private Stream<String> hashes() {
        return IntStream.range(0, 128)
            .mapToObj(i -> this.input + "-" + i)
            .map(KnotHash::binString);
    }
    
    private Set<Cell> findRegion(final Grid grid, final Cell start) {
        final Set<Cell> seen = new HashSet<>();
        final Deque<Cell> q = new ArrayDeque<>();
        q.add(start);
        while (!q.isEmpty()) {
            final Cell cell = q.poll();
            grid.getCapitalNeighbours(cell)
                .filter(n -> grid.getValueAt(n) == ON)
                .filter(n -> !seen.contains(n))
                .forEach(n -> {
                    q.add(n);
                    seen.add(n);
                });
        }
        return seen;
    }
    
    @Override
    public Long solvePart1() {
        return hashes()
            .flatMap(Utils::asCharacterStream)
            .filter(c -> c == ON)
            .count();
    }
    
    @Override
    public Integer solvePart2() {
        final Grid grid = Grid.from(hashes().collect(toList()));
        final List<Set<Cell>> regions = new ArrayList<>();
        grid.getAllEqualTo(ON)
            .filter(c -> regions.stream().noneMatch(r -> r.contains(c)))
            .map(c -> findRegion(grid, c))
            .forEach(regions::add);
        return regions.size();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_14.createDebug(TEST).solvePart1() == 8108L;
        assert AoC2017_14.createDebug(TEST).solvePart2() == 1242;

        final Puzzle puzzle = Aocd.puzzle(2017, 14);
        final List<String> input = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", create(input)::solvePart1),
            () -> lap("Part 2", create(input)::solvePart2)
        );
    }
    
    private static final List<String> TEST = splitLines("flqrgnkx");
}

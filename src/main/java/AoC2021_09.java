import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.summingInt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.github.pareronia.aocd.Aocd;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2021_09 extends AoCBase {
    
    private static final Pair N = Pair.of(-1, 0);
    private static final Pair E = Pair.of(0, 1);
    private static final Pair S = Pair.of(1, 0);
    private static final Pair W = Pair.of(0, -1);
    private static final Set<Pair> NESW = Set.of(N, E, S, W);

    private final int[][] heights;
    
    private AoC2021_09(final List<String> input, final boolean debug) {
        super(debug);
        this.heights = new int[input.size()][input.get(0).length()];
		for (int i = 0; i < input.size(); i++) {
		    final String string = input.get(i);
            final int[] row = new int[string.length()];
		    for (int j = 0; j < string.length(); j++) {
		        row[j] = Integer.parseInt(String.valueOf(string.charAt(j)));
		    }
		    this.heights[i] = row;
		}
		printGrid(this.heights);
    }
    
    public static final AoC2021_09 create(final List<String> input) {
        return new AoC2021_09(input, false);
    }

    public static final AoC2021_09 createDebug(final List<String> input) {
        return new AoC2021_09(input, true);
    }
    
    private void printGrid(final int[][] grid) {
        Arrays.stream(grid).forEach(r ->
                log(Arrays.stream(r)
                        .mapToObj(Integer::valueOf)
                        .map(String::valueOf)
                        .collect(joining(" "))));
    }
    
    private boolean inBounds(final int r, final int c) {
        return (r >= 0 && r < this.heights.length && c >= 0 && c < this.heights[0].length);
    }
    
    private List<Pair> findNeighbours(final int r, final int c) {
        final List<Pair> neighbours = new ArrayList<>();
        for (final Pair d : NESW) {
            final int nr = r + d.getOne();
            final int nc = c + d.getTwo();
            if (!inBounds(nr, nc)) {
                continue;
            }
            neighbours.add(Pair.of(nr,  nc));
        }
        return neighbours;
    }

    private List<Pair> findLows() {
        final List<Pair> lows = new ArrayList<>();
        for (int r = 0; r < this.heights.length; r++) {
            for (int c = 0; c < this.heights[r].length; c++) {
                final int height = this.heights[r][c];
                boolean min = true;
                for (final Pair n : findNeighbours(r, c)) {
                    if (this.heights[n.getOne()][n.getTwo()] <= height) {
                        min = false;
                    }
                }
                if (min) {
                    lows.add(Pair.of(r, c));
                    log(String.format("%d: (%d,%d)", height, r, c));
                }
            }
        }
        return lows;
    }
    
    @Override
    public Integer solvePart1() {
        return findLows().stream()
            .map(p -> this.heights[p.getOne()][p.getTwo()] + 1)
            .collect(summingInt(Integer::intValue));
    }

    @Override
    public Integer solvePart2() {
        final List<Pair> lows = findLows();
        final List<Set<Pair>> basins = new ArrayList<>();
        for (final Pair low : lows) {
            final Set<Pair> basin = new HashSet<>();
            basin.add(low);
            while (true) {
                final Set<Pair> add = new HashSet<>();
                for (final Pair p : basin) {
                    for (final Pair n : findNeighbours(p.getOne(), p.getTwo())) {
                        if (basin.contains(n)) {
                            continue;
                        }
                        if (heights[n.getOne()][n.getTwo()] == 9) {
                            continue;
                        }
                        add.add(n);
                    }
                }
                if (add.isEmpty()) {
                    break;
                }
                basin.addAll(add);
            }
            basins.add(basin);
            log(basin);
        }
        return basins.stream()
                .map(b -> b.size())
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .reduce(1, (a, b) -> a * b);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_09.create(TEST).solvePart1() == 15;
        assert AoC2021_09.createDebug(TEST).solvePart2() == 1134;

        final List<String> input = Aocd.getData(2021, 9);
        lap("Part 1", () -> AoC2021_09.create(input).solvePart1());
        lap("Part 2", () -> AoC2021_09.create(input).solvePart2());
    }

    private static final List<String> TEST = splitLines(
        "2199943210\r\n" +
        "3987894921\r\n" +
        "9856789892\r\n" +
        "8767896789\r\n" +
        "9899965678"
    );
    
    @RequiredArgsConstructor(staticName = "of")
    @Getter
    @ToString
    private static final class Pair {
        private final int one;
        private final int two;
        
        @Override
        public int hashCode() {
            return Objects.hash(one, two);
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Pair other = (Pair) obj;
            return one == other.one && two == other.two;
        }
    }
}

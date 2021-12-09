import static java.util.stream.Collectors.joining;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aocd.Aocd;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AoC2021_09 extends AoCBase {
    
    private static final Pair<Integer, Integer> N = Pair.of(-1, 0);
    private static final Pair<Integer, Integer> E = Pair.of(0, 1);
    private static final Pair<Integer, Integer> S = Pair.of(1, 0);
    private static final Pair<Integer, Integer> W = Pair.of(0, -1);
    private static final Set<Pair<Integer, Integer>> NESW = Set.of(N, E, S, W);

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
    
    @Override
    public Integer solvePart1() {
        int ans = 0;
        for (int r = 0; r < this.heights.length; r++) {
            for (int c = 0; c < this.heights[r].length; c++) {
                final int height = this.heights[r][c];
                boolean min = true;
                for (final Pair<Integer, Integer> d : NESW) {
                    final int nr = r + d.getOne();
                    final int nc = c + d.getTwo();
                    if (!inBounds(nr, nc)) {
                        continue;
                    }
                    if (this.heights[nr][nc] <= height) {
                        min = false;
                    }
                }
                if (min) {
                    log(String.format("%d: (%d,%d)", height, r, c));
                    ans += height + 1;
                }
            }
        }
        return ans;
    }

    @Override
    public Integer solvePart2() {
        return null;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_09.create(TEST).solvePart1() == 15;
        assert AoC2021_09.create(TEST).solvePart2() == null;

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
    private static final class Pair<L, R> {
        private final L one;
        private final R two;
    }
}

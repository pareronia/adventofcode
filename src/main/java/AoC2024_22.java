import static com.github.pareronia.aoc.IterTools.enumerate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_22 extends SolutionBase<List<Integer>, Long, Integer> {

    private AoC2024_22(final boolean debug) {
        super(debug);
    }

    public static AoC2024_22 create() {
        return new AoC2024_22(false);
    }

    public static AoC2024_22 createDebug() {
        return new AoC2024_22(true);
    }

    @Override
    protected List<Integer> parseInput(final List<String> inputs) {
        return inputs.stream().map(Integer::valueOf).toList();
    }

    private int secret(final long seed) {
        long num = ((seed ^ (seed * 64)) % 16_777_216);
        num = (num ^ (num / 32)) % 16_777_216;
        return (int) (num ^ (num * 2048) % 16_777_216);
    }

    @Override
    public Long solvePart1(final List<Integer> seeds) {
        return seeds.stream()
                .mapToLong(
                        s -> {
                            long ss = s;
                            for (int i = 0; i < 2000; i++) {
                                ss = secret(ss);
                            }
                            return ss;
                        })
                .sum();
    }

    @Override
    public Integer solvePart2(final List<Integer> seeds) {
        final Map<Integer, Integer> p = new HashMap<>();
        final int[] seen = new int[19 * 19 * 19 * 19];
        Arrays.fill(seen, -1);
        enumerate(seeds.stream())
                .forEach(
                        e -> {
                            final int i = e.index();
                            int num = e.value();
                            final int na = num;
                            final int nb = secret(na);
                            final int nc = secret(nb);
                            final int nd = secret(nc);
                            int a;
                            int b = (9 + na % 10 - nb % 10);
                            int c = (9 + nb % 10 - nc % 10);
                            int d = (9 + nc % 10 - nd % 10);
                            num = nd;
                            int prevPrice = num % 10;
                            for (int j = 3; j < 2000; j++) {
                                num = secret(num);
                                final int price = num % 10;
                                a = b;
                                b = c;
                                c = d;
                                d = (9 + prevPrice - price);
                                prevPrice = price;
                                final int key = 6589 * a + 361 * b + 19 * c + d;
                                if (seen[key] == i) {
                                    continue;
                                }
                                seen[key] = i;
                                p.merge(key, price, Integer::sum);
                            }
                        });
        return p.values().stream().mapToInt(Integer::intValue).max().getAsInt();
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "37327623"),
        @Sample(method = "part2", input = TEST2, expected = "23")
    })
    public void samples() {}

    public static void main(final String[] args) throws Exception {
        AoC2024_22.create().run();
    }

    private static final String TEST1 =
            """
            1
            10
            100
            2024
            """;

    private static final String TEST2 =
            """
            1
            2
            3
            2024
            """;
}

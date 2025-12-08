import static com.github.pareronia.aoc.IntegerSequence.Range.range;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toMap;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.geometry3d.Position3D;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2025_08 extends SolutionBase<AoC2025_08.Input, Long, Long> {

    private AoC2025_08(final boolean debug) {
        super(debug);
    }

    public static AoC2025_08 create() {
        return new AoC2025_08(false);
    }

    public static AoC2025_08 createDebug() {
        return new AoC2025_08(true);
    }

    @Override
    protected Input parseInput(final List<String> inputs) {
        final List<Position3D> boxes =
                inputs.stream()
                        .map(
                                s -> {
                                    final String[] splits = s.split(",");
                                    return Position3D.of(
                                            Integer.parseInt(splits[0]),
                                            Integer.parseInt(splits[1]),
                                            Integer.parseInt(splits[2]));
                                })
                        .toList();
        final List<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < boxes.size(); i++) {
            for (int j = i + 1; j < boxes.size(); j++) {
                pairs.add(new Pair(i, j, boxes.get(i).squaredDistance(boxes.get(j))));
            }
        }
        Collections.sort(pairs, comparing(Pair::distance));
        return new Input(boxes, pairs);
    }

    private long solve1(final Input input, final int size) {
        final DSU dsu = new DSU(input.boxes().size());
        input.pairs().stream().limit(size).forEach(pair -> dsu.unify(pair.first(), pair.second()));
        return range(input.boxes().size()).stream()
                .collect(toMap(i -> i, dsu::componentSize))
                .values()
                .stream()
                .distinct()
                .sorted(reverseOrder())
                .limit(3)
                .reduce((acc, sz) -> acc * sz)
                .get();
    }

    @Override
    public Long solvePart1(final Input input) {
        return this.solve1(input, 1000);
    }

    @Override
    public Long solvePart2(final Input input) {
        final DSU dsu = new DSU(input.boxes().size());
        for (final Pair pair : input.pairs()) {
            dsu.unify(pair.first(), pair.second());
            if (dsu.components() == 1 && dsu.componentSize(0) == input.boxes().size()) {
                return ((long) input.boxes().get(pair.first()).getX())
                        * input.boxes().get(pair.second()).getX();
            }
        }
        throw new IllegalStateException("Unsolvable");
    }

    @Override
    protected void samples() {
        final AoC2025_08 test = createDebug();
        final Input input = test.parseInput(StringOps.splitLines(TEST));
        assert test.solve1(input, 10) == 40;
        assert test.solvePart2(input) == 25_272;
    }

    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            162,817,812
            57,618,57
            906,360,560
            592,479,940
            352,342,300
            466,668,158
            542,29,236
            431,825,988
            739,650,466
            52,470,668
            216,146,977
            819,987,18
            117,168,530
            805,96,715
            346,949,466
            970,615,88
            941,993,340
            862,61,35
            984,92,344
            425,690,689
            """;

    record Pair(int first, int second, long distance) {}

    record Input(List<Position3D> boxes, List<Pair> pairs) {}

    private static class DSU {

        private final int[] sz;
        private final int[] id;
        private int numComponents;

        public DSU(final int size) {
            this.numComponents = size;
            sz = new int[size];
            id = new int[size];

            for (int i = 0; i < size; i++) {
                id[i] = i;
                sz[i] = 1;
            }
        }

        public int find(final int p) {
            int target = p;
            int root = target;
            while (root != id[root]) {
                root = id[root];
            }

            while (target != root) {
                final int next = id[target];
                id[target] = root;
                target = next;
            }

            return root;
        }

        public int componentSize(final int p) {
            return sz[find(p)];
        }

        public int components() {
            return numComponents;
        }

        @SuppressWarnings("PMD.OnlyOneReturn")
        public int unify(final int p, final int q) {
            if (find(p) == find(q)) {
                return 0;
            }
            final int root1 = find(p);
            final int root2 = find(q);
            if (sz[root1] < sz[root2]) {
                sz[root2] += sz[root1];
                id[root1] = root2;
                sz[root1] = 0;
                numComponents--;
                return root2;
            } else {
                sz[root1] += sz[root2];
                id[root2] = root1;
                sz[root2] = 0;
                numComponents--;
                return root1;
            }
        }
    }
}

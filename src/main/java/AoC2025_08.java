import static com.github.pareronia.aoc.IntegerSequence.Range.range;

import static java.util.Comparator.comparing;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.geometry3d.Position3D;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.Arrays;
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
        final List<Box> boxes =
                range(inputs.size()).stream().map(i -> Box.fromInput(i, inputs.get(i))).toList();
        final List<Pair> pairs =
                range(boxes.size()).stream()
                        .flatMap(
                                i ->
                                        range(i + 1, boxes.size(), 1).stream()
                                                .map(j -> Pair.of(boxes.get(i), boxes.get(j))))
                        .sorted(comparing(Pair::distance))
                        .toList();
        return new Input(boxes, pairs);
    }

    @SuppressWarnings("PMD.LawOfDemeter")
    private long solve1(final Input input, final int size) {
        final DSU dsu = new DSU(input.boxes().size());
        input.pairs().stream()
                .limit(size)
                .forEach(pair -> dsu.unify(pair.first().idx(), pair.second().idx()));
        return Arrays.stream(dsu.sz)
                .sorted()
                .skip(dsu.sz.length - 3)
                .reduce((acc, sz) -> acc * sz)
                .getAsInt();
    }

    @Override
    public Long solvePart1(final Input input) {
        return this.solve1(input, 1000);
    }

    @Override
    @SuppressWarnings("PMD.LawOfDemeter")
    public Long solvePart2(final Input input) {
        final DSU dsu = new DSU(input.boxes().size());
        return input.pairs().stream()
                .mapToLong(
                        pair -> {
                            dsu.unify(pair.first().idx(), pair.second().idx());
                            return ((long) pair.first().position().getX())
                                    * pair.second().position().getX();
                        })
                .dropWhile(
                        x -> dsu.numComponents != 1 || dsu.sz[dsu.find(0)] != input.boxes().size())
                .findFirst()
                .orElseThrow();
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

    record Box(int idx, Position3D position) {

        public static Box fromInput(final int idx, final String input) {
            final String[] splits = input.split(",");
            return new Box(
                    idx,
                    Position3D.of(
                            Integer.parseInt(splits[0]),
                            Integer.parseInt(splits[1]),
                            Integer.parseInt(splits[2])));
        }
    }

    record Pair(Box first, Box second, long distance) {

        public static Pair of(final Box first, final Box second) {
            return new Pair(first, second, first.position().squaredDistance(second.position()));
        }
    }

    record Input(List<Box> boxes, List<Pair> pairs) {}

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
            int root = p;
            while (root != id[root]) {
                root = id[root];
            }
            return root;
        }

        public void unify(final int p, final int q) {
            final int root1 = find(p);
            final int root2 = find(q);
            if (root1 != root2) {
                if (sz[root1] < sz[root2]) {
                    sz[root2] += sz[root1];
                    id[root1] = root2;
                    sz[root1] = 0;
                } else {
                    sz[root1] += sz[root2];
                    id[root2] = root1;
                    sz[root2] = 0;
                }
                numComponents--;
            }
        }
    }
}

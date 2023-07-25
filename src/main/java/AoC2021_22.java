import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Pattern;

import com.github.pareronia.aoc.Range;
import com.github.pareronia.aoc.geometry3d.Cuboid;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2021_22 extends AoCBase {
    
    private static final Pattern PATTERN = Pattern.compile(
        "^([a-z]{2,3}) x=(-?[0-9]*)\\.{2}(-?[0-9]*),"
        + "y=(-?[0-9]*)\\.{2}(-?[0-9]*),"
        + "z=(-?[0-9]*)\\.{2}(-?[0-9]*)$");
    
    private final List<RebootStep> steps;
    
    private AoC2021_22(final List<String> input, final boolean debug) {
        super(debug);
        this.steps = input.stream()
            .flatMap(s -> PATTERN.matcher(s).results())
            .map(m -> {
                    final Integer x1 = Integer.valueOf(m.group(2));
                    final Integer x2 = Integer.valueOf(m.group(3));
                    final Integer y1 = Integer.valueOf(m.group(4));
                    final Integer y2 = Integer.valueOf(m.group(5));
                    final Integer z1 = Integer.valueOf(m.group(6));
                    final Integer z2 = Integer.valueOf(m.group(7));
                    assert x1 <= x2 && y1 <= y2 && z1 <= z2;
                    final Range<Integer> x = Range.between(x1, x2);
                    final Range<Integer> y = Range.between(y1, y2);
                    final Range<Integer> z = Range.between(z1, z2);
                    final boolean on = "on".equals(m.group(1));
                    return new RebootStep(x, y, z, on);
            })
            .collect(toList());
    }
    
    public static final AoC2021_22 create(final List<String> input) {
        return new AoC2021_22(input, false);
    }

    public static final AoC2021_22 createDebug(final List<String> input) {
        return new AoC2021_22(input, true);
    }

    private long solve(final List<RebootStep> steps) {
        final Map<Cuboid, Long> cuboids = new HashMap<>();
        for (final RebootStep s : steps) {
            final Map<Cuboid, Long> update = new HashMap<>();
            final Cuboid cuboid = Cuboid.of(
                s.x.getMinimum(), s.x.getMaximum(),
                s.y.getMinimum(), s.y.getMaximum(),
                s.z.getMinimum(), s.z.getMaximum());
            for (final Entry<Cuboid, Long> e : cuboids.entrySet()) {
                final Optional<Cuboid> ix
                        = Cuboid.intersection(cuboid, e.getKey());
                if (ix.isPresent()) {
                    update.merge(ix.get(), -1L * e.getValue(), Long::sum);
                }
            }
            if (s.on) {
                update.merge(cuboid, 1L, Long::sum);
            }
            for (final Entry<Cuboid, Long> u : update.entrySet()) {
                cuboids.merge(u.getKey(), u.getValue(), Long::sum);
            }
        }
        return cuboids.entrySet().stream()
            .mapToLong(e -> e.getValue() * e.getKey().getVolume())
            .sum();
    }
    
    @Override
    public Integer solvePart1() {
        final List<RebootStep> initSteps = this.steps.stream()
            .filter(s -> !(s.x.isBefore(-50) || s.x.isAfter(50)
                        || s.y.isBefore(-50) || s.y.isAfter(50)
                        || s.z.isBefore(-50) || s.z.isAfter(50)))
            .collect(toList());
        return (int) solve(initSteps);
    }
    
    @Override
    public Long solvePart2() {
        return solve(this.steps);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_22.create(TEST1).solvePart1() == 39;
        assert AoC2021_22.create(TEST2).solvePart1() == 590_784;
        assert AoC2021_22.create(TEST3).solvePart1() == 474_140;
        assert AoC2021_22.create(TEST3).solvePart2() == 2_758_514_936_282_235L;

        final Puzzle puzzle = Aocd.puzzle(2021, 22);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_22.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_22.create(puzzle.getInputData()).solvePart2())
        );
    }

    private static final List<String> TEST1 = splitLines(
        "on x=10..12,y=10..12,z=10..12\r\n" +
        "on x=11..13,y=11..13,z=11..13\r\n" +
        "off x=9..11,y=9..11,z=9..11\r\n" +
        "on x=10..10,y=10..10,z=10..10"
    );
    private static final List<String> TEST2 = splitLines(
        "on x=-20..26,y=-36..17,z=-47..7\r\n" +
        "on x=-20..33,y=-21..23,z=-26..28\r\n" +
        "on x=-22..28,y=-29..23,z=-38..16\r\n" +
        "on x=-46..7,y=-6..46,z=-50..-1\r\n" +
        "on x=-49..1,y=-3..46,z=-24..28\r\n" +
        "on x=2..47,y=-22..22,z=-23..27\r\n" +
        "on x=-27..23,y=-28..26,z=-21..29\r\n" +
        "on x=-39..5,y=-6..47,z=-3..44\r\n" +
        "on x=-30..21,y=-8..43,z=-13..34\r\n" +
        "on x=-22..26,y=-27..20,z=-29..19\r\n" +
        "off x=-48..-32,y=26..41,z=-47..-37\r\n" +
        "on x=-12..35,y=6..50,z=-50..-2\r\n" +
        "off x=-48..-32,y=-32..-16,z=-15..-5\r\n" +
        "on x=-18..26,y=-33..15,z=-7..46\r\n" +
        "off x=-40..-22,y=-38..-28,z=23..41\r\n" +
        "on x=-16..35,y=-41..10,z=-47..6\r\n" +
        "off x=-32..-23,y=11..30,z=-14..3\r\n" +
        "on x=-49..-5,y=-3..45,z=-29..18\r\n" +
        "off x=18..30,y=-20..-8,z=-3..13\r\n" +
        "on x=-41..9,y=-7..43,z=-33..15\r\n" +
        "on x=-54112..-39298,y=-85059..-49293,z=-27449..7877\r\n" +
        "on x=967..23432,y=45373..81175,z=27513..53682"
    );
    private static final List<String> TEST3 = splitLines(
        "on x=-5..47,y=-31..22,z=-19..33\r\n"
        + "on x=-44..5,y=-27..21,z=-14..35\r\n"
        + "on x=-49..-1,y=-11..42,z=-10..38\r\n"
        + "on x=-20..34,y=-40..6,z=-44..1\r\n"
        + "off x=26..39,y=40..50,z=-2..11\r\n"
        + "on x=-41..5,y=-41..6,z=-36..8\r\n"
        + "off x=-43..-33,y=-45..-28,z=7..25\r\n"
        + "on x=-33..15,y=-32..19,z=-34..11\r\n"
        + "off x=35..47,y=-46..-34,z=-11..5\r\n"
        + "on x=-14..36,y=-6..44,z=-16..29\r\n"
        + "on x=-57795..-6158,y=29564..72030,z=20435..90618\r\n"
        + "on x=36731..105352,y=-21140..28532,z=16094..90401\r\n"
        + "on x=30999..107136,y=-53464..15513,z=8553..71215\r\n"
        + "on x=13528..83982,y=-99403..-27377,z=-24141..23996\r\n"
        + "on x=-72682..-12347,y=18159..111354,z=7391..80950\r\n"
        + "on x=-1060..80757,y=-65301..-20884,z=-103788..-16709\r\n"
        + "on x=-83015..-9461,y=-72160..-8347,z=-81239..-26856\r\n"
        + "on x=-52752..22273,y=-49450..9096,z=54442..119054\r\n"
        + "on x=-29982..40483,y=-108474..-28371,z=-24328..38471\r\n"
        + "on x=-4958..62750,y=40422..118853,z=-7672..65583\r\n"
        + "on x=55694..108686,y=-43367..46958,z=-26781..48729\r\n"
        + "on x=-98497..-18186,y=-63569..3412,z=1232..88485\r\n"
        + "on x=-726..56291,y=-62629..13224,z=18033..85226\r\n"
        + "on x=-110886..-34664,y=-81338..-8658,z=8914..63723\r\n"
        + "on x=-55829..24974,y=-16897..54165,z=-121762..-28058\r\n"
        + "on x=-65152..-11147,y=22489..91432,z=-58782..1780\r\n"
        + "on x=-120100..-32970,y=-46592..27473,z=-11695..61039\r\n"
        + "on x=-18631..37533,y=-124565..-50804,z=-35667..28308\r\n"
        + "on x=-57817..18248,y=49321..117703,z=5745..55881\r\n"
        + "on x=14781..98692,y=-1341..70827,z=15753..70151\r\n"
        + "on x=-34419..55919,y=-19626..40991,z=39015..114138\r\n"
        + "on x=-60785..11593,y=-56135..2999,z=-95368..-26915\r\n"
        + "on x=-32178..58085,y=17647..101866,z=-91405..-8878\r\n"
        + "on x=-53655..12091,y=50097..105568,z=-75335..-4862\r\n"
        + "on x=-111166..-40997,y=-71714..2688,z=5609..50954\r\n"
        + "on x=-16602..70118,y=-98693..-44401,z=5197..76897\r\n"
        + "on x=16383..101554,y=4615..83635,z=-44907..18747\r\n"
        + "off x=-95822..-15171,y=-19987..48940,z=10804..104439\r\n"
        + "on x=-89813..-14614,y=16069..88491,z=-3297..45228\r\n"
        + "on x=41075..99376,y=-20427..49978,z=-52012..13762\r\n"
        + "on x=-21330..50085,y=-17944..62733,z=-112280..-30197\r\n"
        + "on x=-16478..35915,y=36008..118594,z=-7885..47086\r\n"
        + "off x=-98156..-27851,y=-49952..43171,z=-99005..-8456\r\n"
        + "off x=2032..69770,y=-71013..4824,z=7471..94418\r\n"
        + "on x=43670..120875,y=-42068..12382,z=-24787..38892\r\n"
        + "off x=37514..111226,y=-45862..25743,z=-16714..54663\r\n"
        + "off x=25699..97951,y=-30668..59918,z=-15349..69697\r\n"
        + "off x=-44271..17935,y=-9516..60759,z=49131..112598\r\n"
        + "on x=-61695..-5813,y=40978..94975,z=8655..80240\r\n"
        + "off x=-101086..-9439,y=-7088..67543,z=33935..83858\r\n"
        + "off x=18020..114017,y=-48931..32606,z=21474..89843\r\n"
        + "off x=-77139..10506,y=-89994..-18797,z=-80..59318\r\n"
        + "off x=8476..79288,y=-75520..11602,z=-96624..-24783\r\n"
        + "on x=-47488..-1262,y=24338..100707,z=16292..72967\r\n"
        + "off x=-84341..13987,y=2429..92914,z=-90671..-1318\r\n"
        + "off x=-37810..49457,y=-71013..-7894,z=-105357..-13188\r\n"
        + "off x=-27365..46395,y=31009..98017,z=15428..76570\r\n"
        + "off x=-70369..-16548,y=22648..78696,z=-1892..86821\r\n"
        + "on x=-53470..21291,y=-120233..-33476,z=-44150..38147\r\n"
        + "off x=-93533..-4276,y=-16170..68771,z=-104985..-24507"
    );
    
    @RequiredArgsConstructor
    @ToString
    private static final class RebootStep {
        private final Range<Integer> x;
        private final Range<Integer> y;
        private final Range<Integer> z;
        private final boolean on;
    }
}
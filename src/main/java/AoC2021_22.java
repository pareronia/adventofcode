import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.math3.util.IntegerSequence.Range;

import com.github.pareronia.aoc.geometry3d.Position3D;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2021_22 extends AoCBase {
    
    private static final Pattern PATTERN = Pattern.compile(
            "^([a-z]{2,3}) x=(-?[0-9]*)\\.{2}(-?[0-9]*),y=(-?[0-9]*)\\.{2}(-?[0-9]*),z=(-?[0-9]*)\\.{2}(-?[0-9]*)$");
    
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
                    final Range x;
                    if (x1 < x2) {
                        x = new Range(x1, x2, 1);
                    } else {
                        x = new Range(x2, x1, 1);
                    }
                    final Range y;
                    if (y1 < y2) {
                        y = new Range(y1, y2, 1);
                    } else {
                        y = new Range(y2, y1, 1);
                    }
                    final Range z;
                    if (z1 < z2) {
                        z = new Range(z1, z2, 1);
                    } else {
                        z = new Range(z2, z1, 1);
                    }
                    return new RebootStep(x, y, z, m.group(1));
            })
            .collect(toList());
    }
    
    public static final AoC2021_22 create(final List<String> input) {
        return new AoC2021_22(input, false);
    }

    public static final AoC2021_22 createDebug(final List<String> input) {
        return new AoC2021_22(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        final Set<Position3D> cubes = new HashSet<>();
        this.steps.stream()
            .filter(s -> {
                for (final int x : s.x) {
                    if (x < -50 || x > 50) {
                        return false;
                    }
                }
                for (final int y : s.y) {
                    if (y < -50 || y > 50) {
                        return false;
                    }
                }
                for (final int z : s.z) {
                    if (z < -50 || z > 50) {
                        return false;
                    }
                }
                return true;
            })
            .forEach(s -> {
                for (final int x : s.x) {
                    for (final int y : s.y) {
                        for (final int z : s.z) {
                            if ("on".equals(s.state)) {
                                cubes.add(Position3D.of(x, y, z));
                            } else {
                                cubes.remove(Position3D.of(x, y, z));
                            }
                        }
                    }
                }
            });
        return cubes.size();
    }
    
    @Override
    public Integer solvePart2() {
        return null;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_22.create(TEST1).solvePart1() == 39;
        assert AoC2021_22.createDebug(TEST2).solvePart1() == 590784;
        assert AoC2021_22.create(TEST1).solvePart2() == null;

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
    
    @RequiredArgsConstructor
    @ToString
    private static final class RebootStep {
        private final Range x;
        private final Range y;
        private final Range z;
        private final String state;
    }
}
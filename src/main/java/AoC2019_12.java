import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.IterTools.combinations;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.geometry3d.Point3D;
import com.github.pareronia.aoc.geometry3d.Position3D;
import com.github.pareronia.aoc.geometry3d.Vector3D;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.ToString;

public class AoC2019_12 extends AoCBase {
    
    private final List<Position3D> initialPositions;
    
    private AoC2019_12(final List<String> input, final boolean debug) {
        super(debug);
        this.initialPositions = input.stream().map(Utils::integerNumbers)
            .map(p -> Position3D.of(p[0], p[1], p[2]))
            .collect(toList());
    }

    public static AoC2019_12 create(final List<String> input) {
        return new AoC2019_12(input, false);
    }

    public static AoC2019_12 createDebug(final List<String> input) {
        return new AoC2019_12(input, true);
    }
    
    private int gravity(
            final Position3D a,
            final Position3D b,
            final Function<Position3D, Integer> f
    ) {
        if (f.apply(a) < f.apply(b)) {
            return 1;
        } else if (f.apply(a) > f.apply(b)) {
            return -1;
        } else {
            return 0;
        }
    }
       
    private void step(final List<Moon> moons) {
        combinations(moons.size(), 2).forEach(idxs -> {
            final Moon a = moons.get(idxs[0]);
            final Moon b = moons.get(idxs[1]);
            final int dx = gravity(a.position, b.position, Position3D::getX);
            final int dy = gravity(a.position, b.position, Position3D::getY);
            final int dz = gravity(a.position, b.position, Position3D::getZ);
            a.adjustVelocity(dx, dy, dz);
            b.adjustVelocity(-dx, -dy, -dz);
        });
        moons.forEach(m -> m.position = m.position.translate(m.velocity));
    }
    
    private int solve1(final int steps) {
        final List<Moon> moons
            = this.initialPositions.stream().map(Moon::create).collect(toList());
        range(steps).forEach(i -> step(moons));
        return moons.stream().mapToInt(Moon::totalEnergy).sum();
    }

    @Override
    public Integer solvePart1() {
        return solve1(1000);
    }
    
    @Override
    public Integer solvePart2() {
        return 0;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2019_12.createDebug(TEST1).solve1(10) == 179;
        assert AoC2019_12.createDebug(TEST2).solve1(100) == 1940;
        
        final Puzzle puzzle = Aocd.puzzle(2019, 12);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2019_12.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2019_12.create(inputData)::solvePart2)
        );
    }
    
    private static final List<String> TEST1 = splitLines(
            "<x=-1, y=0, z=2>\r\n" +
            "<x=2, y=-10, z=-7>\r\n" +
            "<x=4, y=-8, z=8>\r\n" +
            "<x=3, y=5, z=-1>"
    );
    private static final List<String> TEST2 = splitLines(
            "<x=-8, y=-10, z=0>\r\n" +
            "<x=5, y=5, z=10>\r\n" +
            "<x=2, y=-7, z=3>\r\n" +
            "<x=9, y=-8, z=-3>"
    );
    
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    private static final class Moon {
        private Position3D position;
        private Vector3D velocity;
        
        public static Moon create(final Position3D initialPosition) {
            return new Moon(initialPosition, Vector3D.of(0, 0, 0));
        }
        
        private static int energy(final Point3D p) {
            return Math.abs(p.getX()) + Math.abs(p.getY()) + Math.abs(p.getZ());
        }
        
        public int totalEnergy() {
            return energy(this.position) * energy(this.velocity);
        }

        public void adjustVelocity(final int dx, final int dy, final int dz) {
            this.velocity = this.velocity.add(Vector3D.of(dx, dy, dz), 1);
        }
    }
}
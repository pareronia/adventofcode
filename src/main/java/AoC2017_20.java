import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.geometry3d.Position3D;
import com.github.pareronia.aoc.geometry3d.Vector3D;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2017_20 extends AoCBase {

    private static final int TICKS = 400;
    private static final Pattern PATTERN = Pattern.compile("[-0-9]+");
    
    private final List<Particle> buffer;
    
    private AoC2017_20(final List<String> inputs, final boolean debug) {
        super(debug);
        this.buffer = IntStream.range(0, inputs.size())
            .mapToObj(i -> parse(i, inputs.get(i)))
            .collect(toList());
    }

    private Particle parse(final int number, final String input) {
        final Matcher matcher = PATTERN.matcher(input);
        final List<Integer> numbers = new ArrayList<>();
        while (matcher.find()) {
            numbers.add(Integer.valueOf(matcher.group()));
        }
        return Particle.create(number, numbers.toArray(Integer[]::new));
    }

    public static AoC2017_20 create(final List<String> input) {
        return new AoC2017_20(input, false);
    }

    public static AoC2017_20 createDebug(final List<String> input) {
        return new AoC2017_20(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        List<Particle> b = new ArrayList<>(this.buffer);
        for (int i = 0; i < TICKS; i++) {
            b = b.stream().map(Particle::next).collect(toList());
        }
        return b.stream()
            .sorted(Particle.byDistance())
            .findFirst()
            .map(Particle::number)
            .orElseThrow();
    }
    
    @Override
    public Integer solvePart2() {
        List<Particle> b = new ArrayList<>(this.buffer);
        for (int i = 0; i < TICKS; i++) {
            b = b.stream()
                .map(Particle::next)
                .collect(Particle.groupingByPosition())
                .values().stream()
                .filter(v -> v.size() == 1)
                .flatMap(List::stream)
                .collect(toList());
        }
        return b.size();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_20.createDebug(TEST1).solvePart1().equals(0);
        assert AoC2017_20.createDebug(TEST2).solvePart2().equals(1);

        final Puzzle puzzle = Aocd.puzzle(2017, 20);
        final List<String> input = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", create(input)::solvePart1),
            () -> lap("Part 2", create(input)::solvePart2)
        );
    }
    
    private static final List<String> TEST1 = splitLines(
        "p=<3,0,0>, v=<2,0,0>, a=<-1,0,0>\r\n" +
        "p=<4,0,0>, v=<0,0,0>, a=<-2,0,0>"
    );
    private static final List<String> TEST2 = splitLines(
        "p=<-6,0,0>, v=< 3,0,0>, a=<0,0,0>\r\n" +
        "p=<-4,0,0>, v=< 2,0,0>, a=<0,0,0>\r\n" +
        "p=<-2,0,0>, v=< 1,0,0>, a=<0,0,0>\r\n" +
        "p=< 3,0,0>, v=<-1,0,0>, a=<0,0,0>"
    );
    
    record Particle(
        int number,
        Position3D position,
        Position3D velocity,
        Position3D acceleration
    ) {
        private static final Position3D ORIGIN = Position3D.of(0, 0, 0);
        
        public static Particle create(final int number, final Integer[] numbers) {
            assert numbers.length == 9;
            final Position3D position = Position3D.of(numbers[0], numbers[1], numbers[2]);
            final Position3D velocity = Position3D.of(numbers[3], numbers[4], numbers[5]);
            final Position3D acceleration = Position3D.of(numbers[6], numbers[7], numbers[8]);
            return new Particle(number, position, velocity, acceleration);
        }
        
        public Particle withPosition(final Position3D position) {
            return new Particle(this.number, position, this.velocity, this.acceleration);
        }
        
        public Particle withVelocity(final Position3D velocity) {
            return new Particle(this.number, this.position, velocity, this.acceleration);
        }
        
        public Particle next() {
            final Position3D v2 = velocity.translate(
                    Vector3D.from(ORIGIN, acceleration));
            final Position3D p2 = position.translate(
                    Vector3D.from(ORIGIN, v2));
            return this.withVelocity(v2).withPosition(p2);
        }
        
        public static Comparator<Particle> byDistance() {
            return (p1, p2) ->
                Integer.compare(p1.position.manhattanDistance(ORIGIN),
                                p2.position.manhattanDistance(ORIGIN));
        }
        
        public static
        Collector<Particle, ?, Map<Position3D, List<Particle>>>
        groupingByPosition() {
            return groupingBy(Particle::position, toList());
        }
    }
}

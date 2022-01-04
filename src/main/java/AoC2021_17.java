import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Vector;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2021_17 extends AoCBase {
    
    private static final Pattern PATTERN = Pattern.compile("^target area: x=(-?[0-9]*)\\.{2}(-?[0-9]*), y=(-?[0-9]*)\\.{2}(-?[0-9]*)$");

    private final Position target_bl;
    private final Position target_ur;
    
    private AoC2021_17(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 1;
        final Matcher matcher = PATTERN.matcher(input.get(0));
        matcher.find();
        this.target_bl = Position.of(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(3)));
        this.target_ur = Position.of(Integer.valueOf(matcher.group(2)), Integer.valueOf(matcher.group(4)));
        log(this.target_bl);
        log(this.target_ur);
    }
    
    public static final AoC2021_17 create(final List<String> input) {
        return new AoC2021_17(input, false);
    }

    public static final AoC2021_17 createDebug(final List<String> input) {
        return new AoC2021_17(input, true);
    }
    
    private Position next(final Position position, final Vector velocity) {
        return position.translate(velocity);
    }
    
    private Vector updateVelocity(final Vector velocity) {
        assert velocity.getX() >= 0;
        return Vector.of(Math.max(0, velocity.getX() - 1), velocity.getY() - 1);
    }
    
    private boolean inTargetArea(final Position position) {
        return position.getX() >= this.target_bl.getX()
            && position.getX() <= this.target_ur.getX()
            && position.getY() <= this.target_ur.getY()
            && position.getY() >= this.target_bl.getY();
    }
    
    private boolean overshot(final Position position) {
        return position.getX() > this.target_ur.getX()
            || position.getY() < this.target_bl.getY();
    }
    
    private boolean shoot(final Vector initialVelocity, final Consumer<Position> trajectoryConsumer) {
        Position position = Position.of(0, 0);
        trajectoryConsumer.accept(position);
        Vector velocity = initialVelocity;
        while (true) {
            position = next(position, velocity);
            trajectoryConsumer.accept(position);
            velocity = updateVelocity(velocity);
            if (overshot(position)) {
                return false;
            }
            if (inTargetArea(position)) {
                return true;
            }
        }
    }

    private Map<Vector, List<Position>> findHits(final boolean shootUpwardsOnly) {
        final Map<Vector, List<Position>> hits = new HashMap<>();
        final Integer minY = shootUpwardsOnly ? 0 : this.target_bl.getY();
        for (int y = 300; y >= minY; y--) {
            for (int x = 1; x <= this.target_ur.getX(); x++) {
                final List<Position> trajectory = new ArrayList<>();
                final Vector velocity = Vector.of(x, y);
                if (shoot(velocity, p -> {
                    trajectory.add(p);
                })) {
                    hits.put(velocity, trajectory);
                }
            }
        }
        return hits;
    }
    
    @Override
    public Integer solvePart1() {
        return findHits(true).values().stream()
            .flatMap(List::stream)
            .mapToInt(Position::getY)
            .max().orElseThrow();
    }
    
    @Override
    public Integer solvePart2() {
        return findHits(false).size();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_17.create(TEST).solvePart1() == 45;
        assert AoC2021_17.create(TEST).solvePart2() == 112;

        final Puzzle puzzle = Aocd.puzzle(2021, 17);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_17.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_17.create(puzzle.getInputData()).solvePart2())
        );
    }

    private static final List<String> TEST = splitLines(
        "target area: x=20..30, y=-10..-5"
    );
}
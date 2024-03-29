import static java.util.stream.Collectors.toList;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aoc.geometry3d.Position3D;
import com.github.pareronia.aoc.geometry3d.Vector3D;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2017_11 extends AoCBase {
    
    private static final Position3D ORIGIN = Position3D.of(0, 0, 0);
    private static final Map<String, Vector3D> HEADING = Map.of(
        "n",  Vector3D.of( 0, -1,  1),
        "ne", Vector3D.of( 1, -1,  0),
        "se", Vector3D.of( 1,  0, -1),
        "s",  Vector3D.of( 0,  1, -1),
        "sw", Vector3D.of(-1,  1,  0),
        "nw", Vector3D.of(-1,  0,  1)
    );

    final List<Vector3D> path;

    private AoC2017_11(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.path = Arrays.stream(inputs.get(0).split(","))
            .map(HEADING::get)
            .collect(toList());
    }

    public static AoC2017_11 create(final List<String> input) {
        return new AoC2017_11(input, false);
    }

    public static AoC2017_11 createDebug(final List<String> input) {
        return new AoC2017_11(input, true);
    }
    
    private Deque<Position3D> positions() {
        final Deque<Position3D> positions = new ArrayDeque<>(List.of(ORIGIN));
        this.path.stream()
            .map(v -> positions.getLast().translate(v))
            .forEach(positions::add);
        return positions;
    }
    
    private int steps(final Position3D p) {
        return p.manhattanDistance(ORIGIN) / 2;
    }
    
    @Override
    public Integer solvePart1() {
        return steps(positions().getLast());
    }
    
    @Override
    public Integer solvePart2() {
        return positions().stream().mapToInt(this::steps).max().getAsInt();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_11.createDebug(TEST1).solvePart1().equals(3);
        assert AoC2017_11.createDebug(TEST2).solvePart1().equals(0);
        assert AoC2017_11.createDebug(TEST3).solvePart1().equals(2);
        assert AoC2017_11.createDebug(TEST4).solvePart1().equals(3);

        final Puzzle puzzle = Aocd.puzzle(2017, 11);
        final List<String> input = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", create(input)::solvePart1),
            () -> lap("Part 2", create(input)::solvePart2)
        );
    }
    
    private static final List<String> TEST1 = splitLines("ne,ne,ne");
    private static final List<String> TEST2 = splitLines("ne,ne,sw,sw");
    private static final List<String> TEST3 = splitLines("ne,ne,s,s");
    private static final List<String> TEST4 = splitLines("se,sw,se,sw,sw");
}

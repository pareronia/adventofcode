import com.github.pareronia.aoc.geometry3d.Position3D;
import com.github.pareronia.aoc.geometry3d.Vector3D;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2017_11 extends SolutionBase<List<Position3D>, Integer, Integer> {

    private static final Position3D ORIGIN = Position3D.of(0, 0, 0);
    private static final Map<String, Vector3D> HEADING =
            Map.of(
                    "n", Vector3D.of(0, -1, 1),
                    "ne", Vector3D.of(1, -1, 0),
                    "se", Vector3D.of(1, 0, -1),
                    "s", Vector3D.of(0, 1, -1),
                    "sw", Vector3D.of(-1, 1, 0),
                    "nw", Vector3D.of(-1, 0, 1));

    private AoC2017_11(final boolean debug) {
        super(debug);
    }

    public static AoC2017_11 create() {
        return new AoC2017_11(false);
    }

    public static AoC2017_11 createDebug() {
        return new AoC2017_11(true);
    }

    @Override
    protected List<Position3D> parseInput(final List<String> inputs) {
        final List<Position3D> positions = new ArrayList<>(List.of(ORIGIN));
        Arrays.stream(inputs.getFirst().split(","))
                .map(HEADING::get)
                .map(v -> positions.getLast().translate(v))
                .forEach(positions::add);
        return positions;
    }

    private int steps(final Position3D p) {
        return p.manhattanDistance(ORIGIN) / 2;
    }

    @Override
    public Integer solvePart1(final List<Position3D> positions) {
        return steps(positions.getLast());
    }

    @Override
    public Integer solvePart2(final List<Position3D> positions) {
        return positions.stream().mapToInt(this::steps).max().getAsInt();
    }

    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    @Samples({
        @Sample(method = "part1", input = "ne,ne,ne", expected = "3"),
        @Sample(method = "part1", input = "ne,ne,sw,sw", expected = "0"),
        @Sample(method = "part1", input = "ne,ne,s,s", expected = "2"),
        @Sample(method = "part1", input = "se,sw,se,sw,sw", expected = "3"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }
}

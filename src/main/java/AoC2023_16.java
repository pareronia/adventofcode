import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import com.github.pareronia.aoc.AssertUtils;
import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aoc.graph.BFS;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_16
        extends SolutionBase<AoC2023_16.Contraption, Integer, Integer> {
    
    private AoC2023_16(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_16 create() {
        return new AoC2023_16(false);
    }
    
    public static AoC2023_16 createDebug() {
        return new AoC2023_16(true);
    }
    
    @Override
    protected Contraption parseInput(final List<String> inputs) {
        return new Contraption(CharGrid.from(inputs));
    }

    @Override
    public Integer solvePart1(final Contraption contraption) {
        return contraption.getInitialEnergy();
    }
    
    @Override
    public Integer solvePart2(final Contraption contraption) {
        return contraption.getMaximalEnergy();
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "46"),
        @Sample(method = "part2", input = TEST, expected = "51"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_16.create().run();
    }
    
    record Contraption(CharGrid grid) {
        
        record Beam(Cell cell, Direction dir) { }
    
        public int getInitialEnergy() {
            return getEnergised(new Beam(Cell.at(0, 0), Direction.RIGHT));
        }
        
        public int getMaximalEnergy() {
            return Stream.of(
                grid.rowIndices().intStream()
                    .mapToObj(row -> new Beam(Cell.at(row, 0), Direction.RIGHT)),
                grid.rowIndices().intStream()
                    .mapToObj(row -> new Beam(Cell.at(row, grid.getWidth() - 1), Direction.LEFT)),
                grid.colIndices().intStream()
                    .mapToObj(col -> new Beam(Cell.at(0, col), Direction.DOWN)),
                grid.colIndices().intStream()
                    .mapToObj(col -> new Beam(Cell.at(grid.getHeight() - 1, col), Direction.UP))
            ).reduce(Stream.empty(), Stream::concat)
            .mapToInt(this::getEnergised)
            .max().getAsInt();
        }

        private Stream<Beam> stream(final Beam beam, final Direction...dirs) {
            return Arrays.stream(dirs)
                .map(d -> new Beam(beam.cell.at(d), d))
                .filter(t -> grid.isInBounds(t.cell));
        }
        
        private int getEnergised(final Beam initialBeam) {
            final Function<Beam, Stream<Beam>> adjacent = beam -> {
                final char val = grid.getValue(beam.cell);
                if (val == '.'
                    || val == '|' && beam.dir.isVertical()
                    || val == '-' && beam.dir.isHorizontal()) {
                    return stream(beam, beam.dir);
                } else if (val == '/' && beam.dir.isHorizontal()) {
                    return stream(beam, beam.dir.turn(Turn.LEFT));
                } else if (val == '/' && beam.dir.isVertical()) {
                    return stream(beam, beam.dir.turn(Turn.RIGHT));
                } else if (val == '\\' && beam.dir.isHorizontal()) {
                    return stream(beam, beam.dir.turn(Turn.RIGHT));
                } else if (val == '\\' && beam.dir.isVertical()) {
                    return stream(beam, beam.dir.turn(Turn.LEFT));
                } else if (val == '|' && beam.dir.isHorizontal()) {
                    return stream(beam, Direction.UP, Direction.DOWN);
                } else if (val == '-' && beam.dir.isVertical()) {
                    return stream(beam, Direction.LEFT, Direction.RIGHT);
                }
                throw AssertUtils.unreachable();
            };

            final Set<Beam> energised = BFS.floodFill(initialBeam, adjacent);
            return (int) energised.stream().map(Beam::cell).distinct().count();
        }
    }

    private static final String TEST = """
            .|...\\....
            |.-.\\.....
            .....|-...
            ........|.
            ..........
            .........\\
            ..../.\\\\..
            .-.-/..|..
            .|....-|.\\
            ..//.|....
            """;
}

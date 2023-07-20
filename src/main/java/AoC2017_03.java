import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;

public final class AoC2017_03 extends AoCBase {

    private final transient Integer input;

    private AoC2017_03(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = Integer.valueOf(inputs.get(0));
    }

    public static AoC2017_03 create(final List<String> input) {
        return new AoC2017_03(input, false);
    }

    public static AoC2017_03 createDebug(final List<String> input) {
        return new AoC2017_03(input, true);
    }
    
    @RequiredArgsConstructor(staticName = "of")
    private static final class HeadingAndPeriod {
        private final Heading heading;
        private final int period;
    }

    private static class CoordinateSupplier implements Supplier<Position> {
        private final Function<Integer, HeadingAndPeriod>
                headingsAndPeriods = new Function<>() {
                    private final List<Headings> directions = List.of(
                            Headings.EAST, Headings.NORTH,
                            Headings.WEST, Headings.SOUTH);
                    private final int[] periods = { 1, 1, 2, 2 };

                    @Override
                    public HeadingAndPeriod apply(final Integer t) {
                        final int idx = t % 4;
                        final int period = periods[idx];
                        periods[idx] = period + 2;
                        final Headings pair = directions.get(idx);
                        return HeadingAndPeriod.of(pair.get(), period);
                    }
        };
        private int x;
        private int y;
        private int k;
        private HeadingAndPeriod headingAndPeriod
                = headingsAndPeriods.apply(k);
        private int j;

        @Override
        public Position get() {
            if (j == headingAndPeriod.period) {
                k++;
                headingAndPeriod = headingsAndPeriods.apply(k);
                j = 0;
            }
            x += headingAndPeriod.heading.getX();
            y += headingAndPeriod.heading.getY();
            j++;
            return Position.of(x,  y);
        }
    }

    @Override
    public Integer solvePart1() {
        if (this.input == 1) {
            return 0;
        }
        
        final CoordinateSupplier supplier = new CoordinateSupplier();
        int i = 1;
        while (i < this.input) {
            i++;
            final Position position = supplier.get();
            if (i == this.input) {
                return position.manhattanDistance();
            }
        }
        
        throw new IllegalStateException("Unsolvable");
    }

    @Override
    public Integer solvePart2() {
        if (this.input == 1) {
            return 1;
        }
        
        final Map<Position, Integer> squares = new HashMap<>();
        squares.put(Position.of(0, 0), 1);
        final CoordinateSupplier supplier = new CoordinateSupplier();
        while (true) {
            final Position position = supplier.get();
            int value = 0;
            for (final Headings d : Headings.OCTANTS) {
                final Position neighbour = Position.of(
                        position.getX() + d.getX(),
                        position.getY() + d.getY());
                value += squares.getOrDefault(neighbour, 0);
            }
            squares.put(position, value);
            if (value > this.input) {
                return value;
            }
        }
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_03.createDebug(splitLines("1")).solvePart1() == 0;
        assert AoC2017_03.createDebug(splitLines("12")).solvePart1() == 3;
        assert AoC2017_03.createDebug(splitLines("23")).solvePart1() == 2;
        assert AoC2017_03.createDebug(splitLines("1024")).solvePart1() == 31;
        assert AoC2017_03.createDebug(splitLines("1")).solvePart2() == 1;
        assert AoC2017_03.createDebug(splitLines("2")).solvePart2() == 4;
        assert AoC2017_03.createDebug(splitLines("3")).solvePart2() == 4;
        assert AoC2017_03.createDebug(splitLines("4")).solvePart2() == 5;
        assert AoC2017_03.createDebug(splitLines("5")).solvePart2() == 10;

        final Puzzle puzzle = Aocd.puzzle(2017, 3);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2017_03.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2017_03.create(inputData)::solvePart2)
        );
    }
}

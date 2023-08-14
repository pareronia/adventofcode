import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Position;
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
    private static final class DirectionAndPeriod {
        private final Direction direction;
        private final int period;
    }

    private static class CoordinateSupplier implements Supplier<Position> {
        private final Function<Integer, DirectionAndPeriod>
                directionsAndPeriods = new Function<>() {
                    private final List<Direction> directions = List.of(
                            Direction.RIGHT, Direction.UP,
                            Direction.LEFT, Direction.DOWN);
                    private final int[] periods = { 1, 1, 2, 2 };

                    @Override
                    public DirectionAndPeriod apply(final Integer t) {
                        final int idx = t % 4;
                        final int period = periods[idx];
                        periods[idx] = period + 2;
                        final Direction direction = directions.get(idx);
                        return DirectionAndPeriod.of(direction, period);
                    }
        };
        private int x;
        private int y;
        private int k;
        private DirectionAndPeriod directionAndPeriod
                = directionsAndPeriods.apply(k);
        private int j;

        @Override
        public Position get() {
            if (j == directionAndPeriod.period) {
                k++;
                directionAndPeriod = directionsAndPeriods.apply(k);
                j = 0;
            }
            x += directionAndPeriod.direction.getX();
            y += directionAndPeriod.direction.getY();
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
            final Iterator<Position> it = position.allNeighbours().iterator();
            while (it.hasNext()) {
                value += squares.getOrDefault(it.next(), 0);
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

import com.github.pareronia.aoc.AssertUtils;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public final class AoC2017_03 extends SolutionBase<Integer, Integer, Integer> {

    private AoC2017_03(final boolean debug) {
        super(debug);
    }

    public static AoC2017_03 create() {
        return new AoC2017_03(false);
    }

    public static AoC2017_03 createDebug() {
        return new AoC2017_03(true);
    }

    @Override
    protected Integer parseInput(final List<String> inputs) {
        return Integer.valueOf(inputs.getFirst());
    }

    record DirectionAndPeriod(Direction direction, int period) {
        public static DirectionAndPeriod of(final Direction direction, final int period) {
            return new DirectionAndPeriod(direction, period);
        }
    }

    private static class CoordinateSupplier implements Supplier<Position> {
        private final Function<Integer, DirectionAndPeriod> directionsAndPeriods =
                new Function<>() {
                    private final List<Direction> directions =
                            List.of(
                                    Direction.RIGHT, Direction.UP,
                                    Direction.LEFT, Direction.DOWN);
                    private final int[] periods = {1, 1, 2, 2};

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
        private DirectionAndPeriod directionAndPeriod = directionsAndPeriods.apply(k);
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
            return Position.of(x, y);
        }
    }

    @Override
    public Integer solvePart1(final Integer input) {
        if (input == 1) {
            return 0;
        }

        final CoordinateSupplier supplier = new CoordinateSupplier();
        int i = 1;
        while (i < input) {
            i++;
            final Position position = supplier.get();
            if (i == input) {
                return position.manhattanDistance();
            }
        }

        throw AssertUtils.unreachable();
    }

    @Override
    public Integer solvePart2(final Integer input) {
        if (input == 1) {
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
            if (value > input) {
                return value;
            }
        }
    }

    @Samples({
        @Sample(method = "part1", input = "1", expected = "0"),
        @Sample(method = "part1", input = "12", expected = "3"),
        @Sample(method = "part1", input = "23", expected = "2"),
        @Sample(method = "part1", input = "1024", expected = "31"),
        @Sample(method = "part2", input = "1", expected = "1"),
        @Sample(method = "part2", input = "2", expected = "4"),
        @Sample(method = "part2", input = "3", expected = "4"),
        @Sample(method = "part2", input = "4", expected = "5"),
        @Sample(method = "part2", input = "5", expected = "10"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }
}

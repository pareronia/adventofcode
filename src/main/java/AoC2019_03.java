import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.CollectionUtils.intersection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

import com.github.pareronia.aocd.Aocd;

import lombok.Value;

public class AoC2019_03 extends AoCBase {
    
    private static final Coordinate ORIGIN = Coordinate.of(0, 0);
    
    private final Wire wire1;
    private final Wire wire2;

    private AoC2019_03(List<String> input, boolean debug) {
        super(debug);
        final Pair<Wire, Wire> wires = parse(input);
        this.wire1 = wires.getOne();
        this.wire2 = wires.getTwo();
    }

    public static AoC2019_03 create(List<String> input) {
        return new AoC2019_03(input, false);
    }

    public static AoC2019_03 createDebug(List<String> input) {
        return new AoC2019_03(input, true);
    }

    private List<Pair<String, Integer>> toInstructions(String input) {
        return Stream.of(input.split(","))
            .map(ins -> {
                final String direction = ins.substring(0, 1);
                final Integer amount = Integer.valueOf(ins.substring(1));
                return Tuples.pair(direction, amount);
            })
            .collect(toList());
    }

    private Wire toWire(List<Pair<String, Integer>> instructions) {
        final List<Coordinate> wireCoordinates = new ArrayList<>();
        Coordinate start = ORIGIN;
        for (final Pair<String, Integer> instruction : instructions) {
            Coordinate coord = null;
            for (int i = 1; i < instruction.getTwo() + 1; i++) {
                switch(instruction.getOne()) {
                case "R":
                    coord = Coordinate.of(start.x + i, start.y);
                    wireCoordinates.add(coord);
                    break;
                case "U":
                    coord = Coordinate.of(start.x, start.y + i);
                    wireCoordinates.add(coord);
                    break;
                case "L":
                    coord = Coordinate.of(start.x - i, start.y);
                    wireCoordinates.add(coord);
                    break;
                case "D":
                    coord = Coordinate.of(start.x, start.y - i);
                    wireCoordinates.add(coord);
                    break;
                default:
                    throw new RuntimeException("Invalid input");
                }
            }
            start = coord;
        }
        return new Wire(wireCoordinates);
    }
    
    private Pair<Wire,Wire> parse(List<String> inputs) {
        assert inputs.size() == 2;
        final Wire wire1 = toWire(toInstructions(inputs.get(0)));
        final Wire wire2 = toWire(toInstructions(inputs.get(1)));
        return Tuples.pair(wire1, wire2);
    }

    @Override
    public Integer solvePart1() {
        return intersection(wire1.asSet(), wire2.asSet()).stream()
                .map(c -> c.manhattanDistance(ORIGIN))
                .reduce(Integer.MAX_VALUE, (a, b) -> a < b ? a : b);
    }

    @Override
    public Integer solvePart2() {
        return intersection(wire1.asSet(), wire2.asSet()).stream()
                .map(c -> wire1.steps(c) + wire2.steps(c))
                .reduce(Integer.MAX_VALUE, (a, b) -> a < b ? a : b);
    }

    public static void main(String[] args) throws Exception {
        assert AoC2019_03.createDebug(TEST1).solvePart1() == 6;
        assert AoC2019_03.createDebug(TEST2).solvePart1() == 159;
        assert AoC2019_03.createDebug(TEST3).solvePart1() == 135;
        assert AoC2019_03.createDebug(TEST1).solvePart2() == 30;
        assert AoC2019_03.createDebug(TEST2).solvePart2() == 610;
        assert AoC2019_03.createDebug(TEST3).solvePart2() == 410;

        final List<String> input = Aocd.getData(2019, 3);
        lap("Part 1", () -> AoC2019_03.create(input).solvePart1());
        lap("Part 2", () -> AoC2019_03.create(input).solvePart2());
    }

    private static final List<String> TEST1 = splitLines(
            "R8,U5,L5,D3\r\n" +
            "U7,R6,D4,L4"
    );
    private static final List<String> TEST2 = splitLines(
            "R75,D30,R83,U83,L12,D49,R71,U7,L72\r\n" +
            "U62,R66,U55,R34,D71,R55,D58,R83"
    );
    private static final List<String> TEST3 = splitLines(
            "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51\r\n" +
            "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"
    );
    
    @Value
    private static class Coordinate {
        private final Integer x;
        private final Integer y;
        
        public static Coordinate of(Integer x, Integer y) {
            return new Coordinate(x, y);
        }
        
        public Integer manhattanDistance(Coordinate from) {
            return Math.abs(this.x - from.x) + Math.abs(this.y - from.y);
        }
    }
    
    @Value
    private static final class Wire {
       private final List<Coordinate> coordinates;
       
       public Set<Coordinate> asSet() {
           return this.coordinates.stream().collect(toSet());
       }
       
       public Integer steps(Coordinate coord) {
           return this.getCoordinates().indexOf(coord) + 1;
       }
    }
}
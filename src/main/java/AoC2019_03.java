import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AoC2019_03 extends AoCBase {
    
    private static final Position ORIGIN = Position.of(0, 0);
    
    private final Wire wire1;
    private final Wire wire2;

    private AoC2019_03(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 2;
        this.wire1 = Wire.fromString(input.get(0));
        this.wire2 = Wire.fromString(input.get(1));
    }

    public static AoC2019_03 create(final List<String> input) {
        return new AoC2019_03(input, false);
    }

    public static AoC2019_03 createDebug(final List<String> input) {
        return new AoC2019_03(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        final Set<Position> coords1 = new HashSet<>(wire1.getCoordinates());
        return wire2.getCoordinates().stream()
                .filter(coords1::contains)
                .mapToInt(c -> c.manhattanDistance(ORIGIN))
                .min().getAsInt();
    }

    @Override
    public Integer solvePart2() {
        final Set<Position> coords1 = new HashSet<>(wire1.getCoordinates());
        return wire2.getCoordinates().stream()
                .filter(coords1::contains)
                .mapToInt(c -> wire1.steps(c) + wire2.steps(c))
                .min().getAsInt();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2019_03.createDebug(TEST1).solvePart1() == 6;
        assert AoC2019_03.createDebug(TEST2).solvePart1() == 159;
        assert AoC2019_03.createDebug(TEST3).solvePart1() == 135;
        assert AoC2019_03.createDebug(TEST1).solvePart2() == 30;
        assert AoC2019_03.createDebug(TEST2).solvePart2() == 610;
        assert AoC2019_03.createDebug(TEST3).solvePart2() == 410;

        final Puzzle puzzle = Aocd.puzzle(2019, 3);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2019_03.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2019_03.create(inputData)::solvePart2)
        );
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
    
    @RequiredArgsConstructor
    private static final class Wire {
        @Getter
        private final List<Position> coordinates;
        
        public static Wire fromString(final String string) {
            final List<Position> wireCoordinates = new ArrayList<>();
            Position start = ORIGIN;
            for (final Instruction instruction : toInstructions(string)) {
                Position coord = null;
                for (int i = 1; i < instruction.amount + 1; i++) {
                    coord = start.translate(instruction.direction, i);
                    wireCoordinates.add(coord);
                }
                start = coord;
            }
            return new Wire(wireCoordinates);
        }
       
        public Integer steps(final Position coord) {
            return this.getCoordinates().indexOf(coord) + 1;
        }

        private static List<Instruction> toInstructions(final String input) {
            return Stream.of(input.split(","))
                    .map(Instruction::fromString)
                    .collect(toList());
        }
    }
    
    @RequiredArgsConstructor
    private static final class Instruction {
        private final Direction direction;
        private final int amount;
        
        public static Instruction fromString(final String str) {
            final Direction direction = Direction.fromString(str.substring(0, 1));
            final int amount = Integer.parseInt(str.substring(1));
            return new Instruction(direction, amount);
        }
    }
}
import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Vector;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_09 extends AoCBase {
    
    private static final Map<String, Heading> MOVES = Map.of(
            "U", Headings.NORTH.get(),
            "D", Headings.SOUTH.get(),
            "L", Headings.WEST.get(),
            "R", Headings.EAST.get()
    );
    
    private final List<Heading> moves;
    
    private AoC2022_09(final List<String> input, final boolean debug) {
        super(debug);
        this.moves = input.stream()
            .flatMap(line -> {
                final String[] splits = line.split(" ");
                final Heading[] h = new Heading[Integer.parseInt(splits[1])];
                Arrays.fill(h, MOVES.get(splits[0]));
                return Arrays.stream(h);
            })
            .collect(toList());
    }
    
    public static final AoC2022_09 create(final List<String> input) {
        return new AoC2022_09(input, false);
    }

    public static final AoC2022_09 createDebug(final List<String> input) {
        return new AoC2022_09(input, true);
    }
    
    void printRope(final Position[] rope) {
        log(Arrays.stream(rope)
            .map(r -> String.format("(%d,%d)", r.getX(), r.getY()))
            .collect(joining(" ")));
    }
    
    private Position catchup(final Position head, final Position tail) {
        final int dx = head.getX() - tail.getX();
        final int dy = head.getY() - tail.getY();
        if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
            final Vector move = Vector.of(
                dx < 0 ? - 1 : dx > 0 ? 1 : 0,
                dy < 0 ? - 1 : dy > 0 ? 1 : 0);
            return tail.translate(move);
        }
        return tail;
    }

    private void moveRope(final Position[] rope, final Heading move) {
        rope[0] = rope[0].translate(move);
        range(1, rope.length, 1).forEach(j ->
                rope[j] = catchup(rope[j - 1], rope[j]));
        printRope(rope);
    }
    
    private int solve(final int size) {
        final Position[] rope = new Position[size];
        Arrays.fill(rope, Position.of(0, 0));
        final Set<Position> seen = this.moves.stream()
            .map(move -> {
                moveRope(rope, move);
                return rope[rope.length - 1];
            })
            .collect(toSet());
        log(seen.size());
        log(seen);
        return seen.size();
    }
    
    @Override
    public Integer solvePart1() {
        return solve(2);
    }

    @Override
    public Integer solvePart2() {
        return solve(10);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_09.createDebug(TEST1).solvePart1() == 13;
        assert AoC2022_09.createDebug(TEST1).solvePart2() == 1;
        assert AoC2022_09.createDebug(TEST2).solvePart2() == 36;

        final Puzzle puzzle = Aocd.puzzle(2022, 9);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_09.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_09.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST1 = splitLines(
        "R 4\r\n" +
        "U 4\r\n" +
        "L 3\r\n" +
        "D 1\r\n" +
        "R 4\r\n" +
        "D 1\r\n" +
        "L 5\r\n" +
        "R 2"
    );
    private static final List<String> TEST2 = splitLines(
        "R 5\r\n" +
        "U 8\r\n" +
        "L 8\r\n" +
        "D 3\r\n" +
        "R 17\r\n" +
        "D 10\r\n" +
        "L 25\r\n" +
        "U 20"
    );
}

import static java.util.stream.Collectors.toSet;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.OCR;
import com.github.pareronia.aoc.geometry.Draw;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aoc.navigation.Heading;
import com.github.pareronia.aoc.navigation.NavigationWithHeading;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2019_11 extends AoCBase {
    
    private static final long BLACK = 0;
    private static final long WHITE = 1;
    private static final long LEFT = 0;
    private static final char FILL = '\u2592';
    private static final char EMPTY = ' ';
    
    private final List<Long> program;
    
    private AoC2019_11(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 1;
        this.program = IntCode.parse(input.get(0));
    }

    public static AoC2019_11 create(final List<String> input) {
        return new AoC2019_11(input, false);
    }

    public static AoC2019_11 createDebug(final List<String> input) {
        return new AoC2019_11(input, true);
    }

    private PaintJob paint(final long start) {
        final Set<Position> white = new HashSet<>();
        final NavigationWithHeading nav
            = new NavigationWithHeading(Position.of(0, 0), Heading.NORTH);
        final IntCode intCode = new IntCode(this.program, false);
        final Deque<Long> input = new ArrayDeque<>(List.of(start));
        final Deque<Long> output = new ArrayDeque<>();
        do {
            intCode.runTillInputRequired(input, output);
            if (output.pop() == WHITE) {
                white.add(nav.getPosition());
            } else {
                white.remove(nav.getPosition());
            }
            nav.turn(output.pop() == LEFT ? Turn.LEFT : Turn.RIGHT);
            nav.forward(1);
            input.add(white.contains(nav.getPosition()) ? 1L : 0L);
            intCode.runTillInputRequired(input, output);
        } while (!intCode.isHalted());
        final Set<Position> visited = nav.getVisitedPositions(false).stream()
                .distinct().collect(toSet());
        return new PaintJob(visited, white);
    }
    
    @Override
    public Integer solvePart1() {
        return paint(BLACK).visited.size();
    }
    
    @Override
    public String solvePart2() {
        final List<String> drawing = Draw.draw(paint(WHITE).white, FILL, EMPTY);
        drawing.forEach(this::log);
        return OCR.convert6(CharGrid.from(drawing), FILL, EMPTY);
    }

    public static void main(final String[] args) throws Exception {
        final Puzzle puzzle = Aocd.puzzle(2019, 11);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2019_11.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2019_11.createDebug(inputData)::solvePart2)
        );
    }
    
    record PaintJob(Set<Position> visited, Set<Position> white) { }
}
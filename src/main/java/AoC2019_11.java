import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.OCR;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aoc.navigation.NavigationWithHeading;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;

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
            = new NavigationWithHeading(Position.of(0, 0), Headings.NORTH.get());
        final IntCode intCode = new IntCode(false);
        final Deque<Long> input = new ArrayDeque<>(List.of(start));
        final Deque<Long> output = new ArrayDeque<>();
        intCode.runTillInputRequired(this.program, input, output);
        while (!intCode.isHalted()) {
            if (output.pop() == WHITE) {
                white.add(nav.getPosition());
            } else {
                white.remove(nav.getPosition());
            }
            if (output.pop() == LEFT) {
                nav.left(90);
            } else {
                nav.right(90);
            }
            nav.forward(1);
            input.add(white.contains(nav.getPosition()) ? 1L : 0L);
            intCode.continueTillInputRequired(input, output);
        }
        final Set<Position> visited = nav.getVisitedPositions(false).stream()
                .distinct().collect(toSet());
        return new PaintJob(visited, white);
    }
    
    private List<String> draw(final Set<Position> positions, final char fill, final char empty) {
        final IntSummaryStatistics statsX = positions.stream()
                .mapToInt(Position::getX).summaryStatistics();
        final IntSummaryStatistics statsY = positions.stream()
                .mapToInt(Position::getY).summaryStatistics();
        final int width = statsX.getMax() + 2;
        return IntStream.rangeClosed(statsY.getMin(), statsY.getMax()).mapToObj(
                y -> IntStream.range(statsX.getMin(), width).mapToObj(
                        x -> positions.contains(Position.of(x, y)) ? fill : empty)
                        .collect(toAString()))
                .collect(toList());
    }
    
    @Override
    public Integer solvePart1() {
        return paint(BLACK).visited.size();
    }
    
    @Override
    public String solvePart2() {
        final Set<Position> white = paint(WHITE).white;
        final List<String> drawing = draw(white, FILL, EMPTY);
        Collections.reverse(drawing);
        drawing.forEach(this::log);
        return OCR.convert6(Grid.from(drawing), FILL, EMPTY);
    }

    public static void main(final String[] args) throws Exception {
        final Puzzle puzzle = Aocd.puzzle(2019, 11);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2019_11.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2019_11.createDebug(inputData)::solvePart2)
        );
    }
    
    @RequiredArgsConstructor
    private static final class PaintJob {
        private final Set<Position> visited;
        private final Set<Position> white;
    }
}
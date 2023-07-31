import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static java.util.stream.Collectors.toList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2019_13 extends AoCBase {
    
    private final List<Long> program;
    
    private AoC2019_13(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 1;
        this.program = IntCode.parse(input.get(0));
    }

    public static AoC2019_13 create(final List<String> input) {
        return new AoC2019_13(input, false);
    }

    public static AoC2019_13 createDebug(final List<String> input) {
        return new AoC2019_13(input, true);
    }
    
    @Override
    public Long solvePart1() {
        final IntCode intCode = new IntCode(this.program, this.debug);
        final Deque<Long> input = new ArrayDeque<>();
        final Deque<Long> output = new ArrayDeque<>();
        intCode.run(input, output);
        final List<Long> list = output.stream().collect(toList());
        return range(2, list.size(), 3).intStream()
                .mapToLong(i -> list.get(i))
                .filter(o -> o == 2L)
                .count();
   }
    
    @Override
    public Long solvePart2() {
        final List<Long> quarters = new ArrayList<>();
        quarters.add(2L);
        quarters.addAll(this.program.subList(1, this.program.size()));
        final IntCode intCode = new IntCode(quarters, this.debug);
        final Deque<Long> input = new ArrayDeque<>();
        final Deque<Long> output = new ArrayDeque<>();
        final List<Long> buffer = new ArrayList<>();
        Long ball = null, paddle = null;
        long score = 0;
        
        while (true) {
            intCode.runTillHasOutput(input, output);
            if (intCode.isHalted()) {
                break;
            }
            buffer.add(output.pop());
            if (buffer.size() < 3) {
                continue;
            }
            
            final long x = buffer.get(0);
            final long y = buffer.get(1);
            final long id = buffer.get(2);
            log(buffer);
            buffer.clear();
            if (x == -1 && y == 0) {
                score = id;
            } else if (id == 3) {
                paddle = x;
            } else if (id == 4) {
                ball = x;
            }
            if (ball != null && paddle != null) {
                final long joystick = Long.compare(ball, paddle);
                log(" -> " + joystick);
                input.add(joystick);
                ball = null;
            }
        }
        return score;
    }

    public static void main(final String[] args) throws Exception {
        final Puzzle puzzle = Aocd.puzzle(2019, 13);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2019_13.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2019_13.create(inputData)::solvePart2)
        );
    }
}
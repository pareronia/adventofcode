import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.IterTools.permutations;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2019_07 extends AoCBase {
    
    private final List<Long> program;
    
    private AoC2019_07(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 1;
        this.program = IntCode.parse(input.get(0));
    }

    public static AoC2019_07 create(final List<String> input) {
        return new AoC2019_07(input, false);
    }

    public static AoC2019_07 createDebug(final List<String> input) {
        return new AoC2019_07(input, true);
    }
    
    private Deque<Long> deque(final Long... items) {
        return new ArrayDeque<>(Arrays.asList(items));
    }
    
    @SuppressWarnings("unchecked")
    private Long run(final List<Integer> phaseSettings) {
        assert phaseSettings.size() == 5;
        final Deque<Long>[] queues = range(5).intStream()
                .mapToLong(phaseSettings::get)
                .mapToObj(this::deque)
                .toArray(Deque[]::new);
        queues[0].add(0L);
        final IntCode[] intCode = range(5).intStream()
                .mapToObj(i -> new IntCode(false))
                .toArray(IntCode[]::new);
        range(5).forEach(i -> {
            intCode[i].runTillInputRequired(this.program, queues[i], queues[(i + 1) % 5]);
        });
        while (!Arrays.stream(intCode).allMatch(IntCode::isHalted)) {
            range(5).forEach(i -> {
                intCode[i].continueTillInputRequired(queues[i], queues[(i + 1) % 5]);
            });
        }
        return queues[0].getLast();
    }
    
    private Long solve(final Iterable<Integer> range) {
        return permutations(range)
                .mapToLong(this::run)
                .max().getAsLong();
    }

    @Override
    public Long solvePart1() {
        return solve(range(5));
    }
    
    @Override
    public Long solvePart2() {
        return solve(Set.of(5, 6, 7, 8, 9));
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2019_07.createDebug(TEST1).run(List.of(4, 3, 2, 1, 0)) == 43210;
        assert AoC2019_07.createDebug(TEST2).run(List.of(0, 1, 2, 3, 4)) == 54321;
        assert AoC2019_07.createDebug(TEST3).run(List.of(1, 0, 4, 3, 2)) == 65210;
        assert AoC2019_07.createDebug(TEST4).run(List.of(9, 8, 7, 6, 5)) == 139629729;
        assert AoC2019_07.createDebug(TEST5).run(List.of(9, 7, 8, 5, 6)) == 18216;
        
        final Puzzle puzzle = Aocd.puzzle(2019, 7);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2019_07.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2019_07.create(inputData)::solvePart2)
        );
    }
    
    private static final List<String> TEST1 = splitLines(
        "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0"
    );
    private static final List<String> TEST2 = splitLines(
        "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0"
    );
    private static final List<String> TEST3 = splitLines(
        "3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33," +
        "1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0"
    );
    private static final List<String> TEST4 = splitLines(
        "3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26," +
        "27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5"
    );
    private static final List<String> TEST5 = splitLines(
        "3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54," +
        "-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4," +
        "53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10"
    );
}
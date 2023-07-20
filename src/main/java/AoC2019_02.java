import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aocd.Puzzle;

public class AoC2019_02 extends AoCBase {
    
    private final List<Integer> program;
    
    private AoC2019_02(final List<String> input, final boolean debug) {
        super(debug);
        assert input.size() == 1;
        program = Stream.of(input.get(0).split(",")).map(Integer::valueOf).collect(toList());
    }

    public static AoC2019_02 create(final List<String> input) {
        return new AoC2019_02(input, false);
    }

    public static AoC2019_02 createDebug(final List<String> input) {
        return new AoC2019_02(input, true);
    }
    
    private Integer runProgram(final Integer noun, final Integer verb) {
        final List<Integer> program = new ArrayList<>(this.program);
        program.set(1, noun);
        program.set(2, verb);
        new IntCode(this.debug).run(program);
        return program.get(0);
    }
    
    @Override
    public Integer solvePart1() {
        return runProgram(12, 2);
    }

    @Override
    public Integer solvePart2() {
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb ++) {
                if (runProgram(noun, verb) == 19690720) {
                    return 100 * noun + verb;
                }
            }
        }
        throw new IllegalStateException("Unsolved");
    }

    public static void main(final String[] args) throws Exception {
        final Puzzle puzzle = Puzzle.create(2019, 2);
        final List<String> input = puzzle.getInputData();
        puzzle.check(
           () -> lap("Part 1", AoC2019_02.create(input)::solvePart1),
           () -> lap("Part 2", AoC2019_02.create(input)::solvePart2)
	    );
    }
}
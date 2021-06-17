import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aoc.intcode.IntCode;
import com.github.pareronia.aocd.Aocd;

public class AoC2019_05 extends AoCBase {
    
    private final List<Integer> program;
    
    private AoC2019_05(List<String> input, boolean debug) {
        super(debug);
        assert input.size() == 1;
        program = Stream.of(input.get(0).split(",")).map(Integer::valueOf).collect(toList());
    }

    public static AoC2019_05 create(List<String> input) {
        return new AoC2019_05(input, false);
    }

    public static AoC2019_05 createDebug(List<String> input) {
        return new AoC2019_05(input, true);
    }
    
    @Override
    public Integer solvePart1() {
        final List<Integer> program = new ArrayList<>(this.program);
        final IntCode intCode = new IntCode();
        intCode.run(program, 1);
        return intCode.getOutput();
    }
    
    @Override
    public Integer solvePart2() {
        final List<Integer> program = new ArrayList<>(this.program);
        final IntCode intCode = new IntCode();
        intCode.run(program, 5);
        return intCode.getOutput();
    }

    public static void main(String[] args) throws Exception {
        final List<String> input = Aocd.getData(2019, 5);
        lap("Part 1", () -> AoC2019_05.create(input).solvePart1());
        lap("Part 2", () -> AoC2019_05.create(input).solvePart2());
    }
}
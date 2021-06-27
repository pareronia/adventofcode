import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.github.pareronia.aoc.assembunny.Assembunny;
import com.github.pareronia.aoc.assembunny.Assembunny.AssembunnyInstruction;
import com.github.pareronia.aoc.vm.Program;
import com.github.pareronia.aoc.vm.VirtualMachine;
import com.github.pareronia.aoc.vm.VirtualMachine.InfiniteLoopException;
import com.github.pareronia.aocd.Aocd;

public final class AoC2016_25 extends AoCBase {
    
    private final transient List<AssembunnyInstruction> instructions;
    
    private AoC2016_25(final List<String> inputs, final boolean debug) {
        super(debug);
        log(inputs);
        this.instructions = Assembunny.parse(inputs);
    }
    
    public static AoC2016_25 create(final List<String> input) {
        return new AoC2016_25(input, false);
    }
    
    public static AoC2016_25 createDebug(final List<String> input) {
        return new AoC2016_25(input, true);
    }
    
    private Integer solveCheat() {
        final List<Integer> values = this.instructions.stream()
                .filter(i -> "cpy".equals(i.getOperator()))
                .map(i -> i.getOperands().get(0))
                .filter(StringUtils::isNumeric)
                .map(Integer::valueOf)
                .collect(toList());
        return 2730 - values.get(0) * 182;
    }
    
    private List<Long> runProgram(final Integer initA) {
        final List<Long> output = new ArrayList<>();
        final Program program = new Program(
                Assembunny.translate(this.instructions),
                6_000,
                output::add);
        program.setRegisterValue("a", initA.longValue());
        try {
            new VirtualMachine().runProgram(program);
        } catch (final InfiniteLoopException e) {
            log(() -> program.getCycles());
            log(() -> output);
        }
        return output;
    }
    
    private Integer solveVM() {
        int n = 0;
        while(true) {
            log(n);
            final List<Long> output = runProgram(n);
            if (Stream.iterate(0, i -> i < output.size(), i -> i + 1)
                        .allMatch(i -> i % 2 == output.get(i))) {
                return n;
            }
            n++;
        }
    }
    
    @Override
    public Integer solvePart1() {
        return solveVM();
    }
    
    @Override
    public Integer solvePart2() {
        return 0;
    }
    
    public static void main(final String[] args) throws Exception {
        assert AoC2016_25.createDebug(TEST).solveCheat() == 182;

        final List<String> input = Aocd.getData(2016, 25);
        lap("Part 1", () -> AoC2016_25.createDebug(input).solvePart1());
        lap("Part 2", () -> AoC2016_25.create(input).solvePart2());
    }

    private static final List<String> TEST = splitLines(
            "cpy a d\n" +
            "cpy 14 c\n" +
            "cpy 182 b\n" +
            "inc d\n" +
            "dec b\n" +
            "jnz b -2\n" +
            "dec c\n" +
            "jnz c -5\n" +
            "cpy d a\n" +
            "jnz 0 0\n" +
            "cpy a b\n" +
            "cpy 0 a\n" +
            "cpy 2 c\n" +
            "jnz b 2\n" +
            "jnz 1 6\n" +
            "dec b\n" +
            "dec c\n" +
            "jnz c -4\n" +
            "inc a\n" +
            "jnz 1 -7\n" +
            "cpy 2 b\n" +
            "jnz c 2\n" +
            "jnz 1 4\n" +
            "dec b\n" +
            "dec c\n" +
            "jnz 1 -4\n" +
            "jnz 0 0\n" +
            "out b\n" +
            "jnz a -19\n" +
            "jnz 1 -21"
    );
}
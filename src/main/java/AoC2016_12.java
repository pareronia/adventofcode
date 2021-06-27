import static java.util.stream.Collectors.toList;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.pareronia.aoc.FibonacciUtil;
import com.github.pareronia.aoc.assembunny.Assembunny;
import com.github.pareronia.aoc.assembunny.Assembunny.AssembunnyInstruction;
import com.github.pareronia.aoc.vm.Program;
import com.github.pareronia.aoc.vm.VirtualMachine;
import com.github.pareronia.aocd.Aocd;

public final class AoC2016_12 extends AoCBase {
    
    private final transient List<AssembunnyInstruction> instructions;
    
    private AoC2016_12(final List<String> inputs, final boolean debug) {
        super(debug);
        log(inputs);
        this.instructions = Assembunny.parse(inputs);
    }
    
    public static AoC2016_12 create(final List<String> input) {
        return new AoC2016_12(input, false);
    }
    
    public static AoC2016_12 createDebug(final List<String> input) {
        return new AoC2016_12(input, true);
    }
    
    private Integer solveVM(final Integer initC) {
        final Program program = new Program(Assembunny.translate(this.instructions));
        log(() -> program.getInstructions());
        program.setRegisterValue("c", initC.longValue());
        new VirtualMachine().runProgram(program);
        log(() -> program.getCycles());
        return program.getRegisters().get("a").intValue();
    }
    
    private int fibonacci(final int number) {
        return FibonacciUtil.binet(number).intValue();
    }
    
    private Integer solve(final Integer initC) {
        final List<Integer> values = this.instructions.stream()
                .filter(i -> "cpy".equals(i.getOperator()))
                .map(i -> i.getOperands().get(0))
                .filter(StringUtils::isNumeric)
                .map(Integer::valueOf)
                .collect(toList());
        log(() -> values);
        final int number = values.get(0) + values.get(1) + values.get(2)
                            + (initC == 0 ? 0 : 7);
        return fibonacci(number) + values.get(4) * values.get(5);
    }
    
    @Override
    public Integer solvePart1() {
        return solve(0);
    }
    
    @Override
    public Integer solvePart2() {
        return solve(1);
    }
    
    public static void main(final String[] args) throws Exception {
        assert AoC2016_12.createDebug(TEST1).solveVM(0) == 42;
        assert AoC2016_12.createDebug(TEST2).solveVM(0) == 318_003;
        assert AoC2016_12.createDebug(TEST2).solveVM(1) == 9_227_657;
        assert AoC2016_12.createDebug(TEST2).solve(0) == 318_003;
        assert AoC2016_12.createDebug(TEST2).solve(1) == 9_227_657;
        
        final List<String> input = Aocd.getData(2016, 12);
        lap("Part 1", () -> AoC2016_12.create(input).solvePart1());
        lap("Part 2", () -> AoC2016_12.create(input).solvePart2());
    }
    
    private static final List<String> TEST1 = splitLines(
            "cpy 41 a\n" +
            "inc a\n" +
            "inc a\n" +
            "dec a\n" +
            "jnz a 2\n" +
            "dec a"
    );
    private static final List<String> TEST2 = splitLines(
            "cpy 1 a\n" +
            "cpy 1 b\n" +
            "cpy 26 d\n" +
            "jnz c 2\n" +
            "jnz 1 5\n" +
            "cpy 7 c\n" +
            "inc d\n" +
            "dec c\n" +
            "jnz c -2\n" +
            "cpy a c\n" +
            "inc a\n" +
            "dec b\n" +
            "jnz b -2\n" +
            "cpy c b\n" +
            "dec d\n" +
            "jnz d -6\n" +
            "cpy 16 c\n" +
            "cpy 12 d\n" +
            "inc a\n" +
            "dec d\n" +
            "jnz d -2\n" +
            "dec c\n" +
            "jnz c -5"
    );
}
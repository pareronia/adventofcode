import static com.github.pareronia.aoc.StringOps.splitLines;

import java.util.List;

import com.github.pareronia.aoc.FibonacciUtil;
import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.assembunny.Assembunny;
import com.github.pareronia.aoc.assembunny.Assembunny.AssembunnyInstruction;
import com.github.pareronia.aoc.solution.SolutionBase;
import com.github.pareronia.aoc.vm.Program;
import com.github.pareronia.aoc.vm.VirtualMachine;

public final class AoC2016_12
        extends SolutionBase<List<AssembunnyInstruction>, Integer, Integer> {
    
    private AoC2016_12(final boolean debug) {
        super(debug);
    }
    
    public static AoC2016_12 create() {
        return new AoC2016_12(false);
    }
    
    public static AoC2016_12 createDebug() {
        return new AoC2016_12(true);
    }

    @Override
    protected List<AssembunnyInstruction> parseInput(final List<String> inputs) {
        return Assembunny.parse(inputs);
    }

    private Integer solveVM(
            final List<AssembunnyInstruction> instructions,
            final Integer initC
    ) {
        final Program program = new Program(Assembunny.translate(instructions));
        log(() -> program.getInstructions());
        program.setRegisterValue("c", initC.longValue());
        new VirtualMachine().runProgram(program);
        log(() -> program.getCycles());
        return program.getRegisters().get("a").intValue();
    }
    
    private int fibonacci(final int number) {
        return FibonacciUtil.binet(number).intValue();
    }
    
    private Integer solve(
            final List<AssembunnyInstruction> instructions,
            final Integer initC
    ) {
        final List<Integer> values = instructions.stream()
                .filter(i -> "cpy".equals(i.getOperator()))
                .map(i -> i.getOperands().get(0))
                .filter(StringUtils::isNumeric)
                .map(Integer::valueOf)
                .toList();
        log(() -> values);
        final int number = values.get(0) + values.get(1) + values.get(2)
                            + (initC == 0 ? 0 : 7);
        return fibonacci(number) + values.get(4) * values.get(5);
    }
    
    @Override
    public Integer solvePart1(final List<AssembunnyInstruction> instructions) {
        return solve(instructions, 0);
    }
    
    @Override
    public Integer solvePart2(final List<AssembunnyInstruction> instructions) {
        return solve(instructions, 1);
    }

    @Override
    public void samples() {
        final AoC2016_12 test = AoC2016_12.createDebug();
        assert test.solveVM(test.parseInput(splitLines(TEST1)), 0) == 42;
        assert test.solveVM(test.parseInput(splitLines(TEST2)), 0) == 318_003;
        assert test.solveVM(test.parseInput(splitLines(TEST2)), 1) == 9_227_657;
        assert test.solve(test.parseInput(splitLines(TEST2)), 0) == 318_003;
        assert test.solve(test.parseInput(splitLines(TEST2)), 1) == 9_227_657;
    }

    public static void main(final String[] args) throws Exception {
        AoC2016_12.create().run();
    }
    
    private static final String TEST1 = """
                cpy 41 a
                inc a
                inc a
                dec a
                jnz a 2
                dec a
                """;
    private static final String TEST2 = """
                cpy 1 a
                cpy 1 b
                cpy 26 d
                jnz c 2
                jnz 1 5
                cpy 7 c
                inc d
                dec c
                jnz c -2
                cpy a c
                inc a
                dec b
                jnz b -2
                cpy c b
                dec d
                jnz d -6
                cpy 16 c
                cpy 12 d
                inc a
                dec d
                jnz d -2
                dec c
                jnz c -5
                """;
}
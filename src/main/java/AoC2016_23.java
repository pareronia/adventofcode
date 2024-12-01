import static com.github.pareronia.aoc.StringOps.splitLines;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.assembunny.Assembunny;
import com.github.pareronia.aoc.assembunny.Assembunny.AssembunnyInstruction;
import com.github.pareronia.aoc.solution.SolutionBase;
import com.github.pareronia.aoc.vm.Program;
import com.github.pareronia.aoc.vm.VirtualMachine;

public final class AoC2016_23
        extends SolutionBase<List<AssembunnyInstruction>, Integer, Integer> {
    
    private AoC2016_23(final boolean debug) {
        super(debug);
    }
    
    public static AoC2016_23 create() {
        return new AoC2016_23(false);
    }
    
    public static AoC2016_23 createDebug() {
        return new AoC2016_23(true);
    }
    
    private Integer solveVM(
            final List<AssembunnyInstruction> instructions,
            final Integer initA
    ) {
        final Program program = new Program(Assembunny.translate(instructions));
        log(() -> program.getInstructions());
        program.setRegisterValue("a", initA.longValue());
        new VirtualMachine().runProgram(program);
        log(() -> program.getCycles());
        return program.getRegisters().get("a").intValue();
    }
    
    private Integer solve(
            final List<AssembunnyInstruction> instructions,
            final Integer initA
    ) {
        final List<Integer> values = instructions.stream()
                .filter(i -> List.of("cpy", "jnz").contains(i.getOperator()))
                .map(i -> i.getOperands().get(0))
                .filter(StringUtils::isNumeric)
                .map(Integer::valueOf)
                .collect(toList());
        return values.get(2) * values.get(3)
                + Stream.iterate(1, i -> i <= initA, i -> i + 1)
                        .reduce((a, b) -> a * b).orElseThrow();
    }
 
    @Override
    protected List<AssembunnyInstruction> parseInput(final List<String> inputs) {
        return Assembunny.parse(inputs);
    }

    @Override
    public Integer solvePart1(final List<AssembunnyInstruction> instructions) {
        return solve(instructions, 7);
    }
    
    @Override
    public Integer solvePart2(final List<AssembunnyInstruction> instructions) {
        return solve(instructions, 12);
    }

    @Override
    public void samples() {
        final AoC2016_23 test = AoC2016_23.createDebug();
        assert test.solveVM(test.parseInput(splitLines(TEST1)), 7) == 3;
        assert test.solve(test.parseInput(splitLines(TEST2)), 7) == 11_610;
        assert test.solve(test.parseInput(splitLines(TEST2)), 12) == 479_008_170;
    }

    public static void main(final String[] args) throws Exception {
        AoC2016_23.create().run();
    }
    
    private static final String TEST1 = """
                cpy 2 a
                tgl a
                tgl a
                tgl a
                cpy 1 a
                dec a
                dec a
                """;
    private static final String TEST2 = """
                cpy a b
                dec b
                cpy a d
                cpy 0 a
                cpy b c
                inc a
                dec c
                jnz c -2
                dec d
                jnz d -5
                dec b
                cpy b c
                cpy c d
                dec d
                inc c
                jnz d -2
                tgl c
                cpy -16 c
                jnz 1 c
                cpy 90 c
                jnz 73 d
                inc a
                inc d
                jnz d -2
                inc c
                jnz c -5
                """;
}
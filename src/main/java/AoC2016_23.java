import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.assembunny.Assembunny;
import com.github.pareronia.aoc.assembunny.Assembunny.AssembunnyInstruction;
import com.github.pareronia.aoc.vm.Program;
import com.github.pareronia.aoc.vm.VirtualMachine;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2016_23 extends AoCBase {
    
    private final transient List<AssembunnyInstruction> instructions;
    
    private AoC2016_23(final List<String> inputs, final boolean debug) {
        super(debug);
        log(inputs);
        this.instructions = Assembunny.parse(inputs);
    }
    
    public static AoC2016_23 create(final List<String> input) {
        return new AoC2016_23(input, false);
    }
    
    public static AoC2016_23 createDebug(final List<String> input) {
        return new AoC2016_23(input, true);
    }
    
    private Integer solveVM(final Integer initA) {
        final Program program = new Program(Assembunny.translate(this.instructions));
        log(() -> program.getInstructions());
        program.setRegisterValue("a", initA.longValue());
        new VirtualMachine().runProgram(program);
        log(() -> program.getCycles());
        return program.getRegisters().get("a").intValue();
    }
    
    private Integer solve(final Integer initA) {
        final List<Integer> values = this.instructions.stream()
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
    public Integer solvePart1() {
        return solve(7);
    }
    
    @Override
    public Integer solvePart2() {
        return solve(12);
    }
    
    public static void main(final String[] args) throws Exception {
        assert AoC2016_23.createDebug(TEST1).solveVM(7) == 3;
        assert AoC2016_23.createDebug(TEST2).solve(7) == 11_610;
        assert AoC2016_23.createDebug(TEST2).solve(12) == 479_008_170;
        
		final Puzzle puzzle = Puzzle.create(2016, 23);
		final List<String> input = puzzle.getInputData();
		puzzle.check(
		    () -> lap("Part 1", AoC2016_23.create(input)::solvePart1),
		    () -> lap("Part 2", AoC2016_23.create(input)::solvePart2)
		);
    }
    
    private static final List<String> TEST1 = splitLines(
            "cpy 2 a\n" +
            "tgl a\n" +
            "tgl a\n" +
            "tgl a\n" +
            "cpy 1 a\n" +
            "dec a\n" +
            "dec a"
    );
    private static final List<String> TEST2 = splitLines(
            "cpy a b\n" +
            "dec b\n" +
            "cpy a d\n" +
            "cpy 0 a\n" +
            "cpy b c\n" +
            "inc a\n" +
            "dec c\n" +
            "jnz c -2\n" +
            "dec d\n" +
            "jnz d -5\n" +
            "dec b\n" +
            "cpy b c\n" +
            "cpy c d\n" +
            "dec d\n" +
            "inc c\n" +
            "jnz d -2\n" +
            "tgl c\n" +
            "cpy -16 c\n" +
            "jnz 1 c\n" +
            "cpy 90 c\n" +
            "jnz 73 d\n" +
            "inc a\n" +
            "inc d\n" +
            "jnz d -2\n" +
            "inc c\n" +
            "jnz c -5"
    );
}
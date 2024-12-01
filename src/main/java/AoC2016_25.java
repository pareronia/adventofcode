import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aoc.assembunny.Assembunny;
import com.github.pareronia.aoc.assembunny.Assembunny.AssembunnyInstruction;
import com.github.pareronia.aoc.solution.SolutionBase;
import com.github.pareronia.aoc.vm.Instruction;
import com.github.pareronia.aoc.vm.Program;
import com.github.pareronia.aoc.vm.VirtualMachine;
import com.github.pareronia.aoc.vm.VirtualMachine.InfiniteLoopException;

public final class AoC2016_25
        extends SolutionBase<List<AssembunnyInstruction>, Integer, String> {
    
    private AoC2016_25(final boolean debug) {
        super(debug);
    }
    
    public static AoC2016_25 create() {
        return new AoC2016_25(false);
    }
    
    public static AoC2016_25 createDebug() {
        return new AoC2016_25(true);
    }
    
    private List<Long> runProgram(
            final List<Instruction> vmInstructions,
            final Integer initA
    ) {
        final List<Long> output = new ArrayList<>();
        final Program program = new Program(
                vmInstructions,
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
    
    private Integer solveVM(final List<AssembunnyInstruction> instructions) {
        final List<Instruction> vmInstructions
                = Assembunny.translate(instructions);
        int n = 150;
        while(true) {
            log(n);
            final List<Long> output = runProgram(vmInstructions, n);
            if (Stream.iterate(0, i -> i < output.size(), i -> i + 1)
                        .allMatch(i -> i % 2 == output.get(i))) {
                return n;
            }
            n++;
        }
    }
    
    @Override
    protected List<AssembunnyInstruction> parseInput(final List<String> inputs) {
        return Assembunny.parse(inputs);
    }

    @Override
    public Integer solvePart1(final List<AssembunnyInstruction> instructions) {
        return solveVM(instructions);
    }
    
    @Override
    public String solvePart2(final List<AssembunnyInstruction> instructions) {
        return "🎄";
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2016_25.create().run();
    }
}
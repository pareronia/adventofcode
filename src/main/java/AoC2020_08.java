import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.pareronia.aoc.vm.Instruction;
import com.github.pareronia.aoc.vm.Program;
import com.github.pareronia.aoc.vm.VirtualMachine;
import com.github.pareronia.aoc.vm.VirtualMachine.InfiniteLoopException;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2020_08 extends AoCBase {
    
	private final List<Instruction_> instructions;
	
	private AoC2020_08(final List<String> inputs, final boolean debug) {
		super(debug);
		this.instructions = parse(inputs);
		log(inputs);
	}
	
	private final List<Instruction_> parse(final List<String> inputs) {
	    return inputs.stream()
	            .map(input -> input.split(" "))
	            .map(s -> new Instruction_(s[0], Integer.valueOf(s[1])))
	            .collect(toList());
	}
	
	public static final AoC2020_08 create(final List<String> input) {
		return new AoC2020_08(input, false);
	}

	public static final AoC2020_08 createDebug(final List<String> input) {
		return new AoC2020_08(input, true);
	}
	
	private Program buildProgram(final List<Instruction_> lines) {
	    final List<Instruction> instructions = new ArrayList<>();
	    for (final Instruction_ line : lines) {
	        if (line.operator.equals("nop")) {
	            instructions.add(Instruction.NOP());
	        } else if (line.operator.equals("acc")) {
	            instructions.add(Instruction.ADD("ACC", line.operand.longValue()));
	        } else  {
	            instructions.add(Instruction.JMP(line.operand));
	        }
        }
	    return new Program(instructions, 1);
	}
	
    private Integer tryProgramWithReplaceOperation(
            final List<Instruction_> instructions,
            final int index,
            final String newOperation
    ) {
        Objects.checkFromToIndex(index, index, instructions.size());
        final Instruction_ origInstruction = instructions.get(index);
        instructions.set(index, new Instruction_(newOperation, origInstruction.operand));
	    final Program program = buildProgram(this.instructions);
	    log(program.getInstructions());
        try {
            new VirtualMachine().runProgram(program);
        } catch (final InfiniteLoopException e) {
            instructions.set(index, origInstruction);
            return null;
        }
        log(program.getCycles());
	    return program.getRegisters().get("ACC").intValue();
	}
	
	@Override
	public Integer solvePart1() {
	    final Program program = buildProgram(this.instructions);
	    log(program.getInstructions());
        try {
            new VirtualMachine().runProgram(program);
        } catch (final InfiniteLoopException e) {
        }
        log(program.getCycles());
	    return program.getRegisters().get("ACC").intValue();
	}
	
	@Override
	public Integer solvePart2() {
        for (int i = 0; i < this.instructions.size(); i++) {
            final Instruction_ instruction = this.instructions.get(i);
            if (instruction.operator.equals("nop")) {
                final Integer result = tryProgramWithReplaceOperation(instructions, i, "jmp");
                if (result != null) {
                    return result;
                }
	        } else if (instruction.operator.equals("jmp")) {
                final Integer result = tryProgramWithReplaceOperation(instructions, i, "nop");
                if (result != null) {
                    return result;
                }
	        } else if (instruction.operator.equals("acc")) {
	            // nop
	        }
        }
	    return 0;
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2020_08.createDebug(TEST).solvePart1() == 5;
		assert AoC2020_08.createDebug(TEST).solvePart2() == 8;
		
        final Puzzle puzzle = Aocd.puzzle(2020, 8);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2020_08.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2020_08.create(inputData)::solvePart2)
        );
	}

	private static final List<String> TEST = splitLines(
	        "nop +0\n" +
	        "acc +1\n" +
	        "jmp +4\n" +
	        "acc +3\n" +
	        "jmp -3\n" +
	        "acc -99\n" +
	        "acc +1\n" +
	        "jmp -4\n" +
	        "acc +6"
	);
	
	record Instruction_(String operator, Integer operand) {}
}
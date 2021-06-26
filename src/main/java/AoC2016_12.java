import java.util.List;

import com.github.pareronia.aoc.assembunny.Assembunny;
import com.github.pareronia.aoc.assembunny.Assembunny.AssembunnyInstruction;
import com.github.pareronia.aoc.vm.Program;
import com.github.pareronia.aoc.vm.VirtualMachine;
import com.github.pareronia.aocd.Aocd;

public class AoC2016_12 extends AoCBase {
    
	private final List<AssembunnyInstruction> instructions;
	
	private AoC2016_12(List<String> inputs, boolean debug) {
		super(debug);
		log(inputs);
		this.instructions = Assembunny.parse(inputs);
	}
	
	public static final AoC2016_12 create(List<String> input) {
		return new AoC2016_12(input, false);
	}

	public static final AoC2016_12 createDebug(List<String> input) {
		return new AoC2016_12(input, true);
	}
	
	private Integer solve(Integer initC) {
	    final Program program = new Program(Assembunny.translate(this.instructions));
	    log(program.getInstructions());
	    program.setRegisterValue("c", initC.longValue());
	    new VirtualMachine().runProgram(program);
	    log(program.getCycles());
	    return program.getRegisters().get("a").intValue();
	}
	
	@Override
	public Integer solvePart1() {
	    return solve(0);
	}
	
	@Override
	public Integer solvePart2() {
	    return solve(1);
	}

	public static void main(String[] args) throws Exception {
		assert AoC2016_12.createDebug(TEST).solvePart1() == 42;
		
		final List<String> input = Aocd.getData(2016, 12);
		lap("Part 1", () -> AoC2016_12.create(input).solvePart1());
		lap("Part 2", () -> AoC2016_12.create(input).solvePart2());
	}

	private static final List<String> TEST = splitLines(
	        "cpy 41 a\n" +
	        "inc a\n" +
	        "inc a\n" +
	        "dec a\n" +
	        "jnz a 2\n" +
	        "dec a"
	);
}
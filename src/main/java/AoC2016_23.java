import java.util.List;

import com.github.pareronia.aoc.assembunny.Assembunny;
import com.github.pareronia.aoc.assembunny.Assembunny.AssembunnyInstruction;
import com.github.pareronia.aoc.vm.Program;
import com.github.pareronia.aoc.vm.VirtualMachine;
import com.github.pareronia.aocd.Aocd;

public class AoC2016_23 extends AoCBase {
    
	private final List<AssembunnyInstruction> instructions;
	
	private AoC2016_23(List<String> inputs, boolean debug) {
		super(debug);
		log(inputs);
		this.instructions = Assembunny.parse(inputs);
	}
	
	public static final AoC2016_23 create(List<String> input) {
		return new AoC2016_23(input, false);
	}

	public static final AoC2016_23 createDebug(List<String> input) {
		return new AoC2016_23(input, true);
	}
	
	private Integer solve(Integer initA) {
	    final Program program = new Program(Assembunny.translate(this.instructions));
	    log(program.getInstructions());
	    program.setRegisterValue("a", initA.longValue());
	    new VirtualMachine().runProgram(program);
	    log(program.getCycles());
	    return program.getRegisters().get("a").intValue();
	}
	
	@Override
	public Integer solvePart1() {
	    return solve(7);
	}
	
	@Override
	public Integer solvePart2() {
	    return solve(12);
	}

	public static void main(String[] args) throws Exception {
		assert AoC2016_23.createDebug(TEST).solvePart1() == 3;
		
		final List<String> input = Aocd.getData(2016, 23);
		lap("Part 1", () -> AoC2016_23.createDebug(input).solvePart1());
		lap("Part 2", () -> AoC2016_23.create(input).solvePart2());
	}

	private static final List<String> TEST = splitLines(
	        "cpy 2 a\n" +
	        "tgl a\n" +
	        "tgl a\n" +
	        "tgl a\n" +
	        "cpy 1 a\n" +
	        "dec a\n" +
	        "dec a"
	);
}
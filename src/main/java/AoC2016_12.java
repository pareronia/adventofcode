import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.subarray;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.github.pareronia.aoc.vm.Instruction;
import com.github.pareronia.aoc.vm.Program;
import com.github.pareronia.aoc.vm.VirtualMachine;
import com.github.pareronia.aocd.Aocd;

import lombok.Value;

public class AoC2016_12 extends AoCBase {
    
    private static final Set<String> REGISTERS = Set.of("a", "b", "c", "d");

	private final List<Instruction_> instructions;
	
	private AoC2016_12(List<String> inputs, boolean debug) {
		super(debug);
		this.instructions = parse(inputs);
		log(inputs);
	}
	
	private final List<Instruction_> parse(List<String> inputs) {
	    return inputs.stream()
	            .map(input -> input.split(" "))
	            .map(s -> new Instruction_(s[0], asList(subarray(s, 1, s.length))))
	            .collect(toList());
	}
	
	public static final AoC2016_12 create(List<String> input) {
		return new AoC2016_12(input, false);
	}

	public static final AoC2016_12 createDebug(List<String> input) {
		return new AoC2016_12(input, true);
	}
	
	private boolean isNumeric(String s) {
	    return StringUtils.isNumeric(s.replace("-", ""));
	}
	
	private Program buildProgram() {
	    final List<Instruction> instructions = new ArrayList<>();
	    for (final Instruction_ line : this.instructions) {
	        if (line.operator.equals("cpy")) {
	            final String value = line.operands.get(0);
	            final String register = line.operands.get(1);
	            if (isNumeric(value)) {
	                instructions.add(Instruction.SET(register, Long.valueOf(value)));
	            } else {
	                instructions.add(Instruction.CPY(value, register));
	            }
	        } else if (line.operator.equals("inc")) {
	            final String register = line.operands.get(0);
	            instructions.add(Instruction.ADD(register, 1L));
	        } else if (line.operator.equals("dec")) {
	            final String register = line.operands.get(0);
	            instructions.add(Instruction.ADD(register, -1L));
	        } else if (line.operator.equals("jnz")) {
	            final String register = line.operands.get(0);
	            final Long value = Long.valueOf(line.operands.get(1));
	            if (REGISTERS.contains(register)) {
	                instructions.add(Instruction.JN0(register, value.intValue()));
	            } else if (isNumeric(register)) {
	                instructions.add(Instruction.SET("tmp", value));
	                instructions.add(Instruction.JN0("tmp", value.intValue()));
	            } else {
	                throw new IllegalArgumentException("Invalid operands for jnz");
	            }
	        }
        }
	    return new Program(instructions);
	}
	
	@Override
	public Integer solvePart1() {
	    final Program program = buildProgram();
	    log(program.getInstructions());
        new VirtualMachine().runProgram(program);
        log(program.getCycles());
	    return program.getRegisters().get("a").intValue();
	}
	
	@Override
	public Integer solvePart2() {
	    final Program program = buildProgram();
	    program.getInstructions().add(0, Instruction.SET("c", 1L));
	    log(program.getInstructions());
        new VirtualMachine().runProgram(program);
        log(program.getCycles());
	    return program.getRegisters().get("a").intValue();
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
	
	@Value
	private static final class Instruction_ {
	    private final String operator;
	    private final List<String> operands;
	}
}
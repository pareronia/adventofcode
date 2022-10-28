import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import com.github.pareronia.aoc.vm.Instruction;
import com.github.pareronia.aoc.vm.Program;
import com.github.pareronia.aoc.vm.VirtualMachine;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2017_23 extends AoCBase {
    
    private final List<String> input;
    
    private AoC2017_23(final List<String> inputs, final boolean debug) {
        super(debug);
        this.input = inputs;
    }

    public static AoC2017_23 create(final List<String> input) {
        return new AoC2017_23(input, false);
    }

    public static AoC2017_23 createDebug(final List<String> input) {
        return new AoC2017_23(input, true);
    }
    
    private List<Instruction> buildInstructions(final List<String> inputs) {
        final Function<String, String> getValue
                = cs -> cs.matches("[+-]?[0-9]*") ? cs : "*" + cs;
        final List<Instruction> instructions = new ArrayList<>();
        for (final String s : inputs) {
            final String[] splits = s.split(" ");
            switch (splits[0]) {
            case "set":
                instructions.add(Instruction.SET(splits[1], getValue.apply(splits[2])));
                break;
            case "sub":
                instructions.add(Instruction.SUB(splits[1], getValue.apply(splits[2])));
                break;
            case "mul":
                instructions.add(Instruction.MUL(splits[1], getValue.apply(splits[2])));
                break;
            case "jnz":
                instructions.add(Instruction.JN0(getValue.apply(splits[1]), getValue.apply(splits[2])));
                break;
            default:
                throw new IllegalStateException();
            }
        }
        return instructions;
    }
    
    @Override
    public Integer solvePart1() {
        final List<Instruction> instructions = buildInstructions(this.input);
        final Set<Integer> muls = IntStream.range(0, instructions.size())
            .filter(i -> instructions.get(i).isMUL())
            .boxed().collect(toSet());
        final Program program = new Program(instructions);
        for (final char ch : "abcdefgh".toCharArray()) {
            program.setRegisterValue(String.valueOf(ch), 0L);
        }
        int ans = 0;
        final VirtualMachine vm = new VirtualMachine();
        while (program.getInstructionPointer() < instructions.size()) {
            if (muls.contains(program.getInstructionPointer())) {
                ans++;
            }
            vm.step(program);
        }
        return ans;
    }

    public boolean isPrime(final Long number) {
        final long start = (long) Math.floor(Math.sqrt(number));
        for (long i = start; i >= 2; i--) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public Long solvePart2() {
        final List<Instruction> instructions = buildInstructions(this.input);
        final Program program = new Program(instructions);
        program.setRegisterValue("a", 1L);
        for (final char ch : "bcdefgh".toCharArray()) {
            program.setRegisterValue(String.valueOf(ch), 0L);
        }
        final VirtualMachine vm = new VirtualMachine();
        while (program.getInstructionPointer() < 9) {
            vm.step(program);
        }
        final long from = program.getRegisters().get("b");
        final long to = program.getRegisters().get("c");
        final long step = -1L * Long.parseLong((String)
                instructions.get(instructions.size() - 2).getOperands().get(1));
        return LongStream.iterate(from, i -> i <= to, i -> i + step)
            .filter(this::isPrime)
            .count();
    }

    public static void main(final String[] args) throws Exception {
        final Puzzle puzzle = Aocd.puzzle(2017, 23);
        final List<String> input = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", create(input)::solvePart1),
            () -> lap("Part 2", createDebug(input)::solvePart2)
        );
    }
}

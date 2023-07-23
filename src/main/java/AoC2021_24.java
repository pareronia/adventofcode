import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.subarray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.vm.Instruction;
import com.github.pareronia.aoc.vm.Program;
import com.github.pareronia.aoc.vm.VirtualMachine;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2021_24 extends AoCBase {
    
    private static final int DIGITS = 14;
    private static final Set<String> REGISTERS = Set.of("w", "x", "y", "z");
    
    private final List<MonadInstruction> monadInstructions;
    private final int[] divz;
    private final int[] addy;
    private final int[] addx;
    
    private AoC2021_24(final List<String> input, final boolean debug) {
        super(debug);
        this.divz = getOperandsAt(input, 4);
        this.addy = getOperandsAt(input, 15);
        this.addx = getOperandsAt(input, 5);
        this.monadInstructions = input.stream()
                .map(s -> s.split(" "))
                .map(s -> new MonadInstruction(s[0], asList(subarray(s, 1, s.length))))
                .collect(toList());
    }
    
    private final int[] getOperandsAt(final List<String> input, final int offset) {
        if (input.size() % 18 != 0) {
            return new int[] {};
        }
        return IntStream.range(0, input.size())
                .filter(i -> i % 18 == offset)
                .mapToObj(i -> input.get(i))
                .map(s -> s.split(" ")[2])
                .mapToInt(Integer::parseInt)
                .toArray();
    }
    
    public static final AoC2021_24 create(final List<String> input) {
        return new AoC2021_24(input, false);
    }

    public static final AoC2021_24 createDebug(final List<String> input) {
        return new AoC2021_24(input, true);
    }
    
    private static boolean isNumeric(final String s) {
        return StringUtils.isNumeric(s.replace("-", ""));
    }
    
    private static String getOperand(final MonadInstruction line, final int idx) {
        final String operand_ = line.operands.get(idx);
        final String operand;
        if (REGISTERS.contains(operand_)) {
            operand = "*" + operand_;
        } else if (isNumeric(operand_)) {
            operand = operand_;
        } else {
            throw new IllegalArgumentException("Invalid operands for " + line.operator);
        }
        return operand;
    }
    
    private Program buildProgram(final List<MonadInstruction> monadInstructions) {
        final List<Instruction> instructions = new ArrayList<>();
        for (final MonadInstruction line : monadInstructions) {
            final String register = line.operands.get(0);
            if (line.operator.equals("inp")) {
                instructions.add(Instruction.INP(register));
            } else if (line.operator.equals("add")) {
                final String value = getOperand(line, 1);
                instructions.add(Instruction.ADD(register, value));
	        } else if (line.operator.equals("mul")) {
	            final String value = getOperand(line, 1);
	            instructions.add(Instruction.MUL(register, value));
	        } else if (line.operator.equals("div")) {
	            final String value = getOperand(line, 1);
	            instructions.add(Instruction.DIV(register, value));
	        } else if (line.operator.equals("mod")) {
	            final String value = getOperand(line, 1);
	            instructions.add(Instruction.MOD(register, value));
	        } else if (line.operator.equals("eql")) {
	            final String value = getOperand(line, 1);
	            instructions.add(Instruction.EQL(register, value));
	        } else {
	            throw new IllegalArgumentException("Invalid operation");
	        }
        }
        log(instructions);
        return new Program(instructions);
    }
    
    private Supplier<Long> inputSupplier(final String input) {
        return new Supplier<>() {
            int i = 0;
            
            @Override
            public Long get() {
                final long number = Long.parseLong(input.substring(i, i + 1));
                i++;
                return number;
            }
        };
    }
    
    private Map<String, Long> runProgram(final String input) {
        final Program program = buildProgram(this.monadInstructions);
        REGISTERS.forEach(r -> program.getRegisters().put(r, 0L));
        program.setInputSupplier(inputSupplier(input));
        new VirtualMachine().runProgram(program);
        log(program.getRegisters());
        return program.getRegisters();
    }
    
    //    mul x 0
    //    add x z
    //    mod x 26
    //    div z <param1>
    //    add x <param2>
    //    eql x w
    //    eql x 0
    //    mul y 0
    //    add y 25
    //    mul y x
    //    add y 1
    //    mul z y
    //    mul y 0
    //    add y w
    //    add y <param3>
    //    mul y x
    //    add z y
    private Program createProgram(final int digit) {
        if (programCache.containsKey(digit)) {
            return programCache.get(digit);
        }
        final List<Instruction> instructions = new ArrayList<>();
        instructions.add(Instruction.MUL("x", "0"));
        instructions.add(Instruction.ADD("x", "*z"));
        instructions.add(Instruction.MOD("x", "26"));
        instructions.add(Instruction.DIV("z", String.valueOf(this.divz[digit])));
        instructions.add(Instruction.ADD("x", String.valueOf(this.addx[digit])));
        instructions.add(Instruction.EQL("x", "*w"));
        instructions.add(Instruction.EQL("x", "0"));
        instructions.add(Instruction.MUL("y", "0"));
        instructions.add(Instruction.ADD("y", "25"));
        instructions.add(Instruction.MUL("y", "*x"));
        instructions.add(Instruction.ADD("y", "1"));
        instructions.add(Instruction.MUL("z", "*y"));
        instructions.add(Instruction.MUL("y", "0"));
        instructions.add(Instruction.ADD("y", "*w"));
        instructions.add(Instruction.ADD("y", String.valueOf(this.addy[digit])));
        instructions.add(Instruction.MUL("y", "*x"));
        instructions.add(Instruction.ADD("z", "*y"));
//        log(instructions);
        final Program program = new Program(instructions);
        programCache.put(digit, program);
        return program;
    }
    
    private final Map<Integer, Program> programCache = new HashMap<>();
    
    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static final class ProgramParams {
        private final long w;
        private final long z;
        private final int digit;
    }
    
    private long execProgram(final ProgramParams params) {
        final Program program = createProgram(params.digit);
        program.reset();
        program.getRegisters().put("w", params.w);
        program.getRegisters().put("x", 0L);
        program.getRegisters().put("y", 0L);
        program.getRegisters().put("z", params.z);
        new VirtualMachine().runProgram(program);
        return program.getRegisters().get("z");
    }
    
    /**
     * @see <a href="https://github.com/fuglede/adventofcode/blob/1ae9b4a917052e0be1ef938d8ad47cbac485f22d/2021/day24/solutions.py#L84">inspiration</a>
     */
    private long execProgram2(final ProgramParams params) {
        final int a = this.divz[params.digit];
        final int b = this.addx[params.digit];
        final int c = this.addy[params.digit];
        final long z = params.z;
        final long w = params.w;
        long ans;
        if (z % 26 + b == w) {
            ans = Math.floorDiv(z, a);
        } else {
            ans = Math.floorDiv(z, a) * 26 + w + c;
        }
        return ans;
    }
    
    private long solve(final boolean highest) {
        final Map<Integer, Set<Long>> wzz = new HashMap<>();
        wzz.put(DIGITS, Set.of(0L));
        Set<Long> zz = new HashSet<>();
        wzz.put(DIGITS - 1, zz);
        for (int i = DIGITS - 1; i >= 1; i--) {
            log("Looking for possible z input values, digit " + (i + 1));
            zz = new HashSet<>();
            for (long w = 9; w > 0; w--) {
                for (long z = 0; z <= 400_000; z++) {
                    // Running 400_000 iterations with the vm literally takes a minute,
                    // so using coded form here
                    final long z_ = execProgram2(new ProgramParams(w, z, i));
                    if (wzz.get(i + 1).contains(z_)) {
                        zz.add(z);
                    }
                }
            }
            wzz.put(i, zz);
            log(String.format("digit: %d, divz: %d, addx: %d, addy: %d, z-values (%d): %s",
                    i + 1, this.divz[i], this.addx[i], this.addy[i], zz.size(), new TreeSet<>(zz)));
        }
        final long[] ans = new long[DIGITS];
        Arrays.fill(ans, -1L);
        List<Long> range;
        if (highest) {
            range = List.of(9L, 8L, 7L, 6L, 5L, 4L, 3L, 2L, 1L);
        } else {
            range = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        }
        long z = 0;
        for (int j = 0; j < DIGITS; j++) {
            log("Finding digit " + (j + 1) + " satisfying known valid z values");
            for (final long w : range) {
                final long z_ = execProgram(new ProgramParams(w, z, j));
                if (wzz.get(j + 1).contains(z_)) {
                    z = z_;
                    ans[j] = w;
                    break;
                }
            }
            assert ans[j] != -1L;
        }
        return Long.parseLong(
                Arrays.stream(ans)
                .mapToObj(String::valueOf)
                .collect(joining()));
    }
    
    @Override
    public Long solvePart1() {
        return solve(true);
    }
    
    @Override
    public Long solvePart2() {
        return solve(false);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_24.create(TEST1).runProgram("5").get("x") == -5;
        assert AoC2021_24.create(TEST2).runProgram("13").get("z") == 1;
        assert AoC2021_24.create(TEST2).runProgram("31").get("z") == 0;
        assert AoC2021_24.create(TEST3).runProgram("9").equals(Map.of("w", 1L, "x", 0L, "y", 0L, "z", 1L));
        assert AoC2021_24.create(TEST4).runProgram("9").equals(Map.of("w", 9L, "x", 1L, "y", 23L, "z", 23L));
        assert AoC2021_24.create(TEST4).runProgram("1").get("z") == 15;

        final Puzzle puzzle = Aocd.puzzle(2021, 24);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2021_24.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2021_24.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST1 = splitLines(
        "inp x\r\n" +
        "mul x -1"
    );
    private static final List<String> TEST2 = splitLines(
        "inp z\r\n" +
        "inp x\r\n" +
        "mul z 3\r\n" +
        "eql z x"
    );
    private static final List<String> TEST3 = splitLines(
        "inp w\r\n" +
        "add z w\r\n" +
        "mod z 2\r\n" +
        "div w 2\r\n" +
        "add y w\r\n" +
        "mod y 2\r\n" +
        "div w 2\r\n" +
        "add x w\r\n" +
        "mod x 2\r\n" +
        "div w 2\r\n" +
        "mod w 2"
    );
    private static final List<String> TEST4 = splitLines(
        "inp w\r\n" +
        "mul x 0\r\n" +
        "add x z\r\n" +
        "mod x 26\r\n" +
        "div z 1\r\n" +
        "add x 11\r\n" +
        "eql x w\r\n" +
        "eql x 0\r\n" +
        "mul y 0\r\n" +
        "add y 25\r\n" +
        "mul y x\r\n" +
        "add y 1\r\n" +
        "mul z y\r\n" +
        "mul y 0\r\n" +
        "add y w\r\n" +
        "add y 14\r\n" +
        "mul y x\r\n" +
        "add z y"
    );
    
    @RequiredArgsConstructor
    @ToString
    private static final class MonadInstruction {
        private final String operator;
        private final List<String> operands;
    }
}

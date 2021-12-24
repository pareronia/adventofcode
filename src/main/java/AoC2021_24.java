import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.ArrayUtils.subarray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;

import com.github.pareronia.aoc.vm.Instruction;
import com.github.pareronia.aoc.vm.Program;
import com.github.pareronia.aoc.vm.VirtualMachine;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2021_24 extends AoCBase {
    
    private static final Set<String> REGISTERS = Set.of("w", "x", "y", "z");
    
    private final List<MonadInstruction> monadInstructions;
    
    private AoC2021_24(final List<String> input, final boolean debug) {
        super(debug);
         this.monadInstructions = input.stream()
                .map(s -> s.split(" "))
                .map(s -> new MonadInstruction(s[0], asList(subarray(s, 1, s.length))))
                .collect(toList());
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
    
    private final int[] DIVZ = new int[] { 1, 1, 1, 1, 26, 26, 1, 26, 26, 26, 1, 26, 1, 26 };
    private final int[] ADDY = new int[] { 14, 8, 4, 10, 14, 10, 4, 14, 1, 6, 0, 9, 13, 12 };
    private final int[] ADDX = new int[] { 11, 13, 11, 10, -3, -4, 12, -8, -3, -12, 14, -6, 11, -12 };
   
    
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
    private Program createProgram(final int param1, final int param2, final int param3) {
        final String key = String.format("%d/%d/%d", param1, param2, param3);
        if (programeCache.containsKey(key)) {
            return programeCache.get(key);
        }
        final List<Instruction> instructions = new ArrayList<>();
        instructions.add(Instruction.MUL("x", "0"));
        instructions.add(Instruction.ADD("x", "*z"));
        instructions.add(Instruction.MOD("x", "26"));
        instructions.add(Instruction.DIV("z", String.valueOf(param1)));
        instructions.add(Instruction.ADD("x", String.valueOf(param2)));
        instructions.add(Instruction.EQL("x", "*w"));
        instructions.add(Instruction.EQL("x", "0"));
        instructions.add(Instruction.MUL("y", "0"));
        instructions.add(Instruction.ADD("y", "25"));
        instructions.add(Instruction.MUL("y", "*x"));
        instructions.add(Instruction.ADD("y", "1"));
        instructions.add(Instruction.MUL("z", "*y"));
        instructions.add(Instruction.MUL("y", "0"));
        instructions.add(Instruction.ADD("y", "*w"));
        instructions.add(Instruction.ADD("y", String.valueOf(param3)));
        instructions.add(Instruction.MUL("y", "*x"));
        instructions.add(Instruction.ADD("z", "*y"));
        log(instructions);
        final Program program = new Program(instructions);
        programeCache.put(key, program);
        return program;
    }
    
    private final Map<String, Program> programeCache = new HashMap<>();
    
    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static final class ProgramParams {
        private final long w;
        private final long z;
        private final int param1;
        private final int param2;
        private final int param3;
    }
    
    private final Map<ProgramParams, Long> cache = new HashMap<>();
    
    @SuppressWarnings("unused")
    private long execProgram2(final ProgramParams params) {
        final int a = params.param1;
        final int b = params.param2;
        final int c = params.param3;
        final long z = params.z;
        final long w = params.w;
        if (z % 26 + b == w) {
            return Math.floorDiv(z, a);
        } else {
            return Math.floorDiv(z, a) * 26 + w + c;
        }
    }
    
    private long execProgram(final ProgramParams params) {
        if (cache.containsKey(params)) {
            return cache.get(params);
        }
        final Program program = createProgram(params.param1, params.param2, params.param3);
        program.reset();
        program.getRegisters().put("w", params.w);
        program.getRegisters().put("x", 0L);
        program.getRegisters().put("y", 0L);
        program.getRegisters().put("z", params.z);
        new VirtualMachine().runProgram(program);
        assert program.getRegisters().get("x") <= 25;
        final Long z = program.getRegisters().get("z");
        cache.put(params, z);
        return z;
    }
    
    private void search(final long z, final int i) {
        for (long w = 9L; w > 0; w--) {
            log(String.format("w: %d, z: %d, i: %d", w, z, i));
            for (int zi = -26; zi <= 26 ; zi++) {
                final long zz = execProgram(new ProgramParams(w, zi, DIVZ[i], ADDX[i], ADDY[i]));
                if (zz == z) {
                    if (i == 0) {
                        log(w);
                    } else {
                        search(zi, i - 1);
                    }
                }
            }
        }
    }
    
    private void solve() {
//        for (int w = 9; w > 0; w--) {
            search(0L, 13);
//            log(cache.size());
//        }
//        for (long n = 99_999_999_999_999L; n >= 11_111_111_111_111L; n--) {
//            final String s = String.valueOf(n);
//            if (s.contains("0")) {
//                continue;
//            }
//            if (n % 1_000_000L == 999_999L) {
//                log(s);
//                log("cache: " + cache.size());
//            }
//            long w = 0;
//            long z = 0;
//            for (int i = 0; i < 14; i++) {
//                final char ch = s.charAt(i);
//                w = Long.valueOf(Character.digit(ch, 10));
//                z = execProgram(new ProgramParams(w, z, DIVZ[i], ADDX[i], ADDY[i]));
//            }
//            if (z == 0L) {
//                log(n);
//                break;
//            }
//        }
    }
    
    @Override
    public String solvePart1() {
        solve();
        return null;
    }
    
    @Override
    public Integer solvePart2() {
        return null;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_24.create(TEST1).runProgram("5").get("x") == -5;
        assert AoC2021_24.create(TEST2).runProgram("13").get("z") == 1;
        assert AoC2021_24.create(TEST2).runProgram("31").get("z") == 0;
        assert AoC2021_24.create(TEST3).runProgram("9").equals(Map.of("w", 1L, "x", 0L, "y", 0L, "z", 1L));
        assert AoC2021_24.createDebug(TEST4).runProgram("9").equals(Map.of("w", 9L, "x", 1L, "y", 23L, "z", 23L));
        assert AoC2021_24.createDebug(TEST4).runProgram("1").get("z") == 15;
        assert AoC2021_24.createDebug(TEST1).solvePart1() == null;
        assert AoC2021_24.create(TEST1).solvePart2() == null;

        final Puzzle puzzle = Aocd.puzzle(2021, 24);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_24.createDebug(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_24.create(puzzle.getInputData()).solvePart2())
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

import static java.util.stream.Collectors.joining;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;
import com.github.pareronia.aoc.vm.Instruction;
import com.github.pareronia.aoc.vm.VirtualMachine;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public final class AoC2024_17 extends SolutionBase<AoC2024_17.Input, String, Long> {

    private AoC2024_17(final boolean debug) {
        super(debug);
    }

    public static AoC2024_17 create() {
        return new AoC2024_17(false);
    }

    public static AoC2024_17 createDebug() {
        return new AoC2024_17(true);
    }

    @Override
    protected Input parseInput(final List<String> inputs) {
        return Input.fromInput(inputs);
    }

    @Override
    public String solvePart1(final Input input) {
        return Program.create(input.program).run(input.a, input.b, input.c).stream()
                .map(String::valueOf)
                .collect(joining(","));
    }

    @Override
    public Long solvePart2(final Input input) {
        final Program program = Program.create(input.program);
        final List<Long> ops = input.program.stream().map(Long::valueOf).toList();
        final Set<Long> seen = new HashSet<>(List.of(0L));
        final Deque<Long> q = new ArrayDeque<>(List.of(0L));
        while (!q.isEmpty()) {
            final long canda = q.pollFirst() * 8;
            for (int i = 0; i < 8; i++) {
                final long na = canda + i;
                final List<Long> res = program.run(na, input.b, input.c);
                if (res.equals(ops)) {
                    return na;
                }
                final List<Long> subList = ops.subList(ops.size() - res.size(), ops.size());
                if (res.equals(subList) && !seen.contains(na)) {
                    seen.add(na);
                    q.addLast(na);
                }
            }
        }
        throw new IllegalStateException("Unsolvable");
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "4,6,3,5,6,3,5,2,1,0"),
        @Sample(method = "part2", input = TEST2, expected = "117440")
    })
    public void samples() {}

    public static void main(final String[] args) throws Exception {
        AoC2024_17.create().run();
    }

    private static final String TEST1 =
            """
            Register A: 729
            Register B: 0
            Register C: 0

            Program: 0,1,5,4,3,0
            """;
    private static final String TEST2 =
            """
            Register A: 2024
            Register B: 0
            Register C: 0

            Program: 0,3,5,4,3,0
            """;

    record Input(long a, long b, long c, List<Integer> program) {

        public static Input fromInput(final List<String> inputs) {
            final long[] abc =
                    IntStream.range(0, 3)
                            .mapToLong(i -> Long.parseLong(inputs.get(i).substring(12)))
                            .toArray();
            final List<Integer> ops =
                    Arrays.stream(inputs.get(4).substring(9).split(","))
                            .map(Integer::valueOf)
                            .toList();
            return new Input(abc[0], abc[1], abc[2], ops);
        }
    }

    record Program(List<Instruction> instructions) {

        private static String combo(final int operand) {
            return switch (operand) {
                case 0, 1, 2, 3 -> String.valueOf(operand);
                case 4, 5, 6 -> List.of("*A", "*B", "*C").get(operand - 4);
                default -> throw new IllegalArgumentException("Unexpected value: " + operand);
            };
        }

        public static Program create(final List<Integer> ops) {
            final List<Instruction> ins = new ArrayList<>();
            int ip = 0;
            final Map<Integer, Integer> ipMap = new HashMap<>();
            for (int i = 0; i < ops.size(); i += 2) {
                final int opcode = ops.get(i);
                final int operand = ops.get(i + 1);
                ipMap.put(i, ip);
                switch (opcode) {
                    case 0:
                        {
                            ins.add(Instruction.RSH("A", combo(operand)));
                            ip++;
                            continue;
                        }
                    case 1:
                        {
                            ins.add(Instruction.XOR("B", String.valueOf(operand)));
                            ip++;
                            continue;
                        }
                    case 2:
                        {
                            ins.add(Instruction.SET("B", combo(operand)));
                            ins.add(Instruction.AND("B", "7"));
                            ip += 2;
                            continue;
                        }
                    case 3:
                        {
                            ins.add(
                                    Instruction.JN0(
                                            "*A", "!" + String.valueOf(ipMap.get(operand))));
                            ip++;
                            continue;
                        }
                    case 4:
                        {
                            ins.add(Instruction.XOR("B", "*C"));
                            ip++;
                            continue;
                        }
                    case 5:
                        {
                            ins.add(Instruction.SET("X", combo(operand)));
                            ins.add(Instruction.AND("X", "7"));
                            ins.add(Instruction.OUT("*X"));
                            ip += 3;
                            continue;
                        }
                    case 6:
                        {
                            ins.add(Instruction.SET("C", "*B"));
                            ins.add(Instruction.RSH("C", combo(operand)));
                            ip += 2;
                            continue;
                        }
                    case 7:
                        {
                            ins.add(Instruction.SET("C", "*A"));
                            ins.add(Instruction.RSH("C", combo(operand)));
                            ip += 2;
                            continue;
                        }
                    default:
                        throw new IllegalArgumentException("Unexpected value: " + opcode);
                }
            }
            return new Program(ins);
        }

        public List<Long> run(final long a, final long b, final long c) {
            final List<Long> output = new ArrayList<>();
            final com.github.pareronia.aoc.vm.Program program =
                    new com.github.pareronia.aoc.vm.Program(instructions, output::add);
            program.setRegisterValue("A", a);
            program.setRegisterValue("B", b);
            program.setRegisterValue("C", c);
            new VirtualMachine().runProgram(program);
            return output;
        }
    }
}

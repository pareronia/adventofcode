import static com.github.pareronia.aoc.IntegerSequence.Range.rangeClosed;
import static com.github.pareronia.aoc.StringOps.splitOnce;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2024_24 extends SolutionBase<AoC2024_24.Circuit, Long, String> {

	private AoC2024_24(final boolean debug) {
		super(debug);
	}
	
	public static final AoC2024_24 create() {
		return new AoC2024_24(false);
	}

	public static final AoC2024_24 createDebug() {
		return new AoC2024_24(true);
	}
	
	@Override
    protected Circuit parseInput(final List<String> inputs) {
		return Circuit.fromInput(inputs);
    }

	@Override
	public Long solvePart1(final Circuit circuit) {
	    final int maxZ = circuit.getGates().stream()
	            .filter(gate -> gate.getName().startsWith("z"))
	            .map(gate -> gate.getName().substring(1))
	            .mapToInt(Integer::parseInt)
	            .max().getAsInt();
	    return rangeClosed(maxZ, 0, -1).intStream()
                .mapToLong(i -> ((1L << i)
                        * circuit.getValue("z%02d".formatted(i))))
                .sum();
	}
	
	@Override
	public String solvePart2(final Circuit circuit) {
		final Predicate<Gate> outputsToZExceptFirstOneAndNotXOR
		    = gate -> (gate.name.startsWith("z")
		            && gate.op != Gate.Op.XOR
		            && !gate.name.equals("z45"));
		final Predicate<Gate> isXORNotConnectedToXorYorZ
		    = gate -> gate.op == Gate.Op.XOR
		            && Set.of(gate.name, gate.in1, gate.in2).stream()
		            .noneMatch(n ->    n.startsWith("x")
		                            || n.startsWith("y")
		                            || n.startsWith("z"));
		final BiPredicate<Gate, List<Gate>> isANDExceptLastWithAnOutputNotToOR
		    = (gate, others) -> (gate.op == Gate.Op.AND
                && !(gate.in1.equals("x00") || gate.in2.equals("x00"))
                && others.stream().anyMatch(other -> other.op != Gate.Op.OR
                    && (Set.of(other.in1, other.in2).contains(gate.name))));
		final BiPredicate<Gate, List<Gate>> isXORWithAnOutputToOR
		    = (gate, others) -> (gate.op == Gate.Op.XOR
                && others.stream().anyMatch(other -> other.op == Gate.Op.OR
                    && (Set.of(other.in1, other.in2).contains(gate.name))));
		final List<Gate> gates = circuit.getGates().stream()
		        .filter(gate -> gate.op != Gate.Op.SET)
		        .toList();
		return gates.stream()
		        .filter(gate -> outputsToZExceptFirstOneAndNotXOR.test(gate)
		            || isXORNotConnectedToXorYorZ.test(gate)
		            || isANDExceptLastWithAnOutputNotToOR.test(gate, gates)
		            || isXORWithAnOutputToOR.test(gate, gates))
		        .map(Gate::getName)
		        .sorted()
		        .collect(joining(","));
	}
	
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "4"),
        @Sample(method = "part1", input = TEST2, expected = "2024"),
    })
    public void samples() {
    }

	public static void main(final String[] args) throws Exception {
		AoC2024_24.create().run();
	}

	private static final String TEST1 = """
	        x00: 1
	        x01: 1
	        x02: 1
	        y00: 0
	        y01: 1
	        y02: 0

	        x00 AND y00 -> z00
	        x01 XOR y01 -> z01
	        x02 OR y02 -> z02
	        """;
	private static final String TEST2 = """
	        x00: 1
	        x01: 0
	        x02: 1
	        x03: 1
	        x04: 0
	        y00: 1
	        y01: 1
	        y02: 1
	        y03: 1
	        y04: 1

	        ntg XOR fgs -> mjb
	        y02 OR x01 -> tnw
	        kwq OR kpj -> z05
	        x00 OR x03 -> fst
	        tgd XOR rvg -> z01
	        vdt OR tnw -> bfw
	        bfw AND frj -> z10
	        ffh OR nrd -> bqk
	        y00 AND y03 -> djm
	        y03 OR y00 -> psh
	        bqk OR frj -> z08
	        tnw OR fst -> frj
	        gnj AND tgd -> z11
	        bfw XOR mjb -> z00
	        x03 OR x00 -> vdt
	        gnj AND wpb -> z02
	        x04 AND y00 -> kjc
	        djm OR pbm -> qhw
	        nrd AND vdt -> hwm
	        kjc AND fst -> rvg
	        y04 OR y02 -> fgs
	        y01 AND x02 -> pbm
	        ntg OR kjc -> kwq
	        psh XOR fgs -> tgd
	        qhw XOR tgd -> z09
	        pbm OR djm -> kpj
	        x03 XOR y03 -> ffh
	        x00 XOR y04 -> ntg
	        bfw OR bqk -> z06
	        nrd XOR fgs -> wpb
	        frj XOR qhw -> z04
	        bqk OR frj -> z07
	        y03 OR x01 -> nrd
	        hwm AND bqk -> z03
	        tgd XOR rvg -> z12
	        tnw OR pbm -> gnj
	        """;
	
    static final class Gate {

        enum Op { SET, AND, OR, XOR }
        
        final String name;
        final String in1;
        final String in2;
        final Op op;
        final Integer arg;
        private Integer result;

        protected Gate(
                final String name,
                final String in1,
                final String in2,
                final Op op,
                final Integer arg
        ) {
            this.name = name;
            this.in1 = in1;
            this.in2 = in2;
            this.op = op;
            this.arg = arg;
        }
        
        public static Gate fromInput(final String input) {
            if (input.contains(": ")) {
                final StringSplit splits = splitOnce(input, ": ");
                return Gate.set(splits.left(), splits.right());
            }
            final StringSplit splits = splitOnce(input, " -> ");
            if (splits.left().contains("AND")) {
                final StringSplit andSplits = splitOnce(splits.left(), " AND ");
                return Gate.and(splits.right(), andSplits.left(), andSplits.right());
            } else if (splits.left().contains("XOR")) {
                final StringSplit xorSplits = splitOnce(splits.left(), " XOR ");
                return Gate.xor(splits.right(), xorSplits.left(), xorSplits.right());
            } else if (splits.left().contains("OR") && !splits.left().contains("XOR")) {
                final StringSplit orSplits = splitOnce(splits.left(), " OR ");
                return Gate.or(splits.right(), orSplits.left(), orSplits.right());
            } else {
                throw new IllegalArgumentException();
            }
        }

        public static Gate xor(final String name, final String in) {
            return new Gate(name, in, null, Op.XOR, null);
        }
        
        public static Gate and(final String name, final String in1, final String in2) {
            return new Gate(name, in1, in2, Op.AND, null);
        }
        
        public static Gate xor(final String name, final String in1, final String in2) {
            return new Gate(name, in1, in2, Op.XOR, null);
        }
        
        public static Gate or(final String name, final String in1, final String in2) {
            return new Gate(name, in1, in2, Op.OR, null);
        }
        
        public static Gate set(final String name, final String in) {
            return new Gate(name, in, null, Op.SET, null);
        }

        public String getName() {
            return name;
        }

        public Integer getResult() {
            return result;
        }

        public Integer updateResult(final Integer in1, final Integer in2) {
            switch (this.op) {
            case SET -> this.result = in1;
            case AND -> this.result = in1 & in2;
            case XOR -> this.result = in1 ^ in2;
            case OR -> this.result = in1 | in2;
            }
            return this.result;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            switch (this.op) {
            case SET -> sb.append(this.in1);
            case AND -> sb.append(this.in1).append(" AND ").append(this.in2);
            case OR -> sb.append(this.in1).append(" OR ").append(this.in2);
            case XOR -> sb.append(this.in1).append(" XOR ").append(this.in2);
            }
            return sb.toString();
        }
    }

    record Circuit(Map<String, Gate> gates) {

        public static Circuit fromInput(final List<String> inputs) {
            return new Circuit(inputs.stream()
                    .filter(s -> !s.isBlank())
                    .map(Gate::fromInput)
                    .collect(toMap(Gate::getName, identity())));
        }
        
        public Collection<Gate> getGates() {
            return this.gates.values();
        }
        
        public Gate getGate(final String name) {
            return this.gates.get(requireNonNull(name));
        }
        
        public int getValue(final String name) {
            assert name != null && !name.isEmpty(): "name is empty";
            if (StringUtils.isNumeric(name)) {
                return Integer.parseInt(name);
            }
            final Gate gate = getGate(name);
            assert gate != null : "Gate '" + name + "' not found";
            final Integer result = gate.getResult();
            if (result != null) {
                return result;
            }
            final Integer in1 = getValue(gate.in1);
            final Integer in2 = gate.in2 != null ? getValue(gate.in2) : null;
            return gate.updateResult(in1, in2);
        }
    }
}
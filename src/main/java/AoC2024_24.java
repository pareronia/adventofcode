import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2024_24 extends SolutionBase<List<AoC2024_24.Gate>, Long, String> {

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
    protected List<Gate> parseInput(final List<String> inputs) {
		return inputs.stream().filter(s -> !s.isBlank()).map(Gate::fromInput).collect(toList());
    }

    int part1(final List<Gate> inputs, final String wire) {
		final Circuit circuit = Circuit.of(inputs);
		return circuit.getValue(wire);
	}
	
	@Override
	public Long solvePart1(final List<Gate> inputs) {
	    String ans = "";
	    for (int i = 0; i <= 45; i++) {
	        try {
                ans = part1(inputs, String.format("z%02d", i)) + ans;
            } catch (final AssertionError e) {
            }
        }
	    return Long.parseLong(ans, 2);
	}
	
	@Override
	public String solvePart2(final List<Gate> inputs) {
		final List<Gate> gates = Circuit.of(inputs).getGates().stream().filter(gate -> gate.op != AoC2024_24.Gate.Op.SET).toList();
		final Set<String> swapped = new HashSet<>();
		for (final Gate gate : gates) {
            if (gate.op == AoC2024_24.Gate.Op.SET) {
		        continue;
		    }
		    if (gate.name.startsWith("z")
		            && gate.op != AoC2024_24.Gate.Op.XOR
		            && !gate.name.equals("z45")) {
		        swapped.add(gate.name);
		    }
		    if (gate.op == AoC2024_24.Gate.Op.XOR
		            && Set.of(gate.name, gate.in1, gate.in2).stream()
		                    .noneMatch(n ->    n.startsWith("x")
		                                    || n.startsWith("y")
		                                    || n.startsWith("z"))) {
		        swapped.add(gate.name);
		    }
		    if (gate.op == AoC2024_24.Gate.Op.AND
		            && !(gate.in1.equals("x00") || gate.in2.equals("x00"))) {
		        for (final Gate other : gates) {
		            if (other.op != AoC2024_24.Gate.Op.OR
		                    && (Set.of(other.in1, other.in2).contains(gate.name))) {
		                swapped.add(gate.name);
		            }
		        }
		    }
		    if (gate.op == AoC2024_24.Gate.Op.XOR) {
		        for (final Gate other : gates) {
		            if (other.op == AoC2024_24.Gate.Op.OR
		                    && (Set.of(other.in1, other.in2).contains(gate.name))) {
		                swapped.add(gate.name);
		            }
		        }
		    }
        }
		log(swapped);
	    return swapped.stream().sorted().collect(joining(","));
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
	
    static final class Gate implements Cloneable {

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
                final StringSplit splits = StringOps.splitOnce(input, ": ");
                return Gate.set(splits.left(), splits.right());
            }
            final String[] splits = input.split(" -> ");
            if (splits[0].contains("AND")) {
                final String[] andSplits = splits[0].split(" AND ");
                return Gate.and(splits[1], andSplits[0], andSplits[1]);
            } else if (splits[0].contains("XOR")) {
                final String in = splits[0].substring(" XOR ".length());
                final String[] xorSplits = splits[0].split(" XOR ");
                return Gate.xor(splits[1], xorSplits[0], xorSplits[1]);
            } else if (splits[0].contains("OR") && !splits[0].contains("XOR")) {
                final String[] orSplits = splits[0].split(" OR ");
                return Gate.or(splits[1], orSplits[0], orSplits[1]);
            } else {
                throw new IllegalArgumentException();
            }
        }

        @Override
        protected Gate clone() throws CloneNotSupportedException {
            return new Gate(this.name, this.in1, this.in2, this.op, this.arg);
        }
        
        public static Gate cloneGate(final Gate gate) {
            try {
                return gate.clone();
            } catch (final CloneNotSupportedException e) {
                throw new RuntimeException(e);
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
            case SET:
                this.result = in1;
                break;
            case AND:
                this.result = in1 & in2;
                break;
            case XOR:
                this.result = in1 ^ in2;
                break;
            case OR:
                this.result = in1 | in2;
                break;
            default:
                throw new IllegalStateException();
            }
            return this.result;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            switch (this.op) {
            case SET:
                sb.append(this.in1);
                break;
            case AND:
                sb.append(this.in1).append(" AND ").append(this.in2);
                break;
            case OR:
                sb.append(this.in1).append(" OR ").append(this.in2);
                break;
            case XOR:
                sb.append(this.in1).append(" XOR ").append(this.in2);
                break;
            default:
                throw new IllegalStateException();
            }
            return sb.toString();
        }
    }

    private static final class Circuit {
        private final Map<String, Gate> gates;

        protected Circuit(final Map<String, AoC2024_24.Gate> gates) {
            this.gates = gates;
        }

        public static Circuit of(final Collection<Gate> gates) {
            return new Circuit(requireNonNull(gates).stream()
                                .collect(toMap(Gate::getName, identity())));
        }
        
        public Collection<Gate> getGates() {
            return this.gates.values();
        }
        
        public Gate getGate(final String name) {
            return this.gates.get(requireNonNull(name));
        }
        
        public void setGate(final String name, final Gate gate) {
            this.gates.put(requireNonNull(name), requireNonNull(gate));
        }
        
        public Optional<Gate> getGateIn1(final String name) {
            final Gate gate = this.getGate(name);
            return Optional.ofNullable(gate.in1).map(this::getGate);
        }
        
        public Optional<Gate> getGateIn2(final String name) {
            final Gate gate = this.getGate(name);
            return Optional.ofNullable(gate.in2).map(this::getGate);
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

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Circuit [gates=").append(gates).append("]");
            return builder.toString();
        }
    }
}
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.pareronia.aocd.Aocd;

public class AoC2015_07 extends AoCBase {

	private static final int BIT_SIZE = 16;

	private final List<String> inputs;
	
	private AoC2015_07(List<String> input, boolean debug) {
		super(debug);
		this.inputs = input;
	}
	
	public static final AoC2015_07 create(List<String> input) {
		return new AoC2015_07(input, false);
	}

	public static final AoC2015_07 createDebug(List<String> input) {
		return new AoC2015_07(input, true);
	}
	
	private Circuit parse() {
		final List<Gate> gates = new ArrayList<>();
		for (final String input : inputs) {
			final String[] splits = input.split(" -> ");
			if (splits[0].contains("AND")) {
				final String[] andSplits = splits[0].split(" AND ");
				gates.add(Gate.and(splits[1], andSplits[0], andSplits[1]));
			} else if (splits[0].contains("OR")) {
				final String[] orSplits = splits[0].split(" OR ");
				gates.add(Gate.or(splits[1], orSplits[0], orSplits[1]));
			} else if (splits[0].contains("LSHIFT")) {
				final String[] shSplits = splits[0].split(" LSHIFT ");
				gates.add(Gate.lshift(splits[1], shSplits[0], shSplits[1]));
			} else if (splits[0].contains("RSHIFT")) {
				final String[] shSplits = splits[0].split(" RSHIFT ");
				gates.add(Gate.rshift(splits[1], shSplits[0], shSplits[1]));
			} else if (splits[0].contains("NOT")) {
				final String in = splits[0].substring("NOT ".length());
				gates.add(Gate.not(splits[1], in));
			} else {
				gates.add(Gate.set(splits[1], splits[0]));
			}
		}
		return Circuit.of(gates);
	}
	
	long part1(String wire) {
		final Circuit circuit = parse();
		log(circuit);
		return circuit.getValue(wire);
	}
	
	@Override
	public long solvePart1() {
		return part1("a");
	}
	
	@Override
	public long solvePart2() {
		final long a = part1("a");
		final Circuit circuit = parse();
		circuit.setGate("b", Gate.set("b", String.valueOf(a)));
		return circuit.getValue("a");
	}
	
	public void visualize(OutputStream os) throws IOException {
		final Circuit circuit = parse();
		final StringBuilder sb = new StringBuilder("digraph circuit {");
		sb.append("rankdir=LR charset=UTF-8 ");
		sb.append("node [ shape=rect ]; " );
		sb.append("edge [ dir=none tailport=e ]; " );
		for (final Gate gate : circuit.getGates()) {
			sb.append(gate.name);
			sb.append(" [label=\"");
			sb.append(gate.op.name());
			if (gate.op.name().endsWith("SHIFT")) {
				sb.append(" ").append(gate.arg);
			}
			sb.append("\"");
			if (asList("SET", "NOT").contains(gate.op.name())) {
				sb.append(" shape=triangle orientation=270");
			} else if ("OR".equals(gate.op.name())) {
				sb.append(" shape=house orientation=270");
			} else if ("AND".equals(gate.op.name())) {
				sb.append(" shape=egg orientation=270");
			} else if ("LSHIFT".equals(gate.op.name())) {
				sb.append(" shape=cds orientation=180");
			} else if ("RSHIFT".equals(gate.op.name())) {
				sb.append(" shape=cds");
			}
			sb.append("]; ");
		}
		for (final Gate gate : circuit.getGates()) {
			sb.append(gate.in1).append(" -> ").append(gate.name);
			sb.append(" [label=\"").append(gate.in1).append("\"");
			circuit.getGateIn1(gate.name).ifPresent(in1 -> {
				if ("NOT".equals(in1.op.name())) {
					sb.append(" dir=back arrowtail=odot");
				}
			});
			sb.append("];");
			if (gate.in2 == null) {
				continue;
			}
			sb.append(gate.in2).append(" -> ").append(gate.name);
			sb.append(" [label=\"").append(gate.in2).append("\"");
			circuit.getGateIn2(gate.name).ifPresent(in2 -> {
				if ("NOT".equals(in2.op.name())) {
					sb.append(" dir=back arrowtail=odot");
				}
			});
			sb.append("];");
		}
		sb.append("}").append(System.lineSeparator());
		IOUtils.write(sb.toString(), os, Charset.forName("UTF-8"));
	}
	
	public static void main(String[] args) throws Exception {
		assert AoC2015_07.createDebug(TEST).part1("x") == 123;
		assert AoC2015_07.createDebug(TEST).part1("y") == 456;
		assert AoC2015_07.createDebug(TEST).part1("d") == 72;
		assert AoC2015_07.createDebug(TEST).part1("e") == 507;
		assert AoC2015_07.createDebug(TEST).part1("f") == 492;
		assert AoC2015_07.createDebug(TEST).part1("g") == 114;
		assert AoC2015_07.createDebug(TEST).part1("h") == 65412;
		assert AoC2015_07.createDebug(TEST).part1("i") == 65079;
		assert AoC2015_07.createDebug(TEST).part1("j") == 65079;

		final List<String> input = Aocd.getData(2015, 7);
		
		lap("Part 1", () -> AoC2015_07.create(input).solvePart1());
		lap("Part 2", () -> AoC2015_07.create(input).solvePart2());
		
		AoC2015_07.create(TEST).visualize(System.out);
		AoC2015_07.create(input).visualize(System.out);
	}

	private static final List<String> TEST = splitLines(
			"123 -> x\r\n" +
			"456 -> y\r\n" +
			"x AND y -> d\r\n" +
			"x OR y -> e\r\n" +
			"x LSHIFT 2 -> f\r\n" +
			"y RSHIFT 2 -> g\r\n" +
			"NOT x -> h\r\n" +
			"NOT y -> i\r\n" +
			"i -> j"
	);
	
	private static final class Gate {

		private enum Op { SET, NOT, AND, OR, LSHIFT, RSHIFT }
		
		private final String name;
		private final String in1;
		private final String in2;
		private final Op op;
		private final Integer arg;
		private Integer result;
		
		private Gate(String name, String in1, String in2, Op op, Integer arg) {
			this.name = name;
			this.in1 = in1;
			this.in2 = in2;
			this.op = op;
			this.arg = arg;
		}
		
		public static Gate not(String name, String in) {
			return new Gate(name, in, null, Op.NOT, null);
		}
		
		public static Gate and(String name, String in1, String in2) {
			return new Gate(name, in1, in2, Op.AND, null);
		}
		
		public static Gate or(String name, String in1, String in2) {
			return new Gate(name, in1, in2, Op.OR, null);
		}
		
		public static Gate lshift(String name, String in, String value) {
			final Integer arg = Integer.valueOf(value);
			assert arg < BIT_SIZE : "Shifting more than 15 positions";
			return new Gate(name, in, null, Op.LSHIFT, arg);
		}
		
		public static Gate rshift(String name, String in, String value) {
			final Integer arg = Integer.valueOf(value);
			assert arg < BIT_SIZE : "Shifting more than 15 positions";
			return new Gate(name, in, null, Op.RSHIFT, arg);
		}
		
		public static Gate set(String name, String in) {
			return new Gate(name, in, null, Op.SET, null);
		}

		public String getName() {
			return name;
		}

		public Integer getResult() {
			return result;
		}

		public Integer updateResult(Integer in1, Integer in2) {
			switch (this.op) {
			case SET:
				this.result = in1;
				break;
			case AND:
				this.result = in1 & in2;
				break;
			case LSHIFT:
				this.result = in1 << arg;
				break;
			case NOT:
				this.result = (int) (Math.pow(2, BIT_SIZE) + ~in1);
				break;
			case OR:
				this.result = in1 | in2;
				break;
			case RSHIFT:
				this.result = in1 >>> arg;
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
			case LSHIFT:
				sb.append(this.in1).append(" LSHIFT ").append(arg);
				break;
			case NOT:
				sb.append("NOT ").append(this.in1);
				break;
			case OR:
				sb.append(this.in1).append(" OR ").append(this.in2);
				break;
			case RSHIFT:
				sb.append(this.in1).append(" RSHIFT ").append(arg);
				break;
			default:
				throw new IllegalStateException();
			}
			return sb.toString();
		}
	}
	
	private static final class Circuit {
		private final Map<String, Gate> gates;

		private Circuit(Map<String, Gate> gates) {
			this.gates = gates;
		}
		
		public static Circuit of(Collection<Gate> gates) {
			return new Circuit(requireNonNull(gates).stream()
								.collect(toMap(Gate::getName, identity())));
		}
		
		public Collection<Gate> getGates() {
			return this.gates.values();
		}
		
		public Gate getGate(String name) {
			return this.gates.get(requireNonNull(name));
		}
		
		public void setGate(String name, Gate gate) {
			this.gates.put(requireNonNull(name), requireNonNull(gate));
		}
		
		public Optional<Gate> getGateIn1(String name) {
			final Gate gate = this.getGate(name);
			return Optional.ofNullable(gate.in1).map(this::getGate);
		}
		
		public Optional<Gate> getGateIn2(String name) {
			final Gate gate = this.getGate(name);
			return Optional.ofNullable(gate.in2).map(this::getGate);
		}
		
		public int getValue(String name) {
			assert name != null && !name.isEmpty(): "name is empty";
			if (StringUtils.isNumeric(name)) {
				final int out = Integer.valueOf(name);
				return out;
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
			return "Circuit [gates=" + gates + "]";
		}
	}
}

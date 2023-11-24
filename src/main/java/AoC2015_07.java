import static com.github.pareronia.aoc.StringOps.splitLines;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.github.pareronia.aoc.StringUtils;
import com.github.pareronia.aoc.solution.SolutionBase;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2015_07 extends SolutionBase<List<Gate>, Integer, Integer> {

	private AoC2015_07(final boolean debug) {
		super(debug);
	}
	
	public static final AoC2015_07 create() {
		return new AoC2015_07(false);
	}

	public static final AoC2015_07 createDebug() {
		return new AoC2015_07(true);
	}
	
	@Override
    protected List<Gate> parseInput(final List<String> inputs) {
		return inputs.stream().map(Gate::fromInput).collect(toList());
    }

    int part1(final List<Gate> inputs, final String wire) {
		final Circuit circuit = Circuit.of(inputs);
		log(circuit);
		return circuit.getValue(wire);
	}
	
	@Override
	public Integer solvePart1(final List<Gate> inputs) {
		return part1(inputs, "a");
	}
	
	@Override
	public Integer solvePart2(final List<Gate> inputs) {
	    final List<Gate> clone = inputs.stream().map(Gate::cloneGate).collect(toList());
		final Circuit circuit1 = Circuit.of(inputs);
		final int a = circuit1.getValue("a");
		final Circuit circuit2 = Circuit.of(clone);
		circuit2.setGate("b", Gate.set("b", String.valueOf(a)));
		return circuit2.getValue("a");
	}
	
	public void visualize(final List<String> inputs, final OutputStream os) throws IOException {
		final Circuit circuit = Circuit.of(this.parseInput(inputs));
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
		os.write(sb.toString().getBytes(Charset.forName("UTF-8")));
	}
	
	public static void main(final String[] args) throws Exception {
		final AoC2015_07 test = AoC2015_07.createDebug();
		final List<Gate> inputs = test.parseInput(TEST);
        assert test.part1(inputs, "x") == 123;
		assert test.part1(inputs, "y") == 456;
		assert test.part1(inputs, "d") == 72;
		assert test.part1(inputs, "e") == 507;
		assert test.part1(inputs, "f") == 492;
		assert test.part1(inputs, "g") == 114;
		assert test.part1(inputs, "h") == 65412;
		assert test.part1(inputs, "i") == 65079;
		assert test.part1(inputs, "j") == 65079;

		final AoC2015_07 solution = AoC2015_07.create();
        solution.run();
		solution.visualize(TEST, System.out);
		solution.visualize(solution.getInputData(), System.out);
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
}
	
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class Gate implements Cloneable {

    private static final int BIT_SIZE = 16;
    
    enum Op { SET, NOT, AND, OR, LSHIFT, RSHIFT }
    
    @Getter
    final String name;
    final String in1;
    final String in2;
    final Op op;
    final Integer arg;
    @Getter
    private Integer result;
    
    public static Gate fromInput(final String input) {
        final String[] splits = input.split(" -> ");
        if (splits[0].contains("AND")) {
            final String[] andSplits = splits[0].split(" AND ");
            return Gate.and(splits[1], andSplits[0], andSplits[1]);
        } else if (splits[0].contains("OR")) {
            final String[] orSplits = splits[0].split(" OR ");
            return Gate.or(splits[1], orSplits[0], orSplits[1]);
        } else if (splits[0].contains("LSHIFT")) {
            final String[] shSplits = splits[0].split(" LSHIFT ");
            return Gate.lshift(splits[1], shSplits[0], shSplits[1]);
        } else if (splits[0].contains("RSHIFT")) {
            final String[] shSplits = splits[0].split(" RSHIFT ");
            return Gate.rshift(splits[1], shSplits[0], shSplits[1]);
        } else if (splits[0].contains("NOT")) {
            final String in = splits[0].substring("NOT ".length());
            return Gate.not(splits[1], in);
        } else {
            return Gate.set(splits[1], splits[0]);
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
    
    public static Gate not(final String name, final String in) {
        return new Gate(name, in, null, Op.NOT, null);
    }
    
    public static Gate and(final String name, final String in1, final String in2) {
        return new Gate(name, in1, in2, Op.AND, null);
    }
    
    public static Gate or(final String name, final String in1, final String in2) {
        return new Gate(name, in1, in2, Op.OR, null);
    }
    
    public static Gate lshift(final String name, final String in, final String value) {
        final Integer arg = Integer.valueOf(value);
        assert arg < BIT_SIZE : "Shifting more than 15 positions";
        return new Gate(name, in, null, Op.LSHIFT, arg);
    }
    
    public static Gate rshift(final String name, final String in, final String value) {
        final Integer arg = Integer.valueOf(value);
        assert arg < BIT_SIZE : "Shifting more than 15 positions";
        return new Gate(name, in, null, Op.RSHIFT, arg);
    }
    
    public static Gate set(final String name, final String in) {
        return new Gate(name, in, null, Op.SET, null);
    }

    public Integer updateResult(final Integer in1, final Integer in2) {
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

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
final class Circuit {
    private final Map<String, Gate> gates;

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
}

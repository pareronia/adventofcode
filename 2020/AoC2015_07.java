import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;

public class AoC2015_07 {

	private static final int BIT_SIZE = 16;

	private final List<String> inputs;
	private final boolean debug;
	
	private AoC2015_07(String input, boolean debug) {
		this.inputs = asList((input + "\n").split("\\r?\\n"));
		this.debug = debug;
	}
	
	public static final AoC2015_07 create(String input) {
		return new AoC2015_07(input, false);
	}

	public static final AoC2015_07 createDebug(String input) {
		return new AoC2015_07(input, true);
	}
	
	private void log(Object obj) {
		if (!debug) {
			return;
		}
		System.out.println(obj);
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
	
	public long solvePart1() {
		return part1("a");
	}
	
	public long solvePart2() {
		final int a = (int) part1("a");
		final Circuit circuit2 = parse();
		circuit2.getGate("b").setResult(a);
		return circuit2.getValue("a");
	}

	public static <V> void lap(String prefix, Callable<V> callable) throws Exception {
		final long timerStart = System.nanoTime();
		final V answer = callable.call();
		final long timeSpent = (System.nanoTime() - timerStart) / 1000;
		double time;
		String unit;
		if (timeSpent < 1000) {
			time = timeSpent;
			unit = "µs";
		} else if (timeSpent < 1000000) {
			time = timeSpent / 1000.0;
			unit = "ms";
		} else {
			time = timeSpent / 1000000.0;
			unit = "s";
		}
		System.out.println(String.format("%s : %s, took: %.3f %s",
										 prefix, answer, time, unit));
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

		lap("Part 1", () -> AoC2015_07.create(INPUT).solvePart1());
		lap("Part 2", () -> AoC2015_07.create(INPUT).solvePart2());
	}

	private static final String TEST =
			"123 -> x\r\n" +
			"456 -> y\r\n" +
			"x AND y -> d\r\n" +
			"x OR y -> e\r\n" +
			"x LSHIFT 2 -> f\r\n" +
			"y RSHIFT 2 -> g\r\n" +
			"NOT x -> h\r\n" +
			"NOT y -> i\r\n" +
			"i -> j";
	
	private static final String INPUT =
			"af AND ah -> ai\r\n" +
			"NOT lk -> ll\r\n" +
			"hz RSHIFT 1 -> is\r\n" +
			"NOT go -> gp\r\n" +
			"du OR dt -> dv\r\n" +
			"x RSHIFT 5 -> aa\r\n" +
			"at OR az -> ba\r\n" +
			"eo LSHIFT 15 -> es\r\n" +
			"ci OR ct -> cu\r\n" +
			"b RSHIFT 5 -> f\r\n" +
			"fm OR fn -> fo\r\n" +
			"NOT ag -> ah\r\n" +
			"v OR w -> x\r\n" +
			"g AND i -> j\r\n" +
			"an LSHIFT 15 -> ar\r\n" +
			"1 AND cx -> cy\r\n" +
			"jq AND jw -> jy\r\n" +
			"iu RSHIFT 5 -> ix\r\n" +
			"gl AND gm -> go\r\n" +
			"NOT bw -> bx\r\n" +
			"jp RSHIFT 3 -> jr\r\n" +
			"hg AND hh -> hj\r\n" +
			"bv AND bx -> by\r\n" +
			"er OR es -> et\r\n" +
			"kl OR kr -> ks\r\n" +
			"et RSHIFT 1 -> fm\r\n" +
			"e AND f -> h\r\n" +
			"u LSHIFT 1 -> ao\r\n" +
			"he RSHIFT 1 -> hx\r\n" +
			"eg AND ei -> ej\r\n" +
			"bo AND bu -> bw\r\n" +
			"dz OR ef -> eg\r\n" +
			"dy RSHIFT 3 -> ea\r\n" +
			"gl OR gm -> gn\r\n" +
			"da LSHIFT 1 -> du\r\n" +
			"au OR av -> aw\r\n" +
			"gj OR gu -> gv\r\n" +
			"eu OR fa -> fb\r\n" +
			"lg OR lm -> ln\r\n" +
			"e OR f -> g\r\n" +
			"NOT dm -> dn\r\n" +
			"NOT l -> m\r\n" +
			"aq OR ar -> as\r\n" +
			"gj RSHIFT 5 -> gm\r\n" +
			"hm AND ho -> hp\r\n" +
			"ge LSHIFT 15 -> gi\r\n" +
			"jp RSHIFT 1 -> ki\r\n" +
			"hg OR hh -> hi\r\n" +
			"lc LSHIFT 1 -> lw\r\n" +
			"km OR kn -> ko\r\n" +
			"eq LSHIFT 1 -> fk\r\n" +
			"1 AND am -> an\r\n" +
			"gj RSHIFT 1 -> hc\r\n" +
			"aj AND al -> am\r\n" +
			"gj AND gu -> gw\r\n" +
			"ko AND kq -> kr\r\n" +
			"ha OR gz -> hb\r\n" +
			"bn OR by -> bz\r\n" +
			"iv OR jb -> jc\r\n" +
			"NOT ac -> ad\r\n" +
			"bo OR bu -> bv\r\n" +
			"d AND j -> l\r\n" +
			"bk LSHIFT 1 -> ce\r\n" +
			"de OR dk -> dl\r\n" +
			"dd RSHIFT 1 -> dw\r\n" +
			"hz AND ik -> im\r\n" +
			"NOT jd -> je\r\n" +
			"fo RSHIFT 2 -> fp\r\n" +
			"hb LSHIFT 1 -> hv\r\n" +
			"lf RSHIFT 2 -> lg\r\n" +
			"gj RSHIFT 3 -> gl\r\n" +
			"ki OR kj -> kk\r\n" +
			"NOT ak -> al\r\n" +
			"ld OR le -> lf\r\n" +
			"ci RSHIFT 3 -> ck\r\n" +
			"1 AND cc -> cd\r\n" +
			"NOT kx -> ky\r\n" +
			"fp OR fv -> fw\r\n" +
			"ev AND ew -> ey\r\n" +
			"dt LSHIFT 15 -> dx\r\n" +
			"NOT ax -> ay\r\n" +
			"bp AND bq -> bs\r\n" +
			"NOT ii -> ij\r\n" +
			"ci AND ct -> cv\r\n" +
			"iq OR ip -> ir\r\n" +
			"x RSHIFT 2 -> y\r\n" +
			"fq OR fr -> fs\r\n" +
			"bn RSHIFT 5 -> bq\r\n" +
			"0 -> c\r\n" +
			"14146 -> b\r\n" +
			"d OR j -> k\r\n" +
			"z OR aa -> ab\r\n" +
			"gf OR ge -> gg\r\n" +
			"df OR dg -> dh\r\n" +
			"NOT hj -> hk\r\n" +
			"NOT di -> dj\r\n" +
			"fj LSHIFT 15 -> fn\r\n" +
			"lf RSHIFT 1 -> ly\r\n" +
			"b AND n -> p\r\n" +
			"jq OR jw -> jx\r\n" +
			"gn AND gp -> gq\r\n" +
			"x RSHIFT 1 -> aq\r\n" +
			"ex AND ez -> fa\r\n" +
			"NOT fc -> fd\r\n" +
			"bj OR bi -> bk\r\n" +
			"as RSHIFT 5 -> av\r\n" +
			"hu LSHIFT 15 -> hy\r\n" +
			"NOT gs -> gt\r\n" +
			"fs AND fu -> fv\r\n" +
			"dh AND dj -> dk\r\n" +
			"bz AND cb -> cc\r\n" +
			"dy RSHIFT 1 -> er\r\n" +
			"hc OR hd -> he\r\n" +
			"fo OR fz -> ga\r\n" +
			"t OR s -> u\r\n" +
			"b RSHIFT 2 -> d\r\n" +
			"NOT jy -> jz\r\n" +
			"hz RSHIFT 2 -> ia\r\n" +
			"kk AND kv -> kx\r\n" +
			"ga AND gc -> gd\r\n" +
			"fl LSHIFT 1 -> gf\r\n" +
			"bn AND by -> ca\r\n" +
			"NOT hr -> hs\r\n" +
			"NOT bs -> bt\r\n" +
			"lf RSHIFT 3 -> lh\r\n" +
			"au AND av -> ax\r\n" +
			"1 AND gd -> ge\r\n" +
			"jr OR js -> jt\r\n" +
			"fw AND fy -> fz\r\n" +
			"NOT iz -> ja\r\n" +
			"c LSHIFT 1 -> t\r\n" +
			"dy RSHIFT 5 -> eb\r\n" +
			"bp OR bq -> br\r\n" +
			"NOT h -> i\r\n" +
			"1 AND ds -> dt\r\n" +
			"ab AND ad -> ae\r\n" +
			"ap LSHIFT 1 -> bj\r\n" +
			"br AND bt -> bu\r\n" +
			"NOT ca -> cb\r\n" +
			"NOT el -> em\r\n" +
			"s LSHIFT 15 -> w\r\n" +
			"gk OR gq -> gr\r\n" +
			"ff AND fh -> fi\r\n" +
			"kf LSHIFT 15 -> kj\r\n" +
			"fp AND fv -> fx\r\n" +
			"lh OR li -> lj\r\n" +
			"bn RSHIFT 3 -> bp\r\n" +
			"jp OR ka -> kb\r\n" +
			"lw OR lv -> lx\r\n" +
			"iy AND ja -> jb\r\n" +
			"dy OR ej -> ek\r\n" +
			"1 AND bh -> bi\r\n" +
			"NOT kt -> ku\r\n" +
			"ao OR an -> ap\r\n" +
			"ia AND ig -> ii\r\n" +
			"NOT ey -> ez\r\n" +
			"bn RSHIFT 1 -> cg\r\n" +
			"fk OR fj -> fl\r\n" +
			"ce OR cd -> cf\r\n" +
			"eu AND fa -> fc\r\n" +
			"kg OR kf -> kh\r\n" +
			"jr AND js -> ju\r\n" +
			"iu RSHIFT 3 -> iw\r\n" +
			"df AND dg -> di\r\n" +
			"dl AND dn -> do\r\n" +
			"la LSHIFT 15 -> le\r\n" +
			"fo RSHIFT 1 -> gh\r\n" +
			"NOT gw -> gx\r\n" +
			"NOT gb -> gc\r\n" +
			"ir LSHIFT 1 -> jl\r\n" +
			"x AND ai -> ak\r\n" +
			"he RSHIFT 5 -> hh\r\n" +
			"1 AND lu -> lv\r\n" +
			"NOT ft -> fu\r\n" +
			"gh OR gi -> gj\r\n" +
			"lf RSHIFT 5 -> li\r\n" +
			"x RSHIFT 3 -> z\r\n" +
			"b RSHIFT 3 -> e\r\n" +
			"he RSHIFT 2 -> hf\r\n" +
			"NOT fx -> fy\r\n" +
			"jt AND jv -> jw\r\n" +
			"hx OR hy -> hz\r\n" +
			"jp AND ka -> kc\r\n" +
			"fb AND fd -> fe\r\n" +
			"hz OR ik -> il\r\n" +
			"ci RSHIFT 1 -> db\r\n" +
			"fo AND fz -> gb\r\n" +
			"fq AND fr -> ft\r\n" +
			"gj RSHIFT 2 -> gk\r\n" +
			"cg OR ch -> ci\r\n" +
			"cd LSHIFT 15 -> ch\r\n" +
			"jm LSHIFT 1 -> kg\r\n" +
			"ih AND ij -> ik\r\n" +
			"fo RSHIFT 3 -> fq\r\n" +
			"fo RSHIFT 5 -> fr\r\n" +
			"1 AND fi -> fj\r\n" +
			"1 AND kz -> la\r\n" +
			"iu AND jf -> jh\r\n" +
			"cq AND cs -> ct\r\n" +
			"dv LSHIFT 1 -> ep\r\n" +
			"hf OR hl -> hm\r\n" +
			"km AND kn -> kp\r\n" +
			"de AND dk -> dm\r\n" +
			"dd RSHIFT 5 -> dg\r\n" +
			"NOT lo -> lp\r\n" +
			"NOT ju -> jv\r\n" +
			"NOT fg -> fh\r\n" +
			"cm AND co -> cp\r\n" +
			"ea AND eb -> ed\r\n" +
			"dd RSHIFT 3 -> df\r\n" +
			"gr AND gt -> gu\r\n" +
			"ep OR eo -> eq\r\n" +
			"cj AND cp -> cr\r\n" +
			"lf OR lq -> lr\r\n" +
			"gg LSHIFT 1 -> ha\r\n" +
			"et RSHIFT 2 -> eu\r\n" +
			"NOT jh -> ji\r\n" +
			"ek AND em -> en\r\n" +
			"jk LSHIFT 15 -> jo\r\n" +
			"ia OR ig -> ih\r\n" +
			"gv AND gx -> gy\r\n" +
			"et AND fe -> fg\r\n" +
			"lh AND li -> lk\r\n" +
			"1 AND io -> ip\r\n" +
			"kb AND kd -> ke\r\n" +
			"kk RSHIFT 5 -> kn\r\n" +
			"id AND if -> ig\r\n" +
			"NOT ls -> lt\r\n" +
			"dw OR dx -> dy\r\n" +
			"dd AND do -> dq\r\n" +
			"lf AND lq -> ls\r\n" +
			"NOT kc -> kd\r\n" +
			"dy AND ej -> el\r\n" +
			"1 AND ke -> kf\r\n" +
			"et OR fe -> ff\r\n" +
			"hz RSHIFT 5 -> ic\r\n" +
			"dd OR do -> dp\r\n" +
			"cj OR cp -> cq\r\n" +
			"NOT dq -> dr\r\n" +
			"kk RSHIFT 1 -> ld\r\n" +
			"jg AND ji -> jj\r\n" +
			"he OR hp -> hq\r\n" +
			"hi AND hk -> hl\r\n" +
			"dp AND dr -> ds\r\n" +
			"dz AND ef -> eh\r\n" +
			"hz RSHIFT 3 -> ib\r\n" +
			"db OR dc -> dd\r\n" +
			"hw LSHIFT 1 -> iq\r\n" +
			"he AND hp -> hr\r\n" +
			"NOT cr -> cs\r\n" +
			"lg AND lm -> lo\r\n" +
			"hv OR hu -> hw\r\n" +
			"il AND in -> io\r\n" +
			"NOT eh -> ei\r\n" +
			"gz LSHIFT 15 -> hd\r\n" +
			"gk AND gq -> gs\r\n" +
			"1 AND en -> eo\r\n" +
			"NOT kp -> kq\r\n" +
			"et RSHIFT 5 -> ew\r\n" +
			"lj AND ll -> lm\r\n" +
			"he RSHIFT 3 -> hg\r\n" +
			"et RSHIFT 3 -> ev\r\n" +
			"as AND bd -> bf\r\n" +
			"cu AND cw -> cx\r\n" +
			"jx AND jz -> ka\r\n" +
			"b OR n -> o\r\n" +
			"be AND bg -> bh\r\n" +
			"1 AND ht -> hu\r\n" +
			"1 AND gy -> gz\r\n" +
			"NOT hn -> ho\r\n" +
			"ck OR cl -> cm\r\n" +
			"ec AND ee -> ef\r\n" +
			"lv LSHIFT 15 -> lz\r\n" +
			"ks AND ku -> kv\r\n" +
			"NOT ie -> if\r\n" +
			"hf AND hl -> hn\r\n" +
			"1 AND r -> s\r\n" +
			"ib AND ic -> ie\r\n" +
			"hq AND hs -> ht\r\n" +
			"y AND ae -> ag\r\n" +
			"NOT ed -> ee\r\n" +
			"bi LSHIFT 15 -> bm\r\n" +
			"dy RSHIFT 2 -> dz\r\n" +
			"ci RSHIFT 2 -> cj\r\n" +
			"NOT bf -> bg\r\n" +
			"NOT im -> in\r\n" +
			"ev OR ew -> ex\r\n" +
			"ib OR ic -> id\r\n" +
			"bn RSHIFT 2 -> bo\r\n" +
			"dd RSHIFT 2 -> de\r\n" +
			"bl OR bm -> bn\r\n" +
			"as RSHIFT 1 -> bl\r\n" +
			"ea OR eb -> ec\r\n" +
			"ln AND lp -> lq\r\n" +
			"kk RSHIFT 3 -> km\r\n" +
			"is OR it -> iu\r\n" +
			"iu RSHIFT 2 -> iv\r\n" +
			"as OR bd -> be\r\n" +
			"ip LSHIFT 15 -> it\r\n" +
			"iw OR ix -> iy\r\n" +
			"kk RSHIFT 2 -> kl\r\n" +
			"NOT bb -> bc\r\n" +
			"ci RSHIFT 5 -> cl\r\n" +
			"ly OR lz -> ma\r\n" +
			"z AND aa -> ac\r\n" +
			"iu RSHIFT 1 -> jn\r\n" +
			"cy LSHIFT 15 -> dc\r\n" +
			"cf LSHIFT 1 -> cz\r\n" +
			"as RSHIFT 3 -> au\r\n" +
			"cz OR cy -> da\r\n" +
			"kw AND ky -> kz\r\n" +
			"lx -> a\r\n" +
			"iw AND ix -> iz\r\n" +
			"lr AND lt -> lu\r\n" +
			"jp RSHIFT 5 -> js\r\n" +
			"aw AND ay -> az\r\n" +
			"jc AND je -> jf\r\n" +
			"lb OR la -> lc\r\n" +
			"NOT cn -> co\r\n" +
			"kh LSHIFT 1 -> lb\r\n" +
			"1 AND jj -> jk\r\n" +
			"y OR ae -> af\r\n" +
			"ck AND cl -> cn\r\n" +
			"kk OR kv -> kw\r\n" +
			"NOT cv -> cw\r\n" +
			"kl AND kr -> kt\r\n" +
			"iu OR jf -> jg\r\n" +
			"at AND az -> bb\r\n" +
			"jp RSHIFT 2 -> jq\r\n" +
			"iv AND jb -> jd\r\n" +
			"jn OR jo -> jp\r\n" +
			"x OR ai -> aj\r\n" +
			"ba AND bc -> bd\r\n" +
			"jl OR jk -> jm\r\n" +
			"b RSHIFT 1 -> v\r\n" +
			"o AND q -> r\r\n" +
			"NOT p -> q\r\n" +
			"k AND m -> n\r\n" +
			"as RSHIFT 2 -> at";
	
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

		public void setResult(Integer result) {
			this.result = result;
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

		public boolean isSet() {
			return this.op == Op.SET;
		}

		public boolean isAnd() {
			return this.op == Op.AND;
		}

		public boolean isOr() {
			return this.op == Op.OR;
		}

		public boolean isLShift() {
			return this.op == Op.LSHIFT;
		}

		public boolean isRShift() {
			return this.op == Op.RSHIFT;
		}

		public boolean isNot() {
			return this.op == Op.NOT;
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
		
		public Gate getGate(String gate) {
			return this.gates.get(requireNonNull(gate));
		}
		
		public int getValue(String name) {
			assert name != null && !name.isEmpty(): "name is empty";
			if (StringUtils.isNumeric(name)) {
				return Integer.valueOf(name);
			} else {
				final Gate gate = getGate(name);
				assert gate != null : "Gate '" + name + "' not found";
				final Integer result = gate.getResult();
				if (result != null) {
					return result;
				} else {
					final int in1 = getValue(gate.in1);
					if (gate.isSet()) {
						final int out = in1;
						gate.setResult(out);
						return out;
					} else if (gate.isLShift()) {
						final int out = in1 << gate.arg;
						gate.setResult(out);
						return out;
					} else if (gate.isRShift()) {
						final int out = in1 >>> gate.arg;
						gate.setResult(out);
						return out;
					} else if (gate.isNot()) {
						final int out = (int) (Math.pow(2, BIT_SIZE) + ~in1);
						gate.setResult(out);
						return out;
					} else {
						final int in2 = getValue(gate.in2);
						if (gate.isAnd()) {
							final int out = in1 & in2;
							gate.setResult(out);
							return out;
						} else if (gate.isOr()) {
							final int out = in1 | in2;
							gate.setResult(out);
							return out;
						} else {
							throw new UnsupportedOperationException();
						}
					}
				}
			}
		}

		@Override
		public String toString() {
			return "Circuit [gates=" + gates + "]";
		}
	}
}

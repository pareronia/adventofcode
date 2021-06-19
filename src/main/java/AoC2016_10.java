import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

import com.github.pareronia.aocd.Aocd;

import lombok.Data;
import lombok.Value;

public class AoC2016_10 extends AoCBase {

	private final Set<Bot> bots;
	private final List<Input> inputs;
	private final Map<Integer, Integer> outputs;
	
	private AoC2016_10(List<String> inputs, boolean debug) {
		super(debug);
		final Pair<Set<Bot>, List<Input>> pair = parse(inputs);
		this.bots = pair.getOne();
		this.inputs = pair.getTwo();
		this.outputs = new HashMap<>();
	}
	
	private Pair<Set<Bot>, List<Input>> parse(List<String> strings) {
	    final Set<Bot> bots = new HashSet<>();
	    final List<Input> inputs = new ArrayList<>();
	    for (final String string : strings) {
	        if (string.startsWith("value ")) {
	            final String[] splits
	                = string.substring("value ".length()).split(" goes to bot ");
	            final Integer value = Integer.valueOf(splits[0]);
                final Integer toBot = Integer.valueOf(splits[1]);
                inputs.add(new Input(value, toBot));
	        } else if (string.startsWith("bot ")) {
	            final String[] splits
	                = string.substring("bot ".length()).split(" ");
	            final Integer bot = Integer.valueOf(splits[0]);
	            final String typeLo = splits[4];
	            final Integer outLo = (typeLo.equals("bot") ? 0 : 1000)
	                                        + Integer.valueOf(splits[5]);
	            final String typeHi = splits[9];
	            final Integer outHi = (typeHi.equals("bot") ? 0 : 1000)
	                                        + Integer.valueOf(splits[10]);
	            bots.add(new Bot(bot, outLo, outHi));
	        } else {
	            throw new IllegalArgumentException("Invalid input");
	        }
        }
	    log(inputs);
	    log(bots);
	    return Tuples.pair(bots, inputs);
	}
	
	public static final AoC2016_10 create(List<String> input) {
		return new AoC2016_10(input, false);
	}

	public static final AoC2016_10 createDebug(List<String> input) {
		return new AoC2016_10(input, true);
	}
	
	private Bot findBot(Integer number) {
	    return bots.stream()
	            .filter(b -> b.getNumber().equals(number))
	            .findFirst()
	            .orElseThrow();
	}
	
	private void output(Integer number, Integer value) {
	    outputs.put(number, value);
	}
	
    private void run() {
        for (final Input input : this.inputs) {
            findBot(input.getToBot())
                    .receive(input.getValue(), this::findBot, this::output);
        }
    }

	private Integer solvePart1(Integer first, Integer second) {
	    run();
	    return bots.stream()
	            .filter(b -> b.getCompares().contains(Set.of(first, second)))
	            .findFirst()
	            .map(Bot::getNumber)
	            .orElseThrow();
    }

	@Override
	public Integer solvePart1() {
	    return solvePart1(17, 61);
	}
	
	@Override
	public Integer solvePart2() {
	    run();
		return outputs.entrySet().stream()
		        .filter(e -> Set.of(0, 1, 2).contains(e.getKey()))
		        .map(Entry::getValue)
		        .reduce(1, (a, b) -> a * b);
	}

	public static void main(String[] args) throws Exception {
		assert AoC2016_10.createDebug(TEST).solvePart1(2, 5) == 2;
		assert AoC2016_10.createDebug(TEST).solvePart2() == 30;
		
		final List<String> input = Aocd.getData(2016, 10);
		lap("Part 1", () -> AoC2016_10.create(input).solvePart1());
		lap("Part 2", () -> AoC2016_10.create(input).solvePart2());
	}

	private static final List<String> TEST = splitLines(
	        "value 5 goes to bot 2\n" +
	        "bot 2 gives low to bot 1 and high to bot 0\n" +
	        "value 3 goes to bot 1\n" +
	        "bot 1 gives low to output 1 and high to bot 0\n" +
	        "bot 0 gives low to output 2 and high to output 0\n" +
	        "value 2 goes to bot 2"
	);
	
	@Data
	private static final class Bot {
	    private final Integer number;
	    private final Integer lowTo;
	    private final Integer highTo;
	    private List<Integer> values = new ArrayList<>();
	    private Set<Set<Integer>> compares = new HashSet<>();
	    
	    public void receive(
	            Integer value,
	            Function<Integer, Bot> botLookup,
	            BiConsumer<Integer, Integer> output
	    ) {
	       this.values.add(value);
	       if (this.values.size() == 2) {
	           this.values.sort(null);
	           final Integer lowValue = this.values.get(0);
	           final Integer highValue = this.values.get(1);
	           if (this.lowTo < 1000) {
	               botLookup.apply(this.lowTo).receive(lowValue, botLookup, output);
	           } else {
	               output.accept(this.lowTo - 1000, lowValue);
	           }
	           if (this.highTo < 1000) {
	               botLookup.apply(this.highTo).receive(highValue, botLookup, output);
	           } else {
	               output.accept(this.highTo - 1000, highValue);
	           }
	           this.compares.add(Set.of(lowValue, highValue));
	           this.values.clear();
	       }
	    }
	}
	
	@Value
	private static final class Input {
	    private final Integer value;
	    private final Integer toBot;
	}
}

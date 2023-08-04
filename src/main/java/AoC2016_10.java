import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.Data;
import lombok.Value;

public class AoC2016_10 extends AoCBase {

	private final Set<Bot> bots;
	private final List<Input> inputs;
	private final Map<Integer, Integer> outputs;
	
	private AoC2016_10(final List<String> inputs, final boolean debug) {
		super(debug);
        this.bots = new HashSet<>();
        this.inputs = new ArrayList<>();
		this.outputs = new HashMap<>();
        for (final String string : inputs) {
            if (string.startsWith("value ")) {
                final String[] splits
                    = string.substring("value ".length()).split(" goes to bot ");
                final Integer value = Integer.valueOf(splits[0]);
                final Integer toBot = Integer.valueOf(splits[1]);
                this.inputs.add(new AoC2016_10.Input(value, toBot));
            } else {
                final String[] splits
                    = string.substring("bot ".length()).split(" ");
                final Integer bot = Integer.valueOf(splits[0]);
                final String typeLo = splits[4];
                final Integer outLo = (typeLo.equals("bot") ? 0 : 1000)
                                            + Integer.valueOf(splits[5]);
                final String typeHi = splits[9];
                final Integer outHi = (typeHi.equals("bot") ? 0 : 1000)
                                            + Integer.valueOf(splits[10]);
                this.bots.add(new AoC2016_10.Bot(bot, outLo, outHi));
            }
        }
        log(this.inputs);
        log(this.bots);
	}
	
	public static final AoC2016_10 create(final List<String> input) {
		return new AoC2016_10(input, false);
	}

	public static final AoC2016_10 createDebug(final List<String> input) {
		return new AoC2016_10(input, true);
	}
	
	private Bot findBot(final Integer number) {
	    return bots.stream()
	            .filter(b -> b.getNumber().equals(number))
	            .findFirst()
	            .orElseThrow();
	}
	
	private void output(final Integer number, final Integer value) {
	    outputs.put(number, value);
	}
	
    private void run() {
        for (final Input input : this.inputs) {
            findBot(input.getToBot())
                    .receive(input.getValue(), this::findBot, this::output);
        }
    }

	private Integer solvePart1(final Integer first, final Integer second) {
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

	public static void main(final String[] args) throws Exception {
		assert AoC2016_10.createDebug(TEST).solvePart1(2, 5) == 2;
		assert AoC2016_10.createDebug(TEST).solvePart2() == 30;
		
        final Puzzle puzzle = Aocd.puzzle(2016, 10);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2016_10.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2016_10.create(inputData)::solvePart2)
        );
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
	            final Integer value,
	            final Function<Integer, Bot> botLookup,
	            final BiConsumer<Integer, Integer> output
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

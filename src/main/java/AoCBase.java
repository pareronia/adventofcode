import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public abstract class AoCBase {

	protected final boolean debug;
	protected boolean trace;
	
	protected static Puzzle puzzle(final Class<? extends AoCBase> klass) {
	   final var split = klass.getSimpleName().substring("AoC".length()).split("_");
	   return Aocd.puzzle(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
	}
	
	protected static List<String> splitLines(final String input) {
		return asList((requireNonNull(input) + "\n").split("\\r?\\n"));
	}
	
	protected static List<List<String>> toBlocks(final List<String> inputs) {
		if (inputs.isEmpty()) {
			return Collections.emptyList();
		}
		final List<List<String>> blocks = new ArrayList<>();
		int i = 0;
		final int last = inputs.size() - 1;
		blocks.add(new ArrayList<String>());
		for (int j = 0; j <= last; j++) {
			if (inputs.get(j).isEmpty()) {
				if (j != last) {
					blocks.add(new ArrayList<String>());
					i++;
				}
			} else {
				blocks.get(i).add(inputs.get(j));
			}
		}
		return blocks;
	}

	protected static <V> V lap(final String prefix, final Callable<V> callable) throws Exception {
	    final long timerStart = System.nanoTime();
	    final var answer = callable.call();
	    final long timeSpent = (System.nanoTime() - timerStart) / 1000;
	    double time;
	    String unit;
	    if (timeSpent < 1000) {
	        time = timeSpent;
	        unit = "µs";
	    } else if (timeSpent < 1_000_000) {
	        time = timeSpent / 1000.0;
	        unit = "ms";
	    } else {
	        time = timeSpent / 1_000_000.0;
	        unit = "s";
	    }
	    System.out.println(String.format("%s : %s, took: %.3f %s",
	    								 prefix, answer, time, unit));
	    return answer;
	}

	protected AoCBase(final boolean debug) {
		this.debug = debug;
	}
	
	public void setTrace(final boolean trace) {
        this.trace = trace;
    }

    public Object warmUp() {
	    return 0L;
	}
	
	public Object solvePart1() {
		return 0L;
	}
	
	public Object solvePart2() {
		return 0L;
	}
	
	protected void log(final Object obj) {
		if (!debug) {
			return;
		}
		System.out.println(obj);
	}

	protected void trace(final Object obj) {
		if (!trace) {
			return;
		}
		System.out.println(obj);
	}

	protected void log(final Supplier<Object> supplier) {
		if (!debug) {
			return;
		}
		System.out.println(supplier.get());
	}

	protected void trace(final Supplier<Object> supplier) {
		if (!trace) {
			return;
		}
		System.out.println(supplier.get());
	}
}